package logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;




public class RandomString {
    private static int n;
    private static File resourcesIn;
    private static File resourcesOut;
    public static void main(String[] args) throws IOException{
        scan();
        FileWriter inWriter , outWriter;
        StringBuilder inBuilder , outBuilder;
        Random random = new Random();
        File inFile , outFile;

        for(int i = 0; i < n; i++){
            inFile = new File(resourcesIn, i +".txt");
            outFile = new File(resourcesOut, i +".txt");
            if(!inFile.createNewFile() || !outFile.createNewFile()){
                continue;
            }
            inWriter = new FileWriter(inFile);
            inBuilder = new StringBuilder();
            for(int j = 0; j< random.nextInt(999951)+50; j++){
                int code = random.nextInt(128); // 10 digits and 26 letters
                if (code != '\0' && code != '\b'){
                    inBuilder.append((char) code);
                }

            }
            inWriter.write(inBuilder.toString());
            inWriter.close();
        }
    }
    private static void scan(){
        System.out.print("Enter number of tests: ");
        Scanner scanner = new Scanner(System.in);
        n = Integer.parseInt(scanner.nextLine());
        scanner.close();
        resourcesIn = new File("src/main/resources/tests/in/");
        if(!resourcesIn.mkdir())
            for(File file: Objects.requireNonNull(resourcesIn.listFiles()))
                file.delete();
        resourcesOut = new File("src/main/resources/tests/out/");
        if(!resourcesOut.mkdir())
            for(File file: Objects.requireNonNull(resourcesOut.listFiles()))
                file.delete();
    }
}
/*
char codes
===============
digits: 48-57
capital letters: 65-90
small letters: 97-122
 */

