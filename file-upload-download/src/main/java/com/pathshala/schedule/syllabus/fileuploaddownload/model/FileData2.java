package com.pathshala.schedule.syllabus.fileuploaddownload.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name="syllabus_timetable_file_data_2")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClassId.class)
public class FileData2 {
	
	
	@Id
	private Integer classId;
	@Id
	private Integer sectionId;
	@Id
	private Integer sessionId;
	
	private String fileName;
	private String fileType;
	private String filePath;
	private String uploadedFileType; // Is it Timetable or syllabus
	
//	@Lob
//	private byte[] data;

}
