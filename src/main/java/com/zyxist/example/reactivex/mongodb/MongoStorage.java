package com.zyxist.example.reactivex.mongodb;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.zyxist.example.reactivex.rest.Article;
import com.zyxist.example.reactivex.rest.Result;
import com.zyxist.example.reactivex.Storage;
import io.reactivex.Flowable;
import org.bson.Document;

import java.util.List;

public class MongoStorage implements Storage {
	private static final String ARTICLE_COLLECTION = "articles";

	private static final String ID_PROP = "id";
	private static final String TITLE_PROP = "title";
	private static final String CONTENT_PROP = "content";
	private static final String REFERENCES_PROP = "references";

	private final MongoClient mongoClient;
	private final MongoDatabase mongoDatabase;

	public MongoStorage(String name) {
		this.mongoClient = MongoClients.create();
		this.mongoDatabase = mongoClient.getDatabase(name);
	}

	@Override
	public Flowable<Article> fetchAll(int limit) {
		MongoCollection<Document> articleCol = mongoDatabase.getCollection(ARTICLE_COLLECTION);
		return Flowable
			.fromPublisher(articleCol.find().limit(limit))
			.map(this::marshallArticle);
	}

	@Override
	public Flowable<Result> insertAll(Flowable<Article> articles) {
		MongoCollection<Document> articleCol = mongoDatabase.getCollection(ARTICLE_COLLECTION);
		return articles
			.map(this::unmarshallArticle)
			.flatMap(articleCol::insertOne)
			.map(success -> Result.success())
			.onErrorReturn(ex -> Result.failure(ex.getMessage()));
	}

	private Document unmarshallArticle(Article article) {
		Document doc = new Document();
		doc.append(ID_PROP, article.getId());
		doc.append(TITLE_PROP, article.getTitle());
		doc.append(CONTENT_PROP, article.getContent());
		doc.append(REFERENCES_PROP, article.getReferences());

		return doc;
	}

	private Article marshallArticle(Document document) {
		Article article = new Article();
		article.setId(document.getInteger(ID_PROP));
		article.setTitle(document.get(TITLE_PROP).toString());
		article.setContent(document.get(CONTENT_PROP).toString());
		document.get(REFERENCES_PROP, List.class).forEach(it -> article.addReference(it.toString()));
		return article;
	}
}
