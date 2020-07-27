import java.io.File;
import java.util.Scanner;


public class CSCD437Lab6Main {

    /*
    *
    *Author: Kevin Underwood
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



    private static void FileInput(Scanner kb) {
        System.out.println("Please enter the name of your input file: ");
        System.out.println("Please enter the name of your output file: ");
    }


    private static void nameInput(Scanner kb) {
        System.out.println("Please enter your first name: ");
        System.out.println("Please enter your Last name: ");
    }
    private static void IntInput(Scanner kb) {
        System.out.println("Please enter an integer value: ");
        System.out.println("Please enter another integer value: ");

    }

    private static void PasswordInput(Scanner kb) {
        File passwordFile = new File("Password.txt");
        System.out.println("Please enter your password: ");
        System.out.println("Please enter your password: ");
    }
    private static String Validate(String input) {
        return "";
    }
}
