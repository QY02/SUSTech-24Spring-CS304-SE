package org.eventCenter.fileServer.config;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.eventCenter.fileServer.utils.RedisUtil;
import org.eventCenter.fileServer.utils.Result;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String token = request.getHeader("token");
        if ((token == null) || (token.isBlank())) {
            token = request.getParameter("token");
        }
        String filePath = redisUtil.getAndDelete(token);
        if (filePath != null) {
            request.setAttribute("filePath", filePath);
            return true;
        } else {
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(Result.error(response, "401", "Invalid token")));
            } catch (IOException e) {
                log.warn("Unable to send response message " + e.getMessage());
                return false;
            }
            return false;
        }
    }
}
