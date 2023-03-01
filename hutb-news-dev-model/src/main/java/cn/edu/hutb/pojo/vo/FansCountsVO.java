package cn.edu.hutb.pojo.vo;

import lombok.Data;

/**
 * 粉丝数目展示类
 */
@Data
public class FansCountsVO {

    /**
     * 男粉丝数目
     */
    private Integer manCounts;

    /**
     * 女粉丝数目
     */
    private Integer womanCounts;
}