package com.ouva.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Heartbeat record class used to map JSON messages consumed from Kafka broker.
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeartbeatKafkaRecord {

  @NotEmpty
  @JsonProperty("room_id")
  private String roomId;

  @NotNull
  @JsonProperty("Red_Signal")
  private Integer redSignal;

  @NotNull
  @JsonProperty("Time")
  private LocalTime time;

}
