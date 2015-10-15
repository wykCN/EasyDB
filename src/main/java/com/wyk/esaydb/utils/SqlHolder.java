package com.wyk.esaydb.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * **to holder the sql string and params   
 * 
 * @author ykwoocn@gmail.com
 * @date 2015年10月14日
 * @version 1.0
 */
public class SqlHolder {
	private String sql ;
	private List<Object> params  = new ArrayList<Object>();
	
	public void addParam(Object o) {
        params.add(o);
    }

    public Object[] getParams() {
        return params.toArray();
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("sql语句:");
        sb.append(sql).append("\r\n").append("             参数值:");
        for (Object obj : params) {
            sb.append(obj).append(",");
        }
        if (params.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
