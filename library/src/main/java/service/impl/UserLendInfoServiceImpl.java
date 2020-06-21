package service.impl;

import entity.UserLendInfo;
import dao.UserLendInfoDao;
import service.UserLendInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户借书信息(UserLendInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-06-03 20:13:04
 */
@Service("userLendInfoService")
public class UserLendInfoServiceImpl implements UserLendInfoService {
    @Resource
    private UserLendInfoDao userLendInfoDao;

    /**
     * 管理员查询所有借书信息
     *
     * @return
     */
    public List<UserLendInfo> queryAll(int offset, int limit) {
        return userLendInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param userId
     * @return 实例对象
     */
    public List<UserLendInfo> queryById(Integer userId) {
        return this.userLendInfoDao.queryById(userId);
    }

    /**
     * 根据id或isbn查询借书信息
     *
     * @param userLendInfo
     * @return
     */
    public List<UserLendInfo> queryByIdorISBN(UserLendInfo userLendInfo) {
        return userLendInfoDao.queryByIdorISBN(userLendInfo);
    }

    /**
     * 查询单个信息
     *
     * @param userid
     * @param isbn
     * @return
     */
    public UserLendInfo selectOne(Integer userid, Integer isbn) {
        return userLendInfoDao.selectOne(userid, isbn);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<UserLendInfo> queryAllByLimit(int offset, int limit) {
        return this.userLendInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userLendInfo 实例对象
     * @return 实例对象
     */
    public UserLendInfo insert(UserLendInfo userLendInfo) {
        this.userLendInfoDao.insert(userLendInfo);
        return userLendInfo;
    }

    /**
     * 修改数据
     *
     * @param userLendInfo 实例对象
     * @return 实例对象
     */
    public int update(UserLendInfo userLendInfo) {
        return this.userLendInfoDao.update(userLendInfo);
    }

    /**
     * 通过主键删除数据
     *
     * @param userId
     * @return 是否成功
     */
    public boolean deleteById(Integer userId) {
        return this.userLendInfoDao.deleteById(userId) > 0;
    }
}