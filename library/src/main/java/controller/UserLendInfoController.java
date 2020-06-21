package controller;

import entity.Blacklist;
import entity.Book;
import entity.UserInfo;
import entity.UserLendInfo;
import exception.MyException;
import service.BlacklistService;
import service.BookService;
import service.UserInfoService;
import service.UserLendInfoService;
import org.springframework.web.bind.annotation.*;
import status.BookLendStatus;
import status.ReturnStatus;
import utils.CountDays;
import vo.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 用户借书信息(UserLendInfo)表控制层
 *
 * @author makejava
 * @since 2020-06-03 20:13:04
 */
@CrossOrigin
@RestController
@RequestMapping("/lend")
public class UserLendInfoController {
    /**
     * 服务对象
     */
    @Resource
    private UserLendInfoService userLendInfoService;

    /**
     * 书本信息服务
     */
    @Resource
    private BookService bookService;

    /**
     * 用户信息服务
     */
    @Resource
    private UserInfoService userInfoService;

    /**
     * 黑名单信息服务
     */
    @Resource
    private BlacklistService blacklistService;


    /**
     * 学生查询借书信息
     *
     * @param
     * @return 单条数据
     */
    @RequestMapping("/queryLendInfo")
    @ResponseBody
    public List<UserLendInfo> queryLendInfoById(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Integer userId = user.getUserInfo().getUserId();
        return this.userLendInfoService.queryById(userId);
    }

    @RequestMapping("/queryAll")
    @ResponseBody
    public List<UserLendInfo> queryAll(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
        return userLendInfoService.queryAll(offset, limit);
    }


