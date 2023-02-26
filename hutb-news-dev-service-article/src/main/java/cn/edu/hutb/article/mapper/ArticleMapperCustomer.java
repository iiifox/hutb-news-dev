package cn.edu.hutb.article.mapper;

import cn.edu.hutb.api.mapper.MyMapper;
import cn.edu.hutb.pojo.Article;

/**
 * @author 田章
 * @description 非逆向工程自动生产的Mapper，搭配对应的xml文件编写SQL语句
 * @date 2023/2/26
 */
public interface ArticleMapperCustomer  extends MyMapper<Article> {

    /**
     * 更新定时发布为即时发布
     */
    void updateAppointToPublish();
}
