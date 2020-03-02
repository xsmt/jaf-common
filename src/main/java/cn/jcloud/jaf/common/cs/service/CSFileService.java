package cn.jcloud.jaf.common.cs.service;

import cn.jcloud.gaea.client.http.WafSecurityHttpClient;
import cn.jcloud.jaf.common.config.JafContext;
import cn.jcloud.jaf.common.cs.core.CSConfig;
import cn.jcloud.jaf.common.cs.core.CSSupportCondition;
import cn.jcloud.jaf.common.cs.domain.CSFileInfo;
import cn.jcloud.jaf.common.exception.JafI18NException;
import cn.jcloud.jaf.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Conditional(CSSupportCondition.class)
public class CSFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSFileService.class);

    @Autowired
    private CSConfig csConfig;

    @Autowired
    private WafSecurityHttpClient httpClient;

    public CSFileInfo getFileInfoById(Long fileId) {
        Assert.notNull(fileId);
        return httpClient.getForObject(csConfig.formatFileInfoUrl(), CSFileInfo.class, fileId);
    }

    public File downloadByFileId(Long fileId) {
        CSFileInfo fileInfo = getFileInfoById(fileId);
        return downloadByFileId(fileInfo);
    }

    public File downloadByFileId(CSFileInfo fileInfo) {
        Map downloadCodeMap = httpClient.getForObject(csConfig.formatFileDownloadCodeUrl(), Map.class, fileInfo.getId(), JafContext.getProjectName());
        String downloadCode = downloadCodeMap.get("down_code").toString();
        ResponseEntity<byte[]> response = httpClient.getForEntity(csConfig.formatFileDownloadUrl(), byte[].class, downloadCode);
        byte[] fileBytes = response.getBody();

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(fileBytes);
            File eSignFile = File.createTempFile("jaf_file_".concat(fileInfo.getId().toString()), fileInfo.getFileType());
            outputStream = new FileOutputStream(eSignFile);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
            inputStream = null;
            outputStream = null;

            return eSignFile;
        }catch (Exception e) {
            LOGGER.error("download file error: id:" + fileInfo.getId(), e);
            throw JafI18NException.of("文件处理失败，请稍后重试");
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public CSFileInfo uploadFile(File file, String fileName) {
        String fileMd5 = FileUtil.getFileHash(file);
        long readSize = 0L;
        int thisReadSize = 0;
        do {
            byte[] fileBytes = FileUtil.readFile(file, readSize);
            List<Byte> bytes = new ArrayList<>(fileBytes.length);
            for (byte fileByte : fileBytes) {
                bytes.add(Byte.valueOf(fileByte));
            }
            thisReadSize = fileBytes.length;
            readSize += thisReadSize;
            httpClient.postForObject(csConfig.formatUploadByteUrl(), bytes, Map.class, fileMd5);
        } while(thisReadSize >= FileUtil.BUFFER_LENGTH);

        CSFileInfo fileInfo = new CSFileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setFileType(FileUtil.getFileType(fileName));

        return httpClient.postForObject(csConfig.formatMergeByteFileUrl(), fileInfo, CSFileInfo.class, fileMd5);
    }
}
