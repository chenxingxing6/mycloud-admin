package com.example.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.example.common.annotation.DataFilter;
import com.example.common.utils.Constant;
import com.example.modules.sys.dao.SysDeptDao;
import com.example.modules.sys.entity.SysDeptEntity;
import com.example.modules.sys.service.ISysDeptService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements ISysDeptService {
	
	@Override
	@DataFilter(subDept = true, user = false)
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		List<SysDeptEntity> deptList =
			this.selectList(new EntityWrapper<SysDeptEntity>()
			.addFilterIfNeed(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER)));
		for(SysDeptEntity sysDeptEntity : deptList){
			SysDeptEntity parentDeptEntity =  this.selectById(sysDeptEntity.getParentId());
			if(parentDeptEntity != null){
				sysDeptEntity.setParentName(parentDeptEntity.getName());
			}
		}
		return deptList;
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();
		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);
		return deptIdList;
	}

	/**
	 * 递归
	 */
	private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}
			deptIdList.add(deptId);
		}
	}

	@Override
	public Long getSuperDeptId(Long deptId) {
		SysDeptEntity deptEntity = this.selectById(deptId);
		while (deptEntity != null && deptEntity.getParentId() != 0){
			deptEntity = this.selectById(deptEntity.getParentId());
			if (deptEntity.getParentId() == 0){
				break;
			}
		}
		return deptEntity.getDeptId();
	}
}
