package com.summer.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 14:02:40
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

