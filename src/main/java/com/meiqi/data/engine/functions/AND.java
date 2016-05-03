package com.meiqi.data.engine.functions;

import com.meiqi.data.engine.ArgsCountError;
import com.meiqi.data.engine.DataUtil;
import com.meiqi.data.engine.RengineException;
import com.meiqi.data.engine.excel.CalInfo;
import com.meiqi.data.engine.excel.CalculateError;
import com.meiqi.data.engine.excel.NumberPool;
import com.meiqi.data.util.LogUtil;

/**
 * User: 
 * Date: 13-11-4
 * Time: 下午1:00
 */
public final class AND extends Function {
    static final String NAME = AND.class.getSimpleName();

    @Override
    public Object eval(CalInfo calInfo, Object[] args) throws RengineException, CalculateError {
        if (args.length == 0) {
            throw new ArgsCountError(NAME);
        }
        for (Object obj : args) {
            if (DataUtil.compare(DataUtil.getNumberValue(obj).doubleValue(), NumberPool.DOUBLE_0)
                    == 0) {
                return false;
            }
        }
        return true;
    }
}
