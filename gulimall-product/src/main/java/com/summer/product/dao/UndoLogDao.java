package com.summer.product.dao;

import com.summer.product.entity.UndoLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLogEntity> {
	
}
