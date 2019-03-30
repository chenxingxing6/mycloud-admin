package com.example.common.validator.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 * @Author: cxx
 * @Date: 2019/1/1 16:30
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
