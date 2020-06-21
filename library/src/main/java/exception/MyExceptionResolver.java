package exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 * @author L.star
 * @date 2020/6/6 22:21
 */
public class MyExceptionResolver implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        MyException myException = null;
        //判断是否是系统自定义异常，如果是，直接取出异常信息，错误页展示
        if (e instanceof MyException){
            myException = (MyException)e;
        }else {
            //如果不是，提示未知错误，请找系统管理员
            myException = new MyException("发生未知错误！请联系系统管理员处理！");
        }

        //错误信息
        String message = myException.getMessage();

        ModelAndView mv = new ModelAndView();

        //将错误信息传到页面
        mv.addObject("message",message);
        //定向到错误页面
        mv.setViewName("error");

        return mv;
    }
}
