package com.example.modules.front.controller;


import com.example.common.annotation.SysLog;
import com.example.common.constants.UserEnum;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;
import com.example.common.validator.Assert;
import com.example.common.validator.ValidatorUtils;
import com.example.common.validator.group.AddGroup;
import com.example.common.validator.group.UpdateGroup;
import com.example.modules.sys.controller.AbstractController;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysUserRoleService;
import com.example.modules.sys.service.ISysUserService;
import com.example.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 前台用户
 *
 * @author lanxinghua
 * @date 2019年3月17日 上午10:40:10
 */

@RestController
@RequestMapping("/front/user")
public class AppUserController extends AbstractController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysUserRoleService sysUserRoleService;

	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		params.put("type", UserEnum.FRONT.getType());
		PageUtils page = sysUserService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		user.setType(Integer.valueOf(UserEnum.FRONT.getType()));
		sysUserService.save(user);
		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		user.setType(Integer.valueOf(UserEnum.FRONT.getType()));
		sysUserService.update(user);
		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		sysUserService.deleteBatchIds(Arrays.asList(userIds));
		return R.ok();
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.selectById(userId);
		return R.ok().put("user", user);
	}
}
