package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分类表(Category)实体类
 *
 * @author makejava
 * @since 2020-06-02 17:49:30
 */
@Getter
@Setter
public class Category implements Serializable {
    private static final long serialVersionUID = -37885099499791497L;
    
    private Integer categoryId;
    /**
    * 分类名
    */
    private String categoryName;



}