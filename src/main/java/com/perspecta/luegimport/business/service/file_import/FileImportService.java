package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.delegate.FileImportDelegate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportService {

	private final FileImportDelegate fileImportDelegate;

	public List<Document> validate(String userName, MultipartFile file){
		// parse file
		List<DocumentWrapper> documentWrapperList = fileImportDelegate.parseFile(userName, file);

		// validate file

		return null;
	}

	public void upload(User user, MultipartFile file){

		// process file
		// update database

	}




}
