package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.document.DocumentRepository;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.delegate.FileImportDelegate;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentErrorTypes;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportService {

	private final FileImportDelegate fileImportDelegate;
	private final DocumentWrapperRepository documentWrapperRepository;
	private final DocumentRepository documentRepository;

	public DocumentView process(String userName, MultipartFile file){
		DocumentView documentView = new DocumentView();
		List<DocumentWrapper> successValidations = new ArrayList<>();
		Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations = new HashMap<>();
		// parse file
		List<DocumentWrapper> documentWrapperList = fileImportDelegate.parseFile(userName, file);

		if(CollectionUtils.isNotEmpty(documentWrapperList)){
			// validate file fields
			fileImportDelegate.validateFields(documentWrapperList, successValidations, failedValidations);

			// check if file location is valid
			fileImportDelegate.checkFilePath(documentWrapperList, successValidations, failedValidations);

			// validate to check if document is a duplicate
			fileImportDelegate.existingDocumentCheck(userName, documentWrapperList, successValidations, failedValidations);

			// TODO: validate with the remote database if the cpId isnt valid
		}

		if(MapUtils.isNotEmpty(failedValidations)){
			documentView.setFailedValidations(failedValidations);
		}

		if(CollectionUtils.isNotEmpty(successValidations)){
			documentView.setSuccessValidations(successValidations);
		}

		/*if(CollectionUtils.isNotEmpty(successValidations)){
			successValidations.forEach(documentWrapper -> {
				documentRepository.save(documentWrapper.getDocument());
				documentWrapperRepository.save(documentWrapper);
			});
		}
*/
		return documentView;
	}

	public void upload(User user, MultipartFile file){

		// process file
		// update database

	}




}
