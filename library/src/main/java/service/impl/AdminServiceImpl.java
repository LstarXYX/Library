package service.impl;

import entity.Admin;
import dao.AdminDao;
import service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 管理员(Admin)表服务实现类
 *
 * @author makejava
 * @since 2020-06-02 15:18:37
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDao adminDao;

    /**
     * 管理员登录接口
     *
     * @param adminId
     * @param adminPassword
     * @return
     */
    public Admin login(Integer adminId, String adminPassword) {
        return adminDao.login(adminId,adminPassword);
    }
}