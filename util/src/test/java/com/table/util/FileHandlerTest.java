package com.table.util;

import com.table.model.Table;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileHandlerTest{
	private String fileName = "test_file.txt";
	private Table table;
	
	 @BeforeEach
    public void initialize() {
        fileName = "test_file.txt";
        table = new Table(0, 0, fileName);
    }
	
	@Test
	public void givenExistingFile_whenLoadFromFile_returnFileExists() throws IOException{
	// Create a test file with sample data
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write("key1,value1;\tkey2,value2;\n");
            fileWriter.write("key3,value3;\tkey4,value4;\n");
        }
 
        // Load the data from the test file
        boolean result = FileHandler.loadFromFile(fileName, table);
 
        // Check if the data was loaded successfully
        assertTrue(result);
        assertEquals(2, table.getRows());
        assertEquals(2, table.getColumns());
        assertEquals("value1", table.getData()[0][0].get("key1"));
        assertEquals("value2", table.getData()[0][1].get("key2"));
        assertEquals("value3", table.getData()[1][0].get("key3"));
        assertEquals("value4", table.getData()[1][1].get("key4"));
    }
	
	@Test
	public void givenNonExistingFile_whenLoadFromFile_returnFileNotExist(){
		String fileName = "nonexistent.txt";
		
		
		boolean result = FileHandler.loadFromFile(fileName, table);
		
		assertFalse(result);
	}
	
	@Test
	public void givenData_whenSaveToFile_returnSavedFile() throws IOException {
		try{
			
			Table table = mock(Table.class);
			String fileName = "output.txt";

			Map<String, String>[][] testData = new HashMap[3][3]; 

			testData[0][1] = new HashMap<>();
			testData[0][1].put("key1", "value1");
			testData[0][2] = new HashMap<>();
			testData[0][2].put("key2", "value2");
			testData[1][1] = new HashMap<>();
			testData[1][1].put("key3", "value3");

			when(table.getData()).thenReturn(testData);
			when(table.getRows()).thenReturn(3);
			when(table.getColumns()).thenReturn(3);
			when(table.getKeyValueDelimiter()).thenReturn(",");
			when(table.getPairDelimiter()).thenReturn(";");

			FileWriter writer = mock(FileWriter.class);
			
			doNothing().when(writer).write(anyString());
			doNothing().when(writer).close();
			
			PowerMockito.whenNew(FileWriter.class).withArguments(eq(fileName)).thenReturn(writer);

			FileHandler.saveToFile(fileName, table);

			verify(writer, times(3)).write(anyString()); // Adjust the number of times as needed
			verify(writer).close();
		} catch(Exception e){
			System.out.println("There was an error... " + e.getMessage());
		}
	}
	
	@Test
	public void givenNonExistingFile_whenCopyDefaultIfFileNotExist_returnDefaultFile() throws IOException{
		String sourceFileName = "default_file.txt";
		String destFileName = "copied_default_table.txt";
		
		String defaultContent = "defaultKey=defaultValue";
		InputStream inputStream = new ByteArrayInputStream(defaultContent.getBytes());
		Path sourcePath = Paths.get(sourceFileName);
		Path destPath = Paths.get(destFileName);
		
		Files.deleteIfExists(sourcePath);
		Files.deleteIfExists(destPath);
		
		Files.copy(inputStream, sourcePath);
		boolean result = FileHandler.copyDefaultIfFileNotExists(destFileName);
		
		assertTrue(result);
	}
}