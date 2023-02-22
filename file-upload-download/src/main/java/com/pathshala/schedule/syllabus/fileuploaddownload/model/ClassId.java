package com.pathshala.schedule.syllabus.fileuploaddownload.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClassId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4930501548940512759L;
	private Integer classId;	
	private Integer sectionId;
	private Integer sessionId;
}
