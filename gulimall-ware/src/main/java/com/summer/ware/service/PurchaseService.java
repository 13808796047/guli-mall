package com.summer.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 14:02:40
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

