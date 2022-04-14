package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.constant.ProductConstant;
import com.summer.product.dao.AttrAttrgroupRelationDao;
import com.summer.product.dao.AttrDao;
import com.summer.product.dao.AttrGroupDao;
import com.summer.product.entity.AttrAttrgroupRelationEntity;
import com.summer.product.entity.AttrEntity;
import com.summer.product.entity.AttrGroupEntity;
import com.summer.product.entity.CategoryEntity;
import com.summer.product.service.AttrAttrgroupRelationService;
import com.summer.product.service.AttrService;
import com.summer.product.service.CategoryService;
import com.summer.product.vo.AttrGroupRelationVo;
import com.summer.product.vo.AttrRespVo;
import com.summer.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), new QueryWrapper<AttrEntity>());

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {

        // 属性实体
        AttrEntity attrEntity = new AttrEntity();
        // 属性拷贝
        BeanUtils.copyProperties(attr, attrEntity);
        // 保存拷贝好的实体
        this.save(attrEntity);

        // 保存关联
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        // 设置关联字段
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());

        // 保存关系表
        attrAttrgroupRelationService.save(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        // 根据分类ID查询
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        // 根据查询条件
        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.and(query -> {

                query.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        // 查询分类名和分组名
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        // 流式编程
        List<AttrRespVo> attrRespVos = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            // 先查询关联关系中的分组和分类
            AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
            if (null != attr_id) {
                // 查询分组
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attr_id.getAttrGroupId());

                // 设置分组名
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());

            }
            // 查询分类
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                // 设置分类名
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;

        }).collect(Collectors.toList());
        // 设置到分页中
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        // 1.先查询自己
        AttrEntity attrEntity = this.getById(attrId);

        // 2.拷贝数据到vo中
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        // 3.设置所属分组Id
        AttrAttrgroupRelationEntity attrgroupRelation = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (attrgroupRelation != null) {
            attrRespVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
            // 分组名设置
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
            if (attrGroupEntity != null) {

                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }

        }
        // 设置分类信息
        Long catelogId = attrEntity.getCatelogId();

        Long[] cateLogPath = categoryService.findCateLogPath(catelogId);
        // 4.设置所属分组的完整路径
        attrRespVo.setCatelogPath(cateLogPath);
        // 设置分类名
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {

            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {

        // 修改自己
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        // 修改分组关联
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();

        // 设置分组ID
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
        attrAttrgroupRelationService.saveOrUpdate(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrEntity.getAttrId()));
    }

    /**
     * 根据分组Id查找关联的所有属性
     *
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId));
        // 查询所有的属性ID
        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        // 属性ID为空时不查询
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        List<AttrEntity> attrs = this.listByIds(attrIds);
        return attrs;
    }

    /**
     * 根据分组id和属性ID删除
     *
     * @param vos
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {

//        attrAttrgroupRelationService.remove(new QueryWrapper<>().eq(""))
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map(item -> {

            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());

        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    /**
     * 获取当前分组没有关联的数据
     *
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // 1. 当前分组只能关联自己所属分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2. 当前分组只能关联别的分组没有用的属性
        // 2.1 当前分类下的其他分组
        List<AttrGroupEntity> groups = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        List<Long> collect = groups.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        // 2.2 查出这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationGroup = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", collect));
        // 收集
        List<Long> attrIds = relationGroup.stream().map(
                item -> item.getAttrId()
        ).collect(Collectors.toList());
        // 2.3 从当前分类的所有属性中移除这些属性

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                .notIn("attr_id", attrIds);

        // 参数查询
        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.and(query -> {
                query.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;

    }

}