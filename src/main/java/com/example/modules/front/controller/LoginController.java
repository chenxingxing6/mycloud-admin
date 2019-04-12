package com.example.modules.front.controller;

import com.example.common.utils.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: lanxinghua
 * Date: 2019/4/12 12:50
 * Desc: 前台用户登录
 */
@RestController
@RequestMapping("/front")
public class LoginController {

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/login")
    public R login(String username, String password) {
        System.out.println(username +"---" + password);
        return R.ok();
    }
}
