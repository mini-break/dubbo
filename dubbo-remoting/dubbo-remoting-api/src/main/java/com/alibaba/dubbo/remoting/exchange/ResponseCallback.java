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
package com.alibaba.dubbo.remoting.exchange;

/**
 * 该接口是回调的接口，定义了两个方法，分别是处理正常的响应结果和处理异常
 *
 * Callback
 */
public interface ResponseCallback {

    /**
     * 处理请求
     * done.
     *
     * @param response
     */
    void done(Object response);

    /**
     * 处理异常
     * caught exception.
     *
     * @param exception
     */
    void caught(Throwable exception);

}