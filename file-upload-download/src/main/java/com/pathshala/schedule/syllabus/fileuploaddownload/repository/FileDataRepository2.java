package com.pathshala.schedule.syllabus.fileuploaddownload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.pathshala.schedule.syllabus.fileuploaddownload.model.ClassId;
import com.pathshala.schedule.syllabus.fileuploaddownload.model.FileData2;

public interface FileDataRepository2 extends JpaRepository<FileData2, ClassId>{

	FileData2 findByClassIdAndFileName(ClassId classId,  String filename);

	@Transactional
	void deleteByClassIdAndFileName(ClassId classId,  String filename);

	//FileData findByClassIdFileName(ClassId classId, String filename);

//	FileData findByClassIdentityAndFileName(ClassIdentity classIdentity,String filename);
//	
//	@Transactional
//	void deleteByClassIdentityAndFileName(ClassIdentity classIdentity,String filename);
//	public FileData findByClassIdSectionIdSessionId

}
