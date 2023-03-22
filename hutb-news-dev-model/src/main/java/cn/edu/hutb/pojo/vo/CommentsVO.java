package cn.edu.hutb.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentsVO {

    private String commentId;
    private String fatherId;
    private String articleId;
    private String commentUserId;
    private String commentUserNickname;
    private String commentUserFace;
    private String content;
    private Date createTime;
    private String quoteUserNickname;
    private String quoteContent;
}