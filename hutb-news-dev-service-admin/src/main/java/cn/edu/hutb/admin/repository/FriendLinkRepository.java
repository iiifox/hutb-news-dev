package cn.edu.hutb.admin.repository;

import cn.edu.hutb.pojo.mongo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author 田章
 * @description
 * @date 2023/2/24
 */
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO, String> {

}
