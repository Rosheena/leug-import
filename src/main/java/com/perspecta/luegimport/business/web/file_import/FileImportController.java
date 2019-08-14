package com.perspecta.luegimport.business.web.file_import;

import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.FileImportService;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/lueg/document")
public class FileImportController {

	private final FileImportService fileImportService;

	@PostMapping("/validate")
	public DocumentView validateFile(@RequestParam MultipartFile file){
		String userName = "rk@wheelsup.com";
		return fileImportService.validate(userName, file);
	}

	@PostMapping("/process")
	public void handleFileUpload(@RequestParam User user, @RequestParam MultipartFile file){
		// TODO: generate a csv file for the failed validations
		// TODO: save the successful validations in the database and process them
		fileImportService.upload(user, file);
	}
}
