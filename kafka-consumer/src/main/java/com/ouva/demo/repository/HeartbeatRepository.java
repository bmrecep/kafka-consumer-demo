package com.ouva.demo.repository;

import com.ouva.demo.model.Heartbeat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartbeatRepository extends MongoRepository<Heartbeat, String> {

}
