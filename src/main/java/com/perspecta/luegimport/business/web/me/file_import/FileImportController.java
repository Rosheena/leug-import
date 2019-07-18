package com.perspecta.luegimport.business.web.me.file_import;

import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.FileImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/upload")
public class FileImportController {

	private final FileImportService fileImportService;

	@PostMapping
	public void handleFileUpload(@RequestParam User user, @RequestParam MultipartFile file){
		fileImportService.upload(user, file);
	}
}
