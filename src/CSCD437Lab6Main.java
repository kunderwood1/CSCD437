import java.io.File;
import java.util.Scanner;

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
        IntInput(kb);
        FileInput(kb);
        PasswordInput(kb);
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

    //#3
    private static void fileNameInput(Scanner kb) {
        System.out.println("Please enter the name of your input file: ");

    }

    //#4
    private static void fileNameOutput(Scanner kb) {
        System.out.println("Please enter the name of your output file: ");
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
}
