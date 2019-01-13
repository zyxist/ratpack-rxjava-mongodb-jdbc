package com.zyxist.example.reactivex.relational;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Objects;

/**
 * This implementation is intentionally WRONG! It breaks several contracts for the Publisher
 * required by the Reactive Streams specification. It is an illustration, what NOT TO DO, despite
 * the fact that it **seems** to work correctly (indeed, if you use it in our application, it will
 * work for us).
 *
 * <p>Implementing the contract correctly is HARD, and it would expand this code at least 3 to 4
 * times. This is why you should make an iterable instead, just like I did: {@link SelectQueryIterable}.</p>
 */
public class SelectQueryPublisher implements Publisher<ArticleEntity> {
	private final Session session;
	private final String hqlQuery;
	private final int limit;

	public SelectQueryPublisher(Session session, String hqlQuery, int limit) {
		this.session = Objects.requireNonNull(session);
		this.hqlQuery = hqlQuery;
		this.limit = limit;
	}

	@Override
	public void subscribe(Subscriber<? super ArticleEntity> subscriber) {
		try {
			Query<ArticleEntity> query = session.createQuery(hqlQuery, ArticleEntity.class);
			query.setFetchSize(limit);
			ScrollableResults scroll = query.scroll();
			while (scroll.next()) {
				subscriber.onNext((ArticleEntity) scroll.get(0));
			}
			subscriber.onComplete();
		} catch (Exception exception) {
			subscriber.onError(exception);
		} finally {
			session.close();
		}
	}
}
