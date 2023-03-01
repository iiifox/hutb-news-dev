package cn.edu.hutb.api.controller.user;

import cn.edu.hutb.result.JSONResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 田章
 * @description 粉丝管理接口
 * @date 2023/3/1
 */
@RequestMapping("/fans")
public interface MyFansControllerApi {

    /**
     * 查询当前用户是否关注作家
     *
     * @param writerId 作家id
     * @param fanId    粉丝id
     * @return false:没关注 ture:关注
     */
    @PostMapping("/isMeFollowThisWriter")
    JSONResult isMeFollowThisWriter(@RequestParam String writerId, @RequestParam String fanId);


    /**
     * 用户关注作家，成为粉丝
     *
     * @param writerId 作家id
     * @param fanId    粉丝id
     */
    @PostMapping("/follow")
    JSONResult follow(@RequestParam String writerId, @RequestParam String fanId);

    /**
     * 取消关注，作家损失粉丝
     *
     * @param writerId 作家id
     * @param fanId    粉丝id
     */
    @PostMapping("/unfollow")
    JSONResult unfollow(@RequestParam String writerId,
                        @RequestParam String fanId);

    /**
     * 查询我的所有粉丝列表
     *
     * @param writerId 作家id
     * @param page     展示的第几页
     * @param pageSize 每页数据条数
     * @return 分页返回
     */
    @PostMapping("/queryAll")
    JSONResult queryAll(@RequestParam String writerId,
                        @RequestParam Integer page,
                        @RequestParam Integer pageSize);

    /**
     * 查询男女粉丝数量
     *
     * @param writerId 作家id
     * @return {@link cn.edu.hutb.pojo.vo.FansCountsVO}
     */
    @PostMapping("/queryRatio")
    JSONResult queryRatio(@RequestParam String writerId);

    /**
     * 根据地域查询粉丝数量
     *
     * @param writerId 作家id
     * @return {@link cn.edu.hutb.pojo.vo.RegionRatioVO}
     */
    @PostMapping("/queryRatioByRegion")
    JSONResult queryRatioByRegion(@RequestParam String writerId);
}
