package com.summer.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 11:21:35
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

