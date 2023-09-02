package com.table.service;

import com.table.util.FileHandler;
import com.table.model.Table;
import com.table.util.RandomGenerator;

import java.util.Scanner;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TableServiceIMPL implements TableService{
	private Table table;
	private RandomGenerator randomGenerator;

	
	public TableServiceIMPL(Table table){
		this.table = table;
		this.randomGenerator = new RandomGenerator();
	}
	
	@Override
	public boolean initializeTable(String fileName, Table table) {
    return FileHandler.loadFromFile(fileName, table);
	}
	
	@Override
	public void generateRandomTable(){
		Random generator = new Random();
		System.out.println("Generating random table with rows: " + table.getRows() + " and columns: " + table.getColumns());
		for(int i = 0; i < table.getRows(); i++){
			Map<String, String>[] row = new HashMap[table.getColumns()];
			for(int j = 0; j < table.getColumns(); j++){
				Map<String, String> keyValuePairs = new HashMap<>();
				String key = randomGenerator.generateRandomString(generator, 3);
				String value = randomGenerator.generateRandomString(generator, 3);
				keyValuePairs.put(key, value);
				row[j] = keyValuePairs;
			}
			table.getData()[i] = row;
		}
	}
	
	@Override
	public void generateAndSaveTable(String fileName, int rows, int columns, Table table) {
		generateRandomTable();
        FileHandler.saveToFile(fileName, this.table);
    }
	
	@Override
	public void regenerateTable(int rows, int columns){
		table.setData(new HashMap[rows][columns]);
		table.setRows(rows);
		table.setColumns(columns);
		generateRandomTable();
	}
	
	@Override
	public void editCell(int rowIndex, int columnIndex){
		Scanner input = new Scanner(System.in);
		String keyValueDelimiter = table.getKeyValueDelimiter();
		String pairDelimiter = table.getPairDelimiter();
		if (isValidCell(rowIndex, columnIndex)) {
        Map<String, String> cellData = table.getData()[rowIndex][columnIndex];
		StringBuilder cellDataFormat = new StringBuilder();
		for (Map.Entry<String, String> entry : cellData.entrySet()) {
            cellDataFormat.append(entry.getKey()).append(keyValueDelimiter)
                       .append(entry.getValue()).append(pairDelimiter).append(" ");
        }
        System.out.println("Contents of the cell: " + cellDataFormat);
        System.out.print("Do you want to edit the key or the value of the cell? (Input key or value): ");
        String choice = input.nextLine().toLowerCase();

        if ("key".equals(choice)) {
            System.out.print("Enter the new value of key: ");
            String newKey = input.nextLine();

            if (!isKeyUnique(newKey, rowIndex, columnIndex)) {
                System.out.println("The key already exists!");
                return;
            }
				String value = cellData.entrySet().iterator().next().getValue();
				cellData.clear();
				cellData.put(newKey, value);
		} else if ("value".equals(choice)) {
			String key = cellData.entrySet().iterator().next().getKey();
			System.out.print("Enter the new value for the key " + key + ": ");
			String newValue = input.nextLine();
			cellData.put(key, newValue);
		} else {
			System.out.println("Invalid input! Please choose from key or value.");
			return;
			}
		System.out.println("Cell updated successfully.");
		} else {
			System.out.println("Invalid cell coordinates! Please try again.");
		}
	}
	
	@Override
	public void sortRow(int rowIndex) {
		if(!isValidRow(rowIndex)) {
			System.out.println("Invalid row index!");
			return;
		}

		Map<String, String>[] row = table.getData()[rowIndex];

		// Sort the keys in the row in ascending order
		Arrays.sort(row, Comparator.comparing(map -> map.keySet().iterator().next()));

		System.out.println("Keys in row " + rowIndex + " sorted in ascending order.");
	}
	
	@Override
	public void addRowWithColumns(int numColumns) {
        if (numColumns <= 0) {
            System.out.println("Number of columns must be greater than 0.");
            return;
        }

        Map<String, String>[][] newData = new HashMap[table.getRows() + 1][table.getColumns()];

        for (int i = 0; i < table.getRows(); i++) {
            System.arraycopy(table.getData()[i], 0, newData[i], 0, table.getColumns());
        }

        Random generator = new Random();

        for (int j = 0; j < numColumns; j++) {
            Map<String, String> keyValuePairs = new HashMap<>();
            String key = randomGenerator.generateRandomString(generator, 3);
            String value = randomGenerator.generateRandomString(generator, 3);
            keyValuePairs.put(key, value);
            newData[table.getRows()][j] = keyValuePairs;
        }

        table.setData(newData);
        table.setRows(table.getRows() + 1);

        System.out.println("New row added with " + numColumns + " columns and randomly generated key-value pairs.");
    }
	
	@Override
	public String searchByPattern(String pattern) {
		StringBuilder result = new StringBuilder();

		if (table.getData() == null){
			System.out.println("No data available or data is invalid!");
			return result.toString();
		}

		int totalMatchesCount = 0; 

		for (int i = 0; i < table.getRows(); i++) {
			for (int j = 0; j < table.getColumns(); j++) {
				Map<String, String> cellData = table.getData()[i][j];
				int matchesCount = 0; 

				for (String key : cellData.keySet()) {
					matchesCount += countPatternMatches(key, pattern);
				}
				
				for(String value : cellData.values()){
					matchesCount += countPatternMatches(value, pattern);
				}

				if (matchesCount > 0) {
					String cellCoordinates = "(" + i + ", " + j  + ")";
					result.append("Cell ").append(cellCoordinates).append(": ").append(matchesCount)
                        .append(" instance").append(matchesCount > 1 ? "s" : "").append(System.lineSeparator());
						
						System.out.println("Matches in cell " + cellCoordinates + ": " + matchesCount);
					totalMatchesCount += matchesCount;
				}
			}
		}

		if (totalMatchesCount == 0) {
			result.append("No matches found.");
		}

		return result.toString();
	}
	
	//Checking for validity of cell coordinates
	private boolean isValidCell(int row, int column){
		return row >= 0 && row < table.getRows() && column >= 0 && column < table.getColumns();
	}
	
	// Checking for uniqueness of key
	private boolean isKeyUnique(String newKey, int row, int column){
		for(int i = 0; i < table.getRows(); i++){
			for(int j = 0; j < table.getColumns(); j++){
				if(i == row && j == column){
					continue;
				}
				Map<String, String> cellData = table.getData()[i][j];
				if(cellData.containsKey(newKey)){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isValidRow(int row) {
		return row >= 0 && row < table.getRows();
	}
	
	private int countPatternMatches(String input, String pattern) {
		Pattern regexPattern = Pattern.compile(pattern);
		Matcher matcher = regexPattern.matcher(input);

		int count = 0;
		while (matcher.find()) {
			count++;
		}

		return count;
	}
}
