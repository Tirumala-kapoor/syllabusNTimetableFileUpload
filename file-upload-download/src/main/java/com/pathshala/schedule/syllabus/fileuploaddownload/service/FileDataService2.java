package com.pathshala.schedule.syllabus.fileuploaddownload.service;


import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.pathshala.schedule.syllabus.fileuploaddownload.dto.ResponseData;

public interface FileDataService2 {
	
	ResponseData uploadFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId) throws Exception;

//	FileData downloadFile(String fileName) throws Exception;
	
//	String replaceUploadedFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId, String fileName) throws Exception;
	
//	boolean deleteUploadedFile(MultipartFile file, String fileName) throws Exception;

//	void init();

	ResponseData load(String filename, Integer classId, Integer sectionId, Integer sessionId);
	
	boolean deleteByFileName(String filename, Integer classId, Integer sectionId, Integer sessionId);

	void deleteAll();

	Stream<Path> loadAll();

	ResponseData updateFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId, String filename) throws Exception;

	Resource load(String filename);
	
}
