package com.pathshala.schedule.syllabus.fileuploaddownload.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="syllabus_timetable_file_data")
@NoArgsConstructor
@AllArgsConstructor
public class FileData {
	
	@EmbeddedId
	private ClassIdentity classIdentity;
	
	private String fileName;
	private String fileType;
	private String filePath;
	private String uploadedFileType; // Is it Timetable or syllabus
	
//	@Lob
//	private byte[] data;

}
