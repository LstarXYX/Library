package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 管理员(Admin)实体类
 *
 * @author makejava
 * @since 2020-06-02 15:18:37
 */
@Getter
@Setter
public class Admin implements Serializable {
    private static final long serialVersionUID = -76677378322371098L;
    
    private Integer adminId;
    
    private String adminName;
    
    private String adminPassword;

    /**
     * 返回的状态码
     */
    private Integer status;

}