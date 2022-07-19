package logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;

public class HuffmanUnZipping {
    static PriorityQueue<TREE> treePriorityQueue = new PriorityQueue<TREE>();
    static int[] frequency = new int[300];
    static String[] myStrings = new String[300]; // INT TO CODE
    static String[] biteToString = new String[300]; // INT TO BIN
    static String wholeString; // THE BIG STRING
    static String temp; // TEMPORARY STRING
    static int extraBits; // EXTRA BITS ADDED AT THE LAST TO MAKE THE FINAL ZIP
    // CODE MULTIPLE OF 8
    static int putIt; //
    static int counterOfFrequencies; // NUMBER OF frequencies available

    static class TREE implements Comparable<TREE> {
        TREE leftChild;
        TREE rightChild;
        public String deb;
        public int Bite;
        public int frequency;

        public int compareTo(TREE T) {
            if (this.frequency < T.frequency) return -1;
            else if (this.frequency > T.frequency) return 1;
            else return 0;
        }
    }

    static TREE Root;

    //freeing the memory
    public static void initHuffmanUnZipping() {
        if (Root != null) dfsToFreeMemory(Root);
        for (int i = 0; i < 300; i++)
            frequency[i] = 0;
        for (int i = 0; i < 300; i++)
            myStrings[i] = "";
        treePriorityQueue.clear();
        wholeString = ""; // THE BIG STRING
        temp = ""; // TEMPORARY STRING
        extraBits = 0; // EXTRA BITS ADDED AT THE LAST TO MAKE THE FINAL ZIP CODE
        // MULTIPLE OF 8
        putIt = 0; //
        counterOfFrequencies = 0;
    }
    //DFS to free memory

    public static void dfsToFreeMemory(TREE now) {

        if (now.leftChild == null && now.rightChild == null) {
            now = null;
            return;
        }
        if (now.leftChild != null)
            dfsToFreeMemory(now.leftChild);
        if (now.rightChild != null)
            dfsToFreeMemory(now.rightChild);
    }

    //DFS to make the codes
    public static void dfsToMakeNode(TREE now, String myString) {
        now.deb = myString;
        if ((now.leftChild == null) && (now.rightChild == null)) {
            myStrings[now.Bite] = myString;
            return;
        }
        if (now.leftChild != null)
            dfsToMakeNode(now.leftChild, myString + "0");
        if (now.rightChild != null)
            dfsToMakeNode(now.rightChild, myString + "1");
    }
    //Making all the nodes in a priority Q making the tree

    public static void makeNode() {
        counterOfFrequencies = 0;
        for (int i = 0; i < 300; i++) {
            if (frequency[i] != 0) {
                TREE tempTree = new TREE();
                tempTree.Bite = i;
                tempTree.frequency = frequency[i];
                tempTree.leftChild = null;
                tempTree.rightChild = null;
                treePriorityQueue.add(tempTree);
                counterOfFrequencies++;
            }

        }

        if (counterOfFrequencies == 0) return;
        else if (counterOfFrequencies == 1) {
            for (int i = 0; i < 300; i++)
                if (frequency[i] != 0) {
                    myStrings[i] = "0";
                    break;
                }
            return;
        }
        TREE tempTree1, tempTree2;
        while (treePriorityQueue.size() != 1) {
            TREE temp = new TREE();
            tempTree1 = treePriorityQueue.poll();
            tempTree2 = treePriorityQueue.poll();
            temp.leftChild = tempTree1;
            temp.rightChild = tempTree2;
            temp.frequency = tempTree1.frequency + tempTree2.frequency;
            treePriorityQueue.add(temp);
        }
        Root = treePriorityQueue.poll();
    }

