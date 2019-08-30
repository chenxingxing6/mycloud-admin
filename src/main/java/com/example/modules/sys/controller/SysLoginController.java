package com.example.modules.sys.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.exception.BizException;
import com.example.common.face.FaceManage;
import com.example.common.face.constant.FaceConstant;
import com.example.common.face.constant.ImageTypeEnum;
import com.example.common.face.dto.FaceResult;
import com.example.common.face.dto.FaceUserDTO;
import com.example.common.face.dto.ImageU;
import com.example.common.face.utils.FaceUtil;
import com.example.modules.oss.cloud.OSSFactory;
import com.example.modules.oss.entity.SysOssEntity;
import com.example.modules.oss.service.ISysOssService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.example.common.utils.R;
import com.example.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * 登录相关
 */
@Controller
public class SysLoginController {
	@Autowired
	private Producer producer;
	@Autowired
	private ISysOssService sysOssService;

	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到shiro session
        ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
	}

	/**
	 * 登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public R login(String username, String password, String captcha) {
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return R.error("验证码不正确");
		}
		try{
			Subject subject = ShiroUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
		}catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		}catch (IncorrectCredentialsException e) {
			return R.error("账号或密码不正确");
		}catch (LockedAccountException e) {
			return R.error("账号已被锁定,请联系管理员");
		}catch (AuthenticationException e) {
			return R.error("账户验证失败");
		}
		return R.ok();
	}


	/**
	 * 人脸登录
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/loginface", method = RequestMethod.POST)
	public R facelogin(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException("上传文件不能为空");
		}
		//上传文件到oss
		// String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		// String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
		// System.out.println("用户上传图片：" + url);
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

	/**
	 * 人脸注册
	 */
	@ResponseBody
	@RequestMapping(value = "/sys/registerface", method = RequestMethod.POST)
	public R faceRegister(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new BizException("上传文件不能为空");
		}
		FaceUserDTO<String> userDTO = new FaceUserDTO<>();
		userDTO.setGroupId("group2");
		String data = FaceUtil.encodeBase64(file.getBytes());
		ImageU imageU = ImageU.builder().data(data).imageTypeEnum(ImageTypeEnum.BASE64).build();
		userDTO.setUser("用户信息 group2");
		FaceManage.faceRegister(userDTO, imageU);
		return R.ok("注册成功");
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		ShiroUtils.logout();
		return "redirect:login.html";
	}

}
