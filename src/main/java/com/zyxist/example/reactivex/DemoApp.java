package com.zyxist.example.reactivex;

import com.zyxist.example.reactivex.mongodb.MongoStorage;
import com.zyxist.example.reactivex.relational.RelationalStorage;
import com.zyxist.example.reactivex.rest.handlers.ArticleListHandler;
import com.zyxist.example.reactivex.rest.handlers.ArticleWriteHandler;
import com.zyxist.example.reactivex.rest.handlers.RatpackErrorHandler;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ratpack.error.ServerErrorHandler;
import ratpack.handling.Handler;
import ratpack.registry.Registry;
import ratpack.server.RatpackServer;

import java.util.concurrent.Executors;

public class DemoApp {
	private static final int BLOCKING_IO_THREADS = 2;

	private static final String DB_NAME = "ratpack";
	private final Scheduler reactiveScheduler = Schedulers.from(Executors.newFixedThreadPool(BLOCKING_IO_THREADS));
	private final MongoStorage mongoStorage;
	private final RelationalStorage relationalStorage;

	public DemoApp() {
		this.mongoStorage = new MongoStorage(DB_NAME);
		this.relationalStorage = new RelationalStorage(reactiveScheduler);
	}

	public void runApplication() {
		try {
			RatpackServer.start(s -> s
				.registry(Registry.single(ServerErrorHandler.class, new RatpackErrorHandler()))
				.handlers(h -> h
					.path("mongo/articles", createArticleHandler(mongoStorage))
					.path("relational/articles", createArticleHandler(relationalStorage))
				)
			);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private Handler createArticleHandler(Storage storage) {
		ArticleListHandler articleListHandler = new ArticleListHandler(storage);
		ArticleWriteHandler articleWriteHandler = new ArticleWriteHandler(storage);
		return ctx -> ctx.byMethod(m -> m
			.get(articleListHandler::handleRequest)
			.post(articleWriteHandler::handleRequest)
		);
	}

	public static void main(String args[]) {
		new DemoApp().runApplication();
	}
}
