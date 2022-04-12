package com.summer.product;

import com.summer.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {
@Autowired
    BrandService brandService;
    @Test
    void contextLoads() {
        brandService.list().forEach(System.out::println);
    }

}
