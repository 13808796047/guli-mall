package com.summer.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.utils.PageUtils;
import com.summer.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 12:31:38
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