    //reading the frequency from "codes.txt"  to updating string
    public static void readFrequencies(String stringOfCodes) {

        File inputFile = new File(stringOfCodes);
        int temp;
        Byte bite;
        try {
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            counterOfFrequencies = dataInputStream.readInt();

            for (int i = 0; i < counterOfFrequencies; i++) {
                bite = dataInputStream.readByte();
                temp = dataInputStream.readInt();
                frequency[convertBiteToInt(bite)] = temp;
            }
            dataInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        // making corresponding nodes
        makeNode();
        // dfs to make the codes
        if (counterOfFrequencies > 1) dfsToMakeNode(Root, "");

        for (int i = 0; i < 256; i++) {
            if (myStrings[i] == null) myStrings[i] = "";
        }
        inputFile = null;
    }

    //int to bin string conversion code creating
    public static void intToBinaryConversion() {
        String temp;
        for (int i = 0; i < 256; i++) {
            biteToString[i] = "";
            int j = i;
            while (j != 0) {
                if (j % 2 == 1)
                    biteToString[i] += "1";
                else
                    biteToString[i] += "0";
                j /= 2;
            }
            temp = "";
            for (j = biteToString[i].length() - 1; j >= 0; j--) {
                temp += biteToString[i].charAt(j);
            }
            biteToString[i] = temp;
        }
        biteToString[0] = "0";
    }

    //got yes means temp is a valid code and putIt is the code's corresponding val
    public static boolean got() {
        for (int i = 0; i < 256; i++) {
            if (myStrings[i].compareTo(temp) == 0) {
                putIt = i;
                return true;
            }
        }
        return false;

    }

    //byte to int conversion
    public static int convertBiteToInt(Byte bite) {
        int result = bite;
        if (result < 0) {
            result = ~bite;
            result = result + 1;
            result = result ^ 255;
            result += 1;
        }
        return result;
    }

    //convert any string into eight digit string
    public static String makeEightDigitString(String myString) {
        String result = "";
        int len = myString.length();
        for (int i = 0; i < (8 - len); i++)
            result += "0";
        result += myString;
        return result;
    }

    //unzipping function
    public static void readBinary(String zipFileName, String unZipFileName) {
        File zipFile = null, unZipFile = null;
        int bite;
        boolean isOk;
        Byte Bite;
        wholeString = "";
        zipFile = new File(zipFileName);
        unZipFile = new File(unZipFileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(unZipFile);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            try {
                counterOfFrequencies = dataInputStream.readInt();
                System.out.println(counterOfFrequencies);
                for (int i = 0; i < counterOfFrequencies; i++) {
                    Bite = dataInputStream.readByte();
                    int j = dataInputStream.readInt();

                }
                extraBits = dataInputStream.readInt();
                System.out.println(extraBits);

            } catch (EOFException eof) {
                System.out.println("End of File");
            }

            while (true) {
                try {
                    Bite = dataInputStream.readByte();
                    bite = convertBiteToInt(Bite);
                    wholeString += makeEightDigitString(biteToString[bite]);

                    while (true) {
                        isOk = true;
                        temp = "";
                        for (int i = 0; i < wholeString.length() - extraBits; i++) {
                            temp += wholeString.charAt(i);
                            if (got()) {
                                dataOutputStream.write(putIt);
                                isOk = false;
                                String s = "";
                                for (int j = temp.length(); j < wholeString.length(); j++) {
                                    s += wholeString.charAt(j);
                                }
                                wholeString = s;
                                break;
                            }
                        }

                        if (isOk) break;
                    }
                } catch (EOFException eof) {
                    System.out.println("End Of File");
                    break;
                }
            }
            fileOutputStream.close();
            dataOutputStream.close();
            fileInputStream.close();
            dataInputStream.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

        zipFile = null;
        unZipFile = null;
    }

    public static void beginHuffmanUnZipping(String argument) {
        initHuffmanUnZipping();
        readFrequencies(argument);
        intToBinaryConversion();
        int length = argument.length();
        String arg2 = argument.substring(0, length - 11);
        readBinary(argument, arg2);
        initHuffmanUnZipping();
    }

}