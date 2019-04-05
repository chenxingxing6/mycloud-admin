package com.example;

import com.alibaba.fastjson.JSON;
import com.example.modules.sys.entity.server.Server;

/**
 * User: lanxinghua
 * Date: 2019/4/5 21:31
 * Desc: 服务器监控
 */
public class ServerTest {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.copyTo();
        System.out.println("****"+ JSON.toJSONString(server));
    }
}
