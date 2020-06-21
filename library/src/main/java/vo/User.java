package vo;

import entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author L.star
 * @date 2020/5/29 16:35
 */
@Getter
@Setter
public class User implements Serializable {
    private UserInfo userInfo;

    /**
     * 返回的状态码
     * 1 普通用户   0 管理员  -1 没有这用户
     */
    private int status;

}
