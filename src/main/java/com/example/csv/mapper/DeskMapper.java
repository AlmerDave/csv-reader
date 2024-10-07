package com.example.csv.mapper;

import java.util.Date;

import com.example.csv.dto.CsvDto;
import com.example.csv.entity.Branch;
import com.example.csv.entity.Desk;

public class DeskMapper {
	
	public static Desk toEntity(CsvDto dto) {
		
		Desk desk = new Desk();
		desk.setDeskCode(dto.getSecondColumn());
		desk.setDeskDesc(dto.getFirstColumn());
		desk.setDepartment(dto.getThirdColumn());
		desk.setCreatedBy("OMNIA");
		desk.setStatus(dto.getFourthColumn());
		desk.setDateCreated(new Date());
		desk.setDateModified(new Date());
		return desk;
		
	}

}
