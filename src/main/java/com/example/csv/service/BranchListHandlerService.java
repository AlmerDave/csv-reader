package com.example.csv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.csv.constants.CsvConstants;
import com.example.csv.dto.CsvDto;
import com.example.csv.entity.Branch;
import com.example.csv.entity.Desk;
import com.example.csv.mapper.BranchMapper;
import com.example.csv.mapper.DeskMapper;
import com.example.csv.repository.BranchRepository;
import com.example.csv.service.impl.CsvHandler;

@Service
public class BranchListHandlerService implements CsvHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BranchListHandlerService.class);
	
	@Autowired
	private BranchRepository branchRepository;
	
	@Value("#{'${branchCode.csv.column.headers}'.split('\\|')}")
	private List<String> expectedHeaders;

	@Override
	public void processCsvData(List<CsvDto> csvData, String filename) throws Exception {
		try {
			List<Branch> dataList = convertCsvDtoToEntities(csvData);
	        // Enter additional validation here per row

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	private List<Branch> convertCsvDtoToEntities(List<CsvDto> csvData) {
        return csvData.stream()
        		.map(BranchMapper::toEntity)
	            .collect(Collectors.toList());
	}

	@Override
	public void validateHeaders(CsvDto actualHeaders, String fileName) throws Exception {
		
		
	}

}
