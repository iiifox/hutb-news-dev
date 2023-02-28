package cn.edu.hutb.api.controller.admin;

import cn.edu.hutb.pojo.bo.SaveOrUpdateFriendLinkBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 管理员端查询友情链接列表（包含isDelete为true的友情链接）
     *
     * @return 友情链接列表
     */
    @PostMapping("/getFriendLinkList")
    JSONResult list();

    /**
     * 删除友情链接
     *
     * @param linkId 友情链接id
     */
    @PostMapping("/delete")
    JSONResult delete(@RequestParam String linkId);

    /**
     * 门户端查询友情链接列表（不包含isDelete为true的友情链接）
     * @return 友情链接列表
     */
    @GetMapping("/portal/list")
    JSONResult queryPortalFriendLinkList();
}
