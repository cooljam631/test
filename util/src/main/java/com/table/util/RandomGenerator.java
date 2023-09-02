package com.table.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public class RandomGenerator{
	
	/*public String generateRandomString(Random random, int length){
		String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=[]{}|:.<>?";
		StringBuilder content = new StringBuilder(length);
		for(int i = 0; i < length; i++){
			int index = random.nextInt(allowedCharacters.length());
			content.append(allowedCharacters.charAt(index));
		}
		return content.toString();
	}*/
	
	public String generateRandomString(Random random, int length) {
		int minAscii = 33; // ASCII value of !
		int maxAscii = 126; // ASCII value of '~'

		StringBuilder content = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int asciiValue = random.nextInt(maxAscii - minAscii + 1) + minAscii;
			char character = (char) asciiValue;
			content.append(character);
		}

		return content.toString();
	}

}