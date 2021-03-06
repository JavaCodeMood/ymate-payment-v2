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
package net.ymate.payment.wxpay.base;

import net.ymate.framework.commons.HttpClientHelper;
import net.ymate.payment.wxpay.request.WxPaySandboxSignKey;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import java.io.Serializable;
import java.net.URL;

/**
 * @author 刘镇 (suninformation@163.com) on 17/6/15 下午1:24
 * @version 1.0
 */
public class WxPayAccountMeta implements Serializable {

    private static final Log _LOG = LogFactory.getLog(WxPayAccountMeta.class);

    /**
     * 公众帐号APP_ID
     */
    private String appId;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 商号密钥
     */
    private String mchKey;

    /**
     * 证书文件路径
     */
    private String certFilePath;

    /**
     * 是否开启沙箱测试模式, 默认值: false
     */
    private boolean sandboxEnabled;

    /**
     * 获取沙箱测试模式下的接口URL地址前缀, 默认值: sandboxnew
     */
    private String sandboxPrefix;

    private SSLConnectionSocketFactory connectionSocketFactory;

    /**
     * 异步通知URL
     */
    private String notifyUrl;

    public WxPayAccountMeta(String appId, String mchId, String mchKey, String notifyUrl) {
        this.appId = appId;
        this.mchId = mchId;
        this.mchKey = mchKey;
        this.notifyUrl = notifyUrl;
    }

    public WxPayAccountMeta(String appId, String mchId, String mchKey, String certFilePath, String notifyUrl) {
        this.appId = appId;
        this.mchId = mchId;
        this.mchKey = mchKey;
        this.certFilePath = certFilePath;
        this.notifyUrl = notifyUrl;
    }

    private void __doGetSandboxSignKeyIfNeed() {
        if (sandboxEnabled) {
            try {
                WxPaySandboxSignKey _request = new WxPaySandboxSignKey(this);
                WxPaySandboxSignKey.Response _response = _request.execute();
                if (_response.checkReturnCode()) {
                    this.mchKey = _response.signkey();
                }
            } catch (Exception e) {
                _LOG.error("try get sandbox signkey error: ", RuntimeUtils.unwrapThrow(e));
            }
        }
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public SSLConnectionSocketFactory getConnectionSocketFactory() {
        if (connectionSocketFactory == null) {
            synchronized (this) {
                if (connectionSocketFactory == null && StringUtils.isNotBlank(mchId) && StringUtils.isNotBlank(certFilePath)) {
                    try {
                        connectionSocketFactory = HttpClientHelper.createConnectionSocketFactory(new URL(certFilePath), mchId.toCharArray());
                    } catch (Exception e) {
                        _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                    }
                }
            }
        }
        return connectionSocketFactory;
    }

    public void setConnectionSocketFactory(SSLConnectionSocketFactory connectionSocketFactory) {
        this.connectionSocketFactory = connectionSocketFactory;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public boolean isSandboxEnabled() {
        return sandboxEnabled;
    }

    public void setSandboxEnabled(boolean sandboxEnabled) {
        this.sandboxEnabled = sandboxEnabled;
        //
        __doGetSandboxSignKeyIfNeed();
    }

    public String getSandboxPrefix() {
        if (sandboxEnabled) {
            return sandboxPrefix;
        }
        return "";
    }

    public void setSandboxPrefix(String sandboxPrefix) {
        sandboxPrefix = StringUtils.defaultIfBlank(sandboxPrefix, "sandboxnew");
        if (StringUtils.startsWith(sandboxPrefix, "/")) {
            sandboxPrefix = StringUtils.substringAfter(sandboxPrefix, "/");
        }
        if (!StringUtils.endsWith(this.sandboxPrefix, "/")) {
            sandboxPrefix = sandboxPrefix + "/";
        }
        this.sandboxPrefix = sandboxPrefix;
    }
}
