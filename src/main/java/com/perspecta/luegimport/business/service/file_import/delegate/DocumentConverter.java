package com.perspecta.luegimport.business.service.file_import.delegate;

import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentView;
import com.perspecta.luegimport.business.service.file_import.dto.DocumentWrapperView;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentConverter {

	private final ModelMapper modelMapper;

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

		return documentWrapperViewList;
	}

	public DocumentWrapper convertToDocumentWrapper(DocumentWrapperView documentWrapperView){
		DocumentWrapper documentWrapper = new DocumentWrapper();
		Document document = new Document();

		modelMapper.map(documentWrapperView.getDocument(), document);
		modelMapper.map(documentWrapperView, documentWrapper);

		documentWrapper.setDocument(document);

		return documentWrapper;
	}

	public DocumentWrapperView convertToView(DocumentWrapper documentWrapper){
		DocumentWrapperView documentWrapperView = new DocumentWrapperView();
		DocumentView documentView = new DocumentView();

		modelMapper.map(documentWrapper.getDocument(), documentView);
		modelMapper.map(documentWrapper, documentWrapperView);

		documentWrapperView.setDocument(documentView);

		return documentWrapperView;
	}
}
