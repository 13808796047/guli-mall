package com.summer.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 12:49:45
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

