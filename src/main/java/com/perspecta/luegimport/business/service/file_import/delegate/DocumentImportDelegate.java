package com.perspecta.luegimport.business.service.file_import.delegate;

import com.perspecta.luegimport.business.common.constants.DocumentErrorType;
import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document.DocumentRepository;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.*;

@Slf4j
@Component
public class DocumentImportDelegate {

	private final static String DOCUMENT_LOCATION = "C:\\Records\\DEH";

	private final DocumentRepository documentRepository;
	private final DocumentWrapperRepository documentWrapperRepository;

	@Autowired
	public DocumentImportDelegate(DocumentRepository documentRepository, DocumentWrapperRepository documentWrapperRepository) {
		this.documentRepository = documentRepository;
		this.documentWrapperRepository = documentWrapperRepository;
	}

	public void validateFields(List<DocumentWrapper> documentWrapperList) {
		// validate the list for empty fields
		documentWrapperList.stream()
				.filter(documentWrapper -> BooleanUtils.isTrue(documentWrapper.getValidated()))
				.forEach(documentWrapper -> {
					if (StringUtils.isBlank(documentWrapper.getDocument().getObjectName())
							|| StringUtils.isBlank(documentWrapper.getDocument().getCpId())) {
						documentWrapper.setValidated(false);
						documentWrapper.setDocumentErrorType(DocumentErrorType.MISSING_FIELD);
					} else {
						documentWrapper.setValidated(true);
					}
				});
	}

	public void checkFilePath(List<DocumentWrapper> documentWrapperList){
		documentWrapperList.stream()
				.filter(documentWrapper ->  BooleanUtils.isTrue(documentWrapper.getValidated()))
				.forEach(documentWrapper -> {
					if(!new File(DOCUMENT_LOCATION+documentWrapper.getDocument().getFileLocation()).isFile()){
						documentWrapper.setValidated(false);
						documentWrapper.setDocumentErrorType(DocumentErrorType.INVALID_PATH);
					}
				});
	}

	public void existingDocumentCheck(List<DocumentWrapper> documentWrapperList) {

		// validate with the local database if the cpId exists
		List<DocumentWrapper> userExistingDocuments = documentWrapperRepository.findAll();

		if (CollectionUtils.isNotEmpty(userExistingDocuments)) {
			documentWrapperList
					.forEach(documentWrapper -> {
						boolean documentExists = userExistingDocuments.stream()
								.map(DocumentWrapper::getDocument)
								.anyMatch(document -> (StringUtils.isNotBlank(document.getCpId()) && document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
											&& BooleanUtils.isTrue(documentWrapper.getLocked())));
						if (documentExists) {
							documentWrapper.setValidated(false);
							documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_INPROGRESS);
						} else {
							documentWrapper.setValidated(true);
						}
					});

			documentWrapperList
					.forEach(documentWrapper -> {
						boolean documentExists = userExistingDocuments.stream()
								.map(DocumentWrapper::getDocument)
								.anyMatch(document -> (StringUtils.isNotBlank(document.getCpId()) && document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
										&& BooleanUtils.isTrue(documentWrapper.getProcessed())));
						if (documentExists) {
							documentWrapper.setValidated(false);
							documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_PROCESSED);
						} else {
							documentWrapper.setValidated(true);
						}
					});
		}
	}

	public void persistValidations(List<DocumentWrapper> documentWrapperList){

		if(CollectionUtils.isNotEmpty(documentWrapperList)){
			documentWrapperList.forEach(documentWrapper -> {
				if(Objects.isNull(documentWrapper.getDocumentErrorType()) || !
						(documentWrapper.getDocumentErrorType().equals(DocumentErrorType.DUPLICATE_INPROGRESS)
								|| documentWrapper.getDocumentErrorType().equals(DocumentErrorType.DUPLICATE_PROCESSED))) {
					Optional.ofNullable(documentWrapper.getDocument())
							.ifPresent(document -> {
								Document existingDocument = documentRepository.findByCpId(document.getCpId());
								if (existingDocument != null) {
									document.setId(existingDocument.getId());
									documentRepository.save(document);
									Optional.ofNullable(documentWrapperRepository.findByDocument_Id(existingDocument.getId())).ifPresent(existingDocumentWrapper -> documentWrapper.setId(existingDocumentWrapper.getId()));
								} else {
									documentRepository.save(document);
								}
							});
				}
			});
			documentWrapperRepository.saveAll(documentWrapperList);
		}
	}

	public void lockDocuments(List<DocumentWrapper> documentWrapperList){

		if(CollectionUtils.isNotEmpty(documentWrapperList)){
			documentWrapperList.forEach(documentWrapper -> {
				documentWrapper.setLocked(true);
			});

			documentWrapperRepository.saveAll(documentWrapperList);
		}
	}

	public void markProcessedDocuments(List<DocumentWrapper> documentWrapperList){

		if(CollectionUtils.isNotEmpty(documentWrapperList)){
			documentWrapperList.forEach(documentWrapper -> {
				// TODO: check if document was processed
				documentWrapper.setProcessed(true);
				documentWrapper.setLocked(false);
			});
			documentWrapperRepository.saveAll(documentWrapperList);
		}
	}

	public void validateDocument(DocumentWrapper documentWrapper){

		if (StringUtils.isBlank(documentWrapper.getDocument().getObjectName())
				|| StringUtils.isBlank(documentWrapper.getDocument().getCpId())){
			documentWrapper.setValidated(false);
			documentWrapper.setDocumentErrorType(DocumentErrorType.MISSING_FIELD);
			return;
		}

		List<DocumentWrapper> userExistingDocuments = documentWrapperRepository.findAll();

		boolean documentExists = false;
		if(CollectionUtils.isNotEmpty(userExistingDocuments)){
			documentExists = userExistingDocuments.stream()
					.map(DocumentWrapper::getDocument)
					.anyMatch(document -> (StringUtils.isNotBlank(document.getCpId()) && document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
							&& BooleanUtils.isTrue(documentWrapper.getLocked())));

			if(documentExists){
				documentWrapper.setValidated(false);
				documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_INPROGRESS);
				return;
			}

			documentExists = userExistingDocuments.stream()
					.map(DocumentWrapper::getDocument)
					.anyMatch(document -> (StringUtils.isNotBlank(document.getCpId()) && document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
							&& BooleanUtils.isTrue(documentWrapper.getProcessed())));

			if(documentExists){
				documentWrapper.setValidated(false);
				documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_PROCESSED);
				return;
			}
		}

		if(!new File(DOCUMENT_LOCATION+documentWrapper.getDocument().getFileLocation()).isFile()){
			documentWrapper.setValidated(false);
			documentWrapper.setDocumentErrorType(DocumentErrorType.INVALID_PATH);
		}
	}

	public void persistDocument(DocumentWrapper documentWrapper){
		documentRepository.save(documentWrapper.getDocument());
		documentWrapperRepository.save(documentWrapper);
	}
}
