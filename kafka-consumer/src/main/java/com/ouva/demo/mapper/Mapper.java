package com.ouva.demo.mapper;

import com.ouva.demo.dto.HeartbeatDTO;
import com.ouva.demo.dto.HeartbeatKafkaRecord;
import com.ouva.demo.model.Heartbeat;
import java.util.List;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Mapper {

  Heartbeat toEntity(HeartbeatKafkaRecord rec);

  HeartbeatDTO toDTO(Heartbeat entity);

  List<HeartbeatDTO> toDTOList(List<Heartbeat> entities);

}
