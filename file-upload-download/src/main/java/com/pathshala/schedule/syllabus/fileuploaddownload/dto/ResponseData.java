package com.pathshala.schedule.syllabus.fileuploaddownload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
	
	private String fileName;
	private String downloadUrl;
	private String fileType;
	//private long fileSize;
	private String message;
	
	public ResponseData(String fileName, String downloadUrl) {
		this.fileName = fileName;
		this.downloadUrl = downloadUrl;
	}

	public ResponseData(String message) {
		this.message = message;
	}
}
