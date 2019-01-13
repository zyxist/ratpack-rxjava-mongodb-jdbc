package com.zyxist.example.reactivex.rest.handlers;

import com.zyxist.example.reactivex.Storage;
import ratpack.handling.Context;
import ratpack.jackson.Jackson;

import java.util.Objects;

public class ArticleListHandler {
	private static final int DEFAULT_LIMIT = 1000;
	private final Storage storage;

	public ArticleListHandler(Storage storage) {
		this.storage = Objects.requireNonNull(storage);
	}

	public void handleRequest(Context context) {
		int limit = DEFAULT_LIMIT;
		if (context.getPathTokens().containsKey("limit")) {
			limit = context.getPathTokens().asInt("limit");
		}
		context.render(Jackson.chunkedJsonList(context, storage.fetchAll(limit)));
	}
}
