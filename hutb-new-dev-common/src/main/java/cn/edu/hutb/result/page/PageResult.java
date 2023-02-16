package cn.edu.hutb.result.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {
    /**
     * 当前页数
     */
    private int page;
    /**
     * 总页数
     */
    private long total;
    /**
     * 总记录数
     */
    private long records;
    /**
     * 每行显示的内容
     */
    private List<?> rows;
}
