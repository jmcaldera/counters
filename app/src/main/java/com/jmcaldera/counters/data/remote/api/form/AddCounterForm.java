package com.jmcaldera.counters.data.remote.api.form;

import com.google.gson.annotations.SerializedName;

public class AddCounterForm {

	@SerializedName("title")
	private String title;

	private AddCounterForm(String title) {
		this.title = title;
	}

	public static AddCounterForm create(String title) {
		return new AddCounterForm(title);
	}
}