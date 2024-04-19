package org.cs304.backend.component;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author websocket服务
 */

@Slf4j
@ServerEndpoint(value = "/LLMserver/{userID}")
@Component
@RabbitListener(queues = "ChatConsumeQueue")
public class WebSocketLLMServer {


    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketLLMServer.applicationContext = applicationContext;
    }

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();//<userID,session>

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userID") String userID) {
        if (sessionMap.containsKey(userID)) {
            sendMessage("USER ALREADY EXISTS", session);
            return;
        }
        sessionMap.put(userID, session);
        log.info("有新用户加入，username={}, 当前在线人数为：{}", userID, sessionMap.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userID") String userID) {
        sessionMap.remove(userID);
        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", userID, sessionMap.size());
    }

//    {"session_ID":"12112003","history":[],"query":"你好，我想了解一下南方科技大学的音乐会信息。","LLM_type":"ZHIPU","api_key":"7a2370c91d5cc8833fae73473d490fc1.cHO9Q5k31PnYuZ7Y","event":"query"}
    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userID") String userID) {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        log.info("服务端收到用户username={}的消息，正在推送给大模型服务", userID);
        rabbitTemplate.convertAndSend("ChatProduceExchange", "ChatProduceRouting", message);
    }

    @RabbitHandler
    public void process(byte[] bytes) {
        String testMessage = new String(bytes);
        log.info("收到消息:{}", testMessage);
        JSONObject jsonObject = JSON.parseObject(testMessage);
        String userID = jsonObject.getString("session_ID");
        String message = jsonObject.getString("response");
        if (sessionMap.containsKey(userID)) {
            sendMessage(message, sessionMap.get(userID));
        }else {
            log.error("用户{}不在线", userID);
        }
    }



    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息", toSession.getId());
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
