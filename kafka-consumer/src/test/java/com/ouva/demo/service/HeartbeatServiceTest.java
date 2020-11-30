package com.ouva.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ouva.demo.dto.HeartbeatDTO;
import com.ouva.demo.dto.HeartbeatKafkaRecord;
import com.ouva.demo.dto.validator.BaseValidator;
import com.ouva.demo.mapper.MapperImpl;
import com.ouva.demo.model.Heartbeat;
import com.ouva.demo.repository.HeartbeatRepository;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class HeartbeatServiceTest {

  private static final LocalTime NOW = LocalTime.now();
  private static final String ROOM_ID = "61264a61-8b9c-410d-8463-54bc606cfcdf";
  private static final Integer RED_SIGNAL = 300;

  private HeartbeatRepository repository;
  private MongoTemplate template;
  private HeartbeatService service;

  @BeforeAll
  public void init() {
    repository = mock(HeartbeatRepository.class);
    template = mock(MongoTemplate.class);
    service = new HeartbeatService(repository, template, new MapperImpl(), createValidator());
  }

  private BaseValidator createValidator() {
    Configuration<?> config = Validation.byDefaultProvider().configure();
    javax.validation.ValidatorFactory factory = config.buildValidatorFactory();
    Validator validator = factory.getValidator();
    factory.close();
    return new BaseValidator(validator);
  }

  private HeartbeatKafkaRecord createKafkaRecord() {
    return HeartbeatKafkaRecord.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL)
      .time(NOW)
      .build();
  }

  private Heartbeat createEntity() {
    return Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL)
      .time(NOW)
      .build();
  }

  private List<Heartbeat> createEntities(LocalTime durationBegin) {
    List<Heartbeat> list = new ArrayList<>();
    list.add(Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL + 10)
      .time(durationBegin).build());
    list.add(Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL + 20)
      .time(durationBegin.plusSeconds(1)).build());
    list.add(Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL)
      .time(durationBegin.plusSeconds(2)).build());
    list.add(Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL - 10)
      .time(durationBegin.plusSeconds(3)).build());
    list.add(Heartbeat.builder()
      .roomId(ROOM_ID)
      .redSignal(RED_SIGNAL + 20)
      .time(durationBegin.plusSeconds(4)).build());
    return list;
  }

  @DisplayName("Test HeartbeatService.heartbeatReceived: Should successfully save.")
  @Test
  void heartbeatReceived_shouldSuccessfullySave() {
    HeartbeatKafkaRecord record = createKafkaRecord();
    Heartbeat entity = createEntity();

    when(repository.save(any())).thenReturn(entity);
    service.heartbeatReceived(record);

    assertEquals(record.getRedSignal(), entity.getRedSignal());
    assertEquals(record.getRoomId(), entity.getRoomId());
    assertEquals(record.getTime(), entity.getTime());
  }

  @DisplayName("Test HeartbeatService.heartbeatReceived: Should throw ValidationException when roomId is missing.")
  @Test
  void heartbeatReceived_shouldThrowValidationExceptionWhenRoomIdIsMissing() {
    HeartbeatKafkaRecord record = createKafkaRecord();
    record.setRoomId(null); // invalidate
    assertThrows(ValidationException.class, () -> service.heartbeatReceived(record));
  }

  @DisplayName("Test HeartbeatService.heartbeatReceived: Should throw ValidationException when redSignal is missing.")
  @Test
  void heartbeatReceived_shouldThrowValidationExceptionWhenRedSignalIsMissing() {
    HeartbeatKafkaRecord record = createKafkaRecord();
    record.setRedSignal(null); // invalidate
    assertThrows(ValidationException.class, () -> service.heartbeatReceived(record));
  }

  @DisplayName("Test HeartbeatService.heartbeatReceived: Should throw ValidationException when time is missing.")
  @Test
  void heartbeatReceived_shouldThrowValidationExceptionWhenTimeIsMissing() {
    HeartbeatKafkaRecord record = createKafkaRecord();
    record.setTime(null); // invalidate
    assertThrows(ValidationException.class, () -> service.heartbeatReceived(record));
  }

  @DisplayName("Test HeartbeatService.findBy: Should return empty when roomId does not exist.")
  @Test
  void findBy_shouldReturnEmptyWhenRoomIdDoesNotExist() {
    LocalTime durationBegin = LocalTime.now();
    LocalTime durationEnd = durationBegin.plusMinutes(1);

    when(template.find(any(), eq(Heartbeat.class))).thenReturn(new ArrayList<>());
    List<HeartbeatDTO> result = service
      .findBy(null, Optional.of(RED_SIGNAL), Optional.of(durationBegin), Optional.of(durationEnd));

    assertTrue(result != null && result.isEmpty());
  }

  @DisplayName("Test HeartbeatService.findBy: Should return empty when no heartbeat exceeds threshold.")
  @Test
  void findBy_shouldReturnEmptyWhenNoHeartbeatExceedsThreshold() {
    LocalTime durationBegin = LocalTime.now();
    LocalTime durationEnd = durationBegin.plusMinutes(1);

    when(template.find(any(), eq(Heartbeat.class))).thenReturn(createEntities(durationBegin));
    List<HeartbeatDTO> result = service
      .findBy(ROOM_ID, Optional.of(RED_SIGNAL + 100), Optional.of(durationBegin), Optional.of(durationEnd));

    assertTrue(result != null && result.isEmpty());
  }

  @DisplayName("Test HeartbeatService.findBy: Should return all when no threshold is provided.")
  @Test
  void findBy_shouldReturnAllWhenNoThresholdIsProvided() {
    LocalTime durationBegin = LocalTime.now();
    LocalTime durationEnd = durationBegin.plusMinutes(1);

    List<Heartbeat> entities = createEntities(durationBegin);
    when(template.find(any(), eq(Heartbeat.class))).thenReturn(entities);
    List<HeartbeatDTO> result = service
      .findBy(ROOM_ID, Optional.empty(), Optional.of(durationBegin), Optional.of(durationEnd));

    assertEquals(entities.size(), result.size());
  }

  @DisplayName("Test HeartbeatService.findBy: Should return two entities when threshold is 300.")
  @Test
  void findBy_shouldReturnTwoEntitiesWhenThresholdIs300() {
    LocalTime durationBegin = LocalTime.now();
    LocalTime durationEnd = durationBegin.plusMinutes(1);

    when(template.find(any(), eq(Heartbeat.class))).thenReturn(createEntities(durationBegin));
    List<HeartbeatDTO> result = service
      .findBy(ROOM_ID, Optional.of(RED_SIGNAL), Optional.of(durationBegin), Optional.of(durationEnd));

    assertEquals(2, result.size());
  }

}
