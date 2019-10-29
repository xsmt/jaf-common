package cn.jcloud.jaf.common.im.domain;

import cn.jcloud.jaf.common.im.enums.MessageTemplateParamTypeEnum;
import org.springframework.util.Assert;

public class MessageTemplateParam {
    private String paramKey;
    private String paramName;
    private MessageTemplateParamTypeEnum paramType;
    private Boolean isMultiLine;

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String paramKey;
        private String paramName;
        private MessageTemplateParamTypeEnum paramType;
        private Boolean isMultiLine;

        private Builder(){
        }

        public Builder paramKey(String paramKey) {
            this.paramKey = paramKey;
            return this;
        }
        public Builder paramName(String paramName) {
            this.paramName = paramName;
            return this;
        }
        public Builder paramType(MessageTemplateParamTypeEnum paramType) {
            this.paramType = paramType;
            return this;
        }
        public Builder isMultiLine(Boolean isMultiLine) {
            this.isMultiLine = isMultiLine;
            return this;
        }

        public MessageTemplateParam build() {
            check();
            MessageTemplateParam messageTemplateParam = new MessageTemplateParam();
            messageTemplateParam.paramKey = this.paramKey;
            messageTemplateParam.paramName = this.paramName;
            messageTemplateParam.paramType = this.paramType;
            messageTemplateParam.isMultiLine = this.isMultiLine == null ? false : this.isMultiLine;

            return messageTemplateParam;
        }

        private void check() {
            Assert.hasText(this.paramKey, "参数代码不能为空");
            Assert.hasText(this.paramName, "参数名称不能为空");
            Assert.notNull(this.paramType, "参数类型不能为空");
        }
    }

    public String getParamKey() {
        return paramKey;
    }

    public String getParamName() {
        return paramName;
    }

    public MessageTemplateParamTypeEnum getParamType() {
        return paramType;
    }

    public Boolean getMultiLine() {
        return isMultiLine;
    }
}
