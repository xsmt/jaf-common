package cn.jcloud.jaf.common.im.domain;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.util.Assert;

import java.util.*;

public class Message {
    private String product;
    private String action;
    private String target;
    private String hrefUrl;
    private String checkUrl;
    private String callBackUrl;
    private Date sendTime;
    private Map<String, Object> param;
    private List<Long> receivers;
    private List<Long> receiveOrgIds;

    private Message(){

    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String action;
        private String target;
        private String hrefUrl;
        private String checkUrl;
        private String callBackUrl;
        private Date sendTime;
        private Map<String, Object> param;
        private List<Long> receivers;
        private List<Long> receiveOrgIds;

        private Builder(){
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }
        public Builder target(String target) {
            this.target = target;
            return this;
        }
        public Builder hrefUrl(String hrefUrl) {
            this.hrefUrl = hrefUrl;
            return this;
        }
        public Builder checkUrl(String checkUrl) {
            this.checkUrl = checkUrl;
            return this;
        }
        public Builder callBackUrl(String callBackUrl) {
            this.callBackUrl = callBackUrl;
            return this;
        }
        public Builder sendTime(Date sendTime) {
            this.sendTime = sendTime;
            return this;
        }
        public Builder param(Map<String, Object> param) {
            this.param = param;
            return this;
        }
        public Builder addParam(String key, Object value) {
            if (this.param == null) {
                this.param = new HashMap<String, Object>();
            }
            this.param.put(key, value);
            return this;
        }
        public Builder receivers(List<Long> receivers) {
            this.receivers = receivers;
            return this;
        }
        public Builder addReceiver(Long receiver) {
            if (this.receivers == null) {
                this.receivers = new ArrayList<>();
            }
            this.receivers.add(receiver);
            return this;
        }
        public Builder receiveOrgIds(List<Long> receiveOrgIds) {
            this.receiveOrgIds = receiveOrgIds;
            return this;
        }
        public Builder addReceiveOrgIds(Long receiveOrg) {
            if (this.receiveOrgIds == null) {
                this.receiveOrgIds = new ArrayList<>();
            }
            this.receiveOrgIds.add(receiveOrg);
            return this;
        }


        public Message build() {
            check();
            Message message = new Message();
            message.product = JafContext.getProjectName();
            message.action = this.action;
            message.target = this.target;
            message.hrefUrl = this.hrefUrl;
            message.checkUrl = this.checkUrl;
            message.callBackUrl = this.callBackUrl;
            message.sendTime = this.sendTime;
            message.param = this.param;
            message.receivers = this.receivers;
            message.receiveOrgIds = this.receiveOrgIds;

            return message;
        }

        private void check() {
            Assert.hasText(this.action, "消息类型不能为空");

        }
    }

    public String getProduct() {
        return product;
    }

    public String getAction() {
        return action;
    }

    public String getTarget() {
        return target;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public List<Long> getReceivers() {
        return receivers;
    }

    public List<Long> getReceiveOrgIds() {
        return receiveOrgIds;
    }
}
