package com.summer.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.product.entity.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {


    void deleteBatchRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}
