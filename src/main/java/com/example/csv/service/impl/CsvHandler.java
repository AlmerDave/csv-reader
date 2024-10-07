package com.example.csv.service.impl;

import java.util.List;

import com.example.csv.dto.CsvDto;

public interface CsvHandler {
	void processCsvData(List<CsvDto> csv, String filename) throws Exception;
}
