package com.meiqi.liduoo.data.engine.functions._W_DC_;

import java.text.ParseException;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.meiqi.data.engine.ArgsCountError;
import com.meiqi.data.engine.DataUtil;
import com.meiqi.data.engine.RengineException;
import com.meiqi.data.engine.excel.CalInfo;
import com.meiqi.data.engine.excel.CalculateError;
import com.meiqi.liduoo.base.utils.CacheUtils;
import com.meiqi.liduoo.base.utils.CommonUtils;
import com.meiqi.liduoo.base.utils.WeChatUtils;
import com.meiqi.liduoo.data.engine.functions.WeChatFunction;
import com.meiqi.liduoo.fastweixin.api.DataCubeAPI;
import com.meiqi.liduoo.fastweixin.api.config.ApiConfig;
import com.meiqi.liduoo.fastweixin.api.response.GetInterfaceSummaryResponse;

/**
 * 获取接口发送分布月数据
 * 
 * 参见<a href=
 * "http://mp.weixin.qq.com/wiki/13/32ef6cbf7bf4454c5826edf0136a2431.html">微信文档
 * - 接口分析数据接口</a>
 * 
 * <pre>
 * 1、微信AppId 
 * 2、微信AppSecret
 * 3、开始日期：long类型或者yyyy-MM-dd格式字符串
 * 4、结束日期：long类型或者yyyy-MM-dd格式字符串
 * 5、nocache标志：【可选】设置为nocache表示不使用缓存数据，强制刷新
 * 
 * 返回JSON：
 * {
 * errcode :"0",
 * errmsg :"",
    "list": [ 
         { 
           "ref_date": "2014-12-07", 
           "callback_count": 36974, 
           "fail_count": 67, 
           "total_time_cost": 14994291, 
           "max_time_cost": 5044
       }
	//后续还有不同ref_date（在begin_date和end_date之间）的数据
    ]
}
 * </pre>
 * 
 * @author FrankGui 2015年12月15日
 */
public class _W_DC_INTERFACE_SUMMARY extends WeChatFunction {
	public static final String NAME = _W_DC_INTERFACE_SUMMARY.class.getSimpleName();

	/**
	 * 规则函数执行方法
	 * 
	 * @see com.meiqi.data.engine.functions.Function#eval(com.meiqi.data.engine.excel
	 *      .CalInfo, java.lang.Object[])
	 */
	@Override
	public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateError {
		if (args.length < 4) {
			throw new ArgsCountError(NAME);
		}
		final String appId = getAppId(DataUtil.getStringValue(args[0]));
		final String appSecret = getAppSecret(DataUtil.getStringValue(args[1]));
		final String beginStr = DataUtil.getStringValue(args[2]);
		final String endStr = DataUtil.getStringValue(args[3]);

		String key = appId + "@" + appSecret + "@" + beginStr + "@" + endStr + "@" + NAME;
		final boolean noCache = "nocache".equalsIgnoreCase(DataUtil.getStringValue(args[args.length - 1]));
		GetInterfaceSummaryResponse summary = noCache ? null : (GetInterfaceSummaryResponse) CacheUtils.getCache(key);
		if (summary == null) {
			ApiConfig config = WeChatUtils.initApiConfig(appId, appSecret);
			DataCubeAPI api = new DataCubeAPI(config);

			Date beginDate = null, endDate = null;

			try {
				beginDate = CommonUtils.toDate(beginStr, null);
			} catch (ParseException e) {
				throw new RengineException(calInfo.getServiceName(), NAME + "日期格式错误： " + beginStr);
			}
			try {
				endDate = CommonUtils.toDate(endStr, null);
			} catch (ParseException e) {
				throw new RengineException(calInfo.getServiceName(), NAME + "日期格式错误： " + endStr);
			}
			GetInterfaceSummaryResponse result = api.getInterfaceSummary(beginDate, endDate);

			if (!result.verifyWechatResponse( false,config)) {
				throw new RengineException(calInfo.getServiceName(), NAME + "出现错误： " + result.toJsonString());
			}
			CacheUtils.putCache(key, result);

			summary = result;
		}

		return JSON.toJSONString(summary);
	}

}
