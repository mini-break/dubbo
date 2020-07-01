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
package com.alibaba.dubbo.remoting;

import com.alibaba.dubbo.common.URL;

import java.net.InetSocketAddress;

/**
 * dubbo抽象出一个端的概念，也就是Endpoint接口，这个端就是一个点，而点对点之间是可以双向传输。
 * 在端的基础上在衍生出通道、客户端以及服务端的概念
 *
 * Endpoint. (API/SPI, Prototype, ThreadSafe)
 *
 *
 * @see com.alibaba.dubbo.remoting.Channel
 * @see com.alibaba.dubbo.remoting.Client
 * @see com.alibaba.dubbo.remoting.Server
 */
public interface Endpoint {

    /**
     * 获得该端的url
     * get url.
     *
     * @return url
     */
    URL getUrl();

    /**
     * 获得该端的通道处理器
     * get channel handler.
     *
     * @return channel handler
     */
    ChannelHandler getChannelHandler();

    /**
     * 获得该端的本地地址
     * get local address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * 发送消息
     * send message.
     *
     * @param message
     * @throws RemotingException
     */
    void send(Object message) throws RemotingException;

    /**
     * 发送消息,sent是是否已经发送的标记,为了区分是否是第一次发送消息
     *
     * send message.
     *
     * @param message
     * @param sent    already sent to socket?
     */
    void send(Object message, boolean sent) throws RemotingException;

    /**
     * 关闭
     * close the channel.
     */
    void close();

    /**
     * 优雅的关闭，也就是加入了等待时间
     * Graceful close the channel.
     */
    void close(int timeout);

    /**
     * 开始关闭
     */
    void startClose();

    /**
     * 判断是否已经关闭
     * is closed.
     *
     * @return closed
     */
    boolean isClosed();

}