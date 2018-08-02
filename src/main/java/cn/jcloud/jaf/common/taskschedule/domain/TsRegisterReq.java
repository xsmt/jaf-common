package cn.jcloud.jaf.common.taskschedule.domain;

import cn.jcloud.jaf.common.constant.ErrorCode;
import cn.jcloud.jaf.common.exception.JafI18NException;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Wei Han on 2016-08-25.
 */
public class TsRegisterReq {

    private String name;
    private String ticketUri;
    private String ticketPwd;
    private String projectName;

    private TsRegisterReq() {
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String ticketUri;
        private String ticketPwd;
        private String projectName;

        /**
         * 必填，接入项目名，即消息队列里边名里边的前缀名
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * 选填，发IM消息的对应uri,如果这个值不填，则ImMsg中的fromUid必填
         */
        public Builder ticketUri(String ticketUri) {
            this.ticketUri = ticketUri;
            return this;
        }

        /**
         * 选填，发IM消息对应的密钥,如果这个值不填，则ImMsg中的fromUid必填
         */
        public Builder ticketPwd(String ticketPwd) {
            this.ticketPwd = ticketPwd;
            return this;
        }

        /**
         * 项目名，选填
         */
        public Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public TsRegisterReq build() {
            if (StringUtils.isBlank(this.name)) {
                throw JafI18NException.of("缺少参数【TsRegisterReq.name】", ErrorCode.REQUIRE_ARGUMENT);
            }
            TsRegisterReq req = new TsRegisterReq();
            req.name = this.name;
            req.ticketUri = this.ticketUri;
            req.ticketPwd = this.ticketPwd;
            req.projectName = this.projectName;
            return req;
        }
    }

    public String getName() {
        return name;
    }

    public String getTicketUri() {
        return ticketUri;
    }

    public String getTicketPwd() {
        return ticketPwd;
    }

    public String getProjectName() {
        return projectName;
    }
}
