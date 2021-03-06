/*
 * Copyright 2007-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.payment.alipay;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 17/6/7 上午11:37
 * @version 1.0
 */
public interface IAliPayReqeustSender<RESPONSE extends IAliPayResponse> {

    /**
     * @return 返回接口请求网关地址
     */
    String getGatewayUrl();

    /**
     * @return 返回请求参数映射
     */
    Map<String, String> getRequestParameters();

    /**
     * @return 执行请求发送并返回响应结果对象
     * @throws Exception 可能产生的异常
     */
    RESPONSE execute() throws Exception;

    /**
     * @return 构建Form请求表单
     */
    String executeActionForm();
}
