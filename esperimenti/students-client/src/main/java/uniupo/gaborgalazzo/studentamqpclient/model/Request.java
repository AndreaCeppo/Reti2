package uniupo.gaborgalazzo.studentamqpclient.model;

import java.io.Serializable;

public class Request implements Serializable
{
	public static final String FUN_FIND_BY_ID = "find_by_id";
	public static final String FUN_SEARCH_ALL= "search_all";
	public static final String FUN_ADD = "add";
	public static final String FUN_EDIT = "edit";
	public static final String FUN_DELETE = "delete";

	private String function;
	private Object data;

	public String getFunction()
	{
		return function;
	}

	public void setFunction(String function)
	{
		this.function = function;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}
}
