package cn.jcloud.jaf.common.taskschedule.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务调度：消息数据（任务）
 * Created by Wei Han on 2016/6/15.
 */
@JsonDeserialize(builder = MsgInfo.Builder.class)
public class MsgInfo {

    private MsgType type;
    private String url;
    private HttpMethod method;
    private Map<String, Object> header;
    //为了使body可以接收实体类型，下面的setter方法有特殊处理
    private Object body;
    private Map<String, Object> addition;
    private MsgStrategy strategy;
    private LogLevel loglevel;

    private MsgInfo() {
    }

    public static Builder create() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private MsgType type = MsgType.REPOST;
        private String url;
        private HttpMethod method;
        private Map<String, Object> header;
        //为了使body可以接收实体类型,所以使用Object类型
        private Object body;
        private Map<String, Object> addition;
        private MsgStrategy strategy;
        private LogLevel loglevel;
        private String callbackUrl;
        private ImMsg imMsg;

        /**
         * 消息类型.REPOST=直接进行URL重发的简单业务，
         * 无需再解析addition的专用业务信息；IM=im业务消息专用,
         * 需解析addition的信息并处理；IM_MULTI_LANG=多语言im业务消息专用,
         * 需解析addition的信息并处理。非REPOST类型的消息，如有url字段，也会对url字段进行相应的转发处理
         */
        public Builder type(MsgType type) {
            this.type = type;
            return this;
        }

        /**
         * common消息类型的url
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * POST,GET,PUT,DELETE等对应的method方法
         */
        public Builder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        /**
         * 设置url对应的header信息
         */
        public Builder header(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /**
         * 合并url对应的header信息
         */
        public Builder mergeHeader(Map<String, Object> header) {
            if (this.header == null) {
                this.header = header;
            } else {
                this.header = new HashMap<>(this.header);
                this.header.putAll(header);
            }
            return this;
        }

        /**
         * 合并url对应的header信息
         */
        public Builder mergeHeader(String header, Object value) {
            if (null == value) {
                return this;
            }
            if (CharSequence.class.isAssignableFrom(value.getClass())
                    && StringUtils.isBlank((CharSequence) value)) {
                return this;
            }
            this.mergeHeader(Collections.singletonMap(header, value));
            return this;
        }

        /**
         * url对应的body信息
         */
        public Builder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * 业务的附加数据消息，
         * 对于如IM消息("IMMSG":MSG_IMMSG)及回调信息("CALLBACK":CALL_INFO)，
         * 需解析该字段并获取所需的数据进行处理。
         */
        public Builder addition(Map<String, Object> addition) {
            this.addition = addition;
            return this;
        }

        /**
         * 消息重发策略 见MSG_STRATEGY结构体
         */
        public Builder strategy(MsgStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        /**
         * 记录文件日志的级别(NORMAL:不管最终处理成功或失败，
         * 都记录文件日志;ERROR:最终处理失败才记录文件日志。默认为ERROR级别)
         */
        public Builder loglevel(LogLevel loglevel) {
            this.loglevel = loglevel;
            return this;
        }

        /**
         * 回调url
         */
        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        /**
         * IM 消息
         */
        public Builder imMsg(ImMsg imMsg) {
            this.imMsg = imMsg;
            this.type = MsgType.IM;
            return this;
        }

        public MsgInfo build() {
            check();
            MsgInfo msgInfo = new MsgInfo();
            msgInfo.url = this.url;
            msgInfo.method = this.method;
            msgInfo.header = this.header;
            msgInfo.body = this.body;
            msgInfo.strategy = this.strategy;
            msgInfo.loglevel = this.loglevel;
            if (StringUtils.isNotBlank(this.callbackUrl)) {
                buildCallback();
            }
            if (MsgType.IM == type || MsgType.IM_MULTI_LANG == type) {
                buildImMsg();
            }
            msgInfo.type = this.type;
            msgInfo.addition = this.addition;
            return msgInfo;
        }

        private void buildCallback() {
            Map<String, String> callInfo = new HashMap<>();
            callInfo.put("url", callbackUrl);
            if (null == this.addition) {
                this.addition = new HashMap<>();
            }
            this.addition.put("CALLBACK", callInfo);
        }

        private void buildImMsg() {
            if (null == this.addition) {
                this.addition = new HashMap<>();
            }
            this.addition.put("IMMSG", imMsg);
        }

        private void check() {
            if (MsgType.REPOST == this.type) {
                if (StringUtils.isBlank(this.url)) {
                    throw WafI18NException.of("缺少参数【MsgInfo.url】", ErrorCode.REQUIRE_ARGUMENT);
                }
                if (null == this.method) {
                    throw WafI18NException.of("缺少参数【MsgInfo.method】", ErrorCode.REQUIRE_ARGUMENT);
                }
            }
            if (MsgType.IM == this.type || MsgType.IM_MULTI_LANG == type) {
                if (null == imMsg) {
                    throw WafI18NException.of("缺少参数【MsgInfo.imMsg】", ErrorCode.REQUIRE_ARGUMENT);
                }
                if (MsgType.IM_MULTI_LANG == type && null == imMsg.getAddition()) {
                    throw WafI18NException.of("缺少参数【MsgInfo.imMsg.addition】", ErrorCode.REQUIRE_ARGUMENT);
                }
            }
        }
    }

    public MsgType getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, Object> getAddition() {
        return addition;
    }

    public MsgStrategy getStrategy() {
        return strategy;
    }

    public LogLevel getLoglevel() {
        return loglevel;
    }
}
