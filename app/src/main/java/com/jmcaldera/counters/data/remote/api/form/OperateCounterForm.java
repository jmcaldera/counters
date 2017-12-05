package com.jmcaldera.counters.data.remote.api.form;

import com.google.gson.annotations.SerializedName;

public class OperateCounterForm{

	@SerializedName("id")
	private String id;

	private OperateCounterForm(String id) {
		this.id = id;
	}

	public static OperateCounterForm create(String id) {
		return new OperateCounterForm(id);
	}
}