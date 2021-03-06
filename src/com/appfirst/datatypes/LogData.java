/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appfirst.datatypes;


import org.json.JSONObject;

/**
 * @author Bin Liu
 * <br>Example:
 * <code>
 * "Message":"info log message here", "Severity":"0 - Info"
 * </code>
 */
public class LogData {
	
	/**
	 * @param jsonObject
	 */
	public LogData(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		Message = BaseResourceData.getStringField("Message", jsonObject);
		Severity = BaseResourceData.getStringField("Severity", jsonObject);
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getSeverity() {
		return Severity;
	}
	public void setSeverity(String severity) {
		Severity = severity;
	}
	private String Message;
	private String Severity;
}
