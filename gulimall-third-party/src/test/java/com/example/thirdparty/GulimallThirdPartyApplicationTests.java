package com.example.thirdparty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class GulimallThirdPartyApplicationTests {
    @Resource
    private OSSClient ossClient;

    @Test
    void contextLoads() throws FileNotFoundException {
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "summer-gulimall";
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = "xxx.jpg";

        // 创建OSSClient实例。
        // OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = new FileInputStream("C:\\Users\\Mars\\Pictures\\Bili ScreenShot\\1800130fb96b4a6cba7292acd62af12d.png");

        ossClient.putObject(bucketName, objectName, inputStream);

        ossClient.shutdown();

        System.out.println("上传成功");
    }

}
