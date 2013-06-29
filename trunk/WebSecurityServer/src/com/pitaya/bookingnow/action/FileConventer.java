package com.pitaya.bookingnow.action;


import java.io.File;
import java.util.Map;

import com.opensymphony.xwork2.conversion.impl.DefaultTypeConverter;

public class FileConventer extends DefaultTypeConverter {

	@Override
	public Object convertValue(Map<String, Object> context, Object value,
			Class toType) {
		File file = null;
		if(toType== java.io.File.class) {
			String[] params = (String[])value;

			file = new File(params[0]);
			return file;
		}
		return value;
	}
	
}
