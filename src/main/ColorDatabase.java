package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ColorDatabase {
	
	public void saveNewColor(String name, char index) {
		File f = new File("plugins/GlobalChat/"+name);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(index);
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public char getColorIndex(String name) {
		File f = new File("plugins/GlobalChat/"+name);
		char notExists = '-';
		if(!f.exists()) {
			return notExists;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			char index = line.charAt(0);
			br.close();
			return index;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return notExists;
	}

}
