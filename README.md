## 一、效果图
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901184552824.gif)
**后台获取的数据：**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901184902792.jpg)
我们去百度智能云人脸库查看，正是这个用户
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901185031450.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NDk5MDU5,size_16,color_FFFFFF,t_70)
项目源码已经上传到github，整个项目是springboot项目，下载下来就可以体验了。本工程对百度人脸识别接口进行了封装，理解简单。

下载地址：https://github.com/chenxingxing6/mycloud-admin

---
## 二、注册百度云智能云
https://cloud.baidu.com/?from=console
具体怎么操作，自己百度，然后创建自己人脸库，获取到对应的appKey,appSecret

百度开发文档
http://ai.baidu.com/docs#/Face-Java-SDK/top
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901185707330.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NDk5MDU5,size_16,color_FFFFFF,t_70)
---

## 三、代码实现
```html
基于Springboot框架搭建的，前端使用Vue,通过摄像机拍下照片后，请求后端人脸识别登陆服务，后台调用百度API人脸识别接口，进入百度
大脑搜索人脸识别即可获取官网的Secret Key，将前端获取的人脸信息的base64信息和你本地数据库里的人脸信息传到百度人脸识别的接口
进行人脸比对，返回一个json数据，result参数 带别人脸相似度, result可自己定义，从而实现人脸识别登录。
```
###### 核心代码
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190901190339650.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NDk5MDU5,size_16,color_FFFFFF,t_70)

---
##### 3.1引入Maven依赖
```xml
<!-- 人脸识别 jdk-->
<dependency>
	<groupId>com.baidu.aip</groupId>
	<artifactId>java-sdk</artifactId>
	<version>4.9.0</version>
	<exclusions>
		<exclusion>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-simple</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```

---
##### 3.2 创建FaceUtil，获取ApiFace【单例】
```java
package com.example.common.face.utils;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.util.Base64Util;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:04
 * Desc: 人脸识别工具
 */
public class FaceUtil {
    private static final String APP_ID = "15303677";
    private static final String APP_KEY = "OoOqVhksc3k6ZRNId8Apjd";
    private static final String SECRET_KEY = "unjW6yGDd2B50KulCWfNAgwA75wLI";

    private static volatile AipFace client = new AipFace(APP_ID, APP_KEY, SECRET_KEY);
    // 创建单例避免多次获取sdk
    public static AipFace getClient(){
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }

    /**
     * 编码
     * @param form
     * @return
     */
    public static String encodeBase64(byte[] form){
        return Base64Util.encode(form);
    }

    /**
     * 解码
     * @param data
     * @return
     */
    public static byte[] decodeBase64(String data){
        return Base64Util.decode(data);
    }
}

```

---
##### 3.3 创建FaceResultUtil，统一处理请求
```java
package com.example.common.face.utils;

import com.alibaba.fastjson.JSON;
import com.example.common.exception.BizException;
import com.example.common.face.constant.ErrorEnum;
import com.example.common.face.constant.FaceConstant;
import com.example.common.face.dto.FaceResult;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User: lanxinghua
 * Date: 2019/8/30 13:52
 * Desc: 结果工具封装
 */
public class FaceResultUtil {
    private static final Logger logger = LoggerFactory.getLogger("百度API接口请求结果解析");

    public static FaceResult isSuccess(JSONObject res){
        FaceResult result = parseJsonObject(res);
        if (!result.isSuccess()){
            // 对错误进行分类
            ErrorEnum errorEnum = ErrorEnum.getInstance(result.getErrorCode());
            if (errorEnum == null){
                throw new BizException("百度接口请求失败" + result.getErrorMsg());
            }else {
                throw new BizException(errorEnum.getCnDesc());
            }
        }
        return result;
    }


    /**
     * 解析JsonObject
     * @return
     */
    private static FaceResult parseJsonObject(JSONObject res){
        FaceResult faceResult = FaceResult.builder().build();
        try {
            String logId = res.has(FaceConstant.LOG_ID) ? res.get(FaceConstant.LOG_ID).toString() : "0";
            int errorCode = res.has(FaceConstant.ERROR_CODE) ? res.getInt(FaceConstant.ERROR_CODE) : -1;
            String errorMsg = res.has(FaceConstant.ERROR_MSG) ? res.getString(FaceConstant.ERROR_MSG) : "";
            int cached = res.has(FaceConstant.CACHED) ? res.getInt(FaceConstant.CACHED) : 0;
            long timestamp = res.has(FaceConstant.TIMESTAMP) ? res.getLong(FaceConstant.TIMESTAMP) : 0;
            Object dataString = res.has(FaceConstant.RESULT) ? res.get(FaceConstant.RESULT) : "";
            com.alibaba.fastjson.JSONObject data = null;
            if (dataString != null) {
                 data = com.alibaba.fastjson.JSONObject.parseObject(dataString.toString());
            }
            faceResult.setLogId(logId);
            faceResult.setErrorCode(errorCode);
            faceResult.setErrorMsg(errorMsg);
            faceResult.setCached(cached);
            faceResult.setTimestamp(timestamp);
            faceResult.setData(data);
        }catch (Exception e){
            logger.error("JSONObject解析失败", e);
        }
        return faceResult;
    }
}

```

