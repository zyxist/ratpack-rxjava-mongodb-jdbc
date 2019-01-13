package com.zyxist.example.reactivex.relational;

import com.zyxist.example.reactivex.Storage;
import com.zyxist.example.reactivex.rest.Article;
import com.zyxist.example.reactivex.rest.Result;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

public class RelationalStorage implements Storage {
	private static final int BATCH_INSERT_SIZE = 100;

	private final SessionFactory sessionFactory;
	private final Scheduler blockingIOScheduler;

	public RelationalStorage(Scheduler scheduler) {
		this.blockingIOScheduler = Objects.requireNonNull(scheduler);
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
			.configure()
			.build();
		try {
			this.sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (RuntimeException exception) {
			StandardServiceRegistryBuilder.destroy(registry);
			throw exception;
		}
	}

	@Override
	public Flowable<Article> fetchAll(int limit) {
		Session session = sessionFactory.openSession();
		return Flowable
			.fromIterable(new SelectQueryIterable(session, "FROM ArticleEntity", limit))
			.map(ArticleEntity::toDTO)
			.subscribeOn(blockingIOScheduler);
	}

	@Override
	public Flowable<Result> insertAll(Flowable<Article> articles) {
		BatchInsert inserter = new BatchInsert(sessionFactory);
		return articles
			.map(ArticleEntity::fromDTO)
			.buffer(BATCH_INSERT_SIZE)
			.observeOn(blockingIOScheduler)
			.map(inserter::batchInsert)
			.flatMapIterable(results -> results)
			.onErrorReturn(ex -> Result.failure(ex.getMessage()));
	}
}
