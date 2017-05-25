//Author: Ryan Green
//For this project I consulted Tyler Wilson and Sam Triest
//CSC 172 Project 1

/*
 * Sources Used:
 * https://en.wikipedia.org/wiki/Hamming_code
 * https://www.tutorialspoint.com/java/java_basic_operators.htm
 * http://www.pracspedia.com/CN/hamming.html
 */

package project1;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.file.Path;

public class Hamming {
	/**
	 * 
	 * @param inFileName The name of the file to read in from
	 * @param outFileName The name of the file to write out to
	 * 
	 * This method reads all of the bytes in from a file and encodes every 4 bits using Hamming codes, which adds 3 parity bits.
	 * Every byte from this file is converted into an 8-bit binary string, which is then split in half.
	 * Each half of this string is converted from 4 to 7 bits by adding the parity bits.
	 * A zero is then added to the start of each 7 bit string, and these bytes are written to the output file.
	 */
    public static void encode_7_4(String inFileName, String outFileName) {
        try { //The entire method must be in a try-catch statement to catch the IOException that is thrown if the in file doesn't exist
	        Path path = Paths.get(inFileName); //create path to input file
	        byte[] data = Files.readAllBytes(path); //get all the bytes in the file
	        byte[] encodedBytes = new byte[2*data.length]; //encoded bytes will require twice as many bytes as were read from the file due to parity bits
	        for (int i = 0; i < data.length; i++) { //for each byte from the file
				String binString = Integer.toBinaryString(data[i] & 255 | 256).substring(1); //convert the byte into a string containing the binary representation of the byte
				//Split the binary string into two 4 bit arrays
				Bit[] bit1 = {new Bit(binString.charAt(0)-48), new Bit(binString.charAt(1)-48), new Bit(binString.charAt(2)-48), new Bit(binString.charAt(3)-48)};
				bit1 = addParityBits(bit1);
				Bit[] bit2 = {new Bit(binString.charAt(4)-48), new Bit(binString.charAt(5)-48), new Bit(binString.charAt(6)-48), new Bit(binString.charAt(7)-48)};
				bit2 = addParityBits(bit2);
				String encodedByte = "0"+encode(bit1); //encode the first 4 bits, adding a 0 in front to make it 8 bits
				//Since there are now twice as many bytes, we write to the 2*i and 2*i+1 indices in the array
				encodedBytes[2*i] = Byte.parseByte(encodedByte, 2); //add to the array, converting string back to a byte
				encodedByte = "0"+encode(bit2); //encode the second 4 bits, adding a 0 in front
				encodedBytes[2*i+1] = Byte.parseByte(encodedByte, 2); //add 2nd set of bits to the encoded bits array
	        }  
	        //write all encoded to the output file
			FileOutputStream fos = new FileOutputStream(outFileName); //open output stream on file name 
			fos.write(encodedBytes); //write bytes to file
			fos.close(); //close stream
        } catch (IOException e){ //catch io exception if file can't be opened
        	System.out.println("Error, couldn't open input file: " + inFileName);
        }
    }
	/**
	 * 
	 * @param inFileName The name of the file to read in from
	 * @param outFileName The name of the file to write out to
	 * 
	 * This method reads all of the bytes in from a file and encodes every 4 bits using Hamming codes, which adds 3 parity bits.
	 * Every byte from this file is converted into an 8-bit binary string, which is then split in half.
	 * Each half of this string is converted from 4 to 7 bits by adding the parity bits.
	 * A zero is then added to the start of each 7 bit string, and these bytes are written to the output file.
	 */
    public static void encode_15_11(String inFileName, String outFileName) {
        try { //The entire method must be in a try-catch statement to catch the IOException that is thrown if the in file doesn't exist
	        Path path = Paths.get(inFileName); //create path to input file
	        byte[] data = Files.readAllBytes(path); //get all the bytes in the file
	        byte[] encodedBytes = new byte[2*data.length]; //encoded bytes will require twice as many bytes as were read from the file due to parity bits
	        for (int i = 0; i < data.length; i++) { //for each byte from the file
				String binString = Integer.toBinaryString(data[i] & 255 | 256).substring(1); //convert the byte into a string containing the binary representation of the byte
				//Split the binary string into two 4 bit arrays
				Bit[] bitArray= new Bit[11];
				bitArray[0] = new Bit(0);
				bitArray[1] = new Bit(0);
				bitArray[2] = new Bit(0);
				for (int k = 3; k < binString.length()+3; k++) {
					bitArray[k] = new Bit(binString.charAt(k-3)-48);
 				}
				bitArray = addParityBits(bitArray);
				String encodedByte = "0"+encode(bitArray); //encode the first 4 bits, adding a 0 in front to make it 8 bits
				//Since there are now twice as many bytes, we write to the 2*i and 2*i+1 indices in the array
				encodedBytes[2*i] = (byte)Integer.parseInt(encodedByte.substring(0, 8), 2); //add to the array, converting string back to a byte
				encodedBytes[2*i+1] = (byte)Integer.parseInt(encodedByte.substring(8), 2); //add 2nd set of bits to the encoded bits array
	        }  
	        //write all encoded to the output file
			FileOutputStream fos = new FileOutputStream(outFileName); //open output stream on file name 
			fos.write(encodedBytes); //write bytes to file
			fos.close(); //close stream
        } catch (IOException e){ //catch io exception if file can't be opened
        	System.out.println("Error, couldn't open input file: " + inFileName);
        }
    }
    /**
     * 
     * @param inFileName Encoded file name to be read and decoded
     * @param outFileName File name to write decoded message to
     * 
     * This method decodes the bytes of a file, which have been encoded by the encode_7_4 method. 
     * It reads all the bytes from the file and decodes them byte by byte, writing them out to the output file.
     */
    public static void decode_7_4(String inFileName, String outFileName) {
        try {//The entire method must be in a try-catch statement to catch the IOException that is thrown if the in file doesn't exist
	        Path path = Paths.get(inFileName); //create path to input file
	        byte[] data = Files.readAllBytes(path); //get all the bytes in the file
	        Bit[][] decodedBytes = new Bit[data.length][]; //create a 2 dimensional bit array to store each byte from the file as a separate bit array
	        byte[] outputArray = new byte[data.length/2]; //the array to be outputted is half as long as the bytes that were read in because the parity bits are removed
	        for (int i = 0; i < decodedBytes.length; i++) { //for each byte from the input file
				String binString = Integer.toBinaryString(data[i] & 255 | 256).substring(2); //convert byte to binary string, skip the first number because it will always be 0
	        	Bit[] bitArray = new Bit[7]; //create a new bit array of length 7
	        	for (int j = 0; j < binString.length(); j++) { //for each bit in the string
	        		bitArray[j] = new Bit(binString.charAt(j)-48); //add that value to the bit array, subtracting 48 ensures it is either a 1 or 0
	        	}
	        	decodedBytes[i] = decode(bitArray); //decode the bit array for that byte, will return a bit array with 4 bits
	        }
        	String byteString = ""; //create empty string to add the binary to
        	for (int k = 0; k < decodedBytes.length; k++ ) { //for each byte
        		decodedBytes[k] = removeParityBits(decodedBytes[k]); //remove the parity bits from the byte
        		for (int m = 0; m < decodedBytes[k].length; m++) {  //for each bit in this byte
        			if (decodedBytes[k][m] != null) { //if not where a parity bit was
        				byteString += decodedBytes[k][m].toString(); //add this bit to the binary string
        			}
        		}
        		//we execute this code every 2 times through the loop because each byte from the input file becomes 4 bits, so we combine 2 of these to make a decoded byte
        		if (k%2!=0){ 
        			outputArray[k/2] = Byte.parseByte(byteString, 2); //output to index k/2 with the byte object
        			byteString = "";
        		}
        	}
        	//write all decoded bytes to the output file
			FileOutputStream fos = new FileOutputStream(outFileName); //open output stream on output file
			fos.write(outputArray); //write all bytes to file
			fos.close(); //close stream
        } catch (IOException e) { //catch exception if input file can't be opened
        	System.out.println("Error, couldn't open input file: " + inFileName);
        }
    }
    /**
     * 
     * @param inFileName Encoded file name to be read and decoded
     * @param outFileName File name to write decoded message to
     * 
     * This method decodes the bytes of a file, which have been encoded by the encode_15_11 method. 
     * It reads all the bytes from the file and decodes them byte by byte, writing them out to the output file.
     * If bits have been flipped, this method will correct them and print the correct bytes to the output file
     */
    public static void decode_15_11(String inFileName, String outFileName) {
        try {//The entire method must be in a try-catch statement to catch the IOException that is thrown if the in file doesn't exist
	        Path path = Paths.get(inFileName); //create path to input file
	        byte[] data = Files.readAllBytes(path); //get all the bytes in the file
	        Bit[][] decodedBytes = new Bit[data.length][]; //create a 2 dimensional bit array to store each byte from the file as a separate bit array
	        byte[] outputArray = new byte[data.length/2]; //the array to be outputted is half as long as the bytes that were read in because the parity bits will be removed
	        for (int i = 0; i < outputArray.length; i++) { //for each byte to be outputted
	        	//Take two bytes and add them to one string, in order to decode 15 bits down to 11
				String binString = Integer.toBinaryString(data[2*i] & 255 | 256).substring(1)+Integer.toBinaryString(data[2*i+1] & 255 | 256).substring(1); 
	        	Bit[] bitArray = new Bit[15]; //create a new bit array of length 15
	        	for (int j = 0; j < binString.length()-1; j++) { //for each bit in the string, except 1 because we ignore the first bit
	        		if (j == 0 || j==1 || j==3 || j==7) { //if it is an index that should be a parity bit
	        			bitArray[j] = new ParityBit(binString.charAt(j+1)-48); //add a parity bit to this location in the array, with the value read in
	        		}
	        		else //otherwise, add a normal bit to the array
	        			bitArray[j] = new Bit(binString.charAt(j+1)-48); //add the correct value to the bit array, subtracting 48 ensures it is either a 1 or 0
	        	}
	        	decodedBytes[i] = decode(bitArray); //decode the bit array for that byte, will return a bit array with 11 bits
	        }
        	String byteString = ""; //create empty string to add the binary to
        	for (int k = 0; k < outputArray.length; k++ ) { //for each byte
        		decodedBytes[k] = removeParityBits(decodedBytes[k]); //remove the parity bits from the byte
        		for (int m = 0; m < decodedBytes[k].length; m++) {  //for each bit in this byte
        			if (decodedBytes[k][m] != null) { //if not where a parity bit was
        				byteString += decodedBytes[k][m].toString(); //add this bit to the binary string
        			}
        		}
        		//add the decoded byte to the array, casting it back to a byte. We use the substring method to skip the first 3 bits because they are always 0
        		outputArray[k] = (byte)Integer.parseInt(byteString.substring(3), 2);; 
        		byteString = ""; //reset byteString to empty string
        	}
        	//write all decoded bytes to the output file
			FileOutputStream fos = new FileOutputStream(outFileName); //open output stream on output file
			fos.write(outputArray); //write all bytes to file
			fos.close(); //close stream
        } catch (IOException e) { //catch exception if input file can't be opened
        	System.out.println("Error, couldn't open input file: " + inFileName);
        }
    }    /**
     * 
     * @param bitString The array of bits to be encoded
     * @return The encoded bits in the form of a string
     * 
     * This method takes an array of bits, with parity bits already added, and sets those parity bits to the correct values
     * Each parity bit checks a different combination of bits, and is set to either 1 or 0 to make the total number of 1s even. 
     */
	public static String encode(Bit[] bitString) {
	  for (int i = 1; i < bitString.length; i*=2) { //for each parity bit
	      int total = 0; //total number of 1s
	      if (bitString[i-1] instanceof ParityBit) { //double check that the bit in this location is in fact a parity bit
	         for (int j=0; j<bitString.length; j++) { //for each bit in the array
	        	 //if the bit is not a parity bit, and the location is one that should be checked by the current parity bit
	            if ((((j+1) & i) == i) && !(bitString[j] instanceof ParityBit)) {  
	            	total+=bitString[j].data; //get the data from that bit and add it to the total
	            }
	         }
	      }
	      if (total % 2 != 0) //if the total of the bits checked by this parity is odd, flip it to a 1
      		bitString[i-1].flip();
	   }
	  //convert the bit array into a string to be returned
	  String returnVal = ""; 
	  for (int k = 0; k<bitString.length; k++) {
		  returnVal+=bitString[k].toString(); //add each bit to the string
	  }
	  return returnVal;
	}
	/**
	 * 
	 * @param bitString the array of bits to be decoded
	 * @return The array of bits, with any single bit errors fixed
	 * 
	 * This method decodes a bit array. If there is a single bit error in the array, this method will correct that. 
	 */
   public static Bit[] decode(Bit[] bitString) {
      int wrongBit = 0; //the position of the error bit, if one exists
      for (int i = 1; i < bitString.length; i*=2) { //for each position where there's a parity bit
         int total = 0; //total of the data this parity bit checks
         //if (bitString[i-1] instanceof ParityBit) { //double check that it's a parity bit
            for (int j=0; j<bitString.length; j++) { //for each bit in the array
            	//if this is a bit that should be checked by this parity bit
               if ((((j+1) & i) == i) && !(bitString[j] instanceof ParityBit)) { 
            	   total+=bitString[j].data; //add the data of this bit to the total
               }
            }
            if (total%2 != bitString[i-1].data) //if the total is odd but the parity bit is even, we know that something has been flipped
               wrongBit += i; //add index of bit to wrong bit
         }  
     // }
      if (wrongBit != 0) //if there is an error
         bitString[wrongBit-1].flip(); //flip the bit that is incorrect
      return bitString; //return corrected bit array
   }
   /**
    * 
    * @param bitString The bit array, which the parity bits are to be added to
    * @return The bit array with parity bits added
    * 
    * Takes an array of bits and adds parity bits to it.
    * If the array is longer than 4 bits, 4 parity bits are added, otherwise 3 are added.
    */
   public static Bit[] addParityBits(Bit[] bitString) {
      Bit[] newBitString; //create new bit array to be returned
      int counter = 0; //counter to keep track of how many bits have been added to new array
      if (bitString.length > 4) { //if bit array is longer than 4 bits, then the new array will be 15 because of the hamming 15 11 code
         newBitString = new Bit[15];
      }
      else { //otherwise, it will be an array of length 7 for the hamming 7 4 code
         newBitString = new Bit[7];
      }
      for (int i = 1; i < newBitString.length; i*=2) { //for each place where there should be a parity bit
         newBitString[i-1] = new ParityBit(0); //create new parity bit in that location with a value of 0
      }
      for (int j = 0; j < newBitString.length; j++) { //for every element in the array
         if (!(newBitString[j] instanceof ParityBit)) { //if a parity bit is not already in that location, add the correct value
            newBitString[j] = bitString[counter]; 
            counter++; //increase counter to keep track of how many bits have been added
         }
      }
      return newBitString;
   }
   /**
    * 
    * @param bitString The bit array to remove the parity bits from
    * @return The bit array, with all places where parity bits existed set to null
    * 
    * Removes the parity bits from a given array
    * Simply sets them equal to null
    */
   public static Bit[] removeParityBits(Bit[] bitString) {
	   for (int i = 1; i < bitString.length; i*=2) { //for each location where there is a parity bit (i.e. every index that's a power of 2)
		   bitString[i-1] = null; //set that value to null
	   }
	   return bitString; //return the string
   }
}