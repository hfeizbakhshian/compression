package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;

//if the frequency of a byte is more than 2^32 then there will be problem
public class HZipping {

	private static final PriorityQueue<TREE> minHeap = new PriorityQueue<>();
	private static final int[] frequencies = new int[300];
	private static final String[] ss = new String[300];
	private static byte bt;
	private static int cnt; // number of different characters

	// for keeping frequencies of all the bytes

	// main tree class

	private static class TREE implements Comparable<TREE> {
		TREE leftChild;
		TREE rightChild;
		public String deb;
		public int Bite;
		public int frequency;

		public int compareTo(TREE T) {
			return Integer.compare(this.frequency, T.frequency);
		}
	}

	private static TREE Root;

	/*******************************************************************************
	 * calculating frequency of file fName
	 ******************************************************************************/

	public static void calFrequency(String fName) {
		File file;
		byte bt;

		file = new File(fName);
		try {
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			while (true) {
				try {

					bt = data_in.readByte();
					frequencies[to(bt)]++;
				} catch (EOFException eof) {
					System.out.println("End of File");
					break;
				}
			}
			file_input.close();
			data_in.close();
		} catch (IOException e) {
			System.out.println("IO Exception =: " + e);
		}
	}

	/***********************************************************************************
	 * byte to binary conversion
	 ***********************************************************************************/
	public static int to(Byte b) {
		int ret = b;
		if (ret < 0) {
			ret = ~b;
			ret = ret + 1;
			ret = ret ^ 255;
			ret += 1;
		}
		return ret;
	}

	/**********************************************************************************
	 * freeing the memory
	 *********************************************************************************/
	public static void initHZipping() {
		int i;
		cnt = 0;
		if (Root != null)
			freeDfs(Root);
		for (i = 0; i < 300; i++)
			frequencies[i] = 0;
		for (i = 0; i < 300; i++)
			ss[i] = "";
		minHeap.clear();
	}

	/**********************************************************************************
	 * dfs to free memory
	 *********************************************************************************/
	public static void freeDfs(TREE now) {

		if (now.leftChild == null && now.rightChild == null) {
			return;
		}
		if (now.leftChild != null)
			freeDfs(now.leftChild);
		if (now.rightChild != null)
			freeDfs(now.rightChild);
	}

	/**********************************************************************************
	 * dfs to make the codes
	 *********************************************************************************/
	public static void dfs(TREE now, String st) {
		now.deb = st;
		if ((now.leftChild == null) && (now.rightChild == null)) {
			ss[now.Bite] = st;
			return;
		}
		if (now.leftChild != null)
			dfs(now.leftChild, st + "0");
		if (now.rightChild != null)
			dfs(now.rightChild, st + "1");
	}

	/*******************************************************************************
	 * Making all the nodes in a priority Q making the tree
	 *******************************************************************************/
	public static void makeNode() {
		int i;
		minHeap.clear();

		for (i = 0; i < 300; i++) {
			if (frequencies[i] != 0) {
				TREE Temp = new TREE();
				Temp.Bite = i;
				Temp.frequency = frequencies[i];
				Temp.leftChild = null;
				Temp.rightChild = null;
				minHeap.add(Temp);
				cnt++;
			}

		}
		TREE Temp1, Temp2;

		if (cnt == 0) {
			return;
		} else if (cnt == 1) {
			for (i = 0; i < 300; i++)
				if (frequencies[i] != 0) {
					ss[i] = "0";
					break;
				}
			return;
		}

		// will there b a problem if the file is empty
		// a bug is found if there is only one character
		while (minHeap.size() != 1) {
			TREE Temp = new TREE();
			Temp1 = minHeap.poll();
			Temp2 = minHeap.poll();
			Temp.leftChild = Temp1;
			Temp.rightChild = Temp2;
			Temp.frequency = Temp1.frequency + Temp2.frequency;
			minHeap.add(Temp);
		}
		Root = minHeap.poll();
	}

	/*******************************************************************************
	 * encrypting
	 *******************************************************************************/
	public static void encrypt(String fName) {
		File file;

		file = new File(fName);
		try {
			FileInputStream file_input = new FileInputStream(file);
			DataInputStream data_in = new DataInputStream(file_input);
			while (true) {
				try {

					bt = data_in.readByte();
					frequencies[bt]++;
				} catch (EOFException eof) {
					System.out.println("End of File");
					break;
				}
			}
			file_input.close();
			data_in.close();

		} catch (IOException e) {
			System.out.println("IO Exception =: " + e);
		}
	}

	/*******************************************************************************
	 * fake zip creates a file "fake-zip.txt" where puts the final binary codes
	 * of the real zipped file
	 *******************************************************************************/
	public static void fakeZip(String fname) {

		File filei, fileo;

		filei = new File(fname);
		fileo = new File("fakezipped.txt");
		try {
			FileInputStream file_input = new FileInputStream(filei);
			DataInputStream data_in = new DataInputStream(file_input);
			PrintStream ps = new PrintStream(fileo);

			while (true) {
				try {
					bt = data_in.readByte();
					ps.print(ss[to(bt)]);
				} catch (EOFException eof) {
					System.out.println("End of File");
					break;
				}
			}

			file_input.close();
			data_in.close();
			ps.close();

		} catch (IOException e) {
			System.out.println("IO Exception =: " + e);
		}

	}

	/*******************************************************************************
	 * real zip according to codes of fakezip.txt (fname)
	 *******************************************************************************/
	public static void realZip(String fname, String fname1) {
		File fileI, fileO;
		int i;
		byte btt;

		fileI = new File(fname);
		fileO = new File(fname1);

		try {
			FileInputStream file_input = new FileInputStream(fileI);
			DataInputStream data_in = new DataInputStream(file_input);
			FileOutputStream file_output = new FileOutputStream(fileO);
			DataOutputStream data_out = new DataOutputStream(file_output);

			data_out.writeInt(cnt);
			for (i = 0; i < 256; i++) {
				if (frequencies[i] != 0) {
					btt = (byte) i;
					data_out.write(btt);
					data_out.writeInt(frequencies[i]);
				}
			}
			long texbits;
			texbits = fileI.length() % 8;
			texbits = (8 - texbits) % 8;
			int exBits = (int) texbits;
			data_out.writeInt(exBits);
			while (true) {
				try {
					bt = 0;
					byte ch;
					for (exBits = 0; exBits < 8; exBits++) {
						ch = data_in.readByte();
						bt *= 2;
						if (ch == '1')
							bt++;
					}
					data_out.write(bt);

				} catch (EOFException eof) {
					int x;
					if (exBits != 0) {
						for (x = exBits; x < 8; x++) {
							bt *= 2;
						}
						data_out.write(bt);
					}

					exBits = (int) texbits;
					System.out.println("extrabits: " + exBits);
					System.out.println("End of File");
					break;
				}
			}
			data_in.close();
			data_out.close();
			file_input.close();
			file_output.close();
			System.out.println("output file's size: " + fileO.length());

		} catch (IOException e) {
			System.out.println("IO exception = " + e);
		}
		System.out.println(fileI.delete());
	}

	/*******************************************************************************/

	/*
	 * public static void main (String[] args) { initHzipping();
	 * CalFreq("in.txt"); // calculate the frequency of each digit MakeNode();
	 * // makeing corresponding nodes if(cnt>1) dfs(Root,""); // dfs to make the
	 * codes fakezip("in.txt"); // fake zip file which will have the binary of
	 * the input to fakezipped.txt file
	 * realzip("fakezipped.txt","in.txt"+".huffz"); // making the real zip
	 * according the fakezip.txt file initHzipping();
	 * 
	 * }
	 */

	public static void beginHZipping(String arg1) {
		initHZipping();
		calFrequency(arg1); // calculate the frequency of each digit
		makeNode(); // making corresponding nodes
		if (cnt > 1)
			dfs(Root, ""); // dfs to make the codes
		fakeZip(arg1); // fake zip file which will have the binary of the input
						// to fakezipped.txt file
		realZip("fakezipped.txt", arg1 + ".huffz"); // making the real zip
													// according the fakezip.txt
													// file
		initHZipping();
	}
}