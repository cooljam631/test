package com.table.service;

import com.table.model.Table;
import com.table.service.TableServiceIMPL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.OutputStream;
import java.util.Scanner;

public class TableServiceIMPLTest{
	private TableServiceIMPL tableService;
	private Table table;
	
	@BeforeEach
	public void initializeTableMock(){
		table = new Table(0, 0, "test.txt");
		table.setRows(2);
		table.setColumns(2);
		table.setData(new HashMap[2][2]);
		for (int i = 0; i < table.getRows(); i++) {
			for (int j = 0; j < table.getColumns(); j++) {
				table.getData()[i][j] = new HashMap<>();
        }
    }
		
		tableService = new TableServiceIMPL(table);
		
	}
	
	@Test
	public void givenExistingFile_whenInitializeTable_returnTrue(){
		boolean result = tableService.initializeTable("test.txt", table);
		assertTrue(result);
	}
	
	@Test
	public void givenNonExistingFile_whenInitializeTable_returnFalse(){
		boolean result = tableService.initializeTable("noTest.txt", table);
		assertFalse(result);
	}
	
	@Test
	public void testGenerateRandomTable(){
		tableService.generateRandomTable();
		assertNotNull(table.getData());
	}
	
	@Test
	public void testRegenerateTable(){
		tableService.regenerateTable(4, 3);
		assertEquals(4, table.getRows());
		assertEquals(3, table.getColumns());
		assertNotNull(table.getData());
	}
	
	@Test
	public void testEditCell() {
		table.getData()[1][1] = new HashMap<>();
		table.getData()[1][1].put("key", "value");

		String firstInput = "key\n";
		String secondInput = "newKey\n";

		// Backup the original System.in and System.out
		InputStream originalSystemIn = System.in;
		PrintStream originalSystemOut = System.out;

		// Create input and output streams
		ByteArrayInputStream inputStream = new ByteArrayInputStream((firstInput + secondInput).getBytes());
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		// Redirect System.in and System.out
		System.setIn(inputStream);
		System.setOut(new PrintStream(outputStream));

		// Perform the editCell method
		tableService.editCell(1, 1);

		// Reset System.in and System.out
		System.setIn(originalSystemIn);
		System.setOut(originalSystemOut);

		assertEquals("newKey", table.getData()[1][1].keySet().iterator().next());
	}


	
	@Test
	public void testSortRow(){
		table.getData()[0][0] = new HashMap<>();
		table.getData()[0][0].put("b", "value");
		table.getData()[0][1] = new HashMap<>();
		table.getData()[0][1].put("a", "value");
		tableService.sortRow(0);
		assertEquals("a", table.getData()[0][0].keySet().iterator().next());
		assertEquals("b", table.getData()[0][1].keySet().iterator().next());
		
	}
	
	@Test
	public void testAddRowWithColumns(){
		tableService.addRowWithColumns(2);
		assertEquals(3, table.getRows());
		assertNotNull(table.getData()[2][0]);
		assertNotNull(table.getData()[2][1]);
	}
	
	@Test
	public void testSearchByPattern(){
		table.getData()[0][0] = new HashMap<>();
		table.getData()[0][0].put("key1", "value1");
		table.getData()[0][1] = new HashMap<>();
		table.getData()[0][1].put("key2", "value2");
		table.getData()[1][0] = new HashMap<>();
		table.getData()[1][0].put("key4", "value4");
		table.getData()[1][1] = new HashMap<>();
		table.getData()[1][1].put("key5", "value5");
		String result = tableService.searchByPattern("key");
		assertTrue(result.contains("(0, 0): 1 instance"));
		assertTrue(result.contains("(0, 1): 1 instance"));
	}
}