package com.zyxist.example.reactivex.rest.handlers;

import ratpack.error.ServerErrorHandler;
import ratpack.handling.Context;

import java.util.LinkedHashMap;
import java.util.Map;

import static ratpack.jackson.Jackson.json;

public class RatpackErrorHandler implements ServerErrorHandler {
	@Override
	public void error(Context context, Throwable throwable) throws Exception {
		Map<String, String> errorInfo = new LinkedHashMap<>();
		errorInfo.put("error", throwable.getClass().getSimpleName());
		errorInfo.put("message", throwable.getMessage());
		context.getResponse().status(500);
		context.render(json(errorInfo));
	}
}
