package com.example.csv.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.csv.entity.Branch;

@Repository
public interface BranchRepository extends CrudRepository<Branch, String> {

}
