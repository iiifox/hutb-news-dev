package cn.edu.hutb.admin.service.impl;

import cn.edu.hutb.admin.mapper.CategoryMapper;
import cn.edu.hutb.admin.service.CategoryService;
import cn.edu.hutb.constant.RedisConsts;
import cn.edu.hutb.pojo.Category;
import cn.edu.hutb.result.CustomException;
import cn.edu.hutb.result.ResponseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    public CategoryMapper categoryMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional
    @Override
    public void save(Category category) {
        // 分类不会很多，所以id不需要自增，这个表的数据也不会多到几万甚至分表，数据都会集中在一起
        if (categoryMapper.insert(category) != 1) {
            throw new CustomException(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redisTemplate.delete(RedisConsts.ALL_CATEGORY);
    }

    @Transactional
    @Override
    public void update(Category category) {
        if (categoryMapper.updateByPrimaryKey(category) != 1) {
            throw new CustomException(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        // 直接使用redis删除缓存即可，用户端在查询的时候会直接查库，再把最新的数据放入到缓存中
        redisTemplate.delete(RedisConsts.ALL_CATEGORY);
    }

    @Override
    public boolean categoryIsExist(String catName, String oldCatName) {
        Example example = new Example(Category.class);
        Example.Criteria catCriteria = example.createCriteria().andEqualTo("name", catName);
        if (StringUtils.isNotBlank(oldCatName)) {
            catCriteria.andNotEqualTo("name", oldCatName);
        }

        List<Category> catList = categoryMapper.selectByExample(example);
        return catList != null && !catList.isEmpty();
    }

    @Override
    public List<Category> list() {
        return categoryMapper.selectAll();
    }

}
