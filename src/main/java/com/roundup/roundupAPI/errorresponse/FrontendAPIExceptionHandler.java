//package com.roundup.roundupAPI.errorresponse;
//
//import java.util.Date;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@ControllerAdvice
//public class FrontendAPIExceptionHandler extends ResponseEntityExceptionHandler{
//	
//	
//    @ExceptionHandler(value= {Exception.class})
//    public ResponseEntity<Object> handleAnyException(Exception exception, WebRequest request) {
//    	String exceptionMessage = exception.getMessage() != null ? exception.getMessage(): exception.getLocalizedMessage();
//    	exceptionMessage = exceptionMessage == null ? exception.toString() : exceptionMessage;
//    	ErrorMessage errorMessage = new ErrorMessage(new Date(), exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR.value());
//    	
//    	return new ResponseEntity<>(
//    			errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
