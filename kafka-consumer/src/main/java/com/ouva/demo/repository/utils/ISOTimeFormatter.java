package com.ouva.demo.repository.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class ISOTimeFormatter {

  public static Optional<LocalTime> parse(Optional<String> str) {
    return str.isPresent() ? Optional.of(LocalTime.parse(str.get(), DateTimeFormatter.ISO_TIME)) : Optional.empty();
  }

}
