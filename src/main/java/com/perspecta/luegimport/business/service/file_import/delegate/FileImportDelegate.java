package com.perspecta.luegimport.business.service.file_import.delegate;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapperRepository;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentErrorTypes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportDelegate {

	private final DocumentWrapperRepository documentWrapperRepository;

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
						documentWrapper.setIsValidated(false);
						if (MapUtils.isNotEmpty(failedValidations) && CollectionUtils.isNotEmpty(failedValidations.get(DocumentErrorTypes.MISSING_FIELD))) {
							failedValidations.get(DocumentErrorTypes.MISSING_FIELD).add(documentWrapper);
						} else {
							failedValidations.put(DocumentErrorTypes.MISSING_FIELD, new ArrayList<>(Arrays.asList(documentWrapper)));
						}
					} else {
						documentWrapper.setIsValidated(true);
						successValidations.add(documentWrapper);
					}
				});
	}

	public void checkFilePath(List<DocumentWrapper> documentWrapperList, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations){
		documentWrapperList.stream()
				.filter(documentWrapper -> documentWrapper.getIsValidated())
				.forEach(documentWrapper -> {
					if(!new File("/Users/al/.bash_history").isFile()){
						if (MapUtils.isNotEmpty(failedValidations) && CollectionUtils.isNotEmpty(failedValidations.get(DocumentErrorTypes.INVALID_PATH))) {
							failedValidations.get(DocumentErrorTypes.INVALID_PATH).add(documentWrapper);
						} else {
							failedValidations.put(DocumentErrorTypes.INVALID_PATH, new ArrayList<>(Arrays.asList(documentWrapper)));
						}
					} else {
						successValidations.add(documentWrapper);
					}
				});
	}

	public void existingDocumentCheck(String userName, List<DocumentWrapper> documentWrapperList, List<DocumentWrapper> successValidations, Map<DocumentErrorTypes, List<DocumentWrapper>> failedValidations) {

		// validate with the local database if the cpId exists
		List<DocumentWrapper> userExistingDocuments = documentWrapperRepository.findByUserNameEquals(userName);

		if (CollectionUtils.isNotEmpty(userExistingDocuments)) {
			documentWrapperList.stream()
					.filter(documentWrapper -> documentWrapper.getIsValidated())
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
						} else {
							successValidations.add(documentWrapper);
						}
					});
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
		documentWrapper.setUserName(userName);

		return documentWrapper;
	}

}
