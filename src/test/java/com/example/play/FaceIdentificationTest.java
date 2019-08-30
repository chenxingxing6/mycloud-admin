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
