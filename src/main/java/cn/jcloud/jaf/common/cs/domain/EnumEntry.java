package cn.jcloud.jaf.common.cs.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class EnumEntry {
    private String valueCode;
    private String valueName;
    private String language;

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String valueCode;
        private String valueName;
        private String language;

        private Builder(){
        }

        public Builder valueCode(String valueCode) {
            this.valueCode = valueCode;
            return this;
        }
        public Builder valueName(String valueName) {
            this.valueName = valueName;
            return this;
        }
        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public EnumEntry build() {
            check();
            EnumEntry enumEntry = new EnumEntry();
            enumEntry.valueCode = this.valueCode;
            enumEntry.valueName = this.valueName;
            enumEntry.language = StringUtils.defaultString(this.language, "zh");

            return enumEntry;
        }

        private void check() {
            Assert.hasText(this.valueCode, "值集项编码不能为空");
            Assert.hasText(this.valueName, "值集项描述不能为空");
        }
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
