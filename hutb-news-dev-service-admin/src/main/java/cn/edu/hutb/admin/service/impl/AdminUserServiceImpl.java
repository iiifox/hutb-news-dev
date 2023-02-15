package cn.edu.hutb.admin.service.impl;

import cn.edu.hutb.admin.mapper.AdminUserMapper;
import cn.edu.hutb.admin.service.AdminUserService;
import cn.edu.hutb.pojo.AdminUser;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author 田章
 * @description
 * @date 2023/2/15
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser getAdminByUsername(String username) {
        Example example = new Example(AdminUser.class);
        example.createCriteria().andEqualTo("username", username);
        return adminUserMapper.selectOneByExample(example);
    }
}
