package com.summer.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

