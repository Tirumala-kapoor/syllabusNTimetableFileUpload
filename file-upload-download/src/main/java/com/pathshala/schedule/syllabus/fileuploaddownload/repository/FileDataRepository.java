package com.pathshala.schedule.syllabus.fileuploaddownload.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.pathshala.schedule.syllabus.fileuploaddownload.model.ClassIdentity;
import com.pathshala.schedule.syllabus.fileuploaddownload.model.FileData;

public interface FileDataRepository extends JpaRepository<FileData, ClassIdentity>{

	//FileData findByClassIdFileName(ClassId classId, String filename);

	FileData findByClassIdentityAndFileName(ClassIdentity classIdentity,String filename);
	
	@Transactional
	void deleteByClassIdentityAndFileName(ClassIdentity classIdentity,String filename);
//	public FileData findByClassIdSectionIdSessionId

}
