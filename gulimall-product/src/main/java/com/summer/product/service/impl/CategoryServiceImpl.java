package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.dao.CategoryDao;
import com.summer.product.entity.CategoryEntity;
import com.summer.product.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> categories = baseMapper.selectList(null);
        // 组成父子的树形结构
        // 1.找到所有的一级分类
        List<CategoryEntity> parentCategory = categories.stream()
                .filter(category -> category.getParentCid() == 0)
                .map(category -> {
                    category.setChildren(getChildrens(category, categories));
                    return category;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return parentCategory;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity category, List<CategoryEntity> categories) {
        List<CategoryEntity> children = categories.stream().filter(item ->
                        item.getParentCid().equals(category.getCatId())
                )
                .map(item -> {
                    item.setChildren(getChildrens(item, categories));
                    return item;
                })
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return children;
    }
}