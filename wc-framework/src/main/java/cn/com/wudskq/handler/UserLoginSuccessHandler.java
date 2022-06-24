package cn.com.wudskq.handler;


import cn.com.wudskq.model.SysUserDetails;
import cn.com.wudskq.util.JWTTokenUtil;
import cn.com.wudskq.vo.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wudskq
 */
@Component
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        SysUserDetails sysUserDetails = (SysUserDetails) authentication.getPrincipal();
        String token = JWTTokenUtil.createAccessToken(sysUserDetails);
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        Response.responseJson(response, Response.response(0, "登录成功", tokenMap));
    }
}