package com.summer.product.feign;

import com.summer.common.to.SkuReductionTo;
import com.summer.common.to.SpuBoundTo;
import com.summer.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Summer
 * @date 2022/4/15 1:09
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    /**
     * 1.CouponFeignService.saveSpuBounds(spuBoundTo);
     * 2.@RequestBody SpirngCloud会将对象转为JSON
     * 3.找到gulimall-coupon服务发送请求
     * 4.对方服务收到Json数据
     *
     * @param spuBoundTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
