package com.table.model;

import com.table.model.Table;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.contains;

public class TableTest{
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	
	@BeforeEach
	public void setUpStreams(){
		System.setOut(new PrintStream(outputStream));
	}
	
	@AfterEach
	public void restoreStreams(){
		System.setOut(originalOut);
	}
	
	@Test
	public void givenTestNoDataTxt_whenPrintTable_thenReturnNoData(){
		Table table = new Table(3, 3, "test_noData.txt");
		table.setData(null);
		table.printTable();
		String expectedOutput = "No data available or data is invalid!";
		assertEquals(expectedOutput, outputStream.toString().trim());
	}
	
	@Test
	public void givenTestTxt_whenPrintTable_thenReturnData(){
		Table table = new Table(3, 3, "test.txt");
		Map<String, String>[][] data = new HashMap[3][3];
		data[0][0] = new HashMap<>();
        data[0][0].put("!!!", "[!m");
        data[1][1] = new HashMap<>();
        data[1][1].put("lQt", "alJ");
        table.setData(data);
        table.printTable();
		
		String output = outputStream.toString().trim();
		assertTrue(output.contains("!!!,[!m;"));
		assertTrue(output.contains("lQt,alJ;"));
	}
	
	@Test
	public void givenTestTxt_whenResizeTable_thenReturnTable(){
		Table table = new Table(3, 3, "test.txt");
		table.resizeTable(5, 5);
		assertEquals(5, table.getRows());
		assertEquals(5, table.getColumns());
	}
	
	@Test
	public void givenTestTxt_whenGetData_returnData(){
		Table table = new Table (3, 3, "test.txt");
		Map<String, String>[][] data = table.getData();
		assertNotNull(data);
	}
	
	@Test
	public void givenTestTxt_whenGetRows_returnRows(){
		Table table = new Table (3, 3, "test.txt");
		assertEquals(3, table.getRows());
	}
	
	@Test
	public void givenTestTxt_whenGetColumns_returnColumns(){
		Table table = new Table(3, 3, "test.txt");
		assertEquals(3, table.getColumns());
	}
	
	@Test
	public void givenTestTxt_whenSetData_returnData(){
	Table table = new Table(3, 3, "test.txt");
	Map<String, String>[][] data = new HashMap[3][3];
	table.setData(data);
	assertSame(data, table.getData());
	}
	
	@Test
	public void givenTestTxt_whenSetRows_returnRows(){
		Table table = new Table(3, 3, "test.txt");
		table.setRows(5);
		assertEquals(5, table.getRows());
	}
	
	@Test
	public void givenTestTxt_whenSetColumns_returnColumns(){
		Table table = new Table(3, 3, "test.txt");
		table.setColumns(5);
		assertEquals(5, table.getColumns());
	}
	
	@Test
	public void givenTestTxt_whenGetKeyValueDelimiter_returnKeyValueDelimiter(){
		Table table = new Table(3, 3, "test.txt");
		assertEquals(",", table.getKeyValueDelimiter());
	}
	
	@Test
	public void givenTestTxt_whenGetPairDelimiter_returnPairDelimiter(){
		Table table = new Table(3, 3, "test.txt");
		assertEquals(";", table.getPairDelimiter());
	}
}