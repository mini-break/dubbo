/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.extensionloader;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.extensionloader.activate.ActivateExt1;
import com.alibaba.dubbo.common.extensionloader.activate.impl.ActivateExt1Impl1;
import com.alibaba.dubbo.common.extensionloader.activate.impl.GroupActivateExtImpl;
import com.alibaba.dubbo.common.extensionloader.activate.impl.OrderActivateExtImpl1;
import com.alibaba.dubbo.common.extensionloader.activate.impl.OrderActivateExtImpl2;
import com.alibaba.dubbo.common.extensionloader.activate.impl.ValueActivateExtImpl;
import com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt;
import com.alibaba.dubbo.common.extensionloader.ext1.impl.SimpleExtImpl1;
import com.alibaba.dubbo.common.extensionloader.ext1.impl.SimpleExtImpl2;
import com.alibaba.dubbo.common.extensionloader.ext2.Ext2;
import com.alibaba.dubbo.common.extensionloader.ext6_wrap.WrappedExt;
import com.alibaba.dubbo.common.extensionloader.ext6_wrap.impl.Ext5Wrapper1;
import com.alibaba.dubbo.common.extensionloader.ext6_wrap.impl.Ext5Wrapper2;
import com.alibaba.dubbo.common.extensionloader.ext7.InitErrorExt;
import com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1;
import com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt2;
import com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt3;
import com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt4;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt1Impl1;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt1_ManualAdaptive;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt1_ManualAdd1;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt1_ManualAdd2;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt2_ManualAdaptive;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt3_ManualAdaptive;
import com.alibaba.dubbo.common.extensionloader.ext8_add.impl.AddExt4_ManualAdaptive;
import com.alibaba.dubbo.common.extensionloader.ext9_empty.Ext9Empty;
import com.alibaba.dubbo.common.extensionloader.ext9_empty.impl.Ext9EmptyImpl;
import com.alibaba.dubbo.common.extensionloader.injection.InjectExt;
import com.alibaba.dubbo.common.extensionloader.injection.impl.InjectExtImpl;

import junit.framework.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * 扩展加载器测试用例
 */
public class ExtensionLoaderTest {

