package com.summer.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.product.entity.SkuSaleAttrValueEntity;

import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

