package com.alibaba.dubbo.demo.provider.mytest;

/**
 * @Description Wrapper生成的包装类
 * @Author hg
 * @Date 2020-06-30 10:43
 */
public class Wrapper0 extends com.alibaba.dubbo.common.bytecode.Wrapper {

    // 字段名列表
    public static String[] pns;
    // 字段名与字段类型的映射关系
    public static java.util.Map pts;
    // 方法名列表
    public static String[] mns;
    // 声明的方法名列表
    public static String[] dmns;

    // 每个public方法的参数类型
    public static Class[] mts0;
    public static Class[] mts1;
    public static Class[] mts2;

    public String[] getPropertyNames() {
        return pns;
    }

    public boolean hasProperty(String n) {
        return pts.containsKey(n);
    }

    public Class getPropertyType(String n) {
        return (Class) pts.get(n);
    }

    public String[] getMethodNames() {
        return mns;
    }

    public String[] getDeclaredMethodNames() {
        return dmns;
    }

    public void setPropertyValue(Object o, String n, Object v) {
        Car w;
        try {
            w = ((Car) o);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        throw new com.alibaba.dubbo.common.bytecode.NoSuchPropertyException("Not found property \"" + n + "\" filed or setter method in class com.alibaba.dubbo.common.bytecode.WrapperTest$Car.");
    }

    public Object getPropertyValue(Object o, String n) {
        Car w;
        try {
            w = ((Car) o);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        if (n.equals("weight")) {
            return  w.getWeight();
        }
        if (n.equals("brand")) {
            return  w.getBrand();
        }
        throw new com.alibaba.dubbo.common.bytecode.NoSuchPropertyException("Not found property \"" + n + "\" filed or setter method in class com.alibaba.dubbo.common.bytecode.WrapperTest$Car.");
    }

    /**
     *  在调用接口时，就时调用的这个方法
     @param o 接口实例
     @param n 方法名
     @param p 参数类型
     @param v 参数
     */
    public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws java.lang.reflect.InvocationTargetException {
        Car w;
        try {
            w = ((Car) o);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        // 这个try范围内就是你所需要暴露的所有方法
        try {
            if ("make".equals(n) && p.length == 2) {
                w.make((java.lang.String) v[0], ((Number) v[1]).longValue());
                return null;
            }
            if ("getWeight".equals(n) && p.length == 0) {
                return  w.getWeight();
            }
            if ("getBrand".equals(n) && p.length == 0) {
                return  w.getBrand();
            }
        } catch (Throwable e) {
            throw new java.lang.reflect.InvocationTargetException(e);
        }
        throw new com.alibaba.dubbo.common.bytecode.NoSuchMethodException("Not found method \"" + n + "\" in class com.alibaba.dubbo.common.bytecode.WrapperTest$Car.");
    }
}
