package com.perspecta.luegimport.business.service.file_import;

import com.perspecta.luegimport.business.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileImportService {

	public void upload(User user, MultipartFile file){

		// parse file
		// validate file

		// process file

		// update database

	}



}
