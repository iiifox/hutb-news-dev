package cn.edu.hutb.user.service.impl;

import cn.edu.hutb.enums.Sex;
import cn.edu.hutb.enums.UserStatus;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.user.mapper.AppUserMapper;
import cn.edu.hutb.user.service.UserService;
import cn.edu.hutb.util.DesensitizationUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 田章
 * @description
 * @date 2023/2/10
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private AppUserMapper appUserMapper;

    @Autowired
    private Sid sid;

    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example example = new Example(AppUser.class);
        example.createCriteria().andEqualTo("mobile", mobile);
        return appUserMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        AppUser user = new AppUser();
        /*
        互联网项目都要考虑可扩展性，如果未来的业务激增，那么就需要分库分表
        那么数据库表逐渐id必须保证全局（全库）唯一，不得重复
         */
        user.setId(sid.nextShort());
        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtils.mobilePhone(mobile));
        user.setFace("https://hutb-news-1314926265.cos.ap-shanghai.myqcloud.com/image/uid/22112064CXX989D4/221122B473F1NW28.png");

        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        user.setSex(Sex.SECRET.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);

        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);
        return user;
    }
}
