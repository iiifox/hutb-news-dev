package cn.edu.hutb.pojo.vo;

import lombok.Data;

@Data
public class AppUserVO {
    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;

    /**
     * 关注数
     */
    private Integer myFollowCounts;

    /**
     * 粉丝数
     */
    private Integer myFansCounts;
}