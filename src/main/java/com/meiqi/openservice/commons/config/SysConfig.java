package com.meiqi.openservice.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.meiqi.openservice.action.BaseAction;
import com.meiqi.util.ConfigFileUtil;

public class SysConfig {

    private static Properties  properties           = new Properties();
    // app service mapping
    private static Properties  actionProperties = new Properties();
    //微信页面跳转配置文件
    private static Properties  weChatProperties = new Properties();
	public static final Logger logger               = Logger.getLogger(SysConfig.class);

	
	static String extraPropertiesPath = BaseAction.basePath + File.separator + "lejj_resource" + File.separator + "config"+ File.separator +"extraSysConfig.properties";
	private static Properties  extraProperties = new Properties();
	    
    static {
        readProperties();
    }

    public static String getWeChatValue(String key){
    	return weChatProperties.getProperty(key);
    }

    public static String getValue(String key) {
        return properties.getProperty(key)==null?extraProperties.getProperty(key):properties.getProperty(key);
    }



    public static void readProperties() {
        try {
            logger.info("SysConfig static init start.");
            properties = ConfigFileUtil.propertiesReader("sysConfig.properties");
            weChatProperties=ConfigFileUtil.propertiesReader("weChatRedirectConfig.properties");
            extraProperties.load(new FileInputStream(new File(extraPropertiesPath)));
        } catch (Exception e) {
            logger.error("fail to find config file sysConfig.properties", e);
        }
    }



    /**
     * 
     * @Title: clearAndReadProperties
     * @Description:清空proprties 并再次读入（用于配置了新的property）
     * @param
     * @return void
     * @throws
     */
    public static void clearAndReadProperties() {
        readProperties();
    }
    public static Properties getActionProperties() {
		return actionProperties;
	}
}
