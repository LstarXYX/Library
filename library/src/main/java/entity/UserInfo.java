package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户信息表(UserInfo)实体类
 *
 * @author makejava
 * @since 2020-05-29 15:53:34
 */
@Getter
@Setter
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -31309936540967410L;
    
    private Integer userId;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 密码
    */
    private String password;
    /**
    * 违规次数
    */
    private Integer status;
    /**
    * 可以借的书的数目
    */
    private Integer allowLendNum;

    /**
     * 用户是否已删除
     */
    private Integer isDelete;

    /**
     * 注册时间
     */
    private String registerDate;
}