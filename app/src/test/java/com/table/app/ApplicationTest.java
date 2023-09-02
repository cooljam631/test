package com.table.app;

import com.table.service.TableServiceIMPL;
import com.table.util.FileHandler;
import com.table.model.Table;
import com.table.app.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ApplicationTest {
    private Application application;
	private TableServiceIMPL tableServiceMock;
	private FileHandler fileHandlerMock;
	
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
 
    @BeforeEach
    public void initialize() {
        application = new Application("test.txt");
		Table table = new Table(2, 2, "test.txt");
		table.setData(new HashMap[2][2]);
		for (int i = 0; i < table.getRows(); i++) {
			for (int j = 0; j < table.getColumns(); j++) {
				table.getData()[i][j] = new HashMap<>();
			}
		}
		tableServiceMock = mock(TableServiceIMPL.class);
		fileHandlerMock = mock(FileHandler.class);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
	
 
    @Test
    public void testSearchPattern() {
        // Mocking user input
        String input = "pattern\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
 
        application.searchPattern();
        assertThat(outContent.toString(), containsString("No matches found"));
    }
 
    @Test
    public void testEditCell() {
        // Mocking user input
        String input = "1\n2\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
 
        application.editCell();
        assertThat(outContent.toString(), containsString("Data successfully saved to file: test.txt"));
    }
 
    @Test
    public void testPrintTable() {
        application.printTable();
        assertThat(outContent.toString(), containsString("Table printed"));
    }
 
    @Test
    public void testRegenerateTable() {
        // Mocking user input
        String input = "2\n3\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
 
        application.regenerateTable();
        assertThat(outContent.toString(), containsString("Data successfully saved to file: test.txt"));
    }
 
    @Test
    public void testSortRow() {
        // Mocking user input
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
 
        application.sortRow();
        assertThat(outContent.toString(), containsString("Data successfully saved to file: test.txt"));
    }
	
	private void setInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
    
    // Helper method to capture console output
    private String systemOut() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        return outContent.toString();
    }
}