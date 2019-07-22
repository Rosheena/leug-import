package com.perspecta.luegimport.business.domain.document;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Document {

	@Id
	@GeneratedValue
	@Column(name = "documentId")
	private Long id;

	private String objectType;
	private String objectName;
	private String fileLocation;
	private String cpId;
	private String subType;
	private String year;


}

