package com.example.common.aspect;


import com.example.common.annotation.DataFilter;
import com.example.common.constants.UserEnum;
import com.example.common.exception.BizException;
import com.example.common.utils.Constant;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.modules.sys.service.ISysDeptService;
import com.example.modules.sys.service.ISysRoleDeptService;
import com.example.modules.sys.service.ISysUserRoleService;
import com.example.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据过滤，切面处理类
 */
@Aspect
@Component
public class DataFilterAspect {
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysRoleDeptService sysRoleDeptService;

    @Pointcut("@annotation(com.example.common.annotation.DataFilter)")
    public void dataFilterCut() {

    }

    @Before("dataFilterCut()")
    public void dataFilter(JoinPoint point) throws Throwable {
        Object params = point.getArgs()[0];
        if(params != null && params instanceof Map){
            //如果是前台用户不进行过滤
            Map map = (Map)params;
            Object type = map.get("type");
            if (type !=null && UserEnum.FRONT.getType().equals(type.toString())){
                return;
            }
            SysUserEntity user = ShiroUtils.getUserEntity();
            //如果不是超级管理员，则进行数据过滤
            if(user.getUserId() != Constant.SUPER_ADMIN){
                map.put(Constant.SQL_FILTER, getSQLFilter(user, point));
            }
            return ;
        }
        throw new BizException("数据权限接口，只能是Map类型参数，且不能为NULL");
    }

    /**
     * 获取数据过滤的SQL
     */
    private String getSQLFilter(SysUserEntity user, JoinPoint point){
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataFilter dataFilter = signature.getMethod().getAnnotation(DataFilter.class);
        //获取表的别名
        String tableAlias = dataFilter.tableAlias();
        if(StringUtils.isNotBlank(tableAlias)){
            tableAlias +=  ".";
        }

        //部门ID列表
        Set<Long> deptIdList = new HashSet<>();

        //用户角色对应的部门ID列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if(roleIdList.size() > 0){
            List<Long> userDeptIdList = sysRoleDeptService.queryDeptIdList(roleIdList.toArray(new Long[roleIdList.size()]));
            deptIdList.addAll(userDeptIdList);
        }

        //用户子部门ID列表
        if(dataFilter.subDept()){
            List<Long> subDeptIdList = sysDeptService.getSubDeptIdList(user.getDeptId());
            deptIdList.addAll(subDeptIdList);
        }

        StringBuilder sqlFilter = new StringBuilder();
        sqlFilter.append(" (");

        if(deptIdList.size() > 0){
            sqlFilter.append(tableAlias).append(dataFilter.deptId()).append(" in(").append(StringUtils.join(deptIdList, ",")).append(")");
        }

        //没有本部门数据权限，也能查询本人数据
        if(dataFilter.user()){
            if(deptIdList.size() > 0){
                sqlFilter.append(" or ");
            }
            sqlFilter.append(tableAlias).append(dataFilter.userId()).append("=").append(user.getUserId());
        }
        sqlFilter.append(")");
        return sqlFilter.toString();
    }
}
