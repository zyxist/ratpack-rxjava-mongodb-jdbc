package com.zyxist.example.reactivex.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
	@JsonProperty
	public final boolean success;
	@JsonProperty
	public final String message;

	public static Result success() {
		return new Result(true, "");
	}

	public static Result failure(String message) {
		return new Result(false, message);
	}

	public Result(boolean success, String message) {
		this.success = success;
		this.message = message;
	}
}
