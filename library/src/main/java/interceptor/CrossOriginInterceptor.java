package interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置跨域允许
 * @author L.star
 * @date 2020/6/8 20:03
 */
public class CrossOriginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //允许跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        return true;
    }
}