---
##### 3.4 创建FaceResult
```java
package com.example.common.face.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 13:57
 * Desc: 请求百度API接口结果
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceResult implements Serializable {
    private String logId;

    private String errorMsg;

    private int cached;

    private int errorCode;

    private long timestamp;

    private JSONObject data;

    public boolean isSuccess(){
        return 0 == this.errorCode ? true : false;
    }
}

```
---
##### 3.5 创建ImageU
```java
package com.example.common.face.dto;

import com.example.common.face.constant.ImageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:25
 * Desc: 图像对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageU implements Serializable {

    private ImageTypeEnum imageTypeEnum;

    private String data;
}

```

##### 3.6 创建FaceUserDTO
```java
package com.example.common.face.dto;

import com.example.common.face.constant.FaceConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/30 12:12
 * Desc:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceUserDTO<T> implements Serializable {

    private String userId;

    private String groupId = FaceConstant.DEFAULT_GROUP_ID;

    private String faceToken;

    private T user;
}

```
---
##### 3.7 创建FaceManage[关键类]
```java
package com.example.common.face;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.FaceVerifyRequest;
import com.baidu.aip.face.MatchRequest;
import com.example.common.face.constant.ActionTypeEnum;
import com.example.common.face.constant.FaceConstant;
import com.example.common.face.constant.LivenessControlEnum;
import com.example.common.face.constant.QualityControlEnum;
import com.example.common.face.dto.FaceResult;
import com.example.common.face.dto.FaceUserDTO;
import com.example.common.face.dto.ImageU;
import com.example.common.face.utils.FaceResultUtil;
import com.example.common.face.utils.FaceUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * User: lanxinghua
 * Date: 2019/8/30 11:05
 * Desc: 人脸识别相关服务
 */
public class FaceManage {
    private static final Logger logger = LoggerFactory.getLogger(FaceManage.class);

    /**
     * 人脸注册
     */
    public static void faceRegister(FaceUserDTO userDTO, ImageU imageU){
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 用户资料
        options.put("user_info", JSON.toJSONString(userDTO));
        // 图片质量
        options.put("quality_control", QualityControlEnum.LOW.name());
        // 活体检测控制
        options.put("liveness_control", LivenessControlEnum.NONE.name());
        // 操作方式
        options.put("action_type", ActionTypeEnum.REPLACE.name());

        String image = imageU.getData();
        String imageType = imageU.getImageTypeEnum().name();
        String groupId = userDTO.getGroupId();
        String userId = userDTO.getUserId();

        // 人脸注册
        JSONObject res = FaceUtil.getClient().addUser(image, imageType, groupId, userId, options);
        FaceResultUtil.isSuccess(res);
        logger.info("人脸注册成功");
    }


    /**
     * 人脸更新
     */
    public static void faceUpdate(FaceUserDTO userDTO, ImageU imageU) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 用户资料
        options.put("user_info", JSON.toJSONString(userDTO));
        // 图片质量
        options.put("quality_control", QualityControlEnum.LOW.name());
        // 活体检测控制
        options.put("liveness_control", LivenessControlEnum.NONE.name());
        // 操作方式
        options.put("action_type", ActionTypeEnum.REPLACE.name());

        String image = imageU.getData();
        String imageType = imageU.getImageTypeEnum().name();
        String groupId = userDTO.getGroupId();
        String userId = userDTO.getUserId();

        // 人脸更新
        JSONObject res = FaceUtil.getClient().updateUser(image, imageType, groupId, userId, options);
        FaceResultUtil.isSuccess(res);
        logger.info("人脸更新成功 {}", res.toString(2));
    }


    /**
     * 人脸删除
     */
    public static void faceDelete(String userId, String groupId, String faceToken) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 人脸删除
        JSONObject res = FaceUtil.getClient().faceDelete(userId, groupId, faceToken, options);
        FaceResultUtil.isSuccess(res);
        logger.info("人脸删除成功 {}", res.toString(2));
    }


    /**
     * 用户信息查询
     */
    public static FaceUserDTO<String> findUser(String userId, String groupId) {
        HashMap<String, String> options = new HashMap<>();
        // 用户信息查询
        JSONObject res  = FaceUtil.getClient().getUser(userId, groupId, options);
        FaceResult result = FaceResultUtil.isSuccess(res);
        return JSON.parseObject(result.getData().toJSONString(), FaceUserDTO.class);
    }


    /**
     * 获取用户人脸列表
     * @throws Exception
     */
    public static FaceResult faceGetList(String userId, String groupId){
        HashMap<String, String> options = new HashMap<String, String>();
        // 获取用户人脸列表
        JSONObject res = FaceUtil.getClient().faceGetlist(userId, groupId, options);
        return FaceResultUtil.isSuccess(res);
    }


    /**
     * 获取用户列表
     */
    public static FaceResult listUserByGroupId(String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        options.put("length", "50");
        // 获取用户列表
        JSONObject res = FaceUtil.getClient().getGroupUsers(groupId, options);
        return FaceResultUtil.isSuccess(res);
    }


    /**
     * 删除用户
     */
    public static void deleteUser(String userId, String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 删除用户
        JSONObject res = FaceUtil.getClient().deleteUser(groupId, userId, options);
        FaceResultUtil.isSuccess(res);
        logger.info("用户删除成功 {}", res.toString(2));
    }


    /**
     * 创建用户组
     */
    public static void addGroup(String groupId) {
        HashMap<String, String> options = new HashMap<String, String>();
        // 创建用户组
        JSONObject res = FaceUtil.getClient().groupAdd(groupId, options);
        FaceResultUtil.isSuccess(res);
        logger.info("创建用户组 {}", res.toString(2));
    }


    /**
     * 删除用户组
     */
    public static void deleteGroup(String groupId){
        HashMap<String, String> options = new HashMap<String, String>();
        // 删除用户组
        JSONObject res = FaceUtil.getClient().groupDelete(groupId, options);
        FaceResultUtil.isSuccess(res);
        logger.info("删除用户组 {}", res.toString(2));
    }

    /**
     * 组列表查询
     */
    public static FaceResult listGroup() {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        options.put("length", "50");
        // 组列表查询
        JSONObject res = FaceUtil.getClient().getGroupList(options);
        return FaceResultUtil.isSuccess(res);
    }


    /**
     * 身份验证（没权限使用）
     */
    public static FaceResult personVerify(String idCardNumber, String realName, ImageU imageU){
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", QualityControlEnum.LOW.name());
        options.put("liveness_control", LivenessControlEnum.NONE.name());
        // 身份验证
       JSONObject res = FaceUtil.getClient().personVerify(imageU.getData(), imageU.getImageTypeEnum().name(), idCardNumber, realName, options);
       return FaceResultUtil.isSuccess(res);
    }

    /**
     * 人脸对比
     */
    public static int faceMatchScore(ImageU imageU1, ImageU imageU2){
        MatchRequest req1 = new MatchRequest(imageU1.getData(), imageU1.getImageTypeEnum().name());
        MatchRequest req2 = new MatchRequest(imageU2.getData(), imageU2.getImageTypeEnum().name());
        ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
        requests.add(req1);
        requests.add(req2);
        JSONObject res = FaceUtil.getClient().match(requests);
        FaceResult result = FaceResultUtil.isSuccess(res);
        // 对结果进行特殊处理
        Integer score = result.getData().getInteger(FaceConstant.SCORE);
        return score == null ? 0 : score;
    }


    /**
     * 人脸是否对比成功
     * @param imageU1
     * @param imageU2
     * @param score   匹配分数
     * @return
     */
    public static boolean isfaceMatch(ImageU imageU1, ImageU imageU2, Integer score){
        int defaultScore = FaceConstant.MATCH_SCORE;
        if (Objects.nonNull(score)){
            defaultScore = score;
        }
        return faceMatchScore(imageU1, imageU2) > defaultScore ? true : false;
    }

    /**
     * 人脸检测
     */
    public static FaceResult faceDetect(ImageU imageU) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        // 人脸检测
        JSONObject res = FaceUtil.getClient().detect(imageU.getData(), imageU.getImageTypeEnum().name(), options);
        return FaceResultUtil.isSuccess(res);
    }


    /**
     * 人脸搜索
     */
    public static FaceResult faceSearch(String groupIds, ImageU imageU) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("max_user_num", "1");
        options.put("quality_control", QualityControlEnum.LOW.name());
        options.put("liveness_control", LivenessControlEnum.NONE.name());
        // 人脸搜索
        JSONObject res = FaceUtil.getClient().search(imageU.getData(), imageU.getImageTypeEnum().name(), groupIds, options);
        return FaceResultUtil.isSuccess(res);
    }

    /**
     * 活体检测
     */
    public static FaceResult faceverify(ImageU imageU) {
        FaceVerifyRequest req = new FaceVerifyRequest(imageU.getData(), imageU.getImageTypeEnum().name());
        ArrayList<FaceVerifyRequest> list = new ArrayList<FaceVerifyRequest>();
        list.add(req);
        JSONObject res = FaceUtil.getClient().faceverify(list);
        return FaceResultUtil.isSuccess(res);
    }
}

```
##### 3.7 测试
```java
package com.example.play;

import com.alibaba.fastjson.JSON;
import com.example.common.face.FaceManage;
import com.example.common.face.constant.FaceConstant;
import com.example.common.face.constant.ImageTypeEnum;
import com.example.common.face.dto.FaceResult;
import com.example.common.face.dto.FaceUserDTO;
import com.example.common.face.dto.ImageU;
import com.example.common.face.utils.FaceUtil;
import com.example.common.utils.FilesUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

/**
 * User: lanxinghua
 * Date: 2019/8/29 20:17
 * Desc: 人脸识别测试
 */
public class FaceIdentificationTest {
    /**
     * 人脸注册,导入数据100张人脸图片
     */
    @Test
    public void test00() throws Exception{
        FaceUserDTO<String> userDTO = new FaceUserDTO<>();
        userDTO.setGroupId("group2");
        String filePath = "/Users/cxx/Downloads/entryPhoto/";
        File[] files = FilesUtil.listFile(filePath);
        int j = 0;
        for (File file : files) {
            int id = 7000 + j;
            j++;
            userDTO.setUserId(String.valueOf(id));
            InputStream is = new FileInputStream(new File(filePath + file.getName()));
            byte[] bytes = IOUtils.toByteArray(is);
            String data = FaceUtil.encodeBase64(bytes);
            ImageU imageU = ImageU.builder().data(data).imageTypeEnum(ImageTypeEnum.BASE64).build();
            userDTO.setUser("用户信息 group1 - " + id);
            try {
                FaceManage.faceRegister(userDTO, imageU);
            }catch (Exception e){
                System.out.println("注册失败 msg:" + e.getMessage());
                continue;
            }

        }
    }

    /**
     * 人脸注册
     */
    @Test
    public void test01() {
        FaceUserDTO<String> userDTO = new FaceUserDTO<>();
        userDTO.setGroupId("group1");
        userDTO.setUserId("6031");
        String image = "https://download.2dfire.com/mis/permanent/img2.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        userDTO.setUser("用户信息1");
        FaceManage.faceRegister(userDTO, imageU);
    }


    /**
     * 人脸更新
     */
    @Test
    public void test02() {
        FaceUserDTO<String> userDTO = new FaceUserDTO();
        userDTO.setGroupId("group1");
        userDTO.setUserId("6031");
        String image = "https://download.2dfire.com/mis/permanent/img2.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        userDTO.setUser("用户信息1");
        // 人脸更新
        FaceManage.faceUpdate(userDTO, imageU);
    }


    /**
     * 人脸删除接口
     */
    @Test
    public void test03() {
        String userId = "6030";
        String groupId = "group1";
        String faceToken = "5a1a8c17c40ea41264e8830017134972";
        FaceManage.faceDelete(userId, groupId, faceToken);
    }


    /**
     * 用户信息查询
     */
    @Test
    public void test04() {
        HashMap<String, String> options = new HashMap<>();
        String userId = "6030";
        String groupId = "group1";
        // 用户信息查询
        FaceUserDTO<String> userDTO = FaceManage.findUser(userId, groupId);
        System.out.println("用户信息：" + JSON.toJSONString(userDTO));
    }


    /**
     * 获取用户人脸列表
     */
    @Test
    public void test05() {
        String userId = "6030";
        String groupId = "group1";
        // 获取用户人脸列表
        FaceResult result = FaceManage.faceGetList(userId, groupId);
        String data = result.getData().getString(FaceConstant.FACE_LIST);
        System.out.println("人脸列表"+data);
    }


    /**
     * 获取用户列表
     */
    @Test
    public void test06() {
        String groupId = "group1";
        FaceResult result = FaceManage.listUserByGroupId(groupId);
        // 获取用户列表
        String userIds = result.getData().getString(FaceConstant.USER_ID_LIST);
        System.out.println("userIds" + userIds);
    }


    /**
     * 删除用户
     */
    @Test
    public void test07() {
        HashMap<String, String> options = new HashMap<String, String>();
        String groupId = "group1";
        String userId = "6031";
        // 删除用户
        FaceManage.deleteUser(userId, groupId);
    }


    /**
     * 创建用户组
     */
    @Test
    public void test08() {
        String groupId = "group2";
        FaceManage.addGroup(groupId);
    }


    /**
     * 删除用户组
     */
    @Test
    public void test09() {
        String groupId = "group2";
        FaceManage.deleteGroup(groupId);
    }


    /**
     * 组列表查询
     */
    @Test
    public void test10() {
        FaceResult result = FaceManage.listGroup();
        String groupIds = result.getData().getString(FaceConstant.GROUP_ID_LIST);
        System.out.println(groupIds);
    }


    /**
     * 身份验证（没权限使用）
     */
    @Test
    public void test11() {
        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        String idCardNumber = "235151251";
        String name = "陈星星";
        FaceManage.personVerify(idCardNumber, name, imageU);
    }


    /**
     * 人脸对比
     */
    @Test
    public void test12() {
        String image1 = "https://download.2dfire.com/mis/permanent/img1.jpg";
        String image2 = "https://download.2dfire.com/mis/permanent/img2.jpg";
        ImageU imageU1 = ImageU.builder().data(image1).imageTypeEnum(ImageTypeEnum.URL).build();
        ImageU imageU2 = ImageU.builder().data(image2).imageTypeEnum(ImageTypeEnum.URL).build();

        boolean match = FaceManage.isfaceMatch(imageU2, imageU1, 80);
        int matchScore = FaceManage.faceMatchScore(imageU2, imageU1);
        System.out.println("是否匹配：" + match);
        System.out.println("匹配等分：" + matchScore);
    }

    /**
     * 人脸检测
     */
    @Test
    public void test13() {
        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        FaceResult result = FaceManage.faceDetect(imageU);
        String data = result.getData().getString(FaceConstant.FACE_LIST);
        System.out.println(data);
    }


    /**
     * 人脸搜索
     */
    @Test
    public void test14() {
        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        String groupIds = "group1,group2";
        FaceResult result = FaceManage.faceSearch(groupIds, imageU);
        String users = result.getData().getString(FaceConstant.USER_LIST);
        System.out.println(users);
    }

    /**
     * 活体检测
     */
    @Test
    public void test15() {
        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        ImageU imageU = ImageU.builder().data(image).imageTypeEnum(ImageTypeEnum.URL).build();
        FaceResult result = FaceManage.faceverify(imageU);
        String users = result.getData().toJSONString();
        System.out.println(users);
    }
}

```

##### 3.8 人脸识别登陆Controller
```java
	/**
	 * 人脸登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/loginface", method = RequestMethod.POST)
	public R facelogin(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException("上传文件不能为空");
		}
		String groupIds = "group1";
		String data = FaceUtil.encodeBase64(file.getBytes());
		ImageU imageU = ImageU.builder().data(data).imageTypeEnum(ImageTypeEnum.BASE64).build();
		FaceResult result = FaceManage.faceSearch(groupIds, imageU);
		String users = result.getData().getString(FaceConstant.USER_LIST);
		if (StringUtils.isEmpty(users)){
			return R.error("用户不存在");
		}
		System.out.println("result: " + users);
		JSONArray array = JSONObject.parseArray(users);
		JSONObject object = JSONObject.parseObject(array.get(0).toString());
		Integer score = object.getInteger(FaceConstant.SCORE);
		if (score == null){
			return R.error("登录失败");
		}
		if (score >= FaceConstant.MATCH_SCORE){
			return R.error("登录成功");
		}
		return R.error("用户不存在");
	}
```
---

