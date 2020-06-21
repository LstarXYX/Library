package controller;

import entity.Admin;
import service.AdminService;
import org.springframework.web.bind.annotation.*;
import status.ReturnStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 管理员(Admin)表控制层
 *
 * @author makejava
 * @since 2020-06-02 15:18:37
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    /**
     * 服务对象
     */
    @Resource
    private AdminService adminService;

    /**
     * 通过主键查询单条数据
     *
     * @param adminId
     * @param adminPassword
     * @return 单条数据
     */
    @GetMapping("/login")
    @ResponseBody
    public Admin selectOne(Integer adminId, String adminPassword, HttpSession session) {
        Admin admin = this.adminService.login(adminId,adminPassword);
        if (admin == null) {
            admin = new Admin();
            admin.setStatus(ReturnStatus.ERROR.getStatusCode());
            session.setAttribute("admin",admin);
            return admin;
        }
        admin.setStatus(ReturnStatus.NORMAL.getStatusCode());
        session.setAttribute("admin",admin);
        return admin;
    }

}