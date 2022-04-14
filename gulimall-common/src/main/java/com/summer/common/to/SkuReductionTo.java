package com.summer.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Summer
 * @date 2022/4/15 1:28
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
