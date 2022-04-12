package com.summer.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

