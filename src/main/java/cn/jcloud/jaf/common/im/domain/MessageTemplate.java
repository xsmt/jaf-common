package cn.jcloud.jaf.common.im.domain;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class MessageTemplate {
    private String systemCode;
    private String product;
    private String action;
    private String title;
    private String content;
    private String authCode;
    private String hrefUrl;
    private String hrefTitle;
    private Boolean subscribeAble;
    private List<MessageTemplateParam> params;

    private MessageTemplate(){
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String systemCode;
        private String action;
        private String title;
        private String content;
        private String authCode;
        private String hrefUrl;
        private String hrefTitle;
        private Boolean subscribeAble;
        private List<MessageTemplateParam> params;

        private Builder(){
        }

        public Builder systemCode(String systemCode) {
            this.systemCode = systemCode;
            return this;
        }
        public Builder action(String action) {
            this.action = action;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }
        public Builder content(String content) {
            this.content = content;
            return this;
        }
        public Builder authCode(String authCode) {
            this.authCode = authCode;
            return this;
        }
        public Builder hrefUrl(String hrefUrl) {
            this.hrefUrl = hrefUrl;
            return this;
        }
        public Builder hrefTitle(String hrefTitle) {
            this.hrefTitle = hrefTitle;
            return this;
        }
        public Builder subscribeAble(Boolean subscribeAble) {
            this.subscribeAble = subscribeAble;
            return this;
        }
        public Builder params(List<MessageTemplateParam> params) {
            this.params = params;
            return this;
        }
        public Builder addParam(MessageTemplateParam param) {
            if (this.params == null) {
                this.params = new ArrayList<>();
            }
            this.params.add(param);
            return this;
        }

        public MessageTemplate build() {
            check();
            MessageTemplate messageTemplate = new MessageTemplate();
            messageTemplate.systemCode = this.systemCode;
            messageTemplate.product = JafContext.getProjectName();
            messageTemplate.action = this.action;
            messageTemplate.title = this.title;
            messageTemplate.content = this.content;
            messageTemplate.authCode = this.authCode;
            messageTemplate.hrefUrl = this.hrefUrl;
            messageTemplate.hrefTitle = this.hrefTitle;
            messageTemplate.subscribeAble = this.subscribeAble == null ? false : this.subscribeAble;
            messageTemplate.params = this.params;

            return messageTemplate;
        }

        private void check() {
            Assert.hasText(this.systemCode, "消息应用系统不能为空");
            Assert.hasText(this.action, "消息类型不能为空");
            Assert.hasText(this.title, "消息标题不能为空");
            Assert.hasText(this.content, "消息内容不能为空");
        }
    }

    public String getSystemCode() {
        return systemCode;
    }

    public String getProduct() {
        return product;
    }

    public String getAction() {
        return action;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getHrefUrl() {
        return hrefUrl;
    }

    public String getHrefTitle() {
        return hrefTitle;
    }

    public Boolean getSubscribeAble() {
        return subscribeAble;
    }

    public List<MessageTemplateParam> getParams() {
        return params;
    }
}
