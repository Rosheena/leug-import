package com.perspecta.luegimport.business.service.file_import.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentCsvRow {

	@JsonProperty("lueg_object_type")
	private String objectType;

	@JsonProperty("Object_name")
	private String objectName;

	@JsonProperty("content_file")
	private String fileLocation;

	@JsonProperty("cp_id")
	private String cpId;

	@JsonProperty("lueg_subtype")
	private String subType;

	@JsonProperty("lueg_year")
	private String year;
}
