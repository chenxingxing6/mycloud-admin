package com.example.mock;

import com.example.BaseTest;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;

import java.util.LinkedList;
import java.util.List;

/**
 * User: lanxinghua
 * Date: 2019/8/16 18:30
 * Desc:
 */
public class MockitoTest{

    // Mockito可以验证行为确实发生
    @Test
    public void test01(){
        // 模拟创建List对象
        List one = Mockito.mock(List.class);

        // 对象行为
        one.add(2);
        one.clear();

        // 验证对象行为
        Mockito.verify(one).add(1);
        Mockito.verify(one).clear();
    }

    @Test
    public void testMockitoTest() {
        LinkedList mockedList = Mockito.mock(LinkedList.class);
        // 预设方法返回值，称为stubbing
        Mockito.when(mockedList.get(0)).thenReturn("first");
        System.out.println(mockedList.get(0));
        System.out.println(mockedList.get(2));
    }
}
