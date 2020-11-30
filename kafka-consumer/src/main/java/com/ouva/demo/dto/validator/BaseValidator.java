package com.ouva.demo.dto.validator;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BaseValidator {

  private final Validator validator;

  public <T> void validate(T dto) {
    Set<ConstraintViolation<T>> violations = this.validator.validate(dto);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

}
