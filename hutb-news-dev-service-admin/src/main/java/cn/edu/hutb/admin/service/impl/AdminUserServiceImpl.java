package cn.edu.hutb.admin.service.impl;

import cn.edu.hutb.admin.mapper.AdminUserMapper;
import cn.edu.hutb.admin.service.AdminUserService;
import cn.edu.hutb.pojo.AdminUser;
import cn.edu.hutb.pojo.bo.NewAdminBO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 田章
 * @description
 * @date 2023/2/15
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Autowired
    private Sid sid;

    @Override
    public AdminUser getAdminByUsername(String username) {
        Example example = new Example(AdminUser.class);
        example.createCriteria().andEqualTo("username", username);
        return adminUserMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    public void addAdmin(NewAdminBO bo) {
        AdminUser admin = new AdminUser();
        admin.setId(sid.nextShort());
        admin.setUsername(bo.getUsername());
        admin.setAdminName(bo.getAdminName());

        // 密码不为空，加密存入数据库
        String password = bo.getPassword();
        if (StringUtils.isNotBlank(password)) {
            admin.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }
        // 如果上传了人脸信息，则有faceId
        String faceId = bo.getFaceId();
        if (StringUtils.isNotBlank(faceId)) {
            admin.setFaceId(faceId);
        }

        admin.setCreatedTime(new Date());
        admin.setUpdatedTime(new Date());

        if (adminUserMapper.insert(admin) != 1) {
            throw new CustomException(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }
}
