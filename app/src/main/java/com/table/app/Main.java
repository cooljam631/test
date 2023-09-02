package com.table.app;

import com.table.model.Table;
import com.table.service.TableServiceIMPL;
import com.table.util.FileHandler;
import com.table.app.Application;

import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Main {

    public static void main(String[] args) {
    if (args.length < 1) {
        System.out.println("Usage: java Main (file_name)");
        return;
    }
		String fileName = args[0];
		Application app = new Application(fileName);
		app.initializeApp();
    }
}