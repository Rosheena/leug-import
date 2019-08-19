package com.perspecta.luegimport.business.service.file_import.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DocumentView {

	private Long id;

	private String objectType;
	private String objectName;
	private String fileLocation;
	private String cpId;
	private String subType;
	private String year;
}
