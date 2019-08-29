package com.example.play;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/8/29 20:17
 * Desc: 人脸识别测试
 */
public class FaceIdentificationTest {
    private static final String APP_ID = "15303677";
    private static final String APP_KEY = "OoOqVhksdmc3k6ZRNId8Apjd";
    private static final String SECRET_KEY = "unjW6yGDd2B50KQZfulCWfNAgwA75wLI";
    static AipFace client = null;
    static {
        client = new AipFace(APP_ID, APP_KEY, SECRET_KEY);
    }

    public static void main(String[] args) {

    }

    /**
     * 人脸注册
     */
    @Test
    public void test01() throws Exception{
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 用户资料
        options.put("user_info", "img1");
        // 图片质量
        options.put("quality_control", "LOW");
        // 活体检测控制
        options.put("liveness_control", "NONE");
        // 操作方式
        options.put("action_type", "REPLACE");

        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        String imageType = "URL";
        String groupId = "group1";
        String userId = "6030";

        // 人脸注册
        JSONObject res = client.addUser(image, imageType, groupId, userId, options);
        System.out.println(res.toString(2));
        System.out.println("注册成功");
    }


    /**
     * 人脸更新
     */
    @Test
    public void test02() throws Exception{
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 用户资料
        options.put("user_info", "小学生");
        // 图片质量
        options.put("quality_control", "LOW");
        // 活体检测控制
        options.put("liveness_control", "NONE");
        // 操作方式
        options.put("action_type", "REPLACE");

        String image = "https://download.2dfire.com/mis/permanent/img2.jpg";
        String imageType = "URL";
        String groupId = "group1";
        String userId = "6030";
        // 人脸更新
        JSONObject res = client.updateUser(image, imageType, groupId, userId, options);
        System.out.println(res.toString(2));
        System.out.println("人脸更新成功");
    }


    /**
     * 人脸删除接口
     * @throws Exception
     */
    @Test
    public void test03() throws Exception{
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        String userId = "6030";
        String groupId = "group1";
        String faceToken = "5a1a8c17c40ea41264e8830017134972";

        // 人脸删除
        JSONObject res = client.faceDelete(userId, groupId, faceToken, options);
        System.out.println(res.toString(2));
        System.out.println("人脸删除成功");
    }


    /**
     * 用户信息查询
     * @throws Exception
     */
    @Test
    public void test04() throws Exception{
        HashMap<String, String> options = new HashMap<>();
        String userId = "6030";
        String groupId = "group1";

        // 用户信息查询
        JSONObject res = client.getUser(userId, groupId, options);
        System.out.println(res.toString());
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(res.toString());
        System.out.println("用户信息：" + object.getString("result"));
    }


    /**
     * 获取用户人脸列表
     * @throws Exception
     */
    @Test
    public void test05() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        String userId = "6030";
        String groupId = "group1";

        // 获取用户人脸列表
        JSONObject res = client.faceGetlist(userId, groupId, options);
        System.out.println(res.toString(2));
    }


    /**
     * 获取用户列表
     * @throws Exception
     */
    @Test
    public void test06() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        options.put("length", "50");

        String groupId = "group1";

        // 获取用户列表
        JSONObject res = client.getGroupUsers(groupId, options);
        System.out.println(res.toString(2));
    }


    /**
     * 删除用户
     * @throws Exception
     */
    @Test
    public void test07() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        String groupId = "group1";
        String userId = "6031";

        // 删除用户
        JSONObject res = client.deleteUser(groupId, userId, options);
        System.out.println(res.toString(2));
    }


    /**
     * 创建用户组
     * @throws Exception
     */
    @Test
    public void test08() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        String groupId = "group2";
        // 创建用户组
        JSONObject res = client.groupAdd(groupId, options);
        System.out.println(res.toString(2));
    }


    /**
     * 删除用户组
     * @throws Exception
     */
    @Test
    public void test09() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();

        String groupId = "group2";

        // 删除用户组
        JSONObject res = client.groupDelete(groupId, options);
        System.out.println(res.toString(2));
    }

    /**
     * 组列表查询
     * @throws Exception
     */
    @Test
    public void test10() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("start", "0");
        options.put("length", "50");
        // 组列表查询
        JSONObject res = client.getGroupList(options);
        System.out.println(res.toString(2));
    }


    /**
     * 身份验证（没权限使用）
     * @throws Exception
     */
    @Test
    public void test11() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("quality_control", "LOW");
        options.put("liveness_control", "NONE");

        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        String imageType = "URL";
        String idCardNumber = "235151251";
        String name = "陈星星";

        // 身份验证
        JSONObject res = client.personVerify(image, imageType, idCardNumber, name, options);
        System.out.println(res.toString(2));
    }


    /**
     * 人脸对比
     * @throws Exception
     */
    @Test
    public void test12() throws Exception{
        String image1 = "https://download.2dfire.com/mis/permanent/img1.jpg";
        String image2 = "https://download.2dfire.com/mis/permanent/img2.jpg";

        // image1/image2也可以为url或facetoken, 相应的imageType参数需要与之对应。
        MatchRequest req1 = new MatchRequest(image1, "URL");
        MatchRequest req2 = new MatchRequest(image2, "URL");
        ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
        requests.add(req1);
        requests.add(req2);

        JSONObject res = client.match(requests);
        System.out.println(res.toString(2));
    }

    /**
     * 人脸检测
     * @throws Exception
     */
    @Test
    public void test13() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        //options.put("liveness_control", "LOW");

        String image = "https://download.2dfire.com/mis/permanent/img1.jpg";
        String imageType = "URL";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        System.out.println(res.toString(2));
    }


    /**
     * 人脸搜索
     * @throws Exception
     */
    @Test
    public void test14() throws Exception{
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("max_face_num", "1");
        options.put("quality_control", "LOW");
        options.put("liveness_control", "NONE");
        options.put("max_user_num", "3");

        //String image = "https://download.2dfire.com/mis/permanent/img2.jpg";
        String image = "https://console.bce.baidu.com/ai/s/facelib/face?appId=733341&groupId=group1&uid=6028&faceId=8c77dcb6be991cb1eeee298df719c187";
        String imageType = "URL";
        String groupIdList = "group1,group2";

        // 人脸搜索
        JSONObject res = client.search(image, imageType, groupIdList, options);
        System.out.println(res.toString(2));
    }


}
