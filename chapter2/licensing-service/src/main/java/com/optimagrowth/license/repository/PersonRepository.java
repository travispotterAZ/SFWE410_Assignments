package com.optimagrowth.license.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.optimagrowth.license.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
