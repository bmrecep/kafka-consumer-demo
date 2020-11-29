package com.ouva.demo.controller;

import com.ouva.demo.dto.HeartbeatDTO;
import com.ouva.demo.service.HeartbeatService;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller providing various endpoints for patients.
 */
@RestController("patients")
@AllArgsConstructor
public class PatientController {

  private final HeartbeatService service;

  @GetMapping("/{roomId}/heartbeat")
  @Payload
  public List<HeartbeatDTO> find(@PathVariable("roomId") String roomId,
    @RequestParam("threshold") Optional<Integer> threshold,
    @RequestParam(value = "durationBegin") Optional<LocalTime> durationBegin,
    @RequestParam(value = "durationEnd") Optional<LocalTime> durationEnd) {
    return service.findBy(roomId, threshold, durationBegin, durationEnd);
  }

  // Other endpoints for 'patient' resource...

}
