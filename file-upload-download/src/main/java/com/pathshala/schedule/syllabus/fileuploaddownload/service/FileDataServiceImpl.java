package com.pathshala.schedule.syllabus.fileuploaddownload.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pathshala.schedule.syllabus.fileuploaddownload.controller.FileDataStorageController;
import com.pathshala.schedule.syllabus.fileuploaddownload.dto.ResponseData;
import com.pathshala.schedule.syllabus.fileuploaddownload.model.ClassIdentity;
import com.pathshala.schedule.syllabus.fileuploaddownload.model.FileData;
import com.pathshala.schedule.syllabus.fileuploaddownload.repository.FileDataRepository;
import com.pathshala.schedule.syllabus.fileuploaddownload.util.Constants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileDataServiceImpl implements FileDataService {

	private final FileDataRepository fileDataRepository;

	@Value("${upload-dir}")
	private String FOLDER_PATH;

	private Path root = Paths.get("C:/Pathshala/syllabusNTimetable/files/");

//	@Override
//	public void init() {
//		try {
//			Files.createDirectories(Paths.get("uploads"));
//		} catch (IOException e) {
//			throw new RuntimeException("Could not initialize folder for upload!");
//		}
//	}

	@Override
	public ResponseData uploadFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId)
			throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String filePath = FOLDER_PATH + fileName;
		FileData fileData = null;
		try {

			if (fileName.contains("..")) {
				throw new Exception("Filename contains invalid path sequence : " + fileName);
			} else if (StringUtils.containsWhitespace(fileName)) {
				fileName = fileName.replace(" ", "_");
			}

			if (fileName.startsWith(Constants.TIMETABLE_FILE_TYPE)) {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.TIMETABLE_FILE_TYPE);
			} else if (fileName.startsWith(Constants.SYLLABUS_FILE_TYPE)) {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.SYLLABUS_FILE_TYPE);
			} else {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.OTHERS_FILE_TYPE);
			}

			// file.transferTo(new File(filePath));
			Stream<String> filesNames = Files.walk(this.root, 1)
											.filter(path -> !path.equals(this.root))
											.map(path -> path.getFileName().toString());
			String pattern = fileData.getUploadedFileType()+"_"+classId+"_"+sectionId;
			//Check if there is any existing file in the Filesystem with combination of fileType,classId,sectionId
			List<String> filter = filesNames.filter(t -> t.startsWith(pattern)).collect(Collectors.toList());
			
			for(String fn : filter)
			{
				Path deletefile = root.resolve(fn);
				Files.deleteIfExists(deletefile);
			}
			
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename())
					,StandardCopyOption.REPLACE_EXISTING);
			FileData fileDate = fileDataRepository.save(fileData);

			// construct responseData

			String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
					.path(fileDate.getFileName().toString()).toUriString();

			return new ResponseData(fileDate.getFileName(), downloadUrl, file.getContentType(),
					// file.getSize(),
					null);

			// return fileDate;

		} catch (Exception e) {
			if (e instanceof FileAlreadyExistsException) {
				throw new RuntimeException("A file of that name already exists.");
			}

			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public ResponseData updateFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId,
			String filename) throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String filePath = FOLDER_PATH + fileName;
		FileData fileData = null;
		try {

			if (fileName.contains("..")) {
				throw new Exception("Filename contains invalid path sequence : " + fileName);
			} else if (StringUtils.containsWhitespace(fileName)) {
				fileName = fileName.replace(" ", "_");
			}

			if (fileName.startsWith(Constants.TIMETABLE_FILE_TYPE)) {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.TIMETABLE_FILE_TYPE);
			} else if (fileName.startsWith(Constants.SYLLABUS_FILE_TYPE)) {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.SYLLABUS_FILE_TYPE);
			} else {
				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
						file.getContentType(), filePath, Constants.OTHERS_FILE_TYPE);
			}

			Path deletefile = root.resolve(filename);
			Files.deleteIfExists(deletefile);
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
			
//			fileDataRepository.deleteByClassIdentityAndFileName(new ClassIdentity(classId, sectionId, sessionId),
//					filename);
			FileData fileDate = fileDataRepository.save(fileData);
			
			String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
					.path(fileDate.getFileName().toString()).toUriString();

			return new ResponseData(fileDate.getFileName(), downloadUrl, file.getContentType(),
					// file.getSize(),
					null);
			

		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}

	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.root, 1)
					.filter(path -> !path.equals(this.root))
					.map(this.root::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	@Override
	public ResponseData load(String filename, Integer classId, Integer sectionId, Integer sessionId) {
		try {

			FileData fileData = fileDataRepository
					.findByClassIdentityAndFileName(new ClassIdentity(classId, sectionId, sessionId), filename);
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			String url = MvcUriComponentsBuilder.fromMethodName(FileDataStorageController.class, "download", filename)
					.build().toString();

			if (resource.exists() || resource.isReadable()) {
				// return resource;
				return new ResponseData(fileData.getFileName(), url, fileData.getFileType(),
						// file.getSize(),
						"File retrieved successfully");
			} else {
				throw new RuntimeException("Could not read the file!");
			}

		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public boolean deleteByFileName(String filename, Integer classId, Integer sectionId, Integer sessionId) {
		try {
			// delete from d/b
			fileDataRepository.deleteByClassIdentityAndFileName(new ClassIdentity(classId, sectionId, sessionId),
					filename);

			Path file = root.resolve(filename);
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

//	@Override
//	public boolean deleteUploadedFile(MultipartFile file, String fileName) throws Exception {
//		try {
//			  Path root = Paths.get(FOLDER_PATH);
//		      Path deletedFile = root.resolve(fileName);
//		      return Files.deleteIfExists(deletedFile);
//		    } catch (IOException e) {
//		      throw new RuntimeException("Error: " + e.getMessage());
//		    }
//
//	}

//	@Override
//	public String replaceUploadedFile(MultipartFile file, Integer classId, Integer sectionId, Integer sessionId,
//			String fileName) throws Exception {
//
//		String filePath = FOLDER_PATH + fileName;
//		FileData fileData = null;
//		try {
//
//			if (fileName.contains("..")) {
//				throw new Exception("Filename contains invalid path sequence : " + fileName);
//			} else if (StringUtils.containsWhitespace(fileName)) {
//				fileName = fileName.replace(" ", "_");
//			}
//
//			if (fileName.startsWith(Constants.TIMETABLE_FILE_TYPE)) {
//				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
//						file.getContentType(), filePath, Constants.TIMETABLE_FILE_TYPE);
//			} else if (fileName.startsWith(Constants.SYLLABUS_FILE_TYPE)) {
//				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
//						file.getContentType(), filePath, Constants.SYLLABUS_FILE_TYPE);
//			} else {
//				fileData = new FileData(new ClassIdentity(classId, sectionId, sessionId), fileName,
//						file.getContentType(), filePath, Constants.OTHERS_FILE_TYPE);
//			}
//
//			FileData fileDate = fileDataRepository.save(fileData);
//			file.transferTo(new File(filePath));
//
//			if (fileDate != null) {
//				return "file Successfully uploaded : " + fileName;
//			}
//			return null;
//
//		} catch (Exception e) {
//			throw new Exception("Could not save file : " + fileName);
//		}
//
//	}

}
