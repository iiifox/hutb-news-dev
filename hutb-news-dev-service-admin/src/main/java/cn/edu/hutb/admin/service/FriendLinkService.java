package cn.edu.hutb.admin.service;

import cn.edu.hutb.pojo.mongo.FriendLinkMO;

import java.util.List;

public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    void saveOrUpdate(FriendLinkMO mo);

    /**
     * 查询友情链接
     */
    List<FriendLinkMO> list();

    /**
     * 删除友情链接
     */
    void delete(String id);

    /**
     * 首页查询友情链接
     */
     List<FriendLinkMO> listPortalFriendLink();
}
