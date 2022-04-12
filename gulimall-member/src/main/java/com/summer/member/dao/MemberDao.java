package com.summer.member.dao;

import com.summer.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author summer
 * @email summer@gmail.com
 * @date 2022-04-12 12:31:38
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
