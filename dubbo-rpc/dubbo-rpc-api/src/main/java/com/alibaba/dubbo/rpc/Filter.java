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
 * 调用拦截扩展:服务提供方和服务消费方调用过程拦截，Dubbo 本身的大多功能均基于此扩展点实现，每次远程方法执行，该拦截都会被执行，请注意对性能的影响
 * <p>
 * 扩展配置:
 * <!-- 消费方调用过程拦截 -->
 * <dubbo:reference filter="xxx,yyy" />
 * <!-- 消费方调用过程缺省拦截器，将拦截所有reference -->
 * <dubbo:consumer filter="xxx,yyy"/>
 * <!-- 提供方调用过程拦截 -->
 * <dubbo:service filter="xxx,yyy" />
 * <!-- 提供方调用过程缺省拦截器，将拦截所有service -->
 * <dubbo:provider filter="xxx,yyy"/>
 * <p>
 * Filter. (SPI, Singleton, ThreadSafe)
 */
@SPI
public interface Filter {

    /**
     * 对调用进行过滤
     * do invoke filter.
     * <p>
     * <code>
     * // before filter
     * Result result = invoker.invoke(invocation);
     * // after filter
     * return result;
     * </code>
     *
     * @param invoker    service
     * @param invocation invocation.
     * @return invoke result.
     * @throws RpcException
     * @see com.alibaba.dubbo.rpc.Invoker#invoke(Invocation)
     */
    Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException;

}