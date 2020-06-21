package service;

import entity.UserInfo;
import vo.User;

import java.util.List;

/**
 * 用户信息表(UserInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-29 15:53:43
 */
public interface UserInfoService {

    /**
     * 查询用户列表
     *
     * @return
     */
    List<UserInfo> queryAlluser();


    /**
     * 通过ID查询单条数据
     *
     * @param userId 主键
     * @return 实例对象
     */
    UserInfo queryById(Integer userId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
//    List<UserInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userInfo 实例对象
     * @return 实例对象
     */
    UserInfo insert(UserInfo userInfo);

    /**
     * 用户修改自己数据
     *
     * @param userInfo 实例对象
     * @return 实例对象
     */
    UserInfo updateByUser(UserInfo userInfo);

    /**
     * 管理员修改用户数据
     *
     * @param userInfo
     * @return
     */
    UserInfo updateByAdmin(UserInfo userInfo);


    /**
     * 通过主键删除数据
     *
     * @param userId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer userId);

    /**
     * 用户借书
     *
     * @param userInfo
     * @return
     */
    UserInfo lendBook(UserInfo userInfo);


    /**
     * 登录验证
     *
     * @param userId
     * @param password
     * @return
     */
    UserInfo login(Integer userId, String password);


    /**
     * 将用户status设为0
     *
     * @param userId
     * @return
     */
    int removeBlackList(Integer userId);
}