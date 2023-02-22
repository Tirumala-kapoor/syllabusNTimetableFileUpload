package com.pathshala.schedule.syllabus.fileuploaddownload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pathshala.schedule.syllabus.fileuploaddownload.dto.ResponseData;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

	  @ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<ResponseData> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
	    		.body(new ResponseData("","","","File too large!"));
	  }

}
