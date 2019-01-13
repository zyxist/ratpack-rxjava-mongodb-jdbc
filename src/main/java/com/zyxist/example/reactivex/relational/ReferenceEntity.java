package com.zyxist.example.reactivex.relational;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class ReferenceEntity {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reference_generator")
	@SequenceGenerator(name = "reference_generator", sequenceName = "reference_seq", allocationSize=50)
	private Integer id;

	@Column
	private String reference;

	@ManyToOne
	private ArticleEntity article;
}
