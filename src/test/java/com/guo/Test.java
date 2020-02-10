package com.guo;


import java.util.ArrayList;
import java.util.List;

public class Test {

    @org.junit.Test
    public void test(){

        List<String> list= new ArrayList<>();
        list.add("123");
        list.stream().forEach(System.out::print);

    }
}
