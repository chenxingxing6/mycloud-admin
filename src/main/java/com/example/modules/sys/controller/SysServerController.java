package com.example.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.annotation.SysLog;
import com.example.common.utils.Arith;
import com.example.common.utils.R;
import com.example.common.utils.RedisKeys;
import com.example.common.utils.RedisUtils;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.entity.server.*;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.vo.FileDownVo;
import com.example.modules.sys.vo.IndexVo;
import com.example.modules.sys.vo.SlowInterfaceVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * 服务器监控信息
 */
@RestController
@RequestMapping("/sys")
public class SysServerController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(SysServerController.class);
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private ISysDeptService sysDeptService;

	private static final String DEFAULT_IMG_PATH = "https://chenxingxing6.github.io/img/header.jpg";

	private WeakHashMap<String, Object> map = new WeakHashMap<>();

	@PostConstruct
	public void init(){
		Server server = getServerInfo();
		map.put("server", server);
		logger.info("初始化服务器信息");
	}

	/**
	 * 获取服务器信息
	 */
	@SysLog("获取服务器信息")
	@RequestMapping("/server/getInfo")
	public R getInfo(){
		Object server = map.get("server");
		if (server !=null){
			return R.ok().put("server",(Server) server);
		}
		return R.ok().put("server", getServerInfo());
	}

	/**
	 * 获取服务器信息
	 * @return
	 */
	private Server getServerInfo(){
		Server server = new Server();
		try {
			server.copyTo();
		}catch (Exception e){
			e.printStackTrace();
		}
		return server;
	}

	/**
	 * 获取登陆用户信息
	 * @return
	 */
	@Async
	public SysUserEntity getLoginUserInfo(){
		SysUserEntity loginUser = getUser();
		if (loginUser !=null) {
			Long superDeptId = sysDeptService.getSuperDeptId(loginUser.getDeptId());
			SysDeptEntity deptEntity = sysDeptService.selectById(superDeptId);
			String superDeptName = deptEntity == null ? "" : deptEntity.getName();
			loginUser.setDeptName(superDeptName);
			if (StringUtils.isEmpty(loginUser.getImgPath())){
				loginUser.setImgPath(DEFAULT_IMG_PATH);
			}
		}else {
			loginUser = new SysUserEntity();
		}
		loginUser.setSalt(null);
		return loginUser;
	}

	/**
	 * 获取文件下载列表
	 * @return
	 */
	@Async
	public List<FileDownVo> getFileDowns(){
		// TODO: 2019/4/6
		List<FileDownVo> vos = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			FileDownVo vo = new FileDownVo();
			vo.setName("蓝星花"+i+".java");
			vo.setDownNum(i*5);
			vos.add(vo);
		}
		return vos;
	}


	/**
	 * 获取系统慢接口
	 * @return
	 */
	@Async
	public List<SlowInterfaceVo> getSlowInterfaceVos(){
		// TODO: 2019/4/6
		List<SlowInterfaceVo> vos = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			SlowInterfaceVo vo = new SlowInterfaceVo();
			vo.setName("com.example.modules.sys.controller.SysMenuController.update"+i+"()");
			vo.setTime(String.valueOf(i*1000));
			vos.add(vo);
		}
		return vos;
	}


	/**
	 * 获取服务器信息
	 * @param vo
	 * @param server
	 * @return
	 */
	private void getServerInfo(IndexVo vo, Server server){
		try {
			Jvm jvm = server.getJvm();
			Cpu cpu = server.getCpu();
			Mem mem = server.getMem();
			SysFile sysFile = server.getSysFiles().get(0);
			//系统运行时长
			vo.setJvmRunTime(server.getJvm().getRunTime());
			//内存
			vo.setMemory(mem.getUsed()+"G/"+mem.getTotal()+"G");
			vo.setMemoryRate(String.valueOf(Arith.div(mem.getUsed(), mem.getTotal(), 2)));
			//jvm
			vo.setJvm(String.valueOf(jvm.getUsed())+"MB/"+String.valueOf(jvm.getTotal()+"MB"));
			vo.setJvmRate(String.valueOf(Arith.div(jvm.getUsed(), jvm.getTotal(), 2)));
			//cpu
			vo.setCpu(String.valueOf(cpu.getCpuNum())+"核/"+String.valueOf(cpu.getFree()+"(空闲率)"));
			vo.setCpuRate(String.valueOf(cpu.getFree()));
			//disk
			String userd = sysFile.getUsed().split(" ")[0];
			String total = sysFile.getTotal().split(" ")[0];
			vo.setDisk(userd+"G/"+total+"G");
			vo.setDiskRate(String.valueOf(Arith.div(Double.valueOf(userd), Double.valueOf(total), 2)));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据（浏览量，用户数量，企业网盘文件数量，最佳活跃用户）
	 * @param vo
	 * @return
	 */
	private void getManyData(IndexVo vo){
		// TODO: 2019/4/6
		Random r = new Random();
		vo.setSeeNum(r.nextInt(100));
		vo.setUserNum(r.nextInt(10));
		vo.setTodayDiskNum(r.nextInt(5));
		vo.setTotalDiskNum(r.nextInt(20));
		vo.setUserName("蓝星花");
	}


	/**
	 * 获取服务器信息
	 */
	@SysLog("获取首页信息")
	@RequestMapping("/index/getInfo")
	public R getIndexInfo(){
		SysUserEntity user = getUser();
		IndexVo indexVo = redisUtils.get(RedisKeys.INDEX_INFO_KEY + user.getUserId(), IndexVo.class);
		if (indexVo !=null){
			return R.ok().put("indexvo", indexVo);
		}
		indexVo = new IndexVo();
		//获取服务器信息
		Server server = getServerInfo();

		//获取登陆用户信息
		SysUserEntity loginUser = getLoginUserInfo();

		//获取文件下载列表
		List<FileDownVo> fileDownVos = getFileDowns();

		//获取系统慢接口
		List<SlowInterfaceVo> slowInterfaceVos = getSlowInterfaceVos();

		indexVo.setUser(loginUser);
		indexVo.setFileDownVos(fileDownVos);
		indexVo.setSlowInterfaceVos(slowInterfaceVos);

		getServerInfo(indexVo, server);

		//获取数据
		getManyData(indexVo);
		redisUtils.set(RedisKeys.INDEX_INFO_KEY + user.getUserId(), indexVo, RedisKeys.INDEX_INFO_TIME);
		return R.ok().put("indexvo", indexVo);
	}

	public static void main(String[] args) {

		System.out.println("223.4 GB");
	}
}
