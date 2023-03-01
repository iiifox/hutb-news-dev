package cn.edu.hutb.user.controller;

import cn.edu.hutb.api.controller.user.MyFansControllerApi;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.user.service.MyFansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 田章
 * @description
 * @date 2023/3/1
 */
@RestController
public class MyFansController implements MyFansControllerApi {

    @Autowired
    private MyFansService myFansService;

    @Override
    public JSONResult isMeFollowThisWriter(String writerId, String fanId) {
        return JSONResult.ok(myFansService.isMeFollowThisWriter(writerId, fanId));
    }

    @Override
    public JSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId, fanId);
        return JSONResult.ok();
    }

    @Override
    public JSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId, fanId);
        return JSONResult.ok();
    }

    @Override
    public JSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        return null;
    }

    @Override
    public JSONResult queryRatio(String writerId) {
        return null;
    }

    @Override
    public JSONResult queryRatioByRegion(String writerId) {
        return null;
    }
}
