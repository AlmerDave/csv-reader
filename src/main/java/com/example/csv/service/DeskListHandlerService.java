package com.example.csv.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.csv.constants.CsvConstants;
import com.example.csv.dto.CsvDto;
import com.example.csv.entity.Desk;
import com.example.csv.mapper.DeskMapper;
import com.example.csv.repository.DeskRepository;
import com.example.csv.service.impl.CsvHandler;

@Service
public class DeskListHandlerService implements CsvHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeskListHandlerService.class);
	
	@Autowired
	private DeskRepository deskRepository;
	
	@Value("#{'${deskCode.csv.column.headers}'.split('\\|')}")
	private List<String> expectedHeaders;
	
	
	@Override
	public void processCsvData(List<CsvDto> csvData, String filename) {
		try {
			validateCsvStructure(csvData, filename);
			
			List<CsvDto> csvDataWithoutHeadersAndEof = removeHeadersAndEof(csvData);
	        List<Desk> data = convertCsvDtoToEntities(csvDataWithoutHeadersAndEof);
	        
	        saveDeskList(data);
	        
		} catch (Exception e) {
			LOGGER.error("Error in {}", e.getLocalizedMessage(), e);
		}
	}
	
	public void processCsvDataByBatch(List<CsvDto> csvData, String filename)  throws Exception{
		try {
			
	        List<Desk> data = convertCsvDtoToEntities(csvData);
	        saveDeskList(data);
	        
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	private void validateCsvStructureForBatch(List<CsvDto> csvData, String filename) throws Exception {
    	validateHeaders(csvData.get(CsvConstants.COLUMN_HEADER_INDEX), filename);
		
	}

	private void saveDeskList(List<Desk> data) {
    	deskRepository.saveAll(data);
	}

	private void validateCsvStructure(List<CsvDto> csvData, String fileName) throws Exception {
        validateHeaders(csvData.get(CsvConstants.COLUMN_HEADER_INDEX), fileName);
        validateEofAndRowCount(csvData, fileName);
    }
    
    public void validateHeaders(CsvDto actualHeaders, String fileName) throws Exception {
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
    
    public void validateEofAndRowCount(CsvDto lastRow, int rowCount, String fileName) throws Exception {

        if (!"EOF".equalsIgnoreCase(lastRow.getFirstColumn())) {
        	LOGGER.error("EOF validation failed for file: {}", fileName);
            throw new Exception("Last line is not EOF in file: " + fileName);
        }

        int eofValue = Integer.parseInt(lastRow.getSecondColumn());
        int expectedRowCount = rowCount - CsvConstants.COLUMN_HEADER_EOF_COUNT;
        
        if (eofValue != expectedRowCount) {
        	LOGGER.error("Row count validation failed for file: {}", fileName);
            throw new Exception("EOF value does not match the number of detail rows in file: " + fileName);
        }
    }

    private List<CsvDto> removeHeadersAndEof(List<CsvDto> csvData) {
    	return csvData.subList(1, csvData.size() - 1);
    }

    private List<Desk> convertCsvDtoToEntities(List<CsvDto> csvData) {
        return csvData.stream()
                .map(DeskMapper::toEntity)
                .collect(Collectors.toList());
    }
}
