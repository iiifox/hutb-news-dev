package cn.edu.hutb.api.controller.admin;

import cn.edu.hutb.pojo.bo.SaveOrUpdateCategoryBO;
import cn.edu.hutb.result.JSONResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * 文章分类维护接口
 */
@RequestMapping("/categoryMng")
public interface CategoryMngControllerApi {

    /**
     * 新增或修改文章分类
     */
    @PostMapping("/saveOrUpdateCategory")
    JSONResult saveOrUpdate(@RequestBody @Valid SaveOrUpdateCategoryBO bo, BindingResult result);

    /**
     * 查询文章分类列表
     */
    @PostMapping("/getCatList")
    JSONResult list();

    /**
     * 用户端查询分类列表
     */
    @GetMapping("getCats")
    JSONResult userQueryCategoryList();
}
