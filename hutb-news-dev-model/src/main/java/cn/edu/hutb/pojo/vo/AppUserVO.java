package cn.edu.hutb.pojo.vo;

import lombok.Data;

@Data
public class AppUserVO {
    private String id;
    private String nickname;
    private String face;
    private Integer activeStatus;

    private Integer myFollowCounts;
    private Integer myFansCounts;
}