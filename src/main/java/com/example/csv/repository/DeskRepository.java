package com.example.csv.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.csv.entity.Desk;

@Repository
public interface DeskRepository extends CrudRepository<Desk, String> {

}
