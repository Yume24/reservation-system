package com.ajaros.reservationsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.Instant;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

  private String fromField;
  private String toField;

  @Override
  public void initialize(ValidDateRange constraintAnnotation) {
    this.fromField = constraintAnnotation.fromField();
    this.toField = constraintAnnotation.toField();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    try {
      Instant fromDate = getFieldValue(value, fromField);
      Instant toDate = getFieldValue(value, toField);

      if (fromDate == null || toDate == null) {
        return true;
      }

      return !fromDate.isAfter(toDate);
    } catch (Exception e) {
      return false;
    }
  }

  private Instant getFieldValue(Object object, String fieldName) throws Exception {
    Field field = object.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (Instant) field.get(object);
  }
}
