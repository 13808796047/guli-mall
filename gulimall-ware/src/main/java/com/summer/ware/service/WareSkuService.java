package com.summer.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.ware.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 14:02:40
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

