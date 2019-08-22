package com.perspecta.luegimport.business.web.file_import;

import com.perspecta.luegimport.business.domain.user.User;
import com.perspecta.luegimport.business.service.file_import.FileImportService;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentWrapperView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/lueg/document")
public class FileImportController {

	private final FileImportService fileImportService;

	@PostMapping("/validate")
	public List<DocumentWrapperView> validateFile(@RequestParam MultipartFile file) throws IOException {
		return fileImportService.process(file.getInputStream());
	}

	@PostMapping("/validate-document")
	public DocumentWrapperView validateFile(@RequestParam DocumentWrapperView documentWrapper) {
		return fileImportService.processDocument(documentWrapper);
	}

	@PostMapping("/process")
	public void handleFileUpload(@RequestParam List<DocumentWrapperView> documentWrapperViewList){
		// TODO: generate a csv file for the failed validations
		// TODO: save the successful validations in the database and process them
		fileImportService.importDocuments(documentWrapperViewList);
	}
}
