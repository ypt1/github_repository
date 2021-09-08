package huiminpay;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import com.sun.deploy.net.URLEncoder;

import java.io.*;

public class MyTestQiNiu {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        upload();

//        downloadPublic();

//        downloadPrivate();

    }

    private static void downloadPrivate() throws UnsupportedEncodingException {
        String fileName = "FuZXRf6AO6nAVm__QdTxVFNbZ8el";
        String domainOfBucket = "http://qz1rki6zn.hn-bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);

        String accessKey = "OJVI_ge0bYNugw_9HnOoq2Z4vAbo2LBKQ3zp8eFe";
        String secretKey = "CYWVjzCklJ-vnv688SQE9K-2LkkMAiauQrKzNpQV";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
    }

    private static void downloadPublic() {
        String fileName = "FuZXRf6AO6nAVm__QdTxVFNbZ8el";
        String domainOfBucket = "http://qz1rki6zn.hn-bkt.clouddn.com";
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        System.out.println(finalUrl);
    }

    private static void upload() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "OJVI_ge0bYNugw_9HnOoq2Z4vAbo2LBKQ3zp8eFe";
        String secretKey = "CYWVjzCklJ-vnv688SQE9K-2LkkMAiauQrKzNpQV";
        String bucket = "huiminpay12";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\2823554072\\Desktop\\新建文件夹\\aa.png"));

            byte[] uploadBytes = IOUtils.toByteArray(fileInputStream);

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
