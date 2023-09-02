package com.table.model;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet; 
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table{
	private Map<String,String>[][] data;
	private int rows;
	private int columns;
	private static final String KEY_VALUE_DELIMITER = ",";
	private static final String PAIR_DELIMITER = ";";
	
	public Table(int rows, int columns, String fileName){
		
		this.rows = rows;
		this.columns = columns;
		data = new HashMap[rows][columns];
	}
	
	public void printTable(){
		if(data == null){
			System.out.println("No data available or data is invalid!");
			return;
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
            Map<String, String> keyValuePairs = data[i][j];
            if (keyValuePairs == null) {
                continue;
            } else {
                StringBuilder cellDataFormat = new StringBuilder();
                for (Map.Entry<String, String> entry : keyValuePairs.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    // Check if both key and value are present
                    if (key != null && value != null) {
                        cellDataFormat.append(key).append(KEY_VALUE_DELIMITER)
                                .append(value).append(PAIR_DELIMITER).append(" ");
                    } else {
                        cellDataFormat.append("N/A").append(PAIR_DELIMITER).append(" ");
                    }
                }
                System.out.print(cellDataFormat);
            }
        }
        System.out.println();
		}
	}
	
	public void resizeTable(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new HashMap[rows][columns];
	}
	
	public Map<String, String>[][] getData(){
		return data;
	}
	
	public int getRows(){
		return rows;
	}
	
	public int getColumns(){
		return columns;
	}
	
	public void setData(Map<String, String>[][] newData){
		data = newData;
	}
	
	public void setRows(int newRows){
		rows = newRows;
	}
	
	public void setColumns(int newColumns){
		columns = newColumns;
	}
	
	public String getKeyValueDelimiter(){
		return KEY_VALUE_DELIMITER;
	}
	
	public String getPairDelimiter(){
		return PAIR_DELIMITER;
	}
}
