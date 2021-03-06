package com.meiqi.liduoo.data.engine.functions._W_;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.meiqi.data.engine.ArgsCountError;
import com.meiqi.data.engine.DataUtil;
import com.meiqi.data.engine.RengineException;
import com.meiqi.data.engine.excel.CalInfo;
import com.meiqi.data.engine.excel.CalculateError;
import com.meiqi.liduoo.base.utils.CommonUtils;
import com.meiqi.liduoo.base.utils.WeChatUtils;
import com.meiqi.liduoo.data.engine.functions.WeChatFunction;
import com.meiqi.liduoo.fastweixin.api.MessageAPI;
import com.meiqi.liduoo.fastweixin.api.config.ApiConfig;
import com.meiqi.liduoo.fastweixin.api.response.GetSendMessageResponse;
import com.meiqi.liduoo.fastweixin.message.BaseMsg;
import com.meiqi.liduoo.fastweixin.util.JSONUtil;

/**
 * 群发消息给指定用户
 * 
 * <pre>
 *需要参数：
 * 1、微信AppId 
 * 2、微信AppSecret
 * 3、消息类型：text、image、voice、video、music、mpnews
 * 4、发送消息内容JSON字符串
 * 5、发送的用户OpenID列表,用逗号分隔字符串或者JSON格式String数组,至少两个用户
 * 
 * 返回：JSON字符串：
 * {
	"errcode":"0",
	"errmsg":""
    "msg_id":34182,  //消息发送任务的ID
  }
 * 
 * </pre>
 * 
 * @author FrankGui 2015年12月14日
 */
public class _W_MESSAGE_SENDTOUSER extends WeChatFunction {
	public static final String NAME = _W_MESSAGE_SENDTOUSER.class.getSimpleName();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.meiqi.data.engine.functions.Function#eval(com.meiqi.data.engine.excel
	 * .CalInfo, java.lang.Object[])
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateError {
		if (args.length < 5) {
			throw new ArgsCountError(NAME);
		}
		final String appId = getAppId(DataUtil.getStringValue(args[0]));
		final String appSecret = getAppSecret(DataUtil.getStringValue(args[1]));
		String msgType = DataUtil.getStringValue(args[2]);
		String msg = DataUtil.getStringValue(args[3]);
		final String openIdStr = DataUtil.getStringValue(args[4]);

		if("news".equalsIgnoreCase(msgType)) {
			msgType = "mpnews";//发送消息时强制将news类型改成mpnews，msg JSON字符串中不管他
		}
		msg = msg.replaceAll("\"msgType\"\\s*:\\s*\"news\"", "\"msgType\":\"mpnews\"");
		
		Class clazz = CommonUtils.getMsgClass(msgType);
		ApiConfig config = WeChatUtils.initApiConfig(appId, appSecret);
		MessageAPI api = new MessageAPI(config);
		BaseMsg baseMsg = (BaseMsg)JSONUtil.toBean(msg, clazz);

		String[] openIds = null;
		if (openIdStr.startsWith("[")) {
			List<String> list = JSON.parseArray(openIdStr, String.class);
			openIds = (String[]) list.toArray();
		} else {
			openIds = StringUtils.split(openIdStr, ",");
		}
		GetSendMessageResponse result = api.sendMessageToUser(baseMsg, openIds);
		if (!result.verifyWechatResponse( false,config)) {
			throw new RengineException(calInfo.getServiceName(), NAME + "出现错误： " + result.toJsonString());
		}

		return JSON.toJSONString(result);
	}

}