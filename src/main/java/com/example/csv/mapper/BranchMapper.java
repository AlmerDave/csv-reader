package com.example.csv.mapper;

import java.util.Date;

import com.example.csv.dto.CsvDto;
import com.example.csv.entity.Branch;

public class BranchMapper {
	
	public static Branch toEntity(CsvDto dto) {
		
		Branch branch = new Branch();
		branch.setBranchCode(dto.getFirstColumn());
		branch.setBranchName(dto.getSecondColumn());
		branch.setDeskCode(dto.getThirdColumn());
		branch.setCreatedBy("OMNIA");
		branch.setDateCreated(new Date());
		branch.setDateModified(new Date());
		return branch;
		
	}

}
