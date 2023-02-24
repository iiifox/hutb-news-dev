package cn.edu.hutb.admin.service;

import cn.edu.hutb.pojo.mongo.FriendLinkMO;

public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    void saveOrUpdate(FriendLinkMO mo);

}
