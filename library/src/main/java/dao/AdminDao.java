package dao;

import entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 管理员(Admin)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-02 15:18:37
 */
@Repository("adminDao")
public interface AdminDao {

    /**
     * 通过ID查询单条数据
     *
     * @param adminId
     * @param adminPassword
     * @return 实例对象
     */
    Admin login(@Param("adminId")Integer adminId,@Param("adminPassword")String adminPassword);



}