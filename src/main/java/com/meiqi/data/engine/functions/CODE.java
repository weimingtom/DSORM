package com.meiqi.data.engine.functions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.meiqi.data.engine.ArgsCountError;
import com.meiqi.data.engine.Cache4D2Data;
import com.meiqi.data.engine.D2Data;
import com.meiqi.data.engine.RengineException;
import com.meiqi.data.engine.ServiceNotFound;
import com.meiqi.data.engine.Services;
import com.meiqi.data.engine.excel.CalInfo;
import com.meiqi.data.engine.excel.CalculateError;
import com.meiqi.data.entity.TService;
import com.meiqi.dsmanager.action.IDataAction;
import com.meiqi.dsmanager.po.dsmanager.DsManageReqInfo;
import com.meiqi.dsmanager.po.rule.RuleServiceResponseData;
import com.meiqi.openservice.commons.util.DataUtil;
import com.meiqi.util.MyApplicationContextUtil;

//code生成
//code(长度,code类型,大小写)
//长度：18 意思生成一个18位的code
//code类型: 0=数字字母混合 1=纯字母 2-纯数字
//大小写：如果code要生成字母 0=大小写混合 1=小写 2=大写

public class CODE extends Function{
	static final String NAME = CODE.class.getSimpleName();
	@Override
	public Object eval(CalInfo calInfo, Object[] args) throws RengineException,
			CalculateError {
		if(4!=args.length){
			throw new ArgsCountError(NAME);
		}
		int codeLength=Integer.valueOf(args[0].toString()); //获取长度
		int type=Integer.valueOf(args[1].toString());       //获取类型
		int codeCase=Integer.valueOf(args[2].toString());  //获取大小写
		String codeType=args[3].toString();   //获取查重code类型数据源参数
		//生成规则控制,出现重复生成10次
		int i=1;
		String code=null;
		while(i<10){
			code=randomCode(type, codeCase, codeLength);
			if(null==code){
				i++;
			}else{
				if(getDataByRule(code, codeType,calInfo)){
					break;
				}else{
					code=null;
					i++;
				}
			}
		}
		if(null==code){
			code="";
		}
		return code;
	}

	//通过规则取数据
	//调用规则查重复 true可用，无重复  false不可用，有重复
	private boolean getDataByRule(String code,String codeType,CalInfo calInfo) throws RengineException{
		if("0".equals(codeType)){
			return true; 
		}
		TService servicePo = Services.getService("HS_HSV1_code");
        if (servicePo == null) {
            throw new ServiceNotFound("HS_HSV1_code");
        }
        Map<String, Object> currentParam=new HashMap<String, Object>();
        currentParam.put("code", code);
        currentParam.put("type", codeType);
        final D2Data d2Data =Cache4D2Data.getD2Data(servicePo, currentParam, calInfo.getCallLayer()
                , calInfo.getServicePo(), calInfo.getParam(), NAME);
        if(null==d2Data.getData()){
        	return false;
        }
        Object obj=d2Data.getValue("exist_code", 0);
        if(null==obj){
        	return false;
        }
        
        if("".equals(obj.toString().trim())){
        	return true;
        }
        return false;
		
	}
	
	private String randomCode(int type,int codeCase,int codeLength){
		String code=null;
		if(1==type){ //生成纯字母
			if(1==codeCase){ //字母全小写
				code=RandomStringUtils.random(codeLength, "abcdefghijklmnopqrstuvwxyz");
			}else if(2==codeCase){ //字母全大写
				code=RandomStringUtils.random(codeLength, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			}else{ //大小写都包含
				code=RandomStringUtils.randomAlphabetic(codeLength);  
			}
		}else if(2==type){ //生成纯数字
			code=RandomStringUtils.randomNumeric(codeLength); 
		}else{  //字母数字混合
			if(1==codeCase){ //字母全小写
				code=RandomStringUtils.random(codeLength, "abcdefghijklmnopqrstuvwxyz0123456789");
			}else if(2==codeCase){ //字母全大写
				code=RandomStringUtils.random(codeLength, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
			}else{ //大小写都包含
				code=RandomStringUtils.randomAlphanumeric(codeLength);
			}
		}
		return code;
	}
}
