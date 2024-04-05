//package org.cs304.backend.utils;
//
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.ooad.ProjectHelperBackend.entity.GroupMessage;
//import com.ooad.ProjectHelperBackend.service.IGroupMessageService;
//import com.ooad.ProjectHelperBackend.service.IUserService;
//import jakarta.websocket.*;
//import jakarta.websocket.server.PathParam;
//import jakarta.websocket.server.ServerEndpoint;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//
///**
// * @author websocket服务
// */
//
//@Slf4j
//@ServerEndpoint(value = "/gmserver/{teamID}")
//@Component
//public class WebSocketGroupServer {
//
//    IGroupMessageService groupMessageService;
//
//    IUserService userService;
//
//    private static ApplicationContext applicationContext;
//
//    public static void setApplicationContext(ApplicationContext applicationContext) {
//        WebSocketGroupServer.applicationContext = applicationContext;
//    }
//
//    /**
//     * 记录当前在线连接数
//     */
//    public static final Map<Integer, Set<Session>> sessionMap = new ConcurrentHashMap<>();//<teamID,Set<Session>>
//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("teamID") Integer teamID) {
//        Set<Session> sessions = sessionMap.getOrDefault(teamID, new HashSet<>());
//        sessions.add(session);
//        sessionMap.put(teamID, sessions);
//        log.info("有新用户加入，teamID={}, 当前小组在线人数为：{}", teamID, sessions.size());
//    }
//
//    /**
//     * 连接关闭调用的方法
//     */
//    @OnClose
//    public void onClose(Session session, @PathParam("teamID") Integer teamID) {
//        Set<Session> sessions = sessionMap.get(teamID);
//        if (sessions != null) {
//            sessions.remove(session);
//            if (sessions.isEmpty()) {
//                sessionMap.remove(teamID);
//            } else {
//                sessionMap.put(teamID, sessions);
//            }
//        }
//        log.info("有一连接关闭，移除team={}的用户session", teamID);
//    }
//
//    /**
//     * 收到客户端消息后调用的方法
//     * 后台收到客户端发送过来的消息
//     * onMessage 是一个消息的中转站
//     * 接受 浏览器端 socket.send 发送过来的 json数据
//     *
//     * @param message 客户端发送过来的消息
//     */
//    @OnMessage
//    public void onMessage(String message, Session session, @PathParam("teamID") Integer teamID) {
//        groupMessageService = applicationContext.getBean(IGroupMessageService.class);
//        userService = applicationContext.getBean(IUserService.class);
//        log.info("服务端收到team={}的消息:{}", teamID, message);
//        JSONObject obj = JSONUtil.parseObj(message);
//        String senderID = obj.getStr("from"); // to表示发送给哪个用户，比如 admin
//        String text = obj.getStr("text"); // 发送的消息文本  hello
//        Set<Session> sessions = sessionMap.get(teamID);
//        if (sessions != null) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.set("from", senderID);  // from 是 zhang
//            jsonObject.set("fromName",userService.getById(senderID).getName());
//            jsonObject.set("text", text);  // text 同上面的text
//            for (Session toSession : sessions) {
//                this.sendMessage(jsonObject.toString(), toSession);
//            }
//            log.info("发送给team={}，消息：{}", teamID, jsonObject);
//        }
//        GroupMessage msg = new GroupMessage();
//        msg.setSenderId(senderID);
//        msg.setTeamId(teamID);
//        msg.setContent(text);
//        msg.setSendTime(LocalDateTime.now());
//        groupMessageService.save(msg);
//    }
//
//
//    @OnError
//    public void onError(Session session, Throwable error) {
//        log.error("发生错误");
//        error.printStackTrace();
//    }
//
//    /**
//     * 服务端发送消息给客户端
//     */
//    private void sendMessage(String message, Session toSession) {
//        try {
//            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
//            toSession.getBasicRemote().sendText(message);
//        } catch (Exception e) {
//            log.error("服务端发送消息给客户端失败", e);
//        }
//    }
//}
