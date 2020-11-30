package com.ouva.demo.model;

import java.io.Serializable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patient-heartbeats")
public class Heartbeat implements Serializable {

  @Id
  private String id;

  private String roomId;

  private Integer redSignal;

  private LocalTime time;

}
