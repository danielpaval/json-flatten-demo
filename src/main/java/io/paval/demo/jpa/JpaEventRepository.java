package io.paval.demo.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Profile("jpa")
@Repository
public interface JpaEventRepository extends JpaRepository<JpaEvent, UUID> {
}