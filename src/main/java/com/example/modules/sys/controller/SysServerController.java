package com.example.modules.sys.controller;

import com.example.common.annotation.SysLog;
import com.example.common.utils.R;
import com.example.modules.sys.entity.server.Server;
import org.springframework.web.bind.annotation.*;

import java.util.WeakHashMap;


/**
 * 服务器监控信息
 */
@RestController
@RequestMapping("/sys/server")
public class SysServerController extends AbstractController {
	private WeakHashMap<String, Object> map = new WeakHashMap();

	/**
	 * 获取服务器信息
	 */
	@SysLog("获取服务器信息")
	@RequestMapping("/getInfo")
	public R getInfo(){
		if (map.get("server")!=null){
			return R.ok().put("server", map.get("server"));
		}
		Server server = new Server();
		try {
			server.copyTo();
		}catch (Exception e){
			e.printStackTrace();
		}
		map.put("server", server);
		return R.ok().put("server", server);
	}
}
