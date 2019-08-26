package com.example.rewritehashcode;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.*;

/**
 * User: lanxinghua
 * Date: 2019/8/26 11:05
 * Desc: 现在需求来了，我们需要两个对象的各项属性值一样的就认为这两个对象是相等的；那么此时我们就需要重写equals方法了
 */
public class HashCodeTest {
    public static void main(String[] args) {
        User user1 = User.build().setName("lxh").setAge(10).setIdCard("12345678");
        User user2 = User.build().setName("lxh").setAge(10).setIdCard("12345678");
        // System.out.println(user1.equals(user2)); // 调用equals   true
        // System.out.println(user1 == user2);      // 什么都没调用  false


        // 重新equals后，不重写hashcode，user会存2个值
        Set set = new HashSet();
        set.add(user1);  // 会去调用hashCode方法
        set.add(user2);  // 会去调用hashCode方法&equals方法【如果set已经有值了】
        System.out.println(set.size());

        Map<User, Integer> map = new HashMap<>();
        map.put(user1, 1);
        map.put(user2, 1);
        System.out.println(map.size());

    }

}

class User implements Serializable {
    private String name;

    private int age;

    private String idCard;


    public static User build(){
        return new User();
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public User setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        System.out.println("equals");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return age == user.age &&
                Objects.equals(name, user.name) &&
                Objects.equals(idCard, user.idCard);
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode");
        return Objects.hash(name, age, idCard);
    }
}
