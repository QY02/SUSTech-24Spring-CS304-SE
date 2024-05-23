package org.cs304.backend.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
//import com.alipay.api.AlipayConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cs304.backend.config.AliPayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cs304.backend.constant.constant_OrderRecordStatus;
import org.cs304.backend.entity.OrderRecord;
import org.cs304.backend.exception.ServiceException;
import org.cs304.backend.mapper.EventSessionMapper;
import org.cs304.backend.mapper.SeatMapper;
import org.cs304.backend.service.IEventService;
import org.cs304.backend.service.IOrderRecordService;
import org.cs304.backend.utils.Result;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import static com.alipay.api.AlipayConstants.FORMAT;
//import static com.alipay.api.AlipayConstants.CHARSET;
//import static com.alipay.api.AlipayConstants.SIGN_TYPE;
@Slf4j
@RestController
@RequestMapping("/orderRecord")
public class OrderRecordController {
    private static final String CHARSET = "GBK";
    private static final String FORMAT = "JSON";
    private static final String SIGN_TYPE = "RSA2";
    @Resource
    private IOrderRecordService orderRecordService;

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private IEventService eventService;

    @Resource
    private EventSessionMapper eventSessionMapper;

    @Resource
    private SeatMapper seatMapper;

    @PostMapping("/getMyOrderRecord")
    @Operation(description = """
            ### 参数 ###
            eventId: 根据eventId查询预订记录, 可不填
            
            mode = 0: 只返回eventSessionId
            
            mode = 1: 返回orderRecord的所有数据
            
            mode = 2: 返回orderRecord和eventSession的所有数据
            
            mode = 3: 返回orderRecord, eventSession和event的所有数据
            """)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"eventId\": 1, \"mode\": 0}")))
    public Result getMyOrderRecord(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        String userId = (String) request.getAttribute("loginUserId");
        Integer eventId = requestBody.getInteger("eventId");
        Integer mode = requestBody.getInteger("mode");
        return Result.success(response, orderRecordService.getMyOrderRecord(userId, eventId, mode));
    }

    @PostMapping("/getUnpaidOrderRecord")
    @Operation(description = """
            ### 参数 ###
            eventId: 根据eventId查询预订记录, 可不填
            
            mode = 0: 只返回eventSessionId
            
            mode = 1: 返回orderRecord的所有数据
            
            mode = 2: 返回orderRecord和eventSession的所有数据
            
            mode = 3: 返回orderRecord, eventSession和event的所有数据
            """)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("{\"eventId\": 1, \"mode\": 0}")))
    public Result getUnpaidOrderRecord(HttpServletResponse response, HttpServletRequest request, @RequestBody JSONObject requestBody) {
        String userId = (String) request.getAttribute("loginUserId");
        Integer eventId = requestBody.getInteger("eventId");
        Integer mode = requestBody.getInteger("mode");
        return Result.success(response, orderRecordService.getUnpaidOrderRecord(userId, eventId, mode));
    }

    // 暂时没用到
    // @PostMapping("/getPayResultById")
    // public Result getResult(HttpServletResponse response, HttpServletRequest request){
    //     Integer orderId =  (int) request.getAttribute("id");
    //     int result = orderRecordService.getPaymentById(orderId);
    //     return Result.success(response, result);
    // }

    @PostMapping("/prePay")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(examples = @ExampleObject("""
            {
              "eventId": 0,
              "eventSessionId": 0,
              "seatId": "string",
              "additionalInformation": "string"
            }""")))
    public Result prePayInformation(HttpServletResponse response, HttpServletRequest request, @RequestBody OrderRecord orderRecord) {
        // 先加入
        int userType = (int) request.getAttribute("loginUserType");
        String userId = (String) request.getAttribute("loginUserId");
        eventService.submitBookingData(userType, userId, orderRecord);
        // 加入成功后去找id
        OrderRecord savedOrderRecord = orderRecordService.getOne(new QueryWrapper<OrderRecord>().eq("user_id", userId).eq("event_id", orderRecord.getEventId())
                .eq("event_session_id", orderRecord.getEventSessionId()).eq("status",0));
        savedOrderRecord.setStatus(constant_OrderRecordStatus.UNPAID);
        orderRecordService.updateById(savedOrderRecord);
        int orderId = savedOrderRecord.getId();
        return Result.success(response, orderId);
    }

    @GetMapping("/pay/{orderId}")
    public Result pay(HttpServletResponse httpResponse, @PathVariable int orderId) throws IOException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi-sandbox.dl.alipaydev.com/gateway.do",
                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(),FORMAT,CHARSET,
                aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
