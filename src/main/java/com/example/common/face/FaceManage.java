package com.example.common.face;

import com.alibaba.fastjson.JSON;
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
        logger.info("人脸注册成功 {}", res.toString(2));
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

}
