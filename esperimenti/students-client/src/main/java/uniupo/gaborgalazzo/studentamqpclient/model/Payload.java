package uniupo.gaborgalazzo.studentamqpclient.model;

import java.io.Serializable;

public class Payload implements Serializable
{
	public static final String FUN_FIND_BY_ID = "find_by_id";
	public static final String FUN_SEARCH_ALL= "search_all";
	public static final String FUN_ADD = "add";
	public static final String FUN_EDIT = "edit";
	public static final String FUN_DELETE = "delete";

	private String function;
	private Object request;
	private Object response;
	private String uid;
	private String clientId;

	public String getFunction()
	{
		return function;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}

	public Object getRequest()
	{
		return request;
	}

	public void setRequest(Object request)
	{
		this.request = request;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
