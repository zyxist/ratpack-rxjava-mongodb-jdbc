package com.zyxist.example.reactivex.relational;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.Objects;

public class SelectQueryIterable implements Iterable<ArticleEntity> {
	private final Session session;
	private final String hqlQuery;
	private final int limit;

	public SelectQueryIterable(Session session, String hqlQuery, int limit) {
		this.session = Objects.requireNonNull(session);
		this.hqlQuery = hqlQuery;
		this.limit = limit;
	}


	@Override
	public Iterator<ArticleEntity> iterator() {
		return new Iterator<ArticleEntity>() {
			private ScrollableResults scroll;
			private ArticleEntity toReturn;

			@Override
			public boolean hasNext() {
				return (null != (toReturn = tryGetNext(getScroll())));
			}

			@Override
			public ArticleEntity next() {
				return tryGetNext(getScroll());
			}

			private ScrollableResults getScroll() {
				if (null == scroll) {
					Query<ArticleEntity> query = session.createQuery(hqlQuery, ArticleEntity.class);
					query.setFetchSize(limit);
					scroll = query.scroll();
				}
				return scroll;
			}

			private ArticleEntity tryGetNext(ScrollableResults currentScroll) {
				if (null != toReturn) {
					try {
						return toReturn;
					} finally {
						toReturn = null;
					}
				}
				if (currentScroll.next()) {
					return (ArticleEntity) currentScroll.get(0);
				}
				currentScroll.close();
				session.close();
				return null;
			}
		};
	}
}
