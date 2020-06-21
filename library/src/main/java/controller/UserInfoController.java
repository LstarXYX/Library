package controller;

import entity.Blacklist;
import entity.UserInfo;
import org.springframework.stereotype.Controller;
import service.BlacklistService;
import service.UserInfoService;
import org.springframework.web.bind.annotation.*;
import status.ReturnStatus;
import vo.User;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息表(UserInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-29 15:53:43
 */
@CrossOrigin
@Controller
public class UserInfoController {
    /**
     * 用户服务对象
     */
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private BlacklistService blacklistService;

    /**
     * 通过主键查询单条数据
     * todo:判断是否有借书 有 更新借书的信息
     *
     * @param userId 主键
     * @return 单条数据
     */
    @RequestMapping("/user/login")
    public @ResponseBody
    User userLogin(Integer userId, String password, HttpSession session) throws NullPointerException {
        User user = new User();
        user.setUserInfo(this.userInfoService.login(userId, password));
        if (user.getUserInfo() == null) {
            user.setStatus(ReturnStatus.ERROR.getStatusCode());
            return user;
        }
        //是否黑名单
        Blacklist blacklist = blacklistService.queryById(userId);
        if (blacklist != null) {
            user.setStatus(ReturnStatus.BLACKUSER.getStatusCode());
        } else {
            user.setStatus(ReturnStatus.NORMAL.getStatusCode());
        }
        session.setAttribute("user", user);
        return user;
    }

    /**
     * 用户更新自己的信息
     * 1、昵称  2、密码
     *
     * @param userinfo
     * @param session
     * @return
     */
    //todo:@RequestBody
    @RequestMapping("/user/update")
    public @ResponseBody
    User userUpdate( UserInfo userinfo, HttpSession session) {
        User user = (User) session.getAttribute("user");

        userinfo.setUserId(user.getUserInfo().getUserId());
        //更新用户
        //获取更新后的值
        userinfo = userInfoService.updateByUser(userinfo);
        user.setUserInfo(userinfo);
        user.setStatus(ReturnStatus.NORMAL.getStatusCode());
        //设置session
        session.setAttribute("user", user);
        //返回新数据
        return user;
    }

    /**
     * 根据id删除用户:
     * 把isDelete值设1
     *
     * @param userid
     * @return
     */
    @RequestMapping("/admin/deluser")
    public @ResponseBody
    String deleteUserById(Integer userid) {
        boolean check = userInfoService.deleteById(userid);
        if (check) {
            return ReturnStatus.NORMAL.getResultStatus();
        }
        return ReturnStatus.ERROR.getResultStatus();
    }

    //todo:@RequestBody
    @RequestMapping("/admin/update")
    @ResponseBody
    public User adminUpdate(UserInfo userinfo) {
        User user = new User();
        //更新用户
        //获取更新后的值
        userinfo = userInfoService.updateByAdmin(userinfo);
        user.setUserInfo(userinfo);
        user.setStatus(ReturnStatus.NORMAL.getStatusCode());
        //返回新数据
        return user;
    }


    /**
     * 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping("/admin/queryuser")
    @ResponseBody
    public List<UserInfo> queryUser(Integer userId) {
        List<UserInfo>list = new ArrayList<UserInfo>();
        UserInfo userInfo = userInfoService.queryById(userId);
        if (userInfo==null){
            list.add(userInfo);
            return list;
        }
        list.add(userInfo);
        return list;
    }

    @RequestMapping("/admin/adduser")
    @ResponseBody
    public User addUserInfo(String username, String password) {
        UserInfo userInfo = new UserInfo();
        User user = new User();
        //判断是否空
        if (username == null || "".equals(username) || password == null || "".equals(password)){
            user.setStatus(ReturnStatus.ERROR.getStatusCode());
            return user;
        }
        //设置用户名密码
        userInfo.setUserName(username);
        userInfo.setPassword(password);
        userInfo.setRegisterDate(LocalDate.now().toString());
        //添加获取最新对象
        userInfo = userInfoService.insert(userInfo);
        //如果添加失败
        if (userInfo == null){
            user.setStatus(ReturnStatus.ERROR.getStatusCode());
            return user;
        }
        user.setUserInfo(userInfo);
        user.setStatus(ReturnStatus.NORMAL.getStatusCode());
        return user;
    }

    @RequestMapping("/admin/queryAlluser")
    @ResponseBody
    public List<UserInfo> queryAlluser(){
        return  userInfoService.queryAlluser();
    }

}