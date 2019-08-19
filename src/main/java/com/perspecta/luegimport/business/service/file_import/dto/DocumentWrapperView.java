package com.perspecta.luegimport.business.service.file_import.dto;

import com.perspecta.luegimport.business.common.constants.DocumentErrorType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class DocumentWrapperView {

	private Long id;

	private DocumentView document;

	private Boolean validated;
	private Boolean locked;
	private Boolean processed;

	private DocumentErrorType documentErrorType;
}
