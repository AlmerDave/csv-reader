package com.example.csv.mapper;

import com.example.csv.dto.CsvDto;
import com.example.csv.entity.Person;

public class PersonMapper {
	
	public static Person toEntity(CsvDto dto) {
		
		Person person = new Person();
		person.setFirstName(dto.getFirstColumn());
		person.setLastName(dto.getSecondColumn());
		person.setAge(dto.getThirdColumn());
		person.setBirthDate(dto.getFourthColumn());
		return person;
		
	}

}
