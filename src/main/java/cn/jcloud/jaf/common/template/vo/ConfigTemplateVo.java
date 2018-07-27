package cn.jcloud.jaf.common.template.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author hasayaki(125473)
 *
 */
public class ConfigTemplateVo {

	/**
     * 应用路由id
     */
    @NotNull(message = "app_router_id can't be null")
    private Long appRouterId;

    /**
     * 模板id
     */
    @NotBlank(message = "template_id can't be blank")
	@Pattern(regexp = "^[a-zA-Z0-9-_]{1,36}$", message = "template_id max length 36 and must be made up of [a-zA-Z0-9-_]")
    @Transient
    private String templateId;

    public ConfigTemplateVo() {
		super();
	}

	public Long getAppRouterId() {
		return appRouterId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setAppRouterId(Long appRouterId) {
		this.appRouterId = appRouterId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