//        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        JSONObject bizContent = new JSONObject();

        OrderRecord orderRecord = orderRecordService.getOne(new QueryWrapper<OrderRecord>().eq("id", orderId));

        //根据id去开网页
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", orderRecord.getPrice());
        bizContent.put("subject", "座位"+orderRecord.getSeatId());
        bizContent.put("buyer_id", orderRecord.getUserId());
        bizContent.put("timeout_express", "10m");
//        bizContent.put("product_code", "JSAPI_PAY");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:5173/book");
        request.setNotifyUrl("http://localhost:8083/orderRecord/notify");
        String form = "";
        try{
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e){
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
        return Result.success(httpResponse);
    }
    @PostMapping("/notify") //注意这里必须是POST接口
    public void payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")){
            System.out.println("=========支付宝异步回调========");
            Map<String,String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for(String name : requestParams.keySet()){
                params.put(name, request.getParameter(name));
            }
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "GBK");// 支付宝验签
            if (checkSignature){
                System.out.println("交易名称:"+ params.get("subject"));
                System.out.println("交易状态:"+ params.get("trade_status"));
                System.out.println("支付宝交易凭证号:"+ params.get("trade_no"));
                System.out.println("商户订单号:"+ params.get("out_trade_no"));
                System.out.println("交易金额:"+ params.get("total_amount"));
                System.out.println("买家在支付宝唯一id:"+ params.get("buyer_id"));
                System.out.println("买家付款时间:"+ params.get("gmt_payment"));
                System.out.println("买家付款金额:"+ params.get("buyer_pay_amount"));

                String orderId = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //支付时间
                Date dt2 = df.parse(params.get("gmt_payment"));
                LocalDateTime payTime = DateUtil.toLocalDateTime(dt2);
                //换成PAID！！！
                OrderRecord orderRecord = orderRecordService.getOne(new QueryWrapper<OrderRecord>().eq("id", orderId));
                if(gmtPayment.equals("TRADE_CLOSED")){
                    orderRecord.setPaymentTime(payTime);
                    orderRecord.setStatus(constant_OrderRecordStatus.PAID);
                }
                else {
                    orderRecord.setStatus(constant_OrderRecordStatus.EXPIRED);
                }
                orderRecordService.updateById(orderRecord);
            }
        }
    }

    @PostMapping("/payResult")
    public Result payResult(HttpServletResponse response, @RequestBody JSONObject requestBody) {
        String time = requestBody.getString("time");
        Integer orderId = requestBody.getInteger("orderId");
        System.out.println("orderId:" + orderId);
        Integer result = requestBody.getInteger("result");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //支付时间
        try{
            Date dt2 = df.parse(time);
            LocalDateTime payTime = DateUtil.toLocalDateTime(dt2);
            //换成PAID！！！
            OrderRecord orderRecord = orderRecordService.getOne(new QueryWrapper<OrderRecord>().eq("id", orderId));
            orderRecord.setPaymentTime(payTime);
            if(result==1){
                orderRecord.setStatus(constant_OrderRecordStatus.PAID);
            }
            else{
                orderRecord.setStatus(constant_OrderRecordStatus.EXPIRED);
            }
            orderRecordService.updateById(orderRecord);
        }
        catch(ParseException e){
            log.error(e.getMessage());
            throw new ServiceException("400", "日期格式错误");
        }
        return Result.success(response);
    }
}
