package com.perspecta.luegimport.business.service.file_import.delegate;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document.DocumentRepository;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentErrorTypes;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
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

	@Autowired
	public FileImportDelegate(DocumentRepository documentRepository, DocumentWrapperRepository documentWrapperRepository) {
		this.documentRepository = documentRepository;
		this.documentWrapperRepository = documentWrapperRepository;
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

	public void validateFields(List<DocumentWrapper> documentWrapperList, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations) {
		// validate the list for empty fields
		documentWrapperList
				.forEach(documentWrapper -> {
					if (StringUtils.isBlank(documentWrapper.getDocument().getObjectName())
							|| StringUtils.isBlank(documentWrapper.getDocument().getFileLocation())
							|| StringUtils.isBlank(documentWrapper.getDocument().getCpId())) {
						documentWrapper.setValidated(false);
						if (MapUtils.isNotEmpty(failedValidations) && CollectionUtils.isNotEmpty(failedValidations.get(DocumentErrorTypes.MISSING_FIELD))) {
							failedValidations.get(DocumentErrorTypes.MISSING_FIELD).add(documentWrapper);
						} else {
							failedValidations.put(DocumentErrorTypes.MISSING_FIELD, new ArrayList<>(Arrays.asList(documentWrapper)));
						}
					} else {
						documentWrapper.setValidated(true);
						successValidations.add(documentWrapper);
					}
				});
	}

	public void checkFilePath(List<DocumentWrapper> documentWrapperList, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations){
		documentWrapperList.stream()
				.filter(documentWrapper -> documentWrapper.getValidated())
				.forEach(documentWrapper -> {
					if(!new File(DOCUMENT_LOCATION+documentWrapper.getDocument().getFileLocation()).isFile()){
						if (MapUtils.isNotEmpty(failedValidations) && CollectionUtils.isNotEmpty(failedValidations.get(DocumentErrorTypes.INVALID_PATH))) {
							failedValidations.get(DocumentErrorTypes.INVALID_PATH).add(documentWrapper);
							successValidations.remove(documentWrapper);
						} else {
							failedValidations.put(DocumentErrorTypes.INVALID_PATH, new ArrayList<>(Arrays.asList(documentWrapper)));

						}
						successValidations.remove(documentWrapper);
					}
				});
	}

	public void existingDocumentCheck(List<DocumentWrapper> documentWrapperList, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations) {

		// validate with the local database if the cpId exists
		List<DocumentWrapper> userExistingDocuments = documentWrapperRepository.findAll();

		if (CollectionUtils.isNotEmpty(userExistingDocuments)) {
			documentWrapperList.stream()
					.filter(documentWrapper -> documentWrapper.getValidated())
					.forEach(documentWrapper -> {
						boolean documentExists = userExistingDocuments.stream()
								.map(DocumentWrapper::getDocument)
								.anyMatch(document -> document.getCpId().equalsIgnoreCase(documentWrapper.getDocument().getCpId()));
						if (documentExists) {
							if (MapUtils.isNotEmpty(failedValidations) && CollectionUtils.isNotEmpty(failedValidations.get(DocumentErrorTypes.DUPLICATE))) {
								failedValidations.get(DocumentErrorTypes.DUPLICATE).add(documentWrapper);
							} else {
								failedValidations.put(DocumentErrorTypes.DUPLICATE, new ArrayList<>(Arrays.asList(documentWrapper)));
							}
							successValidations.remove(documentWrapper);
						}
					});
		}
	}

	public void persistValidations(DocumentView documentView, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations){

		if(MapUtils.isNotEmpty(failedValidations)){
			failedValidations.entrySet().forEach(failedValidation -> {
				if(!failedValidation.getKey().equals(DocumentErrorTypes.DUPLICATE_INPROGRESS)
						&& !failedValidation.getKey().equals(DocumentErrorTypes.DUPLICATE_PROCESSED)){
					failedValidation.getValue().forEach(documentWrapper -> {
						Optional.ofNullable(documentWrapper.getDocument())
								.ifPresent(document -> {
									Document existingDocument = documentRepository.findByCpId(document.getCpId());
									if(existingDocument!=null){
										document.setId(existingDocument.getId());
										documentRepository.save(document);
										DocumentWrapper existingDocumentWrapper = documentWrapperRepository.findByDocument_Id(existingDocument.getId());
										documentWrapper.setId(existingDocumentWrapper.getId());
									}
								});
					});
					documentWrapperRepository.saveAll(failedValidation.getValue());
				}
			});
			documentView.setFailedValidations(failedValidations);
		}

		if(CollectionUtils.isNotEmpty(successValidations)){
			successValidations.forEach(documentWrapper -> {
				Optional.ofNullable(documentWrapper.getDocument())
						.ifPresent(document -> {
							Document existingDocument = documentRepository.findByCpId(document.getCpId());
							if(existingDocument!=null){
								document.setId(existingDocument.getId());
								documentRepository.save(document);
								DocumentWrapper existingDocumentWrapper = documentWrapperRepository.findByDocument_Id(existingDocument.getId());
								documentWrapper.setId(existingDocumentWrapper.getId());
							}
						});
			});
			documentWrapperRepository.saveAll(successValidations);
			documentView.setSuccessValidations(successValidations);
		}
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
