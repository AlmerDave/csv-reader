package com.example.csv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
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
	public void processCsvData(List<CsvDto> csvData, String filename) throws Exception {
		List<Desk> dataList = convertCsvDtoToEntities(csvData);
	    // Enter additional validation here per row
		for(Desk desk : dataList) {
			boolean isValid = validateEntity(desk);
			
			if(isValid) {
				saveSingleDesk(desk);
			} else {
				LOGGER.error("Validation from row failed");		
			}
				
		}
	}

	private boolean validateEntity(Desk desk) {
		// Check if value is empty
		if(ObjectUtils.isEmpty(desk.getDeskDesc()) ||
				ObjectUtils.isEmpty(desk.getDeskCode()) ||
				ObjectUtils.isEmpty(desk.getDepartment()) ||
				ObjectUtils.isEmpty(desk.getStatus())) {
			return false;
		}
		
		if(desk.getDeskDesc().length() > 50 ||
				desk.getDeskCode().length() > 25  ||
				desk.getDepartment().length() > 50 ||
				desk.getStatus().length() > 1) {
			
			return false;
		}
			
		return true;
		
	}

	private void saveSingleDesk(Desk data) {
		try {
			deskRepository.save(data);
		} catch (Exception e) {
			LOGGER.error("Error inserting data : {}", e.getMessage() );
		}
    	
	}
	
	@Override
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
    

    private List<Desk> convertCsvDtoToEntities(List<CsvDto> csvData) {
        return csvData.stream()
                .map(DeskMapper::toEntity)
                .collect(Collectors.toList());
    }
}
