package cn.edu.hutb.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class IndexArticleVO {

    private String id;
    private String title;
    private Integer categoryId;
    private Integer articleType;
    private String articleCover;
    private Integer isAppoint;
    private Integer articleStatus;
    private String publishUserId;
    private Date publishTime;
    private Integer readCounts;
    private Integer commentCounts;
    private String mongoFileId;
    private Integer isDelete;
    private Date createTime;
    private Date updateTime;
    private String content;

    private AppUserVO publisherVO;
}