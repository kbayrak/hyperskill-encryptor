package encryptdecrypt;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String function = "enc";
        String input = "";
        String output = "";
        String inInfo = "";
        String outInfo = "";
        String type = "";
        boolean data = false;
        boolean out = false;
        boolean in = false;
        int key = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                switch (args[i]) {
                    case "-mode":
                        if (!args[i + 1].startsWith("-")) {
                            function = args[i + 1];
                        }
                        break;
                    case "-key":
                        if (!args[i + 1].startsWith("-")) {
                            key = Integer.parseInt(args[i + 1]);
                        }
                        break;
                    case "-data":
                        if (!args[i + 1].startsWith("-")) {
                            input = args[i + 1];
                            data = true;
                        }
                        break;
                    case "-in":
                        if (!args[i + 1].startsWith("-")) {
                            inInfo = args[i + 1];
                            in = true;
                        }
                        break;
                    case "-out":
                        if (!args[i + 1].startsWith("-")) {
                            outInfo = args[i + 1];
                            out = true;
                        }
                        break;
                    case "-alg":
                        if (!args[i + 1].startsWith("-")) {
                            type = args[i + 1];
                        }
                        break;
                }

            }
        }

        Context context = new Context();


        if (type.equals("unicode")) {
            context.setAlgorithm(new Unicode());
        } else if (type.equals("shift")) {
            context.setAlgorithm(new Shift());
        }

        if (function.equals("enc")) {
            if (!data && in) {
                input = readOutput(inInfo);
                output = context.crypt(input, key);
                writeOutput(outInfo, output);
            } else {
                output = context.crypt(input, key);
                System.out.println(output);
            }
        } else if (function.equals("dec")) {
            if (!data && in ) {
                input = readOutput(inInfo);
                output = context.decrpyt(input, key);
                writeOutput(outInfo, output);
            } else {
                output = context.decrpyt(input, key);
                    System.out.println(output);
            }
        }

    }

    public static String readOutput(String fileName) {
        String temp = "";

        try {
            Scanner scanner = new Scanner(new File(fileName));
            temp = scanner.nextLine();
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public static void writeOutput(String fileName, String output) {
        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.write(output);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class Context {
    private AlgorithmStrategy algorithm;

    public void setAlgorithm(AlgorithmStrategy algorithm) {
        this.algorithm = algorithm;
    }

    public String crypt(String input, int key) {
        return this.algorithm.encrpytion(input, key);
    }

    public String decrpyt(String input, int key) {
        return this.algorithm.decrpytion(input, key);
    }

}

interface AlgorithmStrategy {

    String encrpytion (String input, int key);

    String decrpytion (String input, int key);
}

class Unicode implements AlgorithmStrategy {

    @Override
    public String encrpytion(String input, int key) {
        char[] inputArray = input.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int unicode = (int) inputArray[i] + key;
            char character = (char) unicode;
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    @Override
    public String decrpytion(String input, int key) {
        char[] inputArray = input.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            int unicode = (int) inputArray[i] - key;
            char character = (char) unicode;
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }
}

class Shift implements AlgorithmStrategy {

    private final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    private final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final char[] lowerChars = lowerCase.toCharArray();
    private final char[] upperChars = upperCase.toCharArray();

    @Override
    public String encrpytion(String input, int key) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isLowerCase(input.charAt(i))) {
                for (int j = 0; j < lowerChars.length; j++) {
                    if (input.charAt(i) == lowerChars[j]) {
                        stringBuilder.append(lowerChars[(j + key) % 26]);
                    }
                }

            } else if (Character.isUpperCase(input.charAt(i))) {
                for (int m = 0; m < upperChars.length; m++) {
                    if (input.charAt(i) == upperChars[m]) {
                        stringBuilder.append(upperChars[(m + key) % 26]);
                    }
                }
            } else {
                stringBuilder.append(input.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String decrpytion(String input, int key) {
        return encrpytion(input, 26 - (key % 26));
    }
}
