import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSCD437Lab6Main {

    /*
    *
    *Author: Kevin Underwood, Jennifer Goodnight, Zachary Stuefen
    *Version 0.1
    *
    */

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        nameInput(kb);
        intInput(kb);
        fileNameInput(kb);
        fileNameOutput(kb);
        passwordInput(kb);
        openOutputFile(kb);
        String str = validate("");
    }

    //#1
    private static void nameInput(Scanner kb) {
        System.out.println("Please enter your first name: ");
        System.out.println("Please enter your Last name: ");
    }

    //#2
    private static void intInput(Scanner kb) {
        System.out.println("Please enter an integer value: ");
        System.out.println("Please enter another integer value: ");
    }

    //#3 Made text.txt for testing.
    private static void fileNameInput(Scanner kb) {
        File inpFile = null;
        String fileNameIn = null;
        do {
            System.out.println("Please enter the name of your input file: ");
            fileNameIn = kb.nextLine();
            if(fileValidate(fileNameIn)) { // Checks if file name is valid
                inpFile = new File(fileNameIn);
            }
            if (inpFile == null || !inpFile.exists()) {
                System.out.println("Please try again.");
            }
        } while(inpFile == null || !inpFile.exists());
    }

    //#4
    private static void fileNameOutput(Scanner kb) {
        File outFile = null;
        String fileNameOut = null;
        do {
            System.out.println("Please enter the name of your output file: ");
            fileNameOut = kb.nextLine();

            if(fileValidate(fileNameOut)) { // Checks if file name is valid
                File f = new File(fileNameOut);

                if(!f.exists()) { // Checks to make sure the file exists
                    try { // If the file name works, but the file does not exist, will try to create the file to use.
                        f.createNewFile();
                        outFile = f;
                    } catch (IOException e) {
                        System.out.println("Unable to create file, please try again or enter an already existing file.");
                    }
                } else { // File already exists
                    outFile = f;
                }
            } else { // File is not validated.
                System.out.println("Please try again.");
            }
        } while(outFile == null);
    }

    //#5
    private static void passwordInput(Scanner kb) {
        File passwordFile = new File("Password.txt");
        System.out.println("Please enter your password: ");
        System.out.println("Please re-enter your password: ");
    }

    //#7
    private static void openOutputFile(Scanner kb) {

    }

    private static String validate(String input) {
        return "";
    }

    private static boolean fileValidate(String input) {
        if(input == null || input.isEmpty() || input.length() >= 233) // Maximum characters windows allows in a file name is 233, this includes the extension
            return false;

        String filePattern = "^[^\\\\|/|*|:|?|\"|<|>|\\|]*$";
        Pattern pattern = Pattern.compile(filePattern);
        Matcher match = pattern.matcher(input);
        boolean res = match.matches();

        return res;
    }
}
