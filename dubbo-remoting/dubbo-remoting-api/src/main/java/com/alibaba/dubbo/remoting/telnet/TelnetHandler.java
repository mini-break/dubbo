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
package com.alibaba.dubbo.remoting.telnet;

import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.RemotingException;

/**
 * Telnet 命令扩展:所有服务器均支持 telnet 访问，用于人工干预
 * <p>
 * 扩展配置:
 * <dubbo:protocol telnet="xxx,yyy" />
 * <!-- 缺省值设置，当<dubbo:protocol>没有配置telnet属性时，使用此配置 -->
 * <dubbo:provider telnet="xxx,yyy" />
 * <p>
 * TelnetHandler
 */
@SPI
public interface TelnetHandler {

    /**
     * telnet.
     *
     * @param channel
     * @param message
     */
    String telnet(Channel channel, String message) throws RemotingException;

}