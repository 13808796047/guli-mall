package com.summer.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import com.summer.common.utils.PageUtils;
import com.summer.common.utils.Query;
import com.summer.product.dao.BrandDao;
import com.summer.product.entity.BrandEntity;
import com.summer.product.service.BrandService;
import com.summer.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 获取key
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> builder = new QueryWrapper<>();
        if (!StringUtils.isNullOrEmpty(key)) {
            builder.eq("brand_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                builder
        );

        return new PageUtils(page);
    }

    // 事务控制
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {

        // 保证关联表数据的更新
        this.updateById(brand);
        if (!StringUtils.isNullOrEmpty(brand.getName())) {
            // 同步更新其他关联表的属性
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
            // TODO 更新其他关联
        }
    }

}