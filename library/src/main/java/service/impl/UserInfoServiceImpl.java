package service.impl;

import dao.UserInfoDao;
import entity.UserInfo;
import org.springframework.stereotype.Service;
import service.UserInfoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息表接口的实现类
 * @author L.star
 * @date 2020/5/29 16:01
 */
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoDao userInfoDao;

    public UserInfo queryById(Integer userId) {
        return userInfoDao.queryById(userId);
    }

    public List<UserInfo> queryAlluser() {
        return userInfoDao.queryAlluser();
    }

    public UserInfo insert(UserInfo userInfo) {
        UserInfo check = null;
        //查询是否有用户名一样的人
        check = userInfoDao.queryByName(userInfo.getUserName());
        //如果有一样的人 返回空
        if (check != null){
            return null;
        }
        //如果没有就插入
        userInfoDao.insert(userInfo);
        return userInfo;
    }

    public UserInfo updateByUser(UserInfo userInfo) {
        userInfoDao.userupdate(userInfo);
        userInfo = userInfoDao.queryById(userInfo.getUserId());
        return userInfo;
    }

    public UserInfo updateByAdmin(UserInfo userInfo) {
        userInfoDao.adminupdate(userInfo);
        userInfo = userInfoDao.queryById(userInfo.getUserId());
        return userInfo;
    }


    public UserInfo lendBook(UserInfo userInfo) {
        userInfoDao.lendBook(userInfo);
        userInfo = userInfoDao.queryById(userInfo.getUserId());
        return userInfo ;
    }

    public boolean deleteById(Integer userId) {
        userInfoDao.deleteById(userId);
        return true;
    }

    public UserInfo login(Integer userId, String password) {
        return userInfoDao.login(userId,password);
    }


    /**
     * 将用户status设为0
     *
     * @param userId
     * @return
     */
    public int removeBlackList(Integer userId) {
        return userInfoDao.removeBlackList(userId);
    }
}
