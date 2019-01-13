package com.zyxist.example.reactivex.relational;

import com.zyxist.example.reactivex.rest.Result;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class BatchInsert {
	private final SessionFactory sessionFactory;

	public BatchInsert(SessionFactory sessionFactory) {
		this.sessionFactory = Objects.requireNonNull(sessionFactory);
	}

	public List<Result> batchInsert(List<ArticleEntity> articles) {
		try (Session session = sessionFactory.openSession()) {
			Transaction transaction = session.beginTransaction();
			try {
				articles.forEach(session::merge);
				session.flush();
				transaction.commit();
				return createBatchResult(articles.size(), Result::success);
			} catch (RuntimeException exception) {
				transaction.rollback();
				return createBatchResult(articles.size(), () -> Result.failure(exception.getMessage()));
			}
		}
	}

	private List<Result> createBatchResult(int size, Supplier<Result> resultSupplier) {
		List<Result> results = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			results.add(resultSupplier.get());
		}
		return results;
	}
}
