package com.stuben.monitop.test.service;

import java.util.stream.Stream;

public class MonitorTest {
    public static void m(Object[] objects) {
        Stream.of(objects).forEach(System.out::println);
    }
}
