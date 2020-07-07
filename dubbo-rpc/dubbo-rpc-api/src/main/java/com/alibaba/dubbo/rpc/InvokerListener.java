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

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 引用监听扩展:当有服务引用时，触发该事件
 * <p>
 * 扩展配置:
 * <!-- 引用服务监听 -->
 * <dubbo:reference listener="xxx,yyy" />
 * <!-- 引用服务缺省监听器 -->
 * <dubbo:consumer listener="xxx,yyy" />
 * <p>
 * InvokerListener. (SPI, Singleton, ThreadSafe)
 */
@SPI
public interface InvokerListener {

    /**
     * 在服务引用的时候进行监听
     * The invoker referred
     *
     * @param invoker
     * @throws RpcException
     * @see com.alibaba.dubbo.rpc.Protocol#refer(Class, com.alibaba.dubbo.common.URL)
     */
    void referred(Invoker<?> invoker) throws RpcException;

    /**
     * 销毁实体域的时候进行监听
     * The invoker destroyed.
     *
     * @param invoker
     * @see com.alibaba.dubbo.rpc.Invoker#destroy()
     */
    void destroyed(Invoker<?> invoker);

}