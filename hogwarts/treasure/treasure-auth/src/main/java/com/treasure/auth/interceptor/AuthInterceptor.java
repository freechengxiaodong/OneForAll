package com.treasure.auth.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.treasure.auth.annotation.CheckToken;
import com.treasure.auth.annotation.PassToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = (String) request.getParameter("token");
        //String token = (String) request.getHeader("token");
        log.info("token:{}", token);
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.info("HandlerMethod {}", handlerMethod);
        Method method = handlerMethod.getMethod();
        log.info("method {}", method);

        //是否免认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.value()) {
                return true;
            }
        }

        log.info("class {}", method.getDeclaringClass());
        //是否TOKEN校验
        if (method.getDeclaringClass().isAnnotationPresent(CheckToken.class)) {
            CheckToken checkToken = method.getDeclaringClass().getAnnotation(CheckToken.class);
            if (checkToken.value()) {
                if (token == null) {
                    throw new Exception("无token，请重新登录");
                }

                // 获取 token 中的 username
                String username;
                try {
                    username = JWT.decode(token).getAudience().get(0);
                } catch (Exception j) {
                    throw new Exception("token错误，请重新登录");
                }

                log.info("username:{}", username);

                // 验证 token  用户密码123456
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123456")).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new Exception("token错误，请重新登录");
                }

                return true;
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
