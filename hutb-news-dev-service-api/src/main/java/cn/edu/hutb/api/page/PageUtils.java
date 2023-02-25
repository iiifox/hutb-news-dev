package cn.edu.hutb.api.page;

import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 田章
 * @description 分页工具类
 * @date 2023/2/25
 */
public final class PageUtils {

    public static PageResult setterPage(List<?> list, int page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);

        PageResult result = new PageResult();
        result.setRows(list);
        result.setPage(page);
        result.setRecords(pageInfo.getTotal());
        result.setTotal(pageInfo.getPages());
        return result;
    }

}
