package cn.edu.hutb.user.service.impl;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.api.page.PageUtils;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.Sex;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.pojo.Fans;
import cn.edu.hutb.pojo.vo.RegionRatioVO;
import cn.edu.hutb.user.mapper.FansMapper;
import cn.edu.hutb.user.service.MyFansService;
import cn.edu.hutb.user.service.UserService;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 田章
 * @description
 * @date 2023/3/1
 */
@Service
public class MyFansServiceImpl implements MyFansService {

    @Resource
    private FansMapper fansMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String[] REGIONS = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西",
            "山东", "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西",
            "甘肃", "青海", "台湾", "内蒙古", "广西", "西藏", "宁夏", "新疆", "香港", "澳门"};

    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fan = new Fans();
        fan.setFanId(fanId);
        fan.setWriterId(writerId);
        return fansMapper.selectOne(fan) != null;
    }

    @Transactional
    @Override
    public void follow(String writerId, String fanId) {
        // 获得粉丝用户的信息
        AppUser fanInfo = userService.getUser(fanId);

        Fans fans = new Fans();
        fans.setId(sid.nextShort());
        fans.setFanId(fanId);
        fans.setWriterId(writerId);

        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());

        fansMapper.insert(fans);

        // redis 作家粉丝数累加
        redisTemplate.opsForValue().increment(String.format(RedisConsts.FAN_COUNT_FORMATTER, writerId), 1);
        // redis 当前用户的（粉丝）关注数累加
        redisTemplate.opsForValue().increment(String.format(RedisConsts.FOLLOW_COUNT_FORMATTER, fanId), 1);
    }

    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);

        fansMapper.delete(fans);

        // redis 作家粉丝数累减
        redisTemplate.opsForValue().decrement(String.format(RedisConsts.FAN_COUNT_FORMATTER, writerId), 1);
        // redis 当前用户的（我的）关注数累减
        redisTemplate.opsForValue().decrement(String.format(RedisConsts.FOLLOW_COUNT_FORMATTER, fanId), 1);
    }

    @Override
    public PageResult listFans(String writerId, int page, int pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(fansMapper.select(fans), page);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);
        return fansMapper.selectCount(fans);
    }

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);

        List<RegionRatioVO> list = new ArrayList<>();
        for (String region : REGIONS) {
            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(region);
            fans.setProvince(region);
            regionRatioVO.setValue(fansMapper.selectCount(fans));
            list.add(regionRatioVO);
        }
        return list;
    }
}
