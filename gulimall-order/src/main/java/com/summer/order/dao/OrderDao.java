package com.summer.order.dao;

import com.summer.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 12:49:45
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
