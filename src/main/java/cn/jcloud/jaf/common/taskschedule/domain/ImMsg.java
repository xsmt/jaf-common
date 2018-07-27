package cn.jcloud.jaf.common.taskschedule.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nd.social.common.constant.ErrorCode;
import com.nd.social.common.exception.WafI18NException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Wei Han on 2016-08-24.
 */
public class ImMsg {

    public enum FilterName {
        PSP("psp"),
        ORG_NODE("orgNode"),
        URI("uri"),
        P2P("p2p"),
        CONV("conv"),
        GID("gid"),
        V_ORG_NODE("vOrgNode");

        private String value;

        FilterName(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }

    @JsonProperty("fromUid")
    private String fromUid;

    @JsonProperty("toUid")
    private List<String> toUid;

    @JsonProperty("data")
    private String data;

    @JsonProperty("addition")
    private String addition;

    @JsonProperty("toUser")
    private boolean toUser;

    @JsonProperty("orgId")
    private String orgId;

    @JsonProperty("nodeId")
    private String nodeId;

    @JsonProperty("filterName")
    private FilterName filterName;

    @JsonProperty("filterArgs")
    private Map<String, Object> filterArgs;

    private ImMsg() {
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private String fromUid;
        private List<String> toUid;
        private String data;
        private String addition;
        private boolean toUser;
        private String orgId;
        private String nodeId;
        private FilterName filterName;
        private Map<String, Object> filterArgs;

        /**
         * 发IM消息的代理用户uid
         */
        public Builder fromUid(String fromUid) {
            this.fromUid = fromUid;
            return this;
        }

        /**
         * 接收用户的uid
         */
        public Builder toUid(List<String> toUid) {
            this.toUid = toUid;
            this.toUser = true;
            return this;
        }

        /**
         * 接收用户的uid
         */
        public Builder toUid(String toUid) {
            return toUid(Collections.singletonList(toUid));
        }

        /**
         * 消息内容
         */
        public Builder data(String data) {
            this.data = data;
            return this;
        }

        /**
         * 附加字段，多语言IM消息时存放相应JSON字符串信息
         */
        public Builder addition(String addition) {
            this.addition = addition;
            return this;
        }

        /**
         * 组织id
         */
        public Builder orgId(String orgId) {
            this.orgId = orgId;
            this.toUser = false;
            return this;
        }

        /**
         * 节点id
         */
        public Builder nodeId(String nodeId) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder filterName(FilterName filterName) {
            this.filterName = filterName;
            return this;
        }

        public Builder filterArgs(Map<String, Object> filterArgs) {
            this.filterArgs = filterArgs;
            return this;
        }

        public ImMsg build() {
            check();
            ImMsg imMsg = new ImMsg();
            imMsg.fromUid = this.fromUid;
            imMsg.toUid = this.toUid;
            imMsg.data = this.data;
            imMsg.addition = this.addition;
            imMsg.toUser = this.toUser;
            imMsg.orgId = this.orgId;
            imMsg.nodeId = this.nodeId;
            imMsg.filterName = this.filterName;
            imMsg.filterArgs = this.filterArgs;
            return imMsg;
        }

        private void check() {
            if (toUser) {
                if (CollectionUtils.isEmpty(toUid)) {
                    throw WafI18NException.of("缺少参数【ImMsg.toUid】", ErrorCode.REQUIRE_ARGUMENT);
                }
            } else {
                if (StringUtils.isBlank(orgId) || StringUtils.isBlank(nodeId)) {
                    throw WafI18NException.of("缺少参数【ImMsg.orgId】或【ImMsg.nodeId】", ErrorCode.REQUIRE_ARGUMENT);
                }
            }
            if (StringUtils.isBlank(data)) {
                if (StringUtils.isBlank(orgId) || StringUtils.isBlank(nodeId)) {
                    throw WafI18NException.of("缺少参数【ImMsg.data】", ErrorCode.REQUIRE_ARGUMENT);
                }
            }
        }
    }

    public String getFromUid() {
        return fromUid;
    }

    public List<String> getToUid() {
        return toUid;
    }

    public String getData() {
        return data;
    }

    public String getAddition() {
        return addition;
    }

    public boolean isToUser() {
        return toUser;
    }

    public String getOrgId() {
        return orgId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public FilterName getFilterName() {
        return filterName;
    }

    public Map<String, Object> getFilterArgs() {
        return filterArgs;
    }
}
