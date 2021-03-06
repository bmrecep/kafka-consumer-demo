package com.ouva.demo.controller;

import com.ouva.demo.dto.HeartbeatDTO;
import com.ouva.demo.repository.utils.ISOTimeFormatter;
import com.ouva.demo.service.HeartbeatService;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller providing various endpoints for patients.
 */
@CrossOrigin("*")
@RestController("patients")
@AllArgsConstructor
public class PatientController {

  private final HeartbeatService service;

  @GetMapping("/{roomId}/heartbeat")
  @Payload
  public List<HeartbeatDTO> find(@PathVariable("roomId") String roomId,
    @RequestParam(value = "threshold", required = false) Optional<Integer> threshold,
    @RequestParam(value = "durationBegin", required = false) Optional<String> durationBegin,
    @RequestParam(value = "durationEnd", required = false) Optional<String> durationEnd) {
    return service
      .findBy(roomId, threshold, ISOTimeFormatter.parse(durationBegin), ISOTimeFormatter.parse(durationEnd));
  }

  // Other endpoints for 'patient' resource...

}
