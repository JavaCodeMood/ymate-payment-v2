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
package net.ymate.payment.alipay.impl;

import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.framework.commons.IHttpResponse;
import net.ymate.framework.commons.ParamUtils;
import net.ymate.payment.alipay.AliPay;
import net.ymate.payment.alipay.IAliPayReqeustSender;
import net.ymate.payment.alipay.IAliPayResponse;
import net.ymate.payment.alipay.IAliPayResponseParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 17/6/7 下午5:33
 * @version 1.0
 */
public class DefaultAliPayRequestSender<RESPONSE extends IAliPayResponse> implements IAliPayReqeustSender<RESPONSE> {

    private String gatewayUrl;

    private Map<String, String> requestParameters;

    private IAliPayResponseParser<RESPONSE> responseParser;

    public DefaultAliPayRequestSender(Map<String, String> requestParameters, IAliPayResponseParser<RESPONSE> responseParser) {
        this.gatewayUrl = AliPay.get().getModuleCfg().getGatewayUrl();
        if (requestParameters != null) {
            this.requestParameters = requestParameters;
        } else {
            this.requestParameters = new HashMap<String, String>();
        }
        this.responseParser = responseParser;
    }

    public RESPONSE execute() throws Exception {
        IHttpResponse _response = HttpClientHelper.create().post(this.gatewayUrl, this.requestParameters);
        return this.responseParser.parserResponse(_response);
    }

    public String executeActionForm() {
        return ParamUtils.buildActionForm(this.gatewayUrl, true, requestParameters);
    }
}
