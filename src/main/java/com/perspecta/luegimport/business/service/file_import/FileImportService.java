package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportService {

	public List<Document> validate(String userName, MultipartFile file){
		// parse file

		List<DocumentWrapper> documentWrapperList = new ArrayList<>();

		try {
			InputStream inputFileStream = file.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFileStream));
			// skip the header of the csv
			documentWrapperList = br.lines().skip(1).map(this::mapToDocumentWrapper).collect(Collectors.toList());
			br.close();
		} catch (Exception ex){
			log.error("Error processing the file: "+ex);
		}

		// validate file
		//
		return null;
	}

	public void upload(User user, MultipartFile file){

		// process file
		// update database

	}

	private DocumentWrapper mapToDocumentWrapper(String line){
		String[] tokens = line.split(",");// a CSV has comma separated lines
		DocumentWrapper documentWrapper = new DocumentWrapper();
		Document document = new Document();
		documentWrapper.setIsValidated(true);

		if(StringUtils.isNotBlank(tokens[0])){
			document.setObjectType(StringUtils.trimToEmpty(tokens[0]));
		}

		if(StringUtils.isNotBlank(tokens[1])){
			document.setObjectName(StringUtils.trimToEmpty(tokens[1]));
		} else {
			documentWrapper.setIsValidated(false);
		}

		if(StringUtils.isNotBlank(tokens[2])){
			document.setFileLocation(StringUtils.trimToEmpty(tokens[2]));
		} else {
			documentWrapper.setIsValidated(false);
		}

		if(StringUtils.isNotBlank(tokens[3])){
			document.setCpId(StringUtils.trimToEmpty(tokens[3]));
		} else {
			documentWrapper.setIsValidated(false);
		}

		if(StringUtils.isNotBlank(tokens[4])){
			document.setSubType(StringUtils.trimToEmpty(tokens[4]));
		}

		if(StringUtils.isNotBlank(tokens[5])){
			document.setYear(StringUtils.trimToEmpty(tokens[5]));
		}

		documentWrapper.setDocument(document);

		return documentWrapper;
	}



}
