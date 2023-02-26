package cn.edu.hutb.admin.controller;

import cn.edu.hutb.admin.service.FriendLinkService;
import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.admin.FriendLinkControllerApi;
import cn.edu.hutb.pojo.bo.SaveOrUpdateFriendLinkBO;
import cn.edu.hutb.pojo.mongo.FriendLinkMO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author 田章
 * @description 首页友情链接维护
 * @date 2023/2/24
 */
@RestController
public class FriendLinkController extends BaseController
        implements FriendLinkControllerApi {

    @Autowired
    private FriendLinkService friendLinkService;

    @Override
    public JSONResult saveOrUpdate(SaveOrUpdateFriendLinkBO bo, BindingResult result) {
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }
        // BO --> MO
        FriendLinkMO mo = new FriendLinkMO();
        BeanUtils.copyProperties(bo, mo);
        mo.setCreateTime(new Date());
        mo.setUpdateTime(new Date());
        // 设置主键id
        mo.setId(mo.getLinkUrl());

        friendLinkService.saveOrUpdate(mo);
        return JSONResult.ok();
    }

    @Override
    public JSONResult list() {
        return JSONResult.ok(friendLinkService.list());
    }

    @Override
    public JSONResult delete(String linkId) {
        friendLinkService.delete(linkId);
        return JSONResult.ok();
    }

    @Override
    public JSONResult queryPortalFriendLinkList() {
        return JSONResult.ok(friendLinkService.listPortalFriendLink());
    }
}