    /**
     * 扩展接口不能为空
     * @throws Exception
     */
    @Test
    public void test_getExtensionLoader_Null() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(),
                    containsString("Extension type == null"));
        }
    }

    /**
     * 扩展类需要为接口
     * @throws Exception
     */
    @Test
    public void test_getExtensionLoader_NotInterface() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(ExtensionLoaderTest.class);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(),
                    containsString("Extension type(class com.alibaba.dubbo.common.extensionloader.ExtensionLoaderTest) is not interface"));
        }
    }

    /**
     * 扩展接口需要使用@SPI注解
     * @throws Exception
     */
    @Test
    public void test_getExtensionLoader_NotSpiAnnotation() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(NoSpiExt.class);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(),
                    allOf(containsString("com.alibaba.dubbo.common.extensionloader.NoSpiExt"),
                            containsString("is not extension"),
                            containsString("WITHOUT @SPI Annotation")));
        }
    }

    /**
     * 获取默认扩展
     * @throws Exception
     */
    @Test
    public void test_getDefaultExtension() throws Exception {
        // 获取SimpleExt接口的默认扩展类
        SimpleExt ext = ExtensionLoader.getExtensionLoader(SimpleExt.class).getDefaultExtension();
        assertThat(ext, instanceOf(SimpleExtImpl1.class));

        // 获取默认扩展类的名称
        String name = ExtensionLoader.getExtensionLoader(SimpleExt.class).getDefaultExtensionName();
        assertEquals("impl1", name);
    }

    /**
     * 由于没有设置默认扩展,所以默认扩展及默认扩展名为null
     * @throws Exception
     */
    @Test
    public void test_getDefaultExtension_NULL() throws Exception {
        Ext2 ext = ExtensionLoader.getExtensionLoader(Ext2.class).getDefaultExtension();
        assertNull(ext);

        String name = ExtensionLoader.getExtensionLoader(Ext2.class).getDefaultExtensionName();
        assertNull(name);
    }

    /**
     * 根据扩展名获取扩展类
     * @throws Exception
     */
    @Test
    public void test_getExtension() throws Exception {
        assertTrue(ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension("impl1") instanceof SimpleExtImpl1);
        assertTrue(ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension("impl2") instanceof SimpleExtImpl2);
    }

    /**
     * 包装类测试，相当于Aop功能
     * @throws Exception
     */
    @Test
    public void test_getExtension_WithWrapper() throws Exception {
        // Ext5Impl1被Ext5Wrapper1和Ext5Wrapper2包装
        WrappedExt impl1 = ExtensionLoader.getExtensionLoader(WrappedExt.class).getExtension("impl1");
        assertThat(impl1, anyOf(instanceOf(Ext5Wrapper1.class), instanceOf(Ext5Wrapper2.class)));

        // Ext5Impl2被Ext5Wrapper1和Ext5Wrapper2包装
        WrappedExt impl2 = ExtensionLoader.getExtensionLoader(WrappedExt.class).getExtension("impl2");
        assertThat(impl2, anyOf(instanceOf(Ext5Wrapper1.class), instanceOf(Ext5Wrapper2.class)));


        URL url = new URL("p1", "1.2.3.4", 1010, "path1");
        int echoCount1 = Ext5Wrapper1.echoCount.get();
        int echoCount2 = Ext5Wrapper2.echoCount.get();

        // echo先经过Ext5Wrapper2.echo 再经过 Ext5Wrapper1.echo 最后到Ext5Impl1.echo
        assertEquals("Ext5Impl1-echo:Ext5Wrapper2:Ext5Wrapper1:ha", impl1.echo(url, "ha"));
        assertEquals(echoCount1 + 1, Ext5Wrapper1.echoCount.get());
        assertEquals(echoCount2 + 1, Ext5Wrapper2.echoCount.get());
    }

    /**
     * 根据扩展名获取扩展类，未找到则抛异常
     * @throws Exception
     */
    @Test
    public void test_getExtension_ExceptionNoExtension() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension("XXX");
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("No such extension com.alibaba.dubbo.common.extensionloader.ext1.SimpleExt by name XXX"));
        }
    }

    /**
     * 根据扩展名无法找到扩展类，自然无法做包装
     * @throws Exception
     */
    @Test
    public void test_getExtension_ExceptionNoExtension_WrapperNotAffactName() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(WrappedExt.class).getExtension("XXX");
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("No such extension com.alibaba.dubbo.common.extensionloader.ext6_wrap.WrappedExt by name XXX"));
        }
    }

    /**
     * 根据扩展名查找扩展类，扩展名参数必传
     * @throws Exception
     */
    @Test
    public void test_getExtension_ExceptionNullArg() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension(null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }

    /**
     * 根据扩展名是否能查找到扩展类
     * @throws Exception
     */
    @Test
    public void test_hasExtension() throws Exception {
        assertTrue(ExtensionLoader.getExtensionLoader(SimpleExt.class).hasExtension("impl1"));
        assertFalse(ExtensionLoader.getExtensionLoader(SimpleExt.class).hasExtension("impl1,impl2"));
        assertFalse(ExtensionLoader.getExtensionLoader(SimpleExt.class).hasExtension("xxx"));

        try {
            ExtensionLoader.getExtensionLoader(SimpleExt.class).hasExtension(null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }

    /**
     * 根据扩展名是否能查找到对应的包装类
     * @throws Exception
     */
    @Test
    public void test_hasExtension_wrapperIsNotExt() throws Exception {
        assertTrue(ExtensionLoader.getExtensionLoader(WrappedExt.class).hasExtension("impl1"));
        assertFalse(ExtensionLoader.getExtensionLoader(WrappedExt.class).hasExtension("impl1,impl2"));
        assertFalse(ExtensionLoader.getExtensionLoader(WrappedExt.class).hasExtension("xxx"));

        assertFalse(ExtensionLoader.getExtensionLoader(WrappedExt.class).hasExtension("wrapper1"));

        try {
            ExtensionLoader.getExtensionLoader(WrappedExt.class).hasExtension(null);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), containsString("Extension name == null"));
        }
    }

    /**
     * 获取所有扩展名
     * @throws Exception
     */
    @Test
    public void test_getSupportedExtensions() throws Exception {
        Set<String> exts = ExtensionLoader.getExtensionLoader(SimpleExt.class).getSupportedExtensions();

        Set<String> expected = new HashSet<String>();
        expected.add("impl1");
        expected.add("impl2");
        expected.add("impl3");

        assertEquals(expected, exts);
    }

    /**
     * 获取所有扩展名,但不包括包装类
     * @throws Exception
     */
    @Test
    public void test_getSupportedExtensions_wrapperIsNotExt() throws Exception {
        Set<String> exts = ExtensionLoader.getExtensionLoader(WrappedExt.class).getSupportedExtensions();

        Set<String> expected = new HashSet<String>();
        expected.add("impl1");
        expected.add("impl2");

        assertEquals(expected, exts);
    }

    /**
     * 增加扩展类(addExtension)测试
     * @throws Exception
     */
    @Test
    public void test_AddExtension() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("Manual1");
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("No such extension com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1 by name Manual"));
        }

        ExtensionLoader.getExtensionLoader(AddExt1.class).addExtension("Manual1", AddExt1_ManualAdd1.class);
        AddExt1 ext = ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("Manual1");

        assertThat(ext, instanceOf(AddExt1_ManualAdd1.class));
        // 根据扩展类类型获取扩展名
        assertEquals("Manual1", ExtensionLoader.getExtensionLoader(AddExt1.class).getExtensionName(AddExt1_ManualAdd1.class));
    }

    /**
     * 加入的扩展为空实现
     * @throws Exception
     */
    @Test
    public void test_AddExtension_NoExtend() throws Exception {
//        ExtensionLoader.getExtensionLoader(Ext9Empty.class).getSupportedExtensions();
        ExtensionLoader.getExtensionLoader(Ext9Empty.class).addExtension("ext9", Ext9EmptyImpl.class);
        Ext9Empty ext = ExtensionLoader.getExtensionLoader(Ext9Empty.class).getExtension("ext9");

        assertThat(ext, instanceOf(Ext9Empty.class));
        assertEquals("ext9", ExtensionLoader.getExtensionLoader(Ext9Empty.class).getExtensionName(Ext9EmptyImpl.class));
    }

    /**
     * 不能加入同名扩展类
     * @throws Exception
     */
    @Test
    public void test_AddExtension_ExceptionWhenExistedExtension() throws Exception {
        SimpleExt ext = ExtensionLoader.getExtensionLoader(SimpleExt.class).getExtension("impl1");

        try {
            ExtensionLoader.getExtensionLoader(AddExt1.class).addExtension("impl1", AddExt1_ManualAdd1.class);
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("Extension name impl1 already existed(Extension interface com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1)!"));
        }
    }

    /**
     * 加入自适应扩展类
     * @throws Exception
     */
    @Test
    public void test_AddExtension_Adaptive() throws Exception {
        ExtensionLoader<AddExt2> loader = ExtensionLoader.getExtensionLoader(AddExt2.class);
        loader.addExtension(null, AddExt2_ManualAdaptive.class);

        AddExt2 adaptive = loader.getAdaptiveExtension();
        assertTrue(adaptive instanceof AddExt2_ManualAdaptive);
    }

    /**
     * 已存在自适应扩展类，再加入就会报错
     * @throws Exception
     */
    @Test
    public void test_AddExtension_Adaptive_ExceptionWhenExistedAdaptive() throws Exception {
        ExtensionLoader<AddExt1> loader = ExtensionLoader.getExtensionLoader(AddExt1.class);

        loader.getAdaptiveExtension();

        try {
            loader.addExtension(null, AddExt1_ManualAdaptive.class);
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("Adaptive Extension already existed(Extension interface com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1)!"));
        }
    }

    /**
     * replaceExtension方法已废弃
     * @throws Exception
     */
    @Test
    public void test_replaceExtension() throws Exception {
        try {
            ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("Manual2");
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("No such extension com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1 by name Manual"));
        }

        {
            AddExt1 ext = ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("impl1");

            assertThat(ext, instanceOf(AddExt1Impl1.class));
            assertEquals("impl1", ExtensionLoader.getExtensionLoader(AddExt1.class).getExtensionName(AddExt1Impl1.class));
        }
        {
            ExtensionLoader.getExtensionLoader(AddExt1.class).replaceExtension("impl1", AddExt1_ManualAdd2.class);
            AddExt1 ext = ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("impl1");

            assertThat(ext, instanceOf(AddExt1_ManualAdd2.class));
            assertEquals("impl1", ExtensionLoader.getExtensionLoader(AddExt1.class).getExtensionName(AddExt1_ManualAdd2.class));
        }
    }

    @Test
    public void test_replaceExtension_Adaptive() throws Exception {
        ExtensionLoader<AddExt3> loader = ExtensionLoader.getExtensionLoader(AddExt3.class);

        AddExt3 adaptive = loader.getAdaptiveExtension();
        assertFalse(adaptive instanceof AddExt3_ManualAdaptive);

        loader.replaceExtension(null, AddExt3_ManualAdaptive.class);

        adaptive = loader.getAdaptiveExtension();
        assertTrue(adaptive instanceof AddExt3_ManualAdaptive);
    }

    @Test
    public void test_replaceExtension_ExceptionWhenNotExistedExtension() throws Exception {
        AddExt1 ext = ExtensionLoader.getExtensionLoader(AddExt1.class).getExtension("impl1");

        try {
            ExtensionLoader.getExtensionLoader(AddExt1.class).replaceExtension("NotExistedExtension", AddExt1_ManualAdd1.class);
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("Extension name NotExistedExtension not existed(Extension interface com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt1)"));
        }
    }

    @Test
    public void test_replaceExtension_Adaptive_ExceptionWhenNotExistedExtension() throws Exception {
        ExtensionLoader<AddExt4> loader = ExtensionLoader.getExtensionLoader(AddExt4.class);

        try {
            loader.replaceExtension(null, AddExt4_ManualAdaptive.class);
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("Adaptive Extension not existed(Extension interface com.alibaba.dubbo.common.extensionloader.ext8_add.AddExt4)"));
        }
    }

    /**
     * 扩展类在初始化时出错，不会放入缓存中
     * @throws Exception
     */
    @Test
    public void test_InitError() throws Exception {
        ExtensionLoader<InitErrorExt> loader = ExtensionLoader.getExtensionLoader(InitErrorExt.class);

        loader.getExtension("ok");

        try {
            // error对应的扩展类在初始化的时候出错了，所以getExtension时自然取不到
            loader.getExtension("error");
            fail();
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage(), containsString("Failed to load extension class(interface: interface com.alibaba.dubbo.common.extensionloader.ext7.InitErrorExt"));
            assertThat(expected.getCause(), instanceOf(ExceptionInInitializerError.class));
        }
    }

    /**
     * 获得自动激活的扩展对象测试
     * @throws Exception
     */
    @Test
    public void testLoadActivateExtension() throws Exception {
        // test default
        URL url = URL.valueOf("test://localhost/test");
        List<ActivateExt1> list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, new String[]{}, "default_group");
        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).getClass() == ActivateExt1Impl1.class);

        // test group
        url = url.addParameter(Constants.GROUP_KEY, "group1");
        list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, new String[]{}, "group1");
        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).getClass() == GroupActivateExtImpl.class);

        // test value
        url = url.removeParameter(Constants.GROUP_KEY);
        url = url.addParameter(Constants.GROUP_KEY, "value");
        url = url.addParameter("value", "value");
        list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, new String[]{}, "value");
        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).getClass() == ValueActivateExtImpl.class);

        // test order
        url = URL.valueOf("test://localhost/test");
        url = url.addParameter(Constants.GROUP_KEY, "order");
        list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, new String[]{}, "order");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.get(0).getClass() == OrderActivateExtImpl1.class);
        Assert.assertTrue(list.get(1).getClass() == OrderActivateExtImpl2.class);
    }

    /**
     * 获取默认激活扩展类
     * @throws Exception
     */
    @Test
    public void testLoadDefaultActivateExtension() throws Exception {
        // test default
        URL url = URL.valueOf("test://localhost/test?ext=order1,default");
        List<ActivateExt1> list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, "ext", "default_group");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.get(0).getClass() == OrderActivateExtImpl1.class);
        Assert.assertTrue(list.get(1).getClass() == ActivateExt1Impl1.class);

        url = URL.valueOf("test://localhost/test?ext=default,order1");
        list = ExtensionLoader.getExtensionLoader(ActivateExt1.class)
                .getActivateExtension(url, "ext", "default_group");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.get(0).getClass() == ActivateExt1Impl1.class);
        Assert.assertTrue(list.get(1).getClass() == OrderActivateExtImpl1.class);
    }

    /**
     * 扩展类依赖注入测试
     */
    @Test
    public void testInjectExtension() {
        // test default
        InjectExt injectExt = ExtensionLoader.getExtensionLoader(InjectExt.class).getExtension("injection");
        InjectExtImpl injectExtImpl = (InjectExtImpl) injectExt;
        org.junit.Assert.assertNotNull(injectExtImpl.getSimpleExt());
        org.junit.Assert.assertNull(injectExtImpl.getSimpleExt1());
        org.junit.Assert.assertNull(injectExtImpl.getGenericType());
    }

}