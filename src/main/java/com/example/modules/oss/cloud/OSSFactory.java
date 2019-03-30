package com.example.modules.oss.cloud;


import com.example.common.utils.ConfigConstant;
import com.example.common.utils.Constant;
import com.example.common.utils.SpringContextUtils;
import com.example.modules.sys.service.ISysConfigService;

/**
 * 文件上传Factory
 */
public final class OSSFactory {
    private static ISysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (ISysConfigService) SpringContextUtils.getBean("sysConfigService");
    }

    public static CloudStorageService build(){
        //获取云存储配置信息
        CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);
        if(config.getType() == Constant.CloudService.QINIU.getValue()){
            return new QiniuCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
            return new AliyunCloudStorageService(config);
        }else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
            return new QcloudCloudStorageService(config);
        }
        return null;
    }

}
