package com.example.csv.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.csv.entity.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

}
