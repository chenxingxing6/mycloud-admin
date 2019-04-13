package com.example.modules.front.dao;

import com.example.modules.front.entity.FileEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与文件对应关系
 *
 * @author lanxinghua
 * @email lanxinghua@2dfire.com
 * @date 2019-03-17 21:38:15
 */
public interface FileDao extends BaseMapper<FileEntity> {

    List<FileEntity> listFileByIdsWithPage(@Param("ids") List<Long> ids,
                                           @Param(value = "fileName") String fileName,
                                           @Param(value = "page") Integer page,
                                           @Param(value = "pageSize") Integer pageSize);


    int getFileTotalByIdsWithPage(@Param("ids") List<Long> ids,
                                  @Param(value = "fileName") String fileName);
}
