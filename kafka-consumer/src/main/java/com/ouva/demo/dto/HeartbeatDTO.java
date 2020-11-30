package com.ouva.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartbeatDTO implements Serializable {

  private Integer redSignal;

  @JsonFormat(
    shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSSSSS")
  private LocalTime time;

}