    /**
     * 更新单个借书信息
     *
     * @param userLendInfo
     * @return
     */
    //todo:@RequestBody
    @RequestMapping("/admin/updateUserLendInfo")
    @ResponseBody
    public String updateUserLendInfo(UserLendInfo userLendInfo) {
        Date date = userLendInfo.getReturnDay();
        String status = userLendInfo.getStatus();
        userLendInfo = userLendInfoService.selectOne(userLendInfo.getUserId(),userLendInfo.getIsbn());
        userLendInfo.setReturnDay(date);
        userLendInfo.setStatus(status);

        //书本信息
        Book book = bookService.queryById(userLendInfo.getIsbn());
        //用户信息
        UserInfo userInfo = userInfoService.queryById(userLendInfo.getUserId());
        //书本状态
        String bookstatus = userLendInfo.getStatus();
        //设置归还 库存加一
        book.setBookNum(book.getBookNum() + 1);
        //计算时间差
        Date now = null;
        if (userLendInfo.getReturnDay() == null){
            now = new Date();
        }else {
            now = userLendInfo.getReturnDay();
        }
        Date past = userLendInfo.getLendDay();
        int countDay = CountDays.daysOfTwo(past, now);
        //大于30天
        if (countDay > 30) {
            //超时
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //判断有无其他违规(破损，丢失）其他违规情节严重递增 覆盖前面 违规加一
        //如果损坏
        if (bookstatus.equals(BookLendStatus.BREAK.getStatus())) {
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //如果丢失
        else if (bookstatus.equals(BookLendStatus.LOSING.getStatus())) {
            //库存重新减一
            book.setBookNum(book.getBookNum() - 1);
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //可借书数加一
        userInfo.setAllowLendNum(userInfo.getAllowLendNum()+1);
        if (userInfo.getAllowLendNum()>5){
            userInfo.setAllowLendNum(5);
        }
        //如果违规次数大于5 加入违规记录名单
        if (userInfo.getStatus() > 5) {
            Blacklist blacklist = new Blacklist();
            blacklist.setUserName(userInfo.getUserName());
            blacklist.setUserId(userInfo.getUserId());
            blacklist.setDate(LocalDate.now().toString());
            blacklistService.insert(blacklist);
        }
        try {
            //更新数据
            userLendInfoService.update(userLendInfo);
            //更新成功
            bookService.update(book);
            userInfoService.updateByAdmin(userInfo);
            return ReturnStatus.NORMAL.getResultStatus();
        }catch (Exception e){
            return ReturnStatus.ERROR.getResultStatus();
        }
    }

    /**
     * 用户借书
     * 判断是否是黑名单 黑名单就不能借书
     * 判断是否已经借够5本，超过五本就不能借
     *
     * @return String
     */
    //todo:@RequestBody
    @RequestMapping("/lendBook")
    @ResponseBody
    public String userLendBook(Book book, HttpSession session) throws Exception {
        //获取用户
        User user = (User) session.getAttribute("user");
        //获取用户id
        Integer userId = user.getUserInfo().getUserId();
        //如果是黑名单 返回
        if (user.getStatus() == ReturnStatus.BLACKUSER.getStatusCode()) {
            return ReturnStatus.BLACKUSER.getResultStatus();
        }
        //用户可以借书的数目
        int allowLendNum = user.getUserInfo().getAllowLendNum();
        //可借书数量减一
        allowLendNum--;
        //判断可借书的数目是否小于0 小于则不能借 返回
        if (allowLendNum < 0) {
            return ReturnStatus.OUTOFNUM.getResultStatus();
        }
        //剩下的可以借
        //格式化时间
        Date time = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = simpleDateFormat.format(time);
        try {
            time = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new MyException("日期格式化错误，请重试");
        }
        //设置用户借书信息
        UserLendInfo userLendInfo = new UserLendInfo();
        userLendInfo.setUserId(userId);
        userLendInfo.setLendDay(time);
        userLendInfo.setStatus(BookLendStatus.Lending.getStatus());
        //插入书本信息
        //库存减一 是否有库存
        //获取书本
        book = bookService.queryById(book.getIsbn());
        book.setBookNum(book.getBookNum() - 1);
        if (book.getBookNum() < 0) {
            return ReturnStatus.ERROR.getResultStatus();
        }
        //是否借了相同的书 || 如果之前有损坏/丢失相同的书则返回500
        UserLendInfo check = userLendInfoService.selectOne(userId, book.getIsbn());
        if (check != null){
           if (check.getStatus().equals(BookLendStatus.Lending.getStatus())) {
                return ReturnStatus.ERROR.getResultStatus();
            }else if (check.getStatus().equals(BookLendStatus.LOSING.getStatus())){
               return ReturnStatus.ERROR.getResultStatus();
           }else if (check.getStatus().equals(BookLendStatus.BREAK.getStatus())){
               return ReturnStatus.ERROR.getResultStatus();
           }
        }
        userLendInfo.setIsbn(book.getIsbn());
        userLendInfo.setLendBookName(book.getTitle());
        userLendInfoService.insert(userLendInfo);
        //更新书本
        bookService.update(book);

        //更新用户信息
        UserInfo userInfo = user.getUserInfo();
        //可借书的数目减少
        userInfo.setAllowLendNum(allowLendNum);
        userInfoService.lendBook(userInfo);
        //重新设置sesssion
        user.setUserInfo(userInfo);
        session.setAttribute("user", user);
        //返回成功
        return ReturnStatus.NORMAL.getResultStatus();
    }


    /**
     * 还书 库存加一 标明归还
     * 计算还书当天和借出那天的时间 如果超过30天就超时 userinfo的status加一 当次数超过一定的时候就加入黑名单
     * 标明状态是破损 则status加一 超过一定次数加入黑名单
     * 状态丢失 status加1 库存不加一 status加一
     *
     * @param isbn
     * @param bookstatus
     * @return
     */
    @RequestMapping("/returnBook")
    @ResponseBody
    public String returnBook(Integer isbn, String bookstatus, HttpSession session) {
        //获取书本信息
        Book book = bookService.queryById(isbn);
        //获取用户信息
        User user = (User) session.getAttribute("user");
        UserInfo userInfo = user.getUserInfo();
        Integer userId = userInfo.getUserId();
        //获取借书信息
        UserLendInfo userLendInfo = userLendInfoService.selectOne(userId, isbn);
        //设置归还 库存加一
        userLendInfo.setStatus(BookLendStatus.RETURNED.getStatus());
        book.setBookNum(book.getBookNum() + 1);
        //计算时间差
        Date now = new Date();
        Date past = userLendInfo.getLendDay();
        int countDay = CountDays.daysOfTwo(past, now);
        //大于30天
        if (countDay > 30) {
            //超时
            userLendInfo.setStatus(BookLendStatus.TIMEOUT.getStatus());
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //判断有无其他违规(破损，丢失）其他违规情节严重递增 覆盖前面 违规加一
        //如果损坏
        if (bookstatus.equals(BookLendStatus.BREAK.getStatus())) {
            userLendInfo.setStatus(BookLendStatus.BREAK.getStatus());
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //如果丢失
        else if (bookstatus.equals(BookLendStatus.LOSING.getStatus())) {
            userLendInfo.setStatus(BookLendStatus.LOSING.getStatus());
            //库存重新减一
            book.setBookNum(book.getBookNum() - 1);
            //违规加一
            userInfo.setStatus(userInfo.getStatus() + 1);
        }
        //可借书数加一
        userInfo.setAllowLendNum(userInfo.getAllowLendNum()+1);

        //如果违规次数大于5 加入违规记录名单
        if (userInfo.getStatus() > 5) {
            Blacklist blacklist = new Blacklist();
            blacklist.setUserName(userInfo.getUserName());
            blacklist.setUserId(userInfo.getUserId());
            blacklist.setDate(LocalDate.now().toString());
            blacklistService.insert(blacklist);
            user.setStatus(ReturnStatus.BLACKUSER.getStatusCode());
        }
        //更新数据
        bookService.update(book);
        userInfo = userInfoService.updateByAdmin(userInfo);
        userLendInfo.setReturnDay(now);
        userLendInfoService.update(userLendInfo);
        user.setUserInfo(userInfo);
        session.setAttribute("user", user);
        //返回成功信息
        return ReturnStatus.NORMAL.getResultStatus();
    }


    /**
     * 根据id或isbn查询借书信息
     */
    @RequestMapping("/queryByLimit")
    @ResponseBody
    public List<UserLendInfo>queryByIdorISBN(@RequestParam(required = false)Integer userId,@RequestParam(required = false)Integer isbn)throws Exception{
        UserLendInfo userLendInfo = new UserLendInfo();
        if(userId != null){
            userLendInfo.setUserId(userId);
        } else if (isbn != null){
            userLendInfo.setIsbn(isbn);
        }else {
            throw new MyException("请填写正确的参数");
        }
        return userLendInfoService.queryByIdorISBN(userLendInfo);
    }


}