package com.zyxist.example.reactivex.relational;

import com.zyxist.example.reactivex.rest.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ArticleEntity {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_generator")
	@SequenceGenerator(name = "article_generator", sequenceName = "article_seq", allocationSize=50)
	private Integer id;

	@Column
	private String title;

	@Column
	private String content;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ReferenceEntity> references = new ArrayList<>();

	public static ArticleEntity fromDTO(Article article) {
		ArticleEntity entity = new ArticleEntity();
		entity.setTitle(article.getTitle());
		entity.setContent(article.getContent());
		for (String ref: article.getReferences()) {
			ReferenceEntity referenceEntity = new ReferenceEntity();
			referenceEntity.setReference(ref);
			referenceEntity.setArticle(entity);
			entity.getReferences().add(referenceEntity);
		}
		return entity;
	}

	public Article toDTO() {
		Article article = new Article();
		article.setId(this.getId());
		article.setTitle(this.getTitle());
		article.setContent(this.getContent());
		this.getReferences().forEach(refEntity -> article.addReference(refEntity.getReference()));
		return article;
	}
}
