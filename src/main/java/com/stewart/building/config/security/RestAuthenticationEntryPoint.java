package com.stewart.building.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stewart.building.common.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效访问接口时，自定义的返回结果
 * Created by macro on 2018/5/14.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * 当未登录或token失效时，返回JSON格式的结果；
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R bean = R.error("未登录，请登录");
        bean.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.close();
        out.flush();
    }
}
