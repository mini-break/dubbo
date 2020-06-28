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

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * Protocol 是服务域，它是 Invoker 暴露和引用的主功能入口，它负责 Invoker 的生命周期管理
 * 协议扩展: RPC 协议扩展，封装远程调用细节
 * <p>
 * 扩展配置:
 * <!-- 声明协议，如果没有配置id，将以name为id -->
 * <dubbo:protocol id="xxx1" name="xxx" />
 * <!-- 引用协议，如果没有配置protocol属性，将在ApplicationContext中自动扫描protocol配置 -->
 * <dubbo:service protocol="xxx1" />
 * <!-- 引用协议缺省值，当<dubbo:service>没有配置prototol属性时，使用此配置 -->
 * <dubbo:provider protocol="xxx1" />
 * <p>
 * <p>
 * Protocol. (API/SPI, Singleton, ThreadSafe)
 */
@SPI("dubbo")
public interface Protocol {

    /**
     * Get default port when user doesn't config the port.
     *
     * @return default port
     */
    int getDefaultPort();

    /**
     * 暴露远程服务：<br>
     * 1. 协议在接收请求时，应记录请求来源方地址信息：RpcContext.getContext().setRemoteAddress();<br>
     * 2. export()必须是幂等的，也就是暴露同一个URL的Invoker两次，和暴露一次没有区别。<br>
     * 3. export()传入的Invoker由框架实现并传入，协议不需要关心。<br>
     *
     * @param <T>     服务的类型
     * @param invoker 服务的执行体
     * @param <T>     Service type
     * @param invoker Service invoker
     * @return exporter reference for exported service, useful for unexport the service later
     * @throws RpcException 当暴露服务出错时抛出，比如端口已占用
     *                      <p>
     *                      Export service for remote invocation: <br>
     *                      1. Protocol should record request source address after receive a request:
     *                      RpcContext.getContext().setRemoteAddress();<br>
     *                      2. export() must be idempotent, that is, there's no difference between invoking once and invoking twice when
     *                      export the same URL<br>
     *                      3. Invoker instance is passed in by the framework, protocol needs not to care <br>
     * @throws RpcException thrown when error occurs during export the service, for example: port is occupied
     */
    @Adaptive
    <T> Exporter<T> export(Invoker<T> invoker) throws RpcException;

    /**
     * 引用远程服务：<br>
     * 1. 当用户调用refer()所返回的Invoker对象的invoke()方法时，协议需相应执行同URL远端export()传入的Invoker对象的invoke()方法。<br>
     * 2. refer()返回的Invoker由协议实现，协议通常需要在此Invoker中发送远程请求。<br>
     * 3. 当url中有设置check=false时，连接失败不能抛出异常，需内部自动恢复。<br>
     *
     * @param <T>  服务的类型
     * @param type 服务的类型
     * @param url  远程服务的URL地址
     * @param <T>  Service type
     * @param type Service class
     * @param url  URL address for the remote service
     * @return invoker service's local proxy
     * @throws RpcException 当连接服务提供方失败时抛出
     *                      <p>
     *                      Refer a remote service: <br>
     *                      1. When user calls `invoke()` method of `Invoker` object which's returned from `refer()` call, the protocol
     *                      needs to correspondingly execute `invoke()` method of `Invoker` object <br>
     *                      2. It's protocol's responsibility to implement `Invoker` which's returned from `refer()`. Generally speaking,
     *                      protocol sends remote request in the `Invoker` implementation. <br>
     *                      3. When there's check=false set in URL, the implementation must not throw exception but try to recover when
     *                      connection fails.
     * @throws RpcException when there's any error while connecting to the service provider
     */
    @Adaptive
    <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException;

    /**
     * Destroy protocol: <br>
     * 1. Cancel all services this protocol exports and refers <br>
     * 2. Release all occupied resources, for example: connection, port, etc. <br>
     * 3. Protocol can continue to export and refer new service even after it's destroyed.
     */
    void destroy();

}