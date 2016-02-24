package compressor;//remove when submit!!!

import java.util.Scanner;


/**
 * This program performs compression of text files using the Move-to-front algorithm.
 * The program automatically detects whether the file is compressed by checking the 
 * beginning of the first line. If the line starts with "0 ", then decompression algorithm 
 * is performed and the original decompressed file is output. 
 * The program uses the standard system input and output, one can use command line redirection to read 
 * and write files.
 * 
 * @author bortsov
 *
 */
public class proj1 {
	private static proj1 p;
	/** A variable of WordList type to store the list of words */
	private static WordList wl;
	/** Constant regex string used to split a string into tokens */ 
	public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
	/** Regex for special characters in original file */ 
	public static final String SPECIAL_CHAR_ORIGINAL = "[^\\p{L}]";
	/** Regex for special characters in compressed file */ 
	public static final String SPECIAL_CHAR_COMPRESSED = "[^\\p{L}0-9]";

	/**
	 * @param args No arguments are required to launch the program
	 */
	public static void main(String[] args){ 
		p = new proj1();
		
		Scanner sc = new Scanner(System.in);
		int uncompressedSize = 0;
		int compressedSize = 0;
		if(sc.hasNextLine()){
			String firstLine = sc.nextLine();
			if(!(firstLine.charAt(0) == '0')){ // Determine if the input is compressed
				System.out.print("0 ");        // Include "0 " in the first line of compressed output 
				String compressedFirstLine = p.compress(firstLine);
				System.out.println(compressedFirstLine);
			    uncompressedSize = uncompressedSize + firstLine.length();
			    compressedSize = compressedSize + compressedFirstLine.length(); 
	  
				while(sc.hasNextLine()){ // Process all lines in the input
				    String inputLine = sc.nextLine();
				    String outputLine = p.compress(inputLine);
				    System.out.println(outputLine);
				    uncompressedSize = uncompressedSize + inputLine.length();
				    compressedSize = compressedSize + outputLine.length();  
				}
				// Include "0 " denoting the end of compressed text, byte count for original uncompressed and compressed text 
				System.out.println("0 Uncompressed: " + uncompressedSize + " bytes; Compressed: " + compressedSize + " bytes");

		    } else {
				String firstLineClear = firstLine.substring(2, firstLine.length()); // Remove "0 " from the first line
				System.out.println(p.decompress(firstLineClear));
				while(sc.hasNextLine()){ 
					String inputLine = sc.nextLine();
					// Proceed with decompression line by line until "0 " is encountered
					if(inputLine.isEmpty() || inputLine.charAt(0) != '0'){ 
					    String outputLine = p.decompress(inputLine);
					    System.out.println(outputLine);						
					} else {
						break;
					}

				}
		    }
		}
		sc.close();
	}
	
	/**
	 * Constructor for proj1.
	 */
	public proj1(){
		wl = new WordList();
	}
	
	
	/**
	 * Compresses an input string using the Move-to-front algorithm.
	 * @param input A String parameter to be compressed.
	 * @return A string with words substituted for integers according to the
	 * compression algorithm.
	 */
	public String compress(String input){
		if(input.isEmpty()){
			return "";
		} else {
			String[] splitString = input.split(String.format(WITH_DELIMITER, SPECIAL_CHAR_ORIGINAL));
			for(int i = 0; i < splitString.length; i++){
				String s = splitString[i];
				if(!s.matches(SPECIAL_CHAR_ORIGINAL)){
					int index = wl.findItem(s);
					if(index != -1){
						splitString[i] = "" + index;
					}
				}
			}
			String output = "";
			for(String st: splitString){
				output = output + st;
			}
			return output;
		}
	}

	/**
	 * Decompresses an input string using the reverse of the Move-to-front algorithm.
	 * @param input A String parameter to be decompressed.
	 * @return The decompressed string.
	 */
	public String decompress(String input){
		if(input.isEmpty()){
			return "";
		} else {
			String[] splitString = input.split(String.format(WITH_DELIMITER, SPECIAL_CHAR_COMPRESSED));
			for(int i = 0; i < splitString.length; i++){
				String s = splitString[i];
				if(s.matches("\\d+")){
					
					int index = Integer.parseInt(s);
					String word = wl.returnWordByIndex(index);
					splitString[i] = word;
	
				} else if(!s.matches(SPECIAL_CHAR_COMPRESSED)){
					wl.addToFront(s);
				}
			}
			String output = "";
			for(String s: splitString){
				output = output + s;
			}
			return output;
		}
	}

	
	
	/**
	 * A linked list of words used for compression/decompression.
	 * Supports the following operations: adding an element to front, finding a node by
	 * string (word), returning a word from the list by provided index.
	 * @author bortsov
	 *
	 */
	private class WordList {
		/** Head pointer */
		public Node head;
		
		/** Constructs an empty list */
		public WordList(){
			head = null;
		}
		
		/**
		 * Inner class to define a Node object
		 */
		private class Node{
			public String data;
			public Node next;
			
			public Node (String s, Node n){ 
				data = s;
				next = n;
			}
		}
		
		/**
		 * Adds the word to the front of the list
		 * @param s A string to add to front.
		 */
		public void addToFront(String s){
			Node newNode = new Node(s, null);
			if(head != null){
				newNode.next = head;
				head = newNode;
			}else{
				head = newNode;
			}
		}
		
		/**
		 * Finds the word in the list, removes it from the current position,
		 * adds it to front and returns its index. If the word is not found,
		 * adds the word to the front of the list and returns -1.
		 * @param s A string to be searched for.
		 * @return An integer index of the list element.
		 */
		public int findItem(String s){
			if(head == null){
				addToFront(s);
				return -1;	
			} else if(head.data.equals(s)){
				return 1;
			} else {
				Node current = head;
				Node previous = head;
				int index = 1;
				while(!current.data.equals(s) && current.next != null){
					previous = current;
					current = current.next;
					index++;
				}
				if(current.data.equals(s)){
					if(current.next != null){
						previous.next = current.next;
					    current.next = null;
					} else {
						previous.next = null;
					}

					addToFront(s);
					return index;
				}
				addToFront(s);
				return -1;
			}
		
		}		

		/**
		 * Returns a word from the list by its index, and moves the word 
		 * to the front of the list.
		 * @param index Index of the word to be returned.
		 * @return A String word
		 */
		public String returnWordByIndex(int index){
			Node current = head;
			Node previous = head;
			for(int i = 1; i < index; i++){
				previous = current;
				current = current.next;
			}
			if(index == 1){
				return current.data;
			} else if(current.next != null){
				previous.next = current.next;
				current.next = null;
			} else {
				previous.next = null;
			}
				
			String str = current.data;
			addToFront(str);
			return str;
		}
		
	}
		
	
}
