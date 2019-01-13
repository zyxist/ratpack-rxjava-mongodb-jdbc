package com.zyxist.example.reactivex.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Article {
	@JsonProperty
	private int id;
	@JsonProperty
	private String title;
	@JsonProperty
	private String content;
	@JsonProperty
	private List<String> references = new ArrayList<>();

	public Article addReference(String uri) {
		this.references.add(uri);
		return this;
	}
}
