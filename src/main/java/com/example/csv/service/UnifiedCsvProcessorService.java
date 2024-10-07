package com.example.csv.service;

import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UnifiedCsvProcessorService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedCsvProcessorService.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
    private CsvService csvService;
	
	@Autowired
	private DeskListHandlerService deskListHandlerService;
	
	@Autowired
	private BranchListHandlerService branchListHandlerService;
	
	@Value("${deskList.prefix}")
	private String deskListPrefix;
	
	@Value("${branchList.prefix}")
	private String branchListPrefix;
	
	@Value("${branchCode.csv.inprogress.directory}")
	private String csvInProgressDirectory;
	
    /**
     * Main processing method that handles all CSV files in the initial folder.
     */
	@Scheduled(fixedDelay= Long.MAX_VALUE)
//	@Scheduled(cron = "${test.run.schedule}")
	public void processDeskCsvFiles() {
		
		try {
			// Get files from initial
			List<String> unprocessedCsvFiles = fileService.getUnprocessedCsvFiles();
			
			for(String filename : unprocessedCsvFiles) {
				if(filename.contains(deskListPrefix)) {
					processLargeSingleCsvFile(filename);
				}
			}
			
		} catch (Exception e) {
			LOGGER.error("Error processing CSV files: {}", e.getMessage(), e);
		}
	}
	
	private void processLargeSingleCsvFile(String filename) throws Exception {
	    LOGGER.info("Processing Large Single file: {}", filename);

	    fileService.moveCsvToInProgressDirectory(filename);
	    Path csvFilePath = fileService.getFilePathWithFileName(csvInProgressDirectory, filename);
	        
	    csvService.csvReaderWithLargeFiles(csvFilePath, filename, deskListHandlerService);

	    fileService.moveCsvToCompleteDirectory(filename);
	    LOGGER.info("Successfully processed file: {}", filename);
			
    }

}
