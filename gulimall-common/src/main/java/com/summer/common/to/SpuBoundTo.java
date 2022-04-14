package com.summer.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Summer
 * @date 2022/4/15 1:14
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
