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
			validateCsvStructure(csvData, filename);
			
			List<CsvDto> csvDataWithoutHeadersAndEof = removeHeadersAndEof(csvData);
	        List<Branch> data = convertCsvDtoToEntities(csvDataWithoutHeadersAndEof);
	        
	        saveBranchList(data);
	        
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
	
	 private void saveBranchList(List<Branch> data) {
		 branchRepository.saveAll(data);
		}

		private void validateCsvStructure(List<CsvDto> csvData, String fileName) throws Exception {
	        validateHeaders(csvData.get(CsvConstants.COLUMN_HEADER_INDEX), fileName);
	        validateEofAndRowCount(csvData, fileName);
	    }
	    
	    private void validateHeaders(CsvDto actualHeaders, String fileName) throws Exception {
	        List<String> actualHeaderList = new ArrayList<>();
	        
	        // Dynamically add non-null headers
	        String[] headerFields = {
	            actualHeaders.getFirstColumn(),
	            actualHeaders.getSecondColumn(),
	            actualHeaders.getThirdColumn(),
	            actualHeaders.getFourthColumn()
	        };
	        
	        for (String header : headerFields) {
	            if (header != null && !header.trim().isEmpty()) {
	                actualHeaderList.add(header.trim());
	            }
	        }

	        if (!actualHeaderList.equals(expectedHeaders)) {
	        	LOGGER.error("Header validation failed for file: {}", fileName);
	        	throw new Exception("CSV header mismatch. Expected: " + expectedHeaders + ", but found: " + actualHeaderList);
	        }
	    }
	    
	    private void validateEofAndRowCount(List<CsvDto> csvData, String fileName) throws Exception {
	        int lastIndex = csvData.size() - 1;
	        CsvDto lastRow = csvData.get(lastIndex);

	        if (!"EOF".equalsIgnoreCase(lastRow.getFirstColumn())) {
	        	LOGGER.error("EOF validation failed for file: {}", fileName);
	            throw new Exception("Last line is not EOF in file: " + fileName);
	        }

	        int eofValue = Integer.parseInt(lastRow.getSecondColumn());
	        int expectedRowCount = lastIndex - 1;
	        
	        if (eofValue != expectedRowCount) {
	        	LOGGER.error("Row count validation failed for file: {}", fileName);
	            throw new Exception("EOF value does not match the number of detail rows in file: " + fileName);
	        }
	    }

	    private List<CsvDto> removeHeadersAndEof(List<CsvDto> csvData) {
	        return csvData.subList(1, csvData.size() - 1);
	    }

	    private List<Branch> convertCsvDtoToEntities(List<CsvDto> csvData) {
	        return csvData.stream()
	                .map(BranchMapper::toEntity)
	                .collect(Collectors.toList());
	    }

}
