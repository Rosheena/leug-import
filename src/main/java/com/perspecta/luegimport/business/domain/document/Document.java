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

	private String fileName;
	private String filePath;
	private Boolean isValidated;
	private Boolean isProcessed;

}
