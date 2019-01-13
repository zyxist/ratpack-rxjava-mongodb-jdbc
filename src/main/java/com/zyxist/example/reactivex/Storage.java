package com.zyxist.example.reactivex;

import com.zyxist.example.reactivex.rest.Article;
import com.zyxist.example.reactivex.rest.Result;
import io.reactivex.Flowable;

public interface Storage {
	Flowable<Article> fetchAll(int limit);
	Flowable<Result> insertAll(Flowable<Article> articles);
}
