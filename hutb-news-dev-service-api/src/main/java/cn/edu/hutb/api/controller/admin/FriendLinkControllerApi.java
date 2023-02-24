package cn.edu.hutb.api.controller.admin;

import cn.edu.hutb.pojo.bo.SaveOrUpdateFriendLinkBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * @author 田章
 * @description 首页友情链接维护接口
 * @date 2023/2/24
 */
@RequestMapping("/friendLinkMng")
public interface FriendLinkControllerApi {

    /**
     * 新增/修改友情链接
     */
    @PostMapping("/saveOrUpdateFriendLink")
    JSONResult saveOrUpdate(@RequestBody @Valid SaveOrUpdateFriendLinkBO bo, BindingResult result);
}
