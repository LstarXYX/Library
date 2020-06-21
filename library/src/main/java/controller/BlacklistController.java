package controller;

import entity.Blacklist;
import entity.UserInfo;
import service.BlacklistService;
import org.springframework.web.bind.annotation.*;
import service.UserInfoService;
import status.ReturnStatus;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * 黑名单(Blacklist)表控制层
 *
 * @author makejava
 * @since 2020-06-01 22:45:32
 */
@CrossOrigin
@RestController
public class BlacklistController {
    /**
     * 服务对象
     */
    @Resource
    private BlacklistService blacklistService;

    @Resource
    private UserInfoService userInfoService;

    /**
     * 手动添加违规用户到违规记录表
     *
     *
     * @param userId
     * @return
     */
    @RequestMapping("/admin/addblacklist")
    @ResponseBody
    public Blacklist addBlackList(Integer userId) {
        Blacklist blacklist = null;
        UserInfo userInfo = userInfoService.queryById(userId);
        if(userInfo == null){
            //没有这个人
            blacklist = new Blacklist();
            blacklist.setStatus(ReturnStatus.ERROR.getStatusCode());
            return blacklist;
        }
        //查看是否有已加入
        blacklist = blacklistService.queryById(userId);
        //如果加入了
        if (blacklist!=null){
            //已经是黑名单
            blacklist.setStatus(ReturnStatus.BLACKUSER.getStatusCode());
            return blacklist;
        }
        //没加入 就新建一个添加进去
        blacklist = new Blacklist();
        //获取黑名单的信息
        String username = userInfoService.queryById(userId).getUserName();
        blacklist.setUserId(userId);
        blacklist.setUserName(username);
        blacklist.setDate(LocalDate.now().toString());
        //userinfo 的status设6
        userInfo.setStatus(6);
        //加入黑名单
        blacklist = blacklistService.insert(blacklist);
        //更新用户信息
        userInfoService.updateByAdmin(userInfo);
        blacklist.setStatus(ReturnStatus.NORMAL.getStatusCode());
        return blacklist;
    }


    /**
     * 手动删除违规用户的记录
     *
     * @param userId
     * @return
     */
    @RequestMapping("/admin/removeblacklist")
    @ResponseBody
    public String removeBlackList(Integer userId) {
        UserInfo userInfo = userInfoService.queryById(userId);
        if (userInfo == null){
            //没有这个学生
            return ReturnStatus.ERROR.getResultStatus();
        }
        //移除黑名单
        boolean check = blacklistService.deleteById(userId);

        if (check) {
            //            移除成功
            userInfoService.removeBlackList(userId);
            return ReturnStatus.NORMAL.getResultStatus();
        }
        //移除失败
        return ReturnStatus.ERROR.getResultStatus();
    }


    @RequestMapping("/admin/queryAllblackList")
    @ResponseBody
    public List<Blacklist> queryAllblackList(){
        Blacklist blacklist = null;
        return blacklistService.queryAll(blacklist);
    }

}