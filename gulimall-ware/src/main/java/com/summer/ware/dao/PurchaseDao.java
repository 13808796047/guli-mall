package com.summer.ware.dao;

import com.summer.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 14:02:40
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
