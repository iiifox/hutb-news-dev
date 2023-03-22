package cn.edu.hutb.article.mapper;

import cn.edu.hutb.pojo.vo.CommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommentsMapperCustom {

    /**
     * 查询文章评论
     */
    List<CommentsVO> listArticleComment(@Param("paramMap") Map<String, Object> map);

}