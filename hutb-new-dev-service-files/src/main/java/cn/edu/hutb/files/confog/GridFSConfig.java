package cn.edu.hutb.files.confog;

import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 田章
 * @description
 * @date 2023/2/23
 */
@Configuration
public class GridFSConfig {

    @Bean
    public GridFSBucket gridFSBucket(MongoClient mongoClient,
                                     @Value("${spring.data.mongodb.database}") String databaseName) {
        return GridFSBuckets.create(mongoClient.getDatabase(databaseName));
    }
}
