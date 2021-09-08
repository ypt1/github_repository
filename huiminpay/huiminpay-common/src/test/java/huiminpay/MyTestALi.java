package huiminpay;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.qiniu.util.IOUtils;

import java.io.*;
import java.util.UUID;

public class MyTestALi {
    public static void main(String[] args) throws IOException {

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。
// 强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI5tLaQiMHAKjSUJRY9NCb";
        String accessKeySecret = "N8VUT8n5LuFyX5DVcbqxGRFiDwAkJl";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

// 填写Byte数组。
//        byte[] content = "Hello OSS".getBytes();

        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\2823554072\\Desktop\\新建文件夹\\aa.png"));

        byte[] content = IOUtils.toByteArray(fileInputStream);
// 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject("ypt12", UUID.randomUUID().toString()+".png", new ByteArrayInputStream(content));

// 关闭OSSClient。
        ossClient.shutdown();
    }
}
