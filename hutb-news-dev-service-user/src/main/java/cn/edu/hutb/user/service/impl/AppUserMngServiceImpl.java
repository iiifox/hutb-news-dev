package cn.edu.hutb.user.service.impl;


import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.api.page.PageUtils;
import cn.edu.hutb.enums.UserStatus;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.user.mapper.AppUserMapper;
import cn.edu.hutb.user.service.AppUserMngService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class AppUserMngServiceImpl implements AppUserMngService {

    @Resource
    public AppUserMapper appUserMapper;

    @Override
    public PageResult listByCondition(String nickname, Integer status, Date startDate, Date endDate, int page, int pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createTime").desc();

        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        if (UserStatus.validStatus(status)) {
            criteria.andEqualTo("activeStatus", status);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createTime", endDate);
        }

        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(appUserMapper.selectByExample(example), page);
    }

    @Transactional
    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        appUserMapper.updateByPrimaryKeySelective(user);
    }
}
