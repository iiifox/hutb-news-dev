package cn.edu.hutb.article.service.impl;

import cn.edu.hutb.api.page.PageResult;
import cn.edu.hutb.api.page.PageUtils;
import cn.edu.hutb.article.mapper.CommentsMapper;
import cn.edu.hutb.article.mapper.CommentsMapperCustom;
import cn.edu.hutb.article.service.ArticlePortalService;
import cn.edu.hutb.article.service.CommentService;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.Comments;
import cn.edu.hutb.pojo.vo.ArticleDetailVO;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import com.github.pagehelper.PageHelper;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 田章
 * @description
 * @date 2023/3/19
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private Sid sid;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private CommentsMapper commentsMapper;

    @Resource
    private CommentsMapperCustom commentsMapperCustom;

    @Transactional
    @Override
    public void insertComment(String articleId, String fatherCommentId, String content, String userId, String nickname, String face) {
        Comments comments = new Comments();
        comments.setId(sid.nextShort());

        comments.setArticleId(articleId);
        comments.setFatherId(fatherCommentId);
        comments.setContent(content);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);
        comments.setCommentUserFace(face);

        ArticleDetailVO article = articlePortalService.getDetail(articleId);
        comments.setWriterId(article.getPublishUserId());
        comments.setArticleTitle(article.getTitle());
        comments.setArticleCover(article.getCover());

        comments.setCreateTime(new Date());

        if (commentsMapper.insert(comments) != 1) {
            throw new CustomException(ResponseStatusEnum.FAILED);
        }
        // 评论数累加
        redisTemplate.opsForValue().increment(String.format(RedisConsts.ARTICLE_COMMENT_COUNTS_FORMATTER, articleId), 1);
    }

    @Override
    public PageResult listArticleComment(String articleId, int page, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleId", articleId);
        PageHelper.startPage(page, pageSize);
        return PageUtils.setterPage(commentsMapperCustom.listArticleComment(map), page);
    }
}
