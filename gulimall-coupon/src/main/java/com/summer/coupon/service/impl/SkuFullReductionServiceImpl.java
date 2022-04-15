package com.summer.coupon.service.impl;

import com.alibaba.nacos.common.utils.Objects;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.to.MemberPrice;
import com.summer.common.to.SkuReductionTo;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.coupon.dao.SkuFullReductionDao;
import com.summer.coupon.entity.MemberPriceEntity;
import com.summer.coupon.entity.SkuFullReductionEntity;
import com.summer.coupon.entity.SkuLadderEntity;
import com.summer.coupon.service.MemberPriceService;
import com.summer.coupon.service.SkuFullReductionService;
import com.summer.coupon.service.SkuLadderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;
    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        log.info("skuReductionTo{}", skuReductionTo);
        // 保存满减打折，会员价
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getFullCount() > 0) {

            skuLadderService.save(skuLadderEntity);
        }
        //
        log.info("skuReductionTo{}", skuReductionTo);
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        // 有满减价格
        if (skuFullReductionEntity.getFullPrice().compareTo(BigDecimal.ZERO) == 1) {

            this.save(skuFullReductionEntity);
        }
        // 会员价格
        if (Objects.nonNull(skuReductionTo.getMemberPrice())) {
            List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
            List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setMemberLevelId(item.getId());
                memberPriceEntity.setMemberLevelName(item.getName());
                memberPriceEntity.setMemberPrice(item.getPrice());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(item -> Objects.nonNull(item.getMemberPrice()) && item.getMemberPrice().compareTo(BigDecimal.ZERO) == 1).collect(Collectors.toList());
            memberPriceService.saveBatch(collect);
        }
    }

}