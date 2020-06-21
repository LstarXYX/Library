package service;

import entity.UserLendInfo;

import java.util.List;

/**
 * 用户借书信息(UserLendInfo)表服务接口
 *
 * @author makejava
 * @since 2020-06-03 20:13:04
 */
public interface UserLendInfoService {

    /**
     * 管理员查询所有借书信息
     *
     * @param offset
     * @param limit
     * @return List
     */
    List<UserLendInfo> queryAll(int offset, int limit);

    /**
     * 查询用户借书信息
     *
     * @param userId
     * @return 实例对象
     */
    List<UserLendInfo> queryById(Integer userId);

    /**
     * 根据id或isbn查询借书信息
     *
     * @param userLendInfo
     * @return
     */
    List<UserLendInfo> queryByIdorISBN(UserLendInfo userLendInfo);


    /**
     * 查询单个信息
     *
     * @param userid
     * @param isbn
     * @return
     */
    UserLendInfo selectOne(Integer userid, Integer isbn);


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<UserLendInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userLendInfo 实例对象
     * @return 实例对象
     */
    UserLendInfo insert(UserLendInfo userLendInfo);

    /**
     * 修改数据
     *
     * @param userLendInfo 实例对象
     * @return 影响行数
     */
    int update(UserLendInfo userLendInfo);

    /**
     * 通过主键删除数据
     *
     * @param userId
     * @return 是否成功
     */
    boolean deleteById(Integer userId);

}