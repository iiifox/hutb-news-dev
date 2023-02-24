package cn.edu.hutb.admin.service;


import cn.edu.hutb.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 新增文章分类
     */
    void save(Category category);

    /**
     * 修改文章分类列表
     */
    void update(Category category);

    /**
     * 查询分类名是否已经存在
     */
    boolean categoryIsExist(String catName, String oldCatName);

    /**
     * 获得文章分类列表
     */
    List<Category> list();

}
