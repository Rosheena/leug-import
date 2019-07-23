package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document.DocumentRepository;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.delegate.FileImportDelegate;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentErrorTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportService {

	private final FileImportDelegate fileImportDelegate;
	private final DocumentWrapperRepository documentWrapperRepository;
	private final DocumentRepository documentRepository;

	public List<Document> validate(String userName, MultipartFile file){
		List<DocumentWrapper> successvalidations = new ArrayList<>();
		Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations = new HashMap<>();
		// parse file
		List<DocumentWrapper> documentWrapperList = fileImportDelegate.parseFile(userName, file);

		// validate file fields
		fileImportDelegate.validateFields(documentWrapperList, successvalidations, failedValidations);

		// validate to check if document is a duplicate
		fileImportDelegate.existingDocumentCheck(userName, documentWrapperList, successvalidations, failedValidations);

		// TODO: validate with the remote database if the cpId isnt valid

		if(CollectionUtils.isNotEmpty(successvalidations)){
			successvalidations.forEach(documentWrapper -> {
				documentRepository.save(documentWrapper.getDocument());
				documentWrapperRepository.save(documentWrapper);
			});
		}

		return null;
	}

	public void upload(User user, MultipartFile file){

		// process file
		// update database

	}




}
