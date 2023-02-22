package com.pathshala.schedule.syllabus.fileuploaddownload.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ClassIdentity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer classId;	
	private Integer sectionId;
	private Integer sessionId;	
//	private String fileType;
//	private String uploadedFileType; // Is it Timetable or syllabus
}
