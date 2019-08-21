package com.example.vavr;

import io.vavr.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * User: lanxinghua
 * Date: 2019/8/21 14:55
 * Desc: Vavr是java8一个函数式库，提供了一些不可变数据类型及函数式控制结构
 */
public class VavrTest extends TestCase {

    // 1.option是一个对象容器，与Optional类似
    @Test
    public void test01(){
        Option<Object> noneOption = Option.of(null);
        Option<Object> someOption = Option.of("hello");

        System.out.println(noneOption.getOrElse("or else"));
        System.out.println(someOption.get());
        assertNotNull(noneOption);
    }


    // 2.元组Tuple java中没有与元组相对应的结构，Tuple是函数式编程中一种常见的概念，不可变，最多保存8个元素
    // 当需要考虑多个对象可以考虑使用Tuple
    @Test
    public void test02(){
        Tuple2<String, Integer> java8 = Tuple.of("java", 8);
        String element1 = java8._1;
        int element2 = java8._2();
        System.out.println("element1:" + element1);
        System.out.println("element2:" + element2);
    }


    // 3.Try是一个容器，包装一段可能产生异常的代码，不用显示的用try_cache来处理异常
    @Test
    public void test03(){
        Try<Integer> result = Try.of(() -> {
            return 1/0;
        });
        int data = result.getOrElse(-1);
        if (result.isFailure()){
            System.out.println(data);
            System.out.println(result.get());
        }
    }

    // 4.函数式接口
    @Test
    public void test04(){
        Function1<String, String> fun1 = (p1) -> p1;
        Function2<String, String, String> fun2 = (p1, p2) -> p1 + " " + p2;
        Function3<Integer, Integer, Integer, Integer> fun3 = Function3.of(this::sum);

        System.out.println(fun1.apply("test"));
        System.out.println(fun2.apply("hello", "world"));
        System.out.println(fun3.apply(1, 2, 3));
    }

    private int sum(int a, int b, int c){
        return a + b + c;
    }

    // 5.集合Collections Java中的集合通常是可变集合，这通常是造成错误的根源,特别是在并发场景
    @Test(expected = UnsupportedOperationException.class)
    public void test05(){
        // JDK还通过一些其它的方法创建不可变集集合,误调用会产生异常
//        List<String> wordList = Arrays.asList("a");
//        List<String> list = Collections.unmodifiableList(wordList);
//        list.add("b");

//        wordList = Lists.newArrayList("a");
//        wordList.add("b");

        io.vavr.collection.List<Integer> intList = io.vavr.collection.List.of(1, 2, 3);
        // 添加不进去，不会报异常
        intList.append(4);
        intList.append(5);
        System.out.println(intList);
    }


    // 6.延迟计算Lazy
    @Test
    public void test06(){
        Lazy<Double> lazy = Lazy.of(Math::random);
        assertFalse(lazy.isEvaluated());
        double v1 = lazy.get();
        System.out.println(v1);
        double v2 = lazy.get();
        System.out.println(v2);
    }


    // 7.模式匹配Pattern Matching
    @Test
    public void test7() {
        int input = 2;
        String output = API.Match(input).of(
                API.Case(API.$(1), "one"),
                API.Case(API.$(2), "two"),
                API.Case(API.$(3), "three"),
                API.Case(API.$(), "?"));
        System.out.println(output);

        //java switch case
        String op = null;
        switch (input) {
            case 1:
                op = "one";
                break;
            case 2:
                op = "two";
                break;
            case 3:
                op = "three";
                break;
            default:
                op = "?";
        }
        System.out.println(op);
    }
}

