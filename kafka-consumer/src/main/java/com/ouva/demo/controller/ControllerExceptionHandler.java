package com.ouva.demo.controller;

import com.ouva.demo.exception.ErrorMessage;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * A global exception handler to return error response to frontend.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
    ErrorMessage message = new ErrorMessage(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      new Date(),
      ex.getMessage(),
      request.getDescription(false));

    return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
