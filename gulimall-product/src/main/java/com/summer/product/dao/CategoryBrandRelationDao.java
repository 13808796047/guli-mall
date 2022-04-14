package com.summer.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.product.entity.CategoryBrandRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 品牌分类关联
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:34
 */
@Mapper
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

    void updateCategory(@Param("catId") Long catId, @Param("name") String name);
}
