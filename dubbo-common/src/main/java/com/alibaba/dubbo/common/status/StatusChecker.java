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
package com.alibaba.dubbo.common.status;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 状态检查扩展:检查服务依赖各种资源的状态，此状态检查可同时用于 telnet 的 status 命令和 hosting 的 status 页面
 * <p>
 * 扩展配置:
 * <dubbo:protocol status="xxx,yyy" />
 * <!-- 缺省值设置，当<dubbo:protocol>没有配置status属性时，使用此配置 -->
 * <dubbo:provider status="xxx,yyy" />
 * <p>
 * StatusChecker
 */
@SPI
public interface StatusChecker {

    /**
     * check status
     *
     * @return status
     */
    Status check();

}