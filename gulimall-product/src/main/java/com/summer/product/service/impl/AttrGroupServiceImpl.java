package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.dao.AttrGroupDao;
import com.summer.product.entity.AttrGroupEntity;
import com.summer.product.service.AttrGroupService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        String key = (String) params.get("key");
        // 组合条件
        QueryWrapper<AttrGroupEntity> builder = new QueryWrapper<>();
        if (!StringUtils.isNullOrEmpty(key)) {
            builder.and(query -> {
                query.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (categoryId != 0) {
            builder.eq("catelog_id", categoryId);

//            IPage<AttrGroupEntity> page = this.page(
//                    new Query<AttrGroupEntity>().getPage(params),
//
//                    builder
//            );
//            return new PageUtils(page);
        }

        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                builder
        );
        return new PageUtils(page);
    }

}