package com.example.modules.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;
import com.example.modules.sys.service.ISysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;


/**
 * 系统日志
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private ISysLogService sysLogService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("sys:log:delete")
	public R delete(){
		sysLogService.deleteAll();
		return R.ok();
	}
}
