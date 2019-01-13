package com.zyxist.example.reactivex.rest.handlers;

import com.zyxist.example.reactivex.Storage;
import com.zyxist.example.reactivex.rest.Article;
import com.zyxist.example.reactivex.rest.Result;
import io.reactivex.Flowable;
import ratpack.handling.Context;

import java.util.List;
import java.util.Objects;

import static ratpack.jackson.Jackson.chunkedJsonList;
import static ratpack.jackson.Jackson.fromJson;
import static ratpack.util.Types.listOf;

public class ArticleWriteHandler {
	private final Storage storage;

	public ArticleWriteHandler(Storage storage) {
		this.storage = Objects.requireNonNull(storage);
	}

	public void handleRequest(Context context) {
		context
			.parse(fromJson(listOf(Article.class))).then(articles ->
				context.render(chunkedJsonList(context, executeDatabaseFlow(articles)))
		);
	}

	private Flowable<Result> executeDatabaseFlow(List<Article> articles) {
		return storage.insertAll(Flowable.fromIterable(articles));
	}
}
