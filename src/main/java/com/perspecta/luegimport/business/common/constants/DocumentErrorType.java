package com.perspecta.luegimport.business.common.constants;

public enum DocumentErrorType {
	DUPLICATE_INPROGRESS("Duplicate_in-progress", "The specified Cp_Id is being processed currently and cannot be imported twice"),
	DUPLICATE_PROCESSED("Duplicate_processed", "The specified Cp_Id is already processed"),
	INVALID_CPID("Invalid_CpId", "The specified Cp_Id is invalid"),
	MISSING_FIELD("Missing_field", "Please check for the missing fields"),
	INVALID_PATH("Invalid_File_Location", "The file location is not valid");

	private String name;
	private String description;

	DocumentErrorType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
