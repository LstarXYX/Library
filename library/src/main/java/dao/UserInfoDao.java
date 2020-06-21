package dao;

import entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息表(UserInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-29 15:58:45
 */
@Repository("userInfoDao")
public interface UserInfoDao {

    UserInfo queryByName(String userName);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<UserInfo>queryAlluser();

    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    UserInfo queryById(Integer userId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<UserInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param userInfo 实例对象
     * @return 对象列表
     */
    List<UserInfo> queryAll(UserInfo userInfo);

    /**
     * 新增数据
     *
     * @param userInfo 实例对象
     * @return 影响行数
     */
    int insert(UserInfo userInfo);

    /**
     * 修改数据
     *
     * @param userInfo 实例对象
     * @return 影响行数
     */
    int userupdate(UserInfo userInfo);


    /**
     * 管理员修改用户数据
     *
     * @param userInfo
     * @return
     */
    int adminupdate(UserInfo userInfo);

    /**
     * 用户借书操作 更改可借书数
     *
     * @param userInfo
     * @return
     */
    int lendBook(UserInfo userInfo);

    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 影响行数
     */
    int deleteById(Integer userId);

    /**
     * 登录验证
     *
     * @param userId
     * @param password
     * @return
     */
    UserInfo login(@Param("userId") Integer userId, @Param("password") String password);

    /**
     * 将用户status设为0
     *
     * @return
     */
    int removeBlackList(Integer userId);


}