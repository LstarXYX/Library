package service;

import entity.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员(Admin)表服务接口
 *
 * @author makejava
 * @since 2020-06-02 15:18:37
 */
public interface AdminService {

    /**
     * 通过ID查询单条数据
     *
     * @param adminId
     * @param adminPassword
     * @return 实例对象
     */
    Admin login(Integer adminId,String adminPassword);

}