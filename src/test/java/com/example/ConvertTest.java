package com.example;

import com.alibaba.common.convert.Convert;

/**
 * User: lanxinghua
 * Date: 2019/8/14 15:02
 * Desc:
 */
public class ConvertTest {
    public static void main(String[] args) {
        System.out.println(Convert.asLong("11", 22L));
        System.out.println(Convert.asLong("11aaaa", 22L));
    }
}
