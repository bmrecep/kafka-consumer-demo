package com.ouva.demo.dto;

import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HeartbeatDTO implements Serializable {

  private Integer redSignal;

  private LocalTime time;

}
