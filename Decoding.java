
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Decoding {
	public static ArrayList<String> Table = null; //dictionary to store the codes

	public static int bitlength = 0; //Bit length
	public static int tableSizeMax = 0; //Max dictionary size

	public static void main(String... args) {
		bitlength = Integer.parseInt(args[1]);
		tableSizeMax = (int) Math.pow((double) 2, (double) bitlength);
		Table = new ArrayList<String>(tableSizeMax);

		BufferedReader input = null; //Buffered Input
		File file = null; //File open
		FileWriter fw = null; // File write
		BufferedWriter output = null; //Buffered output

		String directory_Current = System.getProperty("user.dir") + "/"; //Path of directory
		String file_Input = args[0]; 
		String input_Path = directory_Current + file_Input; //Input file path
		String file_Output = (file_Input.split("\\."))[0] + "_decoded.txt"; //Path of output file (.txt extension)
		String line = ""; //Line check
		String newLine = ""; //Current line read
		String code = ""; //Current code check
		String str = ""; //Current string check
		String newStr = ""; //New string check
		char temp[] = {}; 
		
		try {
			
			file = new File(file_Output);
			fw = new FileWriter(file.getAbsoluteFile());
			
			//Input and output buffer files 
			input = new BufferedReader(new InputStreamReader(new FileInputStream(input_Path), Charset.forName("UTF-16BE")));
			output = new BufferedWriter(fw);
			
			tableInit(); //Initialize dictionary with ASCII 256

			boolean firstTime = true;
			System.out.println("\nDecompression initialized...\tTable size: " + Table.size());
			line = input.readLine(); //First Input line read
			while (line != null) { 
				if((newLine = input.readLine()) != null){ //Next input line read
					line += "\n"; //New line character append
				}
				
				temp = line.toCharArray(); 				
				for (int i = 0; i < temp.length; i++) {
					code = String.valueOf(temp[i]); //New code test
					
					if (firstTime) { /* evaluate this block only first time and only once */
						firstTime = false;
						
						str = Table.get((int) code.charAt(0)); 
						
						output.write(str); 
						System.out.print("Decoded data:\t" + Table.indexOf(str) + "\t" + str);
						System.out.println("\t\t\tcodes:\t" + (int) code.charAt(0) + "\t" + code);
						continue;
					}

					if (Table.size() <= (int) code.charAt(0)) {
						newStr = str + str.toCharArray()[0]; //If code is UNDEFINED, generate new string
					} else {
						newStr = Table.get((int) code.charAt(0)); //Else get new string from table at index code
					}
					
					output.write(newStr); //Append new string to output buffer
					System.out.print("Decoded data:\t" + Table.indexOf(newStr) + "\t" + newStr);

					if (Table.size() <= tableSizeMax) {
						Table.add(str + newStr.toCharArray()[0]); //Add new code for new string
						System.out.print("\t\t\tcodes:\t" + (int) code.charAt(0) + "\t" + code);
						System.out.println("\t\t\tTable updates:\t" + Table.indexOf(str + newStr.toCharArray()[0]) + "\t" + str + newStr.toCharArray()[0]);
					}
					str = newStr; //Repeat with new string
				}
				line = newLine; //Current line is now new line
			}
			System.out.println("Decompressed\tTable size: " + Table.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close(); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close(); 
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Initialize table with 256 ASCII codes
	public static void tableInit() {
		System.out.println("Table Initialization\tTable size: " + Table.size());

		for (int i = 0; i < 256; i++) {
			Table.add(String.valueOf((char) i));
		}

		System.out.println("Complete\tTable size: " + Table.size());
	}

}