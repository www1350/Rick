package com.absurd.rick.util;

import com.google.common.collect.Lists;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class AsmTools {
	
	
	private final static LocalVariableTableParameterNameDiscoverer LOCAL_VARIABLE_TABLE_PARAMETER_NAME_DISCOVERER=new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 获取方法参数名列表
	 * @param m
	 * @return
	 */
	public static List<String> getMethodParamNames(Method m){
		try{
			if(m==null){
				return Collections.emptyList();
			}
			String[] params=LOCAL_VARIABLE_TABLE_PARAMETER_NAME_DISCOVERER.getParameterNames(m);
			if(params==null){
				return Collections.emptyList();
			}
			return Lists.newArrayList(params);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return Collections.emptyList();
	}






}
