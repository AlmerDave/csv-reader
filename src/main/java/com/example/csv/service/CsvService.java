package com.example.csv.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.csv.constants.CsvConstants;
import com.example.csv.dto.CsvDto;
import com.example.csv.service.impl.CsvHandler;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class CsvService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CsvService.class);
	
	private final int BATCH_SIZE = 1000;
	
	
	public List<String[]> readAllLines(Path filePath) throws Exception {
		LOGGER.info("Reading files from {}", filePath);
	    try (Reader reader = Files.newBufferedReader(filePath)) {
	        try (CSVReader csvReader = new CSVReader(reader)) {
	            return csvReader.readAll();
	        }
	    }
	}
	
	public Reader returnToReader(Path filePath) throws Exception {
		LOGGER.info("Reading files from {}", filePath);
	    try (Reader reader = new FileReader(filePath.toFile())) {
	    	return reader;
	    }
	}
	
	public List<String[]> readLineByLine(Path filePath) throws Exception {
		LOGGER.info("Reading files from {}", filePath);
	    List<String[]> list = new ArrayList<>();
	    try (Reader reader = Files.newBufferedReader(filePath)) {
	        try (CSVReader csvReader = new CSVReader(reader)) {
	            String[] line;
	            while ((line = csvReader.readNext()) != null) {
	                list.add(line);
	            }
	        }
	    }
	    return list;
	}
	

	public List<CsvDto> csvReader(Path filePath) throws Exception {
		LOGGER.info("Reading files from {}", filePath);
 
	    FileReader reader = new FileReader(filePath.toFile());	    
	    List<CsvDto> details = new CsvToBeanBuilder<CsvDto>(reader)
	    		.withType(CsvDto.class)
	            .withIgnoreLeadingWhiteSpace(true)
	            .withSeparator('|')
	            .build()
	            .parse();
	    reader.close();
	    return details;
	    
	}
	
	public void csvReaderWithLargeFiles(Path filePath, String filename, CsvHandler handlerService) throws Exception {
		LOGGER.info("Reading files line by line from {}", filePath);
		
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			validateCsvFileStructure(reader, filename, handlerService);
		}
		
 
		try (BufferedReader reader = Files.newBufferedReader(filePath)) {
			LOGGER.info("Start Iterator row details");
			Iterator<CsvDto> rowDetails = new CsvToBeanBuilder<CsvDto>(reader)
		    		.withType(CsvDto.class)
		            .withIgnoreLeadingWhiteSpace(true)
		            .withSeparator('|')
		            .build()
		            .iterator();
		    
			List<CsvDto> batchData = new ArrayList<>();
			int rowCount = 0;
			
		    while(rowDetails.hasNext()) {
		    	CsvDto lastRow = rowDetails.next();
		    	
                // Skip the header row and EOF rows
                if (isHeader(rowCount) || isEof(lastRow)) {
                	rowCount++;
                    continue;
                }
		    	
		    	batchData.add(lastRow);
		    	rowCount++;
		    	
		    	if(batchData.size() == BATCH_SIZE) {
		    		handlerService.processCsvData(batchData, filename);
		    		batchData.clear();
		    	}
		    }
		    
		    if(!batchData.isEmpty()) {
		    	handlerService.processCsvData(batchData, filename);
		    }
		    
		} catch (Exception e) {
			LOGGER.error("Error reading large CSV file: {}", e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	    
	}

	private boolean isEof(CsvDto lastRow) {
		return lastRow.getFirstColumn().equalsIgnoreCase("EOF");
	}

	private boolean isHeader(int rowCount) {
		return rowCount == CsvConstants.COLUMN_HEADER_INDEX;
	}

	private void validateCsvFileStructure(BufferedReader reader, String filename, CsvHandler handlerService) 
			throws Exception {
		LOGGER.info("Start validateCsvFileStructure() : {}", filename);
			
		Iterator<CsvDto> rowDetails = new CsvToBeanBuilder<CsvDto>(reader)
		    	.withType(CsvDto.class)
		           .withIgnoreLeadingWhiteSpace(true)
		           .withSeparator('|')
		           .build()
		           .iterator();
			
		CsvDto lastRow = null;
		CsvDto firstRow = null;
		int rowCount = 0;
		
		// Determine the first row for header validation
	    if (rowDetails.hasNext()) {
	        firstRow = rowDetails.next();
	        rowCount++;
	        
	        handlerService.validateHeaders(firstRow, filename);
	    }
		
	    // Process all rows to count them and determine last row for EOF validation
		while(rowDetails.hasNext()) {
		    lastRow = rowDetails.next();
		    rowCount++;
		 }
		

	    if (rowCount == 0) {
	        throw new Exception("Invalid Row Count");
	    }

	    // Validate EOF row
	    if (lastRow != null) {
	    	validateEofAndRowCount(lastRow, rowCount, filename);
	    }
	    
	    reader.close();
	}
	
    private void validateEofAndRowCount(CsvDto lastRow, int rowCount, String filename) throws Exception {

        if (!"EOF".equalsIgnoreCase(lastRow.getFirstColumn())) {
        	LOGGER.error("EOF validation failed for file: {}", filename);
            throw new Exception("Last line is not EOF in file: " + filename);
        }

        int eofValue = Integer.parseInt(lastRow.getSecondColumn());
        int expectedRowCount = rowCount - CsvConstants.COLUMN_HEADER_EOF_COUNT;
        
        if (eofValue != expectedRowCount) {
        	LOGGER.error("Row count validation failed for file: {}", filename);
            throw new Exception("EOF value does not match the number of detail rows in file: " + filename);
        }
    }
}
