package com.table.service;

import com.table.model.Table;

public interface TableService{
	
	boolean initializeTable(String fileName, Table table);
	
	void generateRandomTable();
	
	void generateAndSaveTable(String fileName, int rows, int columns, Table table);
	
	void regenerateTable(int rows, int columns);
	
	void editCell(int rowIndex, int columnIndex);
	
	void sortRow(int rowIndex);
	
	void addRowWithColumns(int numColumns);
	
	String searchByPattern(String pattern);
	
}