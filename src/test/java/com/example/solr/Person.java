package com.example.solr;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * User: lanxinghua
 * Date: 2019/8/23 18:23
 * Desc:
 */
public class Person implements Serializable {
    @Field
    private String id;
    @Field
    private String name;
    @Field
    private int age;
    @Field
    private String address;

    public Person(){

    }

    public Person(String id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
