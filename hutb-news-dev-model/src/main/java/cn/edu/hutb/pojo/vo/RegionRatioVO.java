package cn.edu.hutb.pojo.vo;

import lombok.Data;

/**
 * 区域比例类
 */
@Data
public class RegionRatioVO {

    /**
     * 省份名
     */
    private String name;

    /**
     * 数目（本项目为该区域的粉丝数）
     */
    private Integer value;
}
