package cn.com.wudskq.utils;

import cn.com.wudskq.constants.SystemConstants;
import cn.com.wudskq.wapper.RequestWrapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenfangchao
 * @title: ServletUtil
 * @projectName wc-manager-system
 * @description: TODO
 * @date 2022/7/29 11:22 PM
 */
public class ServletUtils {

    /**
     * 获取请求的URI
     * @return
     */
    public static String getRequestURI(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        return requestURI;
    }

    /**
     * 获取请求的body数据
     * @return
     */
    public static String getRequestBodyParams(HttpServletRequest request){
        //获取请求参数map
        RequestWrapper requestWrapper = new RequestWrapper(request);
        String wrapperBody = requestWrapper.getBody();
        return wrapperBody;
    }

    public static String getTenantCodePermission(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tenantCodePermission = (String) request.getAttribute(SystemConstants.TENANT_CODE_PERMISSION);
        return tenantCodePermission;
    }
}
