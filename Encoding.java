

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Encoding {
	public static ArrayList<String> Table = null; //dictionary to store the codes
	public static int bitlength = 0; //bit length
	public static int tableSizeMax = 0;  //max size that can be saved in the dictionary 
	public static void main(String args[]) {
		bitlength = Integer.parseInt(args[1]);
		tableSizeMax = (int) Math.pow((double) 2, (double) bitlength);
		Table = new ArrayList<String>(tableSizeMax);
		BufferedReader input = null; //Buffered Input
		BufferedWriter output = null; //Buffered Output
		String directory_Current = System.getProperty("user.dir") + "/"; //Path of the directory
		String file_Input = args[0]; //Input File name
		String input_Path = directory_Current + file_Input; //Path of the file within directory
		String file_Output = (file_Input.split("\\."))[0] + ".lzw"; //Path of output file with .lzw appended

		String line = ""; //Line being read
		String symbol = ""; //Symbol check
		String str = ""; //String check

		char temp[] = {}; 

		try {
			//open the input and output files as buffers
			input = new BufferedReader(new FileReader(input_Path));
			output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_Output), "UTF-16BE")); // 16-bit UTF

			tableInit(); //Initializing dictionary with 256 ASCII codes

			System.out.println("\n\tTable size: " + Table.size());
			while ((line = input.readLine()) != null) { //Loop to read input line by line
				line += "\n"; //Appending new line character at the end
				temp = line.toCharArray(); 

				for (int i = 0; i < temp.length; i++) {
					symbol = String.valueOf(temp[i]); //Fetch new symbol for checks

					if (Table.contains(str + symbol)) {//if STRING + SYMBOL is in dictionary
						str += symbol; //concatenate symbol to string
					} else {
						output.write(Table.indexOf(str)); //Write code to putput buffer for existing string
						System.out.print("Encoded data:\t" + Table.indexOf(str) + "\t" + str);
						if (Table.size() <= tableSizeMax) {
							Table.add(str + symbol); // new code for the new string (STRING + SYMBO) 
							System.out.println("\t\t\tTable updates:\t" + Table.indexOf(str+symbol) + "\t" + str+symbol);
						}
						str = symbol; 
					}
				}
			}
			if(str.length() > 1){
				str = str.substring(0, str.length()-1); //Removing earlier appended new line character '/n'
				output.write(Table.indexOf(str)); 
				System.out.println("Last Encoded data:\t" + Table.indexOf(str) + "\t" + str);
			}
			
			System.out.println("\tTable size: " + Table.size());
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
		System.out.println("\tTable size: " + Table.size());

		for (int i = 0; i < 256; i++) {
			Table.add(String.valueOf((char) i));
		}

		System.out.println("Complete\tTable size: " + Table.size());
	}

}