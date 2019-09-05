package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.service.file_import.delegate.DocumentConverter;
import com.perspecta.luegimport.business.service.file_import.delegate.DocumentImportDelegate;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentWrapperView;
import com.perspecta.luegimport.business.service.file_import.util.DocumentCsvExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentImportService {

	private final DocumentImportDelegate documentImportDelegate;
	private final DocumentWrapperRepository documentWrapperRepository;
	private final DocumentCsvExtractor documentCsvExtractor;
	private final DocumentConverter documentConverter;

	public List<DocumentWrapperView> process(String userName, String documentName, InputStream csvInputStream){
		// parse file
		List<DocumentWrapper> documentWrapperList = documentCsvExtractor.extract(userName, documentName, csvInputStream);

		if(CollectionUtils.isNotEmpty(documentWrapperList)){

			// validate to check if document is a duplicate
			documentImportDelegate.existingDocumentCheck(documentWrapperList);
			// validate file fields
			documentImportDelegate.validateFields(documentWrapperList);

			// check if file location is valid
//			fileImportDelegate.checkFilePath(documentWrapperList);

			// TODO: validate with the remote database if the cpId isnt valid
		}

		documentImportDelegate.persistValidations(documentWrapperList);

		return documentConverter.convertToView(documentWrapperList);
	}

	public DocumentWrapperView processDocument(DocumentWrapperView documentWrapperView){

		DocumentWrapper documentWrapper = documentConverter.convertToDocumentWrapper(documentWrapperView);
		documentImportDelegate.validateDocument(documentWrapper);
		documentImportDelegate.persistDocument(documentWrapper);
		return documentConverter.convertToView(documentWrapper);
	}

	public void importDocuments(List<DocumentWrapperView> documentWrapperViewList){

		List<DocumentWrapper> documentWrappers = documentConverter.convertToDocumentWrapper(documentWrapperViewList);

		// lock the document (persist in database)
		documentImportDelegate.lockDocuments(documentWrappers);
		// TODO : process documents

		// update the successful entries in database with processed true
		documentImportDelegate.markProcessedDocuments(documentWrappers);

	}

	public List<DocumentWrapperView> getProcessedDocuments(){
		List<DocumentWrapper> documentWrappers = documentWrapperRepository.findByProcessedTrueAndValidatedTrueAndLockedFalse();
		return documentConverter.convertToView(documentWrappers);
	}




}
