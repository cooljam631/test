package com.table.app;

import com.table.model.Table;
import com.table.service.TableService;
import com.table.service.TableServiceIMPL;
import com.table.util.FileHandler;

import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Application{
	private TableService tableService;
	private Table table;
	private String fileName;
	private int rows;
	private int columns;
	
	public Application(String fileName){
		this.fileName = fileName;
		this.table = new Table(0, 0, fileName);
		this.tableService = new TableServiceIMPL(table);
	}
	
	public void initializeApp(){
		boolean loaded = tableService.initializeTable(fileName, table);
		
		if(!loaded){
			System.out.println("File not found or is empty.");
			if(FileHandler.copyDefaultIfFileNotExists(fileName)){
				System.out.println("File copied successfully");
				if(tableService.initializeTable(fileName, table)){
					rows = table.getRows();
					columns = table.getColumns();
					System.out.println("File has " + rows + " rows and " + columns + " columns");
				}
				else{
					System.out.println("Failed to initialize table with copied file data.");
				}
			}
			else{
				System.out.println("File copied unsuccessfully");
			}
		} else{
			rows = table.getRows();
			columns = table.getColumns();
			System.out.println("Loaded table with " + rows + " rows and " + columns + " columns.");
		}
		initializeMenu();
	}
	
	public void initializeMenu(){
		boolean regenerate = true;
		Scanner input = new Scanner(System.in);
		while(regenerate){
			System.out.println("MAIN MENU");
			System.out.println("Please select an option:");
			System.out.println("1. Search a pattern");
			System.out.println("2. Edit a cell");
			System.out.println("3. Display table");
			System.out.println("4. Regenerate table");
			System.out.println("5. Add Row");
			System.out.println("6. Sort Row");
			System.out.println("7. Exit");
			
			int response = input.nextInt();
			
			switch(response){
				case 1:
				searchPattern();
				// include all lines in cases to one method so when calling it in the case it is calling only one method.
				break;
				
				case 2:
				editCell();
				break;
				
				case 3:
				printTable();
				break;
				
				case 4:
				regenerateTable();
				break;
				
				case 5:
				addRow();
				break;
				
				case 6:
				sortRow();
				break;
				
				case 7:
				regenerate = false;
				break;
				
				default:
				System.out.println("Invalid input! Please try again.");
				break;
			}
		}
		input.close();
	}
	
	public void searchPattern(){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the pattern to be searched:");
		String pattern = input.next();
		String searchResult = tableService.searchByPattern(pattern);
		System.out.println("Instances with keys matching pattern '" + pattern + "':");
		System.out.println(searchResult);
	}
	
	public void editCell(){
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the row index (0-" + (rows - 1) + "): ");
		int rowIndex = input.nextInt();
		System.out.print("Enter the column index (0-" + (columns - 1) + "): ");
		int columnIndex = input.nextInt();
		tableService.editCell(rowIndex, columnIndex);
		FileHandler.saveToFile(fileName, table);
	}
	
	public void printTable(){
		System.out.println("Printing table...");
		table.printTable();
		System.out.println("Table printed.");
	}
	
	public void regenerateTable(){
		Scanner input = new Scanner(System.in);
		System.out.print("Enter the number of rows of the new table:");
		int newRows = input.nextInt();
		System.out.print("Enter the number of columns of the new table");
		int newColumns = input.nextInt();
		rows = newRows;
		columns = newColumns;
		tableService.regenerateTable(newRows, newColumns);
		FileHandler.saveToFile(fileName, table);
		System.out.println("The newly generated table is shown below: ");
		table.printTable();
	}
	
	public void addRow(){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the length of the row:  (1 - " + columns +  "): ");
		int columnModified = input.nextInt();
		tableService.addRowWithColumns(columnModified);
		FileHandler.saveToFile(fileName, table);
	}
	
	public void sortRow(){
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the row index to be sorted (0-" + (rows - 1) +  "): ");
		int rowToSort = input.nextInt();
		tableService.sortRow(rowToSort);
		table.printTable();
		FileHandler.saveToFile(fileName, table);
	}
}