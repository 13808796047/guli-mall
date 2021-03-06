package com.summer.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.to.SkuReductionTo;
import com.summer.common.utils.PageUtils;
import com.summer.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 12:13:42
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

