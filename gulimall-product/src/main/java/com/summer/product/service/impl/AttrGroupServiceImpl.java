package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.dao.AttrGroupDao;
import com.summer.product.entity.AttrEntity;
import com.summer.product.entity.AttrGroupEntity;
import com.summer.product.service.AttrGroupService;
import com.summer.product.service.AttrService;
import com.summer.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

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

    /**
     * 根据分类ID查出所有的分组以及这些组里面的属性
     *
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 1.查询分组信息
        List<AttrGroupEntity> attrGroups = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        // 2.查出组里面的所有属性
        List<AttrGroupWithAttrsVo> vos = attrGroups.stream().map(attrGroupEntity -> {
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            // 拷贝所有分组到VO中
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrsVo);
            // 查询分组中的属性
            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            // 把查询结果放到vo中
            attrGroupWithAttrsVo.setAttrs(attrs);
            return attrGroupWithAttrsVo;
        }).collect(Collectors.toList());

        return vos;
    }

}