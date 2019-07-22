package com.perspecta.luegimport.business.service.file_import.dto;

import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentView {
	private List<DocumentWrapper> successValidations;
	private Map<String, List<DocumentWrapper>> failedValidations;
}
