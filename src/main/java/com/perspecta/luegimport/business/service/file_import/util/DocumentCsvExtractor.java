package com.perspecta.luegimport.business.service.file_import.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.perspecta.luegimport.business.domain.document.Document;
import com.perspecta.luegimport.business.domain.document_wrapper.DocumentWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class DocumentCsvExtractor {

	private static final ObjectReader READER =
			new CsvMapper().readerFor(DocumentCsvRow.class)
					.with(
							CsvSchema.builder()
									.setSkipFirstDataRow(Boolean.TRUE)
									.addColumn("lueg_object_type")
									.addColumn("Object_name")
									.addColumn("content_file")
									.addColumn("cp_id")
									.addColumn("lueg_subtype")
									.addColumn("lueg_year")
									.build()
					);

	public List<DocumentWrapper> extract(InputStream csvInputStream){

		log.info("Extracting rows");

		long totalRows = 0L;
		long successfulRows = 0L;

		try{
			MappingIterator<DocumentCsvRow> iterator = READER.readValues(csvInputStream);

			while (iterator.hasNext()) {
				DocumentCsvRow csvRow = iterator.next();

			}
		} catch (Throwable throwable){
			log.warn(String.format("Row [%d] will be skipped - %s", totalRows, throwable.getMessage()));
		}

	}

	private DocumentWrapper readDocument(DocumentCsvRow row){
		DocumentWrapper documentWrapper = new DocumentWrapper();
		Document document = new Document();

		if (StringUtils.isNotBlank(row.getObjectType())) {
			document.setObjectType(StringUtils.trimToEmpty(row.getObjectType()));
		}

		if (StringUtils.isNotBlank(row.getObjectName())) {
			document.setObjectName(StringUtils.trimToEmpty(row.getObjectName()));
		}

		if (StringUtils.isNotBlank(row.getFileLocation())) {
			document.setFileLocation(StringUtils.trimToEmpty(row.getFileLocation()));
		}

		if (StringUtils.isNotBlank(row.getCpId())) {
			document.setCpId(StringUtils.trimToEmpty(row.getCpId()));
		}

		if (StringUtils.isNotBlank(row.getSubType())) {
			document.setSubType(StringUtils.trimToEmpty(row.getSubType()));
		}

		if (StringUtils.isNotBlank(row.getYear())) {
			document.setYear(StringUtils.trimToEmpty(row.getYear()));
		}

		documentWrapper.setDocument(document);
		documentWrapper.setBatchId(UUID.randomUUID());

		return documentWrapper;
	}
}
