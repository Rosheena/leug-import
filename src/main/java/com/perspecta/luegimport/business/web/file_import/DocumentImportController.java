package com.perspecta.luegimport.business.web.file_import;

import com.perspecta.luegimport.business.service.file_import.DocumentImportService;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentWrapperView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/lueg/document")
public class DocumentImportController {

	private final DocumentImportService documentImportService;

	@PostMapping("/validate")
	public List<DocumentWrapperView> validateFile(@RequestParam MultipartFile file, @RequestParam String userName) throws IOException {
		return documentImportService.process(userName, file.getOriginalFilename(), file.getInputStream());
	}

	@PostMapping("/validate-document")
	public DocumentWrapperView validateFile(@RequestBody DocumentWrapperView documentWrapper) {
		return documentImportService.processDocument(documentWrapper);
	}

	@PostMapping("/process")
	public void handleFileUpload(@RequestBody List<DocumentWrapperView> documentWrapperViewList){
		// TODO: generate a csv file for the failed validations
		// TODO: save the successful validations in the database and process them
		documentImportService.importDocuments(documentWrapperViewList);
	}

	@GetMapping("/processed-results")
	public List<DocumentWrapperView> getProcessedDocuments(){
		return documentImportService.getProcessedDocuments();
	}
}
