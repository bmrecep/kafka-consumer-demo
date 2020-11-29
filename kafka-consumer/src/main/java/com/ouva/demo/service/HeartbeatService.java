package com.ouva.demo.service;

import com.ouva.demo.dto.HeartbeatDTO;
import com.ouva.demo.dto.HeartbeatKafkaRecord;
import com.ouva.demo.mapper.Mapper;
import com.ouva.demo.model.Heartbeat;
import com.ouva.demo.repository.HeartbeatRepository;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@KafkaListener(topics = "#{'${spring.kafka.consumer.topic}'}")
@AllArgsConstructor
public class HeartbeatService {

  private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("H:mm:ss.SSSSSS");

  private final HeartbeatRepository repository;
  private final MongoTemplate template;
  private final Mapper mapper;

  @KafkaHandler
  public void heartbeatReceived(HeartbeatKafkaRecord kafkaRecord) {
    if (log.isDebugEnabled()) {
      log.debug("Received heartbeat message: " + kafkaRecord.toString());
    }
    repository.save(mapper.toEntity(kafkaRecord));
  }

  @KafkaHandler(isDefault = true)
  public void unknown(Object object) {
    log.warn("Received unknown message: " + object);
  }

  public List<HeartbeatDTO> findBy(String roomId,
    Optional<Integer> threshold,
    Optional<LocalTime> durationBegin,
    Optional<LocalTime> durationEnd) {

    // Query heartbeats by room ID and duration.
    List<Heartbeat> heartbeats = template.find(createQuery(roomId, durationBegin, durationEnd), Heartbeat.class);

    if (!threshold.isPresent()) {
      // No threshold provided, return all queried results.
      return mapper.toDTOList(heartbeats);
    }

    // Filter resulting heartbeats by first occurrence of greater signal value than given threshold.
    return mapper.toDTOList(IntStream.range(0, heartbeats.size())
      .mapToObj(i -> {
        return isFirstOccurrence(i, heartbeats, threshold) ? heartbeats.get(i) : null;
      })
      .filter(Objects::nonNull)
      .collect(Collectors.toList()));
  }

  private boolean isFirstOccurrence(int i, List<Heartbeat> heartbeats, Optional<Integer> threshold) {
    return heartbeats.get(i).getRedSignal() > threshold.get()
      && (i == 0 || (i > 0 && heartbeats.get(i - 1).getRedSignal() <= threshold.get()));
  }

  /**
   * Creates a MongoDB query from given room ID and optional duration parameters.
   *
   * @param roomId        room ID.
   * @param durationBegin beginning of the duration.
   * @param durationEnd   end of the duration.
   * @return MongoDB query.
   */
  private Query createQuery(String roomId, Optional<LocalTime> durationBegin, Optional<LocalTime> durationEnd) {
    Query query = new Query();
    Criteria criteria = Criteria.where("roomId").is(roomId);
    if (durationBegin.isPresent()) {
      Criteria durationCriteria = Criteria.where("time").gte(durationBegin.get());
      if (durationEnd.isPresent()) {
        durationCriteria.and("time").lt(durationEnd.get());
      }
      criteria.andOperator(durationCriteria);
    }
    query.addCriteria(criteria);
    return query;
  }
}
