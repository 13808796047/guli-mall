package com.summer.member.feign;

import com.summer.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Summer
 * @date 2022/4/12 15:42
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @RequestMapping("coupon/coupon/member/coupons")
    public R memberCoupons();
}
