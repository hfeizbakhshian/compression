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
public class HuffmanZipping {

    static PriorityQueue<TREE> priorityQueue = new PriorityQueue<TREE>();
    static int[] frequency = new int[300];
    static String[] myStringArray = new String[300];
    static int extraBits;
    static byte myByte;
    // number of different characters
    static int counterOfDifferentWords;

    // for keeping frequencies of all the bytes
    // main tree class

    static class TREE implements Comparable<TREE> {
        TREE leftChild;
        TREE rightChild;
        public String deb;
        public int Bite;
        public int frequency;

        public int compareTo(TREE T) {
            if (this.frequency < T.frequency) return -1;
            if (this.frequency > T.frequency) return 1;
            else return 0;
        }
    }

    static TREE root;
    //calculating frequency of file fileName

    public static void calculateFrequency(String fileName) {
        File file = null;
        Byte bite;

        file = new File(fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            while (true) {
                try {

                    bite = dataInputStream.readByte();
                    frequency[convertByteToBinary(bite)]++;
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            fileInputStream.close();
            dataInputStream.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }
    //byte to binary conversion
    public static int convertByteToBinary(Byte bite) {
        int result = bite;
        if (result < 0) {
            result = ~bite;
            result = result + 1;
            result = result ^ 255;
            result += 1;
        }
        return result;
    }
    //freeing the memory
    public static void initHuffmanZipping() {
        counterOfDifferentWords = 0;
        if (root != null)
            dfsToFreeMemory(root);
        for (int i = 0; i < 300; i++)
            frequency[i] = 0;
        for (int i = 0; i < 300; i++)
            myStringArray[i] = "";
        priorityQueue.clear();
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
    public static void DFS(TREE now, String myString) {
        now.deb = myString;
        if ((now.leftChild == null) && (now.rightChild == null)) {
            myStringArray[now.Bite] = myString;
            return;
        }
        if (now.leftChild != null)
            DFS(now.leftChild, myString + "0");
        if (now.rightChild != null)
            DFS(now.rightChild, myString + "1");
    }
    //Making all the nodes in a priority Q making the tree
    public static void makeNode() {
        priorityQueue.clear();

        for (int i = 0; i < 300; i++) {
            if (frequency[i] != 0) {
                TREE Temp = new TREE();
                Temp.Bite = i;
                Temp.frequency = frequency[i];
                Temp.leftChild = null;
                Temp.rightChild = null;
                priorityQueue.add(Temp);
                counterOfDifferentWords++;
            }

        }
        TREE Temp1, Temp2;

        if (counterOfDifferentWords == 0) return;
        else if (counterOfDifferentWords == 1) {
            for (int i = 0; i < 300; i++)
                if (frequency[i] != 0) {
                    myStringArray[i] = "0";
                    break;
                }
            return;
        }

        // will there b a problem if the file is empty
        // a bug is found if there is only one character
        while (priorityQueue.size() != 1) {
            TREE Temp = new TREE();
            Temp1 = priorityQueue.poll();
            Temp2 = priorityQueue.poll();
            Temp.leftChild = Temp1;
            Temp.rightChild = Temp2;
            Temp.frequency = Temp1.frequency + Temp2.frequency;
            priorityQueue.add(Temp);
        }
        root = priorityQueue.poll();
    }
    //encrypting
    public static void encrypting(String fileName) {
        File file = null;

        file = new File(fileName);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            while (true) {
                try {

                    myByte = dataInputStream.readByte();
                    frequency[myByte]++;
                } catch (EOFException eof) {
                    System.out.println("End Of File");
                    break;
                }
            }
            fileInputStream.close();
            dataInputStream.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }
    //fake zip creates a file "fakeZip.txt" where puts the final binary codes of the real zipped file
    public static void fakeZip(String fileName) {

        File inputFile, outputFile;

        inputFile = new File(fileName);
        outputFile = new File("fakeZipped.txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            PrintStream ps = new PrintStream(outputFile);

            while (true) {
                try {
                    myByte = dataInputStream.readByte();
                    ps.print(myStringArray[convertByteToBinary(myByte)]);
                } catch (EOFException eof) {
                    System.out.println("End Of File");
                    break;
                }
            }

            fileInputStream.close();
            dataInputStream.close();
            ps.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        inputFile = null;
        outputFile = null;

    }
    //real zip according to codes of fakeZip.txt (fileName)
    public static void realZip(String inputFileName, String outputFileName) {
        File inputFile, outputFile;
        int j = 10;
        Byte bite;

        inputFile = new File(inputFileName);
        outputFile = new File(outputFileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

            dataOutputStream.writeInt(counterOfDifferentWords);
            for (int i = 0; i < 256; i++) {
                if (frequency[i] != 0) {
                    bite = (byte) i;
                    dataOutputStream.write(bite);
                    dataOutputStream.writeInt(frequency[i]);
                }
            }
            long textBits;
            textBits = inputFile.length() % 8;
            textBits = (8 - textBits) % 8;
            extraBits = (int) textBits;
            dataOutputStream.writeInt(extraBits);
            while (true) {
                try {
                    myByte = 0;
                    byte character;
                    for (extraBits = 0; extraBits < 8; extraBits++) {
                        character = dataInputStream.readByte();
                        myByte *= 2;
                        if (character == '1')
                            myByte++;
                    }
                    dataOutputStream.write(myByte);

                } catch (EOFException eof) {
                    int temp;
                    if (extraBits != 0) {
                        for (temp = extraBits; temp < 8; temp++) {
                            myByte *= 2;
                        }
                        dataOutputStream.write(myByte);
                    }

                    extraBits = (int) textBits;
                    System.out.println("extraBits: " + extraBits);
                    System.out.println("End Of File");
                    break;
                }
            }
            dataInputStream.close();
            dataOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
            System.out.println("output file's size: " + outputFile.length());

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        inputFile.delete();
        inputFile = null;
        outputFile = null;
    }

    //begin
    public static void beginHuffmanZipping(String argument) {
        initHuffmanZipping();
        calculateFrequency(argument); // calculate the frequency of each digit
        makeNode(); // making corresponding nodes
        if (counterOfDifferentWords > 1)
            DFS(root, ""); // dfs to make the codes
        fakeZip(argument); // fake zip file which will have the binary of the input
        // to fakeZipped.txt file
        realZip("fakeZipped.txt", argument + ".huffmanzip"); // making the real zip
        // according the fakeZip.txt
        // file
        initHuffmanZipping();
    }
}