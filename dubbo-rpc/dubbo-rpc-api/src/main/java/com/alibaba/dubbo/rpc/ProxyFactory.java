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
package com.alibaba.dubbo.rpc;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * 动态代理扩展:将 Invoker 接口转换成业务接口/将服务转换为Invoker
 * <p>
 * 扩展配置:
 * <dubbo:protocol proxy="xxx" />
 * <!-- 缺省值配置，当<dubbo:protocol>没有配置proxy属性时，使用此配置 -->
 * <dubbo:provider proxy="xxx" />
 * <p>
 * 动态代理在dubbo的另一个典型应用是proxyFactory
 * 1.在服务提供端，将服务的具体实现类转为Invoker
 * 2.在消费端，通过 getProxy(Invoker<T> invoker)将invoker转为客户端需要的接口
 * ProxyFactory. (API/SPI, Singleton, ThreadSafe)
 */
@SPI("javassist")
public interface ProxyFactory {

    /**
     * 创建一个代理
     * create proxy.
     *
     * @param invoker
     * @return proxy
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker) throws RpcException;

    /**
     * 创建一个代理
     * create proxy.
     *
     * @param invoker
     * @return proxy
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> T getProxy(Invoker<T> invoker, boolean generic) throws RpcException;

    /**
     * 创建一个实体域
     * create invoker.
     *
     * @param <T>
     * @param proxy 被代理对象(接口实现)
     * @param type  类型(接口)
     * @param url   dubbo统一资源
     * @return invoker
     */
    @Adaptive({Constants.PROXY_KEY})
    <T> Invoker<T> getInvoker(T proxy, Class<T> type, URL url) throws RpcException;

}