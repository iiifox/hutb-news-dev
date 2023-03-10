package cn.edu.hutb.admin.service.impl;

import cn.edu.hutb.admin.repository.FriendLinkRepository;
import cn.edu.hutb.admin.service.FriendLinkService;
import cn.edu.hutb.enums.YesOrNo;
import cn.edu.hutb.pojo.mongo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 田章
 * @description
 * @date 2023/2/24
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkRepository friendLinkRepository;

    @Override
    public void saveOrUpdate(FriendLinkMO mo) {
        // save：有 id 就更新，无 id 则保存
        friendLinkRepository.save(mo);
    }

    @Override
    public List<FriendLinkMO> list() {
        return friendLinkRepository.findAll();
    }

    @Override
    public void delete(String id) {
        friendLinkRepository.deleteById(id);
    }

    @Override
    public List<FriendLinkMO> listPortalFriendLink() {
        return friendLinkRepository.getAllByIsDelete(YesOrNo.NO.type);
    }
}
