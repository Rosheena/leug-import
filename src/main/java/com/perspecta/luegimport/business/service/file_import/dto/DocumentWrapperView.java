package com.perspecta.luegimport.business.service.file_import.dto;

import com.perspecta.luegimport.business.common.constants.DocumentErrorType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DocumentWrapperView {

	private Long id;

	private String documentName;
	private String userName;

	private DocumentView document;

	private Boolean validated;
	private Boolean locked;
	private Boolean processed;

	private DocumentErrorType documentErrorType;
}
