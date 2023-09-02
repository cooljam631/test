package com.table.util;

import org.apache.commons.lang3.StringUtils;
import com.table.model.Table;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class FileHandler{

	public static boolean loadFromFile(String fileName, Table table){
		String keyValueDelimiter = table.getKeyValueDelimiter();
		String pairDelimiter = table.getPairDelimiter();
		if (!Files.exists(Paths.get(fileName))) {
        return false;
		}
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        List<List<Map<String, String>>> tableData = new ArrayList<>();
        String line;
        int maxColumns = 0; 
        while ((line = reader.readLine()) != null) {
            String[] cells = line.split("\t");
            List<Map<String, String>> rowData = new ArrayList<>();
            for (String cell : cells) {
                String[] keyValuePairs = cell.split(pairDelimiter);
                Map<String, String> cellData = new HashMap<>();
                for (String pair : keyValuePairs) {
                    String[] keyValuePair = pair.split(keyValueDelimiter);
                    if (keyValuePair.length == 2) {
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
                        cellData.put(key, value);
                    }
                }
                rowData.add(cellData);
            }
            tableData.add(rowData);

            
            maxColumns = Math.max(maxColumns, rowData.size());
        }

        int rows = tableData.size();
        int columns = Math.max(maxColumns, table.getColumns());

        table.resizeTable(rows, columns);

        for (int i = 0; i < tableData.size(); i++) {
            List<Map<String, String>> rowData = tableData.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Map<String, String> cellData = rowData.get(j);
                table.getData()[i][j] = cellData;
            }
        }

        System.out.println("Table data successfully loaded from " + fileName);
        return true;
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
        return false;
    } catch (IOException e) {
        System.out.println("An error occurred while loading the file: " + e.getMessage());
        return false;
    }
}
	
	public static void saveToFile(String fileName, Table table) {
		String keyValueDelimiter = table.getKeyValueDelimiter();
		String pairDelimiter = table.getPairDelimiter();
    try (FileWriter writer = new FileWriter(fileName)) {
        Map<String, String>[][] data = table.getData();
        int rows = table.getRows();
        int columns = table.getColumns();

        if (data == null) {
            System.out.println("No data available or data is invalid! File not saved.");
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Map<String, String> cellData = data[i][j];
                if (cellData != null && !cellData.isEmpty()) {
					List<String> keyValuePairs = new ArrayList<>();
                    for (Map.Entry<String, String> entry : cellData.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
						keyValuePairs.add(key + keyValueDelimiter + value + pairDelimiter);
                    }
					String cellString = StringUtils.join(keyValuePairs, pairDelimiter);
					writer.write(cellString);
                }
                writer.write("\t"); 
            }
            writer.write(System.lineSeparator()); 
        }
		
		writer.flush();
		writer.close();

        System.out.println("Data successfully saved to file: " + fileName);
    } catch (IOException e) {
        System.out.println("An error occurred while saving the data to file: " + e.getMessage());
    }
}
	
	public static void createFile(String fileName) {
		if (Files.notExists(Paths.get(fileName))) {
			try {
				Files.createFile(Paths.get(fileName));
			} catch (IOException e) {
				System.out.println("An error occurred when creating the file: " + e.getMessage());
			}
		}
	}
	
	public static boolean doesFileExist(String fileName) {
        return Files.exists(Paths.get(fileName));
    }
	
	/*public static boolean copyDefaultIfFileNotExists(String fileName){
		Path filePath = Paths.get(fileName);
		Path defaultFilePath = Paths.get("util/src/main/resources/default_table.txt");
		
		if(!Files.exists(defaultFilePath)){
			System.out.println("Default file not found!");
			return false;
		}
		
		try {
			Files.copy(defaultFilePath, filePath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Default file copied to: " + fileName);
			return true;
		} catch (IOException e){
			System.out.println("An error occurred while copying default file: " + e.getMessage());
			return false;
		}
	}*/
	
	public static boolean copyDefaultIfFileNotExists(String fileName){
		String defaultFilePath = "/default_table.txt";
		
		try (InputStream inputStream = FileHandler.class.getResourceAsStream(defaultFilePath);
			 OutputStream outputStream = new FileOutputStream(fileName)){
			if(inputStream == null){
				System.out.println("Default file not found in resources.");
				return false;
			}
			byte[] buffer = new byte[1024];
			int bytesRead;
			while((bytesRead = inputStream.read(buffer)) != -1){
				outputStream.write(buffer, 0, bytesRead);
			}
				System.out.println("Default file copied successfully from resources.");
				return true;
		} catch(IOException e){
			System.out.println("Error copying default file: " + e.getMessage());
			return false;
		}
	}
}