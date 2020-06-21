package exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常类
 * @author L.star
 * @date 2020/6/6 22:17
 */
@Getter
@Setter
public class MyException extends Exception {

    private String message;

    public MyException(String message) {
        super(message);
        this.message = message;
    }
}
