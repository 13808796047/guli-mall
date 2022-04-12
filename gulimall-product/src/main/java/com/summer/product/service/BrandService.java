package com.summer.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

