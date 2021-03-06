package com.meiqi.openservice.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName: RepInfo
 * @Description:请求信息
 * @author zhouyongxiong
 * @date 2015年6月30日 下午3:12:55
 *
 */
public class RepInfo {
    private Integer site_id;
    private String  action;
	private String  method;
	private String param;
	private String memKey;
	private Map<String, Object> header = new HashMap<String, Object>();
	private String authFlag;// 是否需要网页授权调用,true表示需要
	
	public Integer getSite_id() {
	        return site_id;
	}
	public void setSite_id(Integer site_id) {
	        this.site_id = site_id;
	}
	public String getMemKey() {
        return memKey;
    }
    public void setMemKey(String memKey) {
        this.memKey = memKey;
    }
    public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, Object> getHeader() {
        return header;
    }
    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

	/**
	 * 是否存在网页授权函数调用的标志,true表示存在，其他表示不存在
	 * 
	 * @return
	 */
	public String getAuthFlag() {
		return authFlag;
	}

	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}
}
