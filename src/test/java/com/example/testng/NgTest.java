package com.example.testng;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * User: lanxinghua
 * Date: 2019/8/16 18:47
 * Desc: TestNG 意味着高级的测试和复杂的集成测试。它更加的灵活，TestNG 也涵盖了 JUnit4 的全部功能。那就没有任何理由使用 Junit了
 */
public class NgTest {
    @BeforeClass
    public void setUp(){
        System.out.println("set up");
    }

    @Test(groups = {"test1"})
    public void test01(){
        System.out.println("test 01");
    }

    @Test(groups = {"test2"})
    public void test02(){
        System.out.println("test 02");
    }
}
