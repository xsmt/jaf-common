package cn.jcloud.jaf.common.cs.core;

import cn.jcloud.gaea.util.UrlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Conditional(CSSupportCondition.class)
@Configuration
@PropertySource(value = "classpath:cs.properties")
public class CSConfig {

    @Value("${cs.host}")
    private String host;

    @Value("${cs.enum.path}")
    private String enumPath;

    @Value("${cs.enum.init_url:/v0.1/enums}")
    private String enumUrl;

    @Value("${cs.sequence.init_url:/v0.1/sequences}")
    private String sequenceInitUrl;

    @Value("${cs.sequence.next_url:/v0.1/sequences/{code}}")
    private String sequenceNextUrl;

    @Value("${cs.file.info_url:/v0.1/files/{id}}")
    private String fileInfoUrl;

    @Value("${cs.file.download_code_url:/v0.1/files/down/code/{id}?module={module}}")
    private String fileDownloadCodeUrl;

    @Value("${cs.file.download_url:/v0.1/files/down/{code}}")
    private String fileDownloadUrl;

    @Value("${cs.file.upload_byte_url:/v0.1/files/upload/byte/{md5}}")
    private String uploadByteUrl;

    @Value("${cs.file.merge_byte_url:/v0.1/files/merge/byte/{md5}}")
    private String mergeByteFileUrl;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEnumPath() {
        return enumPath;
    }

    public void setEnumPath(String enumPath) {
        this.enumPath = enumPath;
    }

    public String getEnumUrl() {
        return enumUrl;
    }

    public void setEnumUrl(String enumUrl) {
        this.enumUrl = enumUrl;
    }

    public String getSequenceInitUrl() {
        return sequenceInitUrl;
    }

    public void setSequenceInitUrl(String sequenceInitUrl) {
        this.sequenceInitUrl = sequenceInitUrl;
    }

    public String getSequenceNextUrl() {
        return sequenceNextUrl;
    }

    public void setSequenceNextUrl(String sequenceNextUrl) {
        this.sequenceNextUrl = sequenceNextUrl;
    }

    public String getFileInfoUrl() {
        return fileInfoUrl;
    }

    public void setFileInfoUrl(String fileInfoUrl) {
        this.fileInfoUrl = fileInfoUrl;
    }

    public String getFileDownloadCodeUrl() {
        return fileDownloadCodeUrl;
    }

    public void setFileDownloadCodeUrl(String fileDownloadCodeUrl) {
        this.fileDownloadCodeUrl = fileDownloadCodeUrl;
    }

    public String getFileDownloadUrl() {
        return fileDownloadUrl;
    }

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public String getUploadByteUrl() {
        return uploadByteUrl;
    }

    public void setUploadByteUrl(String uploadByteUrl) {
        this.uploadByteUrl = uploadByteUrl;
    }

    public String getMergeByteFileUrl() {
        return mergeByteFileUrl;
    }

    public void setMergeByteFileUrl(String mergeByteFileUrl) {
        this.mergeByteFileUrl = mergeByteFileUrl;
    }

    public String formatEnumCreateUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.enumUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.enumUrl);
    }

    public String formatSequenceCreateUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.sequenceInitUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.sequenceInitUrl);
    }

    public String formatSequenceNextUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.sequenceNextUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.sequenceNextUrl);
    }

    public String formatFileInfoUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.fileInfoUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.fileInfoUrl);
    }

    public String formatFileDownloadCodeUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.fileDownloadCodeUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.fileDownloadCodeUrl);
    }

    public String formatFileDownloadUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.fileDownloadUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.fileDownloadUrl);
    }

    public String formatUploadByteUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.uploadByteUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.uploadByteUrl);
    }

    public String formatMergeByteFileUrl() {
        if (StringUtils.isEmpty(this.host) || StringUtils.isEmpty(this.mergeByteFileUrl)) {
            return null;
        }

        return UrlUtil.combine(this.host, this.mergeByteFileUrl);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
