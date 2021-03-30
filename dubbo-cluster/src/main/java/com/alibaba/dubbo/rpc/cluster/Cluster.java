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
package com.alibaba.dubbo.rpc.cluster;

import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.support.FailoverCluster;

/**
 * 集群扩展:当有多个服务提供方时，将多个服务提供方组织成一个集群，并伪装成一个提供方
 * <p>
 * 扩展配置:
 * <dubbo:protocol cluster="xxx" />
 * <!-- 缺省值配置，如果<dubbo:protocol>没有配置cluster时，使用此配置 -->
 * <dubbo:provider cluster="xxx" />
 * <p>
 * Cluster. (SPI, Singleton, ThreadSafe)
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Computer_cluster">Cluster</a>
 * <a href="http://en.wikipedia.org/wiki/Fault-tolerant_system">Fault-Tolerant</a>
 */
@SPI(FailoverCluster.NAME) // 默认是failover 失败重试
public interface Cluster {

    /**
     * 将目录调用程序合并到虚拟调用程序。
     * Merge the directory invokers to a virtual invoker.
     *
     * @param <T>
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    @Adaptive //基于 Dubbo SPI Adaptive 机制，加载对应的 Cluster 实现，使用 URL.cluster 属性
    <T> Invoker<T> join(Directory<T> directory) throws RpcException;

}