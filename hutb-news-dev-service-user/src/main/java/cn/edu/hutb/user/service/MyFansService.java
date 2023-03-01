package cn.edu.hutb.user.service;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.enums.Sex;
import cn.edu.hutb.pojo.vo.RegionRatioVO;

import java.util.List;

/**
 * @author 田章
 * @description
 * @date 2023/3/1
 */
public interface MyFansService {

    /**
     * 查询当前用户是否关注作家
     */
    boolean isMeFollowThisWriter(String writerId, String fanId);

    /**
     * 关注成为粉丝
     */
    void follow(String writerId, String fanId);

    /**
     * 粉丝取消关注
     */
    void unfollow(String writerId, String fanId);

    /**
     * 查询我的粉丝数
     */
    PageResult queryMyFansList(String writerId, Integer page, Integer pageSize);

    /**
     * 查询粉丝数
     */
    Integer queryFansCounts(String writerId, Sex sex);

    /**
     * 查询粉丝数
     */
    List<RegionRatioVO> queryRegionRatioCounts(String writerId);
}
