package com.perspecta.luegimport.business.domain.document_wrapper;

import com.perspecta.luegimport.business.domain.document.Document;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class DocumentWrapper {

	@Id
	@GeneratedValue
	@Column(name = "documentWrapperId")
	private Long id;

	@OneToOne
	@JoinColumn(name = "documentId")
	private Document document;

	private Boolean isValidated;
	private Boolean isProcessed;
	private Boolean isFailed;

	private String userName;
}
