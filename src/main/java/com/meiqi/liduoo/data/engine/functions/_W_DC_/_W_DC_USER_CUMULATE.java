/**
 * 
 */
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
import com.meiqi.liduoo.fastweixin.api.response.GetUserCumulateResponse;

/**
 * 获取用户增减数据，最大跨度为7天
 * 
 * 参见<a href=
 * "http://mp.weixin.qq.com/wiki/2/c87fed3bb002a0ed8184cb996b2acbbe.html">微信文档
 * </a>
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
            "ref_date": "2014-12-07",  //数据的日期
             "cumulate_user": 1217056  //总用户量s
        }
	//后续还有ref_date在begin_date和end_date之间的数据
    ]
}
 * </pre>
 * 
 * @author FrankGui 2015年12月12日
 */
public class _W_DC_USER_CUMULATE extends WeChatFunction {
	public static final String NAME = _W_DC_USER_CUMULATE.class.getSimpleName();

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
		GetUserCumulateResponse summary = noCache ? null : (GetUserCumulateResponse) CacheUtils.getCache(key);
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
			GetUserCumulateResponse result = api.getUserCumulate(beginDate, endDate);

			if (!result.verifyWechatResponse( false,config)) {
				throw new RengineException(calInfo.getServiceName(), NAME + "出现错误： " + result.toJsonString());
			}
			CacheUtils.putCache(key, result);

			summary = result;
		}

		return JSON.toJSONString(summary);
	}

}
