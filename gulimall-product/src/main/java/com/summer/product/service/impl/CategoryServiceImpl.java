package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.dao.CategoryDao;
import com.summer.product.entity.CategoryEntity;
import com.summer.product.service.CategoryBrandRelationService;
import com.summer.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 收集完整路径
     *
     * @param catelogId
     * @return
     */
    @Override // CategoryServiceImpl
    public Long[] findCateLogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        // 收集的时候是顺序 前端是逆序显示的 所以用集合工具类给它逆序一下
        // 子父 转 父子
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    // 事务控制
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {

        // 先更新自已
        this.updateById(category);
        // 更新关联
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    /**
     * 递归收集所有父分类
     */
    private List<Long> findParentPath(Long catlogId, List<Long> paths) {
        // 1、收集当前节点id
        paths.add(catlogId);// 比如父子孙层级，返回的是 孙 子 父
        CategoryEntity byId = this.getById(catlogId);
        if (byId.getParentCid() != 0) {
            // 递归
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
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