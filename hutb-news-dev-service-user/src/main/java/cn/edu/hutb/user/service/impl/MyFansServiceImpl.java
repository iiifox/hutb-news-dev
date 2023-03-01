package cn.edu.hutb.user.service.impl;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.enums.Sex;
import cn.edu.hutb.pojo.AppUser;
import cn.edu.hutb.pojo.Fans;
import cn.edu.hutb.pojo.vo.RegionRatioVO;
import cn.edu.hutb.user.mapper.FansMapper;
import cn.edu.hutb.user.service.MyFansService;
import cn.edu.hutb.user.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    public PageResult queryMyFansList(String writerId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        return null;
    }

    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        return null;
    }
}
