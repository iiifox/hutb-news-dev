package cn.edu.hutb.admin.controller;


import cn.edu.hutb.admin.service.CategoryService;
import cn.edu.hutb.api.controller.BaseController;
import cn.edu.hutb.api.controller.admin.CategoryMngControllerApi;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.pojo.bo.SaveOrUpdateCategoryBO;
import cn.edu.hutb.result.JSONResult;
import cn.edu.hutb.result.ResponseStatusEnum;
import cn.edu.hutb.util.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class CategoryMngController extends BaseController
        implements CategoryMngControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    public JSONResult saveOrUpdate(SaveOrUpdateCategoryBO bo, BindingResult result) {
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            return JSONResult.errorMap(getErrors(result));
        }

        Category category = new Category();
        BeanUtils.copyProperties(bo, category);
        // id为空新增，不为空修改
        if (bo.getId() == null) {
            // 查询新增的分类名称不能重复存在
            if (categoryService.categoryIsExist(category.getName(), null)) {
                return JSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
            // 新增到数据库
            categoryService.save(category);
        } else {
            // 查询修改的分类名称不能重复存在
            if (categoryService.categoryIsExist(category.getName(), bo.getOldName())) {
                return JSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
            // 修改到数据库
            categoryService.update(category);
        }
        return JSONResult.ok();
    }

    @Override
    public JSONResult list() {
        return JSONResult.ok(categoryService.list());
    }

    @Override
    public JSONResult userQueryCategoryList() {
        // 先从redis中查询，如果有，则返回；如果没有，则查询数据库库后先放缓存后返回
        String allCategoryJson = redisTemplate.opsForValue().get(RedisConsts.ALL_CATEGORY);
        List<Category> categoryList;
        if ((categoryList = JsonUtils.jsonToList(allCategoryJson, Category.class)) == null) {
            redisTemplate.opsForValue().set(RedisConsts.ALL_CATEGORY,
                    Objects.requireNonNull(JsonUtils.objectToJson(categoryList = categoryService.list())));
        }
        return JSONResult.ok(categoryList);
    }
}
