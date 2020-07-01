package com.alibaba.dubbo.demo.provider.mytest;

public interface Car {
        String getBrand();
        long getWeight();
        void make(String brand, long weight);
    }