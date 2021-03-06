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

        // ????????????
        AttrEntity attrEntity = new AttrEntity();
        // ????????????
        BeanUtils.copyProperties(attr, attrEntity);
        // ????????????????????????
        this.save(attrEntity);

        // ????????????
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        // ??????????????????
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());

        // ???????????????
        attrAttrgroupRelationService.save(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        // ????????????ID??????
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        // ??????????????????
        String key = (String) params.get("key");
        if (!StringUtils.isNullOrEmpty(key)) {
            queryWrapper.and(query -> {

                query.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        // ???????????????????????????
        PageUtils pageUtils = new PageUtils(page);
        // ???????????????
        List<AttrEntity> records = page.getRecords();
//        System.out.println(records);
        // ????????????
        List<AttrRespVo> attrRespVos = records.stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
//            // ?????????VO
            BeanUtils.copyProperties(attrEntity, attrRespVo);
//            // ??????????????????????????????????????????
//            System.out.println(attrEntity.getAttrId() + "------");
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
//            System.out.println(relationEntity);
            if (relationEntity != null) {
                // ????????????
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());

                // ???????????????
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());

            }
            // ????????????
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                // ???????????????
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
//
        }).collect(Collectors.toList());
//        System.out.println(attrRespVos + "==========");
//        // ??????????????????
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        // 1.???????????????
        AttrEntity attrEntity = this.getById(attrId);

        // 2.???????????????vo???
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        // 3.??????????????????Id
        AttrAttrgroupRelationEntity attrgroupRelation = attrAttrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (attrgroupRelation != null) {
            attrRespVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
            // ???????????????
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupRelation.getAttrGroupId());
            if (attrGroupEntity != null) {

                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }

        }
        // ??????????????????
        Long catelogId = attrEntity.getCatelogId();

        Long[] cateLogPath = categoryService.findCateLogPath(catelogId);
        // 4.?????????????????????????????????
        attrRespVo.setCatelogPath(cateLogPath);
        // ???????????????
        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {

            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {

        // ????????????
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        // ??????????????????
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();

        // ????????????ID
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
        attrAttrgroupRelationService.saveOrUpdate(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrEntity.getAttrId()));
    }

    /**
     * ????????????Id???????????????????????????
     *
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId));
        // ?????????????????????ID
        List<Long> attrIds = entities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        // ??????ID??????????????????
        if (attrIds == null || attrIds.size() == 0) {
            return null;
        }
        List<AttrEntity> attrs = this.listByIds(attrIds);
        return attrs;
    }

    /**
     * ????????????id?????????ID??????
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
     * ???????????????????????????????????????
     *
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        // 1. ???????????????????????????????????????????????????????????????
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2. ??????????????????????????????????????????????????????
        // 2.1 ??????????????????????????????
        List<AttrGroupEntity> groups = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        List<Long> collect = groups.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        // 2.2 ?????????????????????????????????
        List<AttrAttrgroupRelationEntity> relationGroup = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", collect));
        // ??????
        List<Long> attrIds = relationGroup.stream().map(
                item -> item.getAttrId()
        ).collect(Collectors.toList());
        // 2.3 ???????????????????????????????????????????????????

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                .notIn("attr_id", attrIds);

        // ????????????
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