package com.ouva.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Heartbeat record class used to map JSON messages consumed from Kafka broker.
 */
@Data
@ToString
@NoArgsConstructor
public class HeartbeatKafkaRecord {

  @JsonProperty("room_id")
  private String roomId;

  @JsonProperty("Red_Signal")
  private Integer redSignal;

  @JsonProperty("Time")
  private LocalTime time;

}
