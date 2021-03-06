package com.meiqi.dsmanager.rmi;
/*
 * 推送更新server
 */
public interface IRmiPushService {
	/**
	 *通知dsorm 立即加载一个服务名为sername的服务到内存
	 */
	public void addService(String serName); 
	
	/**
	 * 通知dsorm 立即更新一个服务名为sername的服务到内存
	 */
	public void updateService(String serName);
	
	/**
	 * 通知dsorm 立即从内存中删除一个服务名为sername的服务
	 */
	public void deleteService(String jsonTService);
	
	   /*
     * 通过dbId刷新规则对应的db配置信息
     */
    public String reloadRuleDbConfig(String dbId);

	/**
	 * @return
	 */
    public String reloadWxApiInfo();
}
