package com.summer.product.vo;

import lombok.Data;

/**
 * @author Summer
 * @date 2022/4/14 16:05
 */
@Data
public class AttrRespVo extends AttrVo {
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
