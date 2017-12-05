package com.jmcaldera.counters.data.model;

import com.google.gson.annotations.SerializedName;

public class Counter{

	@SerializedName("count")
	private int count;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public Counter(int count, String id, String title) {
		this.count = count;
		this.id = id;
		this.title = title;
	}

	@Override
 	public String toString(){
		return 
			"Counter{" + 
			"count = '" + count + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}
}