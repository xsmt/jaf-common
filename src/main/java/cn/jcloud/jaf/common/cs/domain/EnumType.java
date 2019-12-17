package cn.jcloud.jaf.common.cs.domain;

import cn.jcloud.jaf.common.config.JafContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class EnumType {
    private String product;
    private String code;
    private String name;
    private Boolean editable;
    private List<EnumEntry> values;

    private EnumType(){
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String code;
        private String name;
        private Boolean editable;
        private List<EnumEntry> values;

        private Builder(){
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder editable(Boolean editable) {
            this.editable = editable;
            return this;
        }
        public Builder values(List<EnumEntry> values) {
            this.values = values;
            return this;
        }
        public Builder addValue(EnumEntry value) {
            if (this.values == null) {
                this.values = new ArrayList<>();
            }
            this.values.add(value);
            return this;
        }

        public EnumType build() {
            check();
            EnumType messageTemplate = new EnumType();
            messageTemplate.product = JafContext.getProjectName();
            messageTemplate.code = this.code;
            messageTemplate.name = this.name;
            messageTemplate.editable = this.editable;
            messageTemplate.values = this.values;

            return messageTemplate;
        }

        private void check() {
            Assert.hasText(this.code, "枚举编码不能为空");
            Assert.hasText(this.name, "枚举枚举名称不能为空");
            Assert.notNull(this.editable, "枚举可维护标识不能为空");
            Assert.notEmpty(this.values, "枚举默认值集不能为空");
        }
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public List<EnumEntry> getValues() {
        return values;
    }

    public void setValues(List<EnumEntry> values) {
        this.values = values;
    }
}
