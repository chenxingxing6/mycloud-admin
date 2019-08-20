package com.example;

import io.swagger.models.auth.In;
import javafx.util.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reactor.util.function.Tuple2;

import java.util.AbstractMap;

/**
 * User: lanxinghua
 * Date: 2019/8/20 18:24
 * Desc: pair配对，提供方便方式来处理简单的键值对
 * java:Pair
 * Apache Commons
 * Vavr
 */
public class PairTest {
    public static void main(String[] args) {
        // javax中的
        Pair<String, String> javaPair = new Pair<>("key", "value");
        System.out.println(javaPair.getKey() + ":" + javaPair.getValue());

        AbstractMap.SimpleEntry<String, String> entry = new AbstractMap.SimpleEntry<>("key", "value");
        System.out.println(entry.getKey() + ":" + entry.getValue());

        // 不可变配对
        AbstractMap.SimpleImmutableEntry<String, String> immutableEntry = new AbstractMap.SimpleImmutableEntry<>("key", "value");
        System.out.println(immutableEntry.getKey() + ":" + immutableEntry.getValue());

        // 修改会报异常：Exception in thread "main" java.lang.UnsupportedOperationException
        //immutableEntry.setValue("11");

        // apache
        ImmutablePair<String, String> apachePair = new ImmutablePair<>("1", "1");
        System.out.println(apachePair.getLeft() + ":" +apachePair.getRight());

    }
}
