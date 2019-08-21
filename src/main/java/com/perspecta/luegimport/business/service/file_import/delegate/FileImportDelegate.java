package com.perspecta.luegimport.business.service.file_import.delegate;

import com.perspecta.luegimport.business.common.constants.DocumentErrorType;
import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document.DocumentRepository;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentView;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentWrapperView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileImportDelegate {

	private final static String DOCUMENT_LOCATION = "C:\\Records\\DEH";

	private final DocumentRepository documentRepository;
	private final DocumentWrapperRepository documentWrapperRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public FileImportDelegate(DocumentRepository documentRepository, DocumentWrapperRepository documentWrapperRepository, ModelMapper modelMapper) {
		this.documentRepository = documentRepository;
		this.documentWrapperRepository = documentWrapperRepository;
		this.modelMapper = modelMapper;
	}

	public List<DocumentWrapper> parseFile(String userName, MultipartFile file) {

		List<DocumentWrapper> documentWrapperList = new ArrayList<>();

		try {
			InputStream inputFileStream = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFileStream));
			documentWrapperList = br.lines().skip(1).map(line -> mapToDocumentWrapper(line, userName)).collect(Collectors.toList());
			br.close();
		} catch (Exception ex) {
			log.error("Error processing the file: " + ex);
		}

		return documentWrapperList;
	}

	public void validateFields(List<DocumentWrapper> documentWrapperList) {
		// validate the list for empty fields
		documentWrapperList
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
				.filter(documentWrapper -> documentWrapper.getValidated())
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
			documentWrapperList.stream()
					.filter(documentWrapper -> documentWrapper.getValidated())
					.forEach(documentWrapper -> {
						boolean documentExists = userExistingDocuments.stream()
								.map(DocumentWrapper::getDocument)
								.anyMatch(document -> (document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
											&& BooleanUtils.isTrue(documentWrapper.getLocked())));
						if (documentExists) {
							documentWrapper.setValidated(false);
							documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_INPROGRESS);
						}
					});

			documentWrapperList.stream()
					.filter(documentWrapper -> documentWrapper.getValidated())
					.forEach(documentWrapper -> {
						boolean documentExists = userExistingDocuments.stream()
								.map(DocumentWrapper::getDocument)
								.anyMatch(document -> (document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId())
										&& BooleanUtils.isTrue(documentWrapper.getProcessed())));
						if (documentExists) {
							documentWrapper.setValidated(false);
							documentWrapper.setDocumentErrorType(DocumentErrorType.DUPLICATE_PROCESSED);
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
									DocumentWrapper existingDocumentWrapper = documentWrapperRepository.findByDocument_Id(existingDocument.getId());
									documentWrapper.setId(existingDocumentWrapper.getId());
								} else {
									documentRepository.save(document);
								}
							});
				}
			});
			documentWrapperRepository.saveAll(documentWrapperList);
		}
	}

	public List<DocumentWrapperView> convertToView(List<DocumentWrapper> documentWrapperList){
		List<DocumentWrapperView> documentWrapperViewList = new ArrayList<>();

		Optional.ofNullable(documentWrapperList)
				.orElse(Collections.emptyList())
				.forEach(documentWrapper -> {
					DocumentView documentView = new DocumentView();
					modelMapper.map(documentWrapper.getDocument(), documentView);
					DocumentWrapperView documentWrapperView = new DocumentWrapperView();
					modelMapper.map(documentWrapper, documentWrapperView);
					documentWrapperView.setDocument(documentView);
					documentWrapperViewList.add(documentWrapperView);
				});

		documentWrapperViewList.get(0).setDocumentErrorType(DocumentErrorType.DUPLICATE_INPROGRESS);
		documentWrapperViewList.get(0).setDocumentErrorType(DocumentErrorType.INVALID_CPID);

		return documentWrapperViewList;
	}

	private DocumentWrapper mapToDocumentWrapper(String line, String userName) {
		String[] tokens = line.split(",");// a CSV has comma separated lines
		DocumentWrapper documentWrapper = new DocumentWrapper();
		Document document = new Document();

		if (StringUtils.isNotBlank(tokens[0])) {
			document.setObjectType(StringUtils.trimToEmpty(tokens[0]));
		}

		if (StringUtils.isNotBlank(tokens[1])) {
			document.setObjectName(StringUtils.trimToEmpty(tokens[1]));
		}

		if (StringUtils.isNotBlank(tokens[2])) {
			document.setFileLocation(StringUtils.trimToEmpty(tokens[2]));
		}

		if (StringUtils.isNotBlank(tokens[3])) {
			document.setCpId(StringUtils.trimToEmpty(tokens[3]));
		}

		if (StringUtils.isNotBlank(tokens[4])) {
			document.setSubType(StringUtils.trimToEmpty(tokens[4]));
		}

		if (StringUtils.isNotBlank(tokens[5])) {
			document.setYear(StringUtils.trimToEmpty(tokens[5]));
		}

		documentWrapper.setDocument(document);

		return documentWrapper;
	}

}
