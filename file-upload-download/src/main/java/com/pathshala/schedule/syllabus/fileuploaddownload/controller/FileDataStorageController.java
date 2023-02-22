package com.pathshala.schedule.syllabus.fileuploaddownload.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.pathshala.schedule.syllabus.fileuploaddownload.dto.ResponseData;
import com.pathshala.schedule.syllabus.fileuploaddownload.service.FileDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/filestorage")
@RequiredArgsConstructor
public class FileDataStorageController {
	
	private final FileDataService storageService;
	
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseData> uploadFile(@RequestParam("file") MultipartFile file, 
													@RequestParam("classId") Integer classId,
													@RequestParam("sectionId") Integer sectionId,
													@RequestParam("sessionId") Integer sessionId) 
	{
		String message = "";
		try {
			ResponseData responseData = storageService.uploadFile(file,classId,sectionId,sessionId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			responseData.setMessage(message);
			return ResponseEntity.status(HttpStatus.OK).body(responseData);
			
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseData(file.getOriginalFilename(), 
					"", 
					file.getContentType(), 
				//	file.getSize(),
					message));
		}
	}
	
	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<ResponseData> findFileByName(@PathVariable("filename") String filename,
											@RequestParam("classId") Integer classId,
											@RequestParam("sectionId") Integer sectionId,
											@RequestParam("sessionId") Integer sessionId) 
	{
		ResponseData response = storageService.load(filename,classId,sectionId,sessionId);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + response.getFileName() + "\"")
				.body(response);
	}
	
	@GetMapping("/download/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> download(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@GetMapping("/files")
	public ResponseEntity<List<ResponseData>> getListFiles() {
		List<ResponseData> fileInfos = storageService.loadAll()
							.map(path -> {
								String filename = path.getFileName().toString();
								String url = MvcUriComponentsBuilder
										.fromMethodName(
												FileDataStorageController.class,
												"download",
												path.getFileName().toString()
												).build().toString();
					
								return new ResponseData(filename, url);
							}
						).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	

	@DeleteMapping("/deleteFile/{filename:.+}")
	public ResponseEntity<ResponseData> deleteFile(@PathVariable String filename,
			@RequestParam("classId") Integer classId,
			@RequestParam("sectionId") Integer sectionId,
			@RequestParam("sessionId") Integer sessionId) {
		String message = "";
		try {
			boolean existed = storageService.deleteByFileName(filename,classId,sectionId,sessionId);

			if (existed) {
				message = "Delete the file successfully: " + filename;
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseData(message));
			}

			message = "The file does not exist!";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData(message));
		} catch (Exception e) {
			message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseData(message));
		}
	}

	@PutMapping("/files/{filename:.+}")
	public ResponseEntity<ResponseData> updateFile(@RequestParam("file") MultipartFile file,
			@RequestParam("classId") Integer classId,
			@RequestParam("sectionId") Integer sectionId,
			@RequestParam("sessionId") Integer sessionId,
			@PathVariable String filename) {
		String message = "";
		try {
			ResponseData responseData = storageService.updateFile(file,classId,sectionId,sessionId, filename);
			message = "Updated the file successfully: " + file.getOriginalFilename();
			responseData.setMessage(message);
			return ResponseEntity.status(HttpStatus.OK).body(responseData);
		} catch (Exception e) {
			message = "Could not update the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseData(message));
		}
	}


}
