package com.perspecta.luegimport.business.domain.document_wrapper;

import com.perspecta.luegimport.business.common.constants.DocumentErrorType;
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

	private String documentName;
	private String userName;

	@Enumerated(EnumType.STRING)
	private DocumentErrorType documentErrorType;

	private Boolean validated;
	private Boolean locked;
	private Boolean processed;
}
