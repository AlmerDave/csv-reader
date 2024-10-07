package com.example.csv.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class FileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	
	@Value("${branchCode.csv.initial.directory}")
	private String csvInitialDirectory;
	
	@Value("${branchCode.csv.complete.directory}")
	private String csvCompleteDirectory;
	
	@Value("${branchCode.csv.inprogress.directory}")
	private String csvInProgressDirectory;
	
	
	public Path getFilePathWithFileName(String directory, String fileName) {
        String projectRoot = System.getProperty("user.dir");
        String fullPath = projectRoot + directory + fileName;
        return Paths.get(fullPath);
    }
	
	public Path getFilePath(String directory) {
        String projectRoot = System.getProperty("user.dir");
        String fullPath = projectRoot + directory;
        return Paths.get(fullPath);
    }
	
	public List<String> findCsvFiles(String directory) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
            return stream
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.toLowerCase().contains(".csv"))
                .collect(Collectors.toList());
        }
    }
	
	public void moveCsvToCompleteDirectory(String csvFileName) {
		Path destinationFile = getFilePathWithFileName(csvCompleteDirectory, csvFileName);
		Path sourceFile = getFilePathWithFileName(csvInProgressDirectory, csvFileName);
		
		try {
			Files.createDirectories(destinationFile.getParent());
			
			if(Files.exists(destinationFile)) {
				Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(sourceFile, destinationFile);
			}
			
			LOGGER.info("File : {} was moved successfully to complete directory", csvFileName);
		}  catch (IOException e) {
            LOGGER.error("Error copying file: " + e.getMessage());
        } catch (Exception e) {
        	LOGGER.error("Error : " + e.getLocalizedMessage());
        }
	}
	
	
	public void moveCsvToInProgressDirectory(String csvFileName) {
		Path destinationFile = getFilePathWithFileName(csvInProgressDirectory, csvFileName);
		Path sourceFile = getFilePathWithFileName(csvInitialDirectory, csvFileName);
		
		try {
			Files.createDirectories(destinationFile.getParent());
			
			if(Files.exists(destinationFile)) {
				Files.move(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			} else {
				Files.move(sourceFile, destinationFile);
			}
			
			LOGGER.info("File : {} was moved successfully to in-progress directory", csvFileName);
		}  catch (IOException e) {
            LOGGER.error("Error moving file: " + e.getMessage());
        } catch (Exception e) {
        	LOGGER.error("Error : " + e.getLocalizedMessage());
        }
	}
	
	public void deleteFile(String directory, String fileName) {
	    Path filePath = getFilePathWithFileName(directory, fileName);

	    try {
	        if (Files.exists(filePath)) {
	            Files.delete(filePath);
	            LOGGER.info("File: {} was deleted successfully from {} directory", fileName, directory);
	        } else {
	            LOGGER.warn("File: {} does not exist in {} directory", fileName, directory);
	        }
	    } catch (IOException e) {
	        LOGGER.error("Error deleting file: {}", e.getMessage());
	    } catch (Exception e) {
	        LOGGER.error("Unexpected error while deleting file: {}", e.getLocalizedMessage());
	    }
	}
	
	public List<String> getUnprocessedCsvFiles() throws Exception {
		Path initialFilePath = getFilePath(csvInitialDirectory);
		List<String> initialDirectoryCsvFiles = findCsvFiles(initialFilePath.toString());
		
		Path completeFilePath = getFilePath(csvCompleteDirectory);
		List<String> completeDirectoryCsvFiles = findCsvFiles(completeFilePath.toString());

        return initialDirectoryCsvFiles.stream()
                .filter(file -> !completeDirectoryCsvFiles.contains(file))
                .collect(Collectors.toList());
    }

}
