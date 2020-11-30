package com.ouva.demo.config.exception;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error message class to gracefully handle RESTful API exceptions. Code below is mostly taken from
 * https://bezkoder.com/spring-boot-controlleradvice-exceptionhandler/
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage implements Serializable {

  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;

}
