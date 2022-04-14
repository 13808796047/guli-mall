package com.summer.product;

import com.summer.product.service.BrandService;
import com.summer.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

    @Test
    public void testFindPath() {

        Long[] cateLogPath = categoryService.findCateLogPath(225L);
        log.info("完整路径：{}", Arrays.asList(cateLogPath));
    }

    @Test
    void contextLoads() {
        brandService.list().forEach(System.out::println);
    }


}
