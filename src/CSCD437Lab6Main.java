import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CSCD437Lab6Main {

    /*
     *Author: Kevin Underwood, Jennifer Goodnight, Zachary Stuefen
     *Version 0.2.6
     */

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        //nameInput(kb);
        //intInput(kb);
        //fileNameInput(kb);
        //fileNameOutput(kb);
        passwordInput(kb);
        openOutputFile(kb);
    }

    //#1
    private static void nameInput(Scanner kb) {
        String firstName = nameHelper(kb,"first");
        String lastName = nameHelper(kb,"Last");
        System.out.println("First name : "+firstName);
        System.out.println("Last name : "+lastName);
    }

    private static String nameHelper(Scanner kb, String type) {
        System.out.println("Please enter your "+type+" name: ");
        while (!kb.hasNext("[a-zA-Z]{1,50}$"))
        {
            kb.nextLine(); //clear the invalid input before prompting again
            System.out.print("Please enter your "+type+" name: ");
        }
        return kb.next("[a-zA-Z]{1,50}$");
    }

    //#2
    private static void intInput(Scanner kb) {
        int int1 = intHelper(kb, "first");
        int int2= intHelper(kb, "second");;
        System.out.println("Integer one : "+int1);
        System.out.println("Integer two : "+int2);
        kb.nextLine();
    }

    private static int intHelper(Scanner kb, String type) {
        System.out.println("Please enter your "+type+" number inside the integer range (-2,147,483,648 to 2,147,483,647): ");
        while (!kb.hasNextInt())
        {
            kb.nextLine(); //clear the invalid input before prompting again
            System.out.print("Please enter your "+type+" number inside the integer range (-2,147,483,648 to 2,147,483,647): ");
        }
        return kb.nextInt();
    }

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
        String fileName = "Password.txt";
        File passwordFile = makePasswordFile(fileName);
        FileOutputStream fileWriter = makeFileWriter(fileName);
        FileInputStream fileReader = makeFileReader(passwordFile);

        Boolean match = false;
        String firstPassword, secondPassword;

        while(!match) {
            firstPassword = passwordInputHelper(kb, "");
            storeFirstSaltHashHelper(firstPassword, fileWriter);

            secondPassword = passwordInputHelper(kb, "re-");

            match = verifyHashesMatch(getHashFromFileHelper(passwordFile, fileReader), makeHashForSecondHelper(passwordFile, fileReader, secondPassword));
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        passwordFile.delete();
    }

    private static String passwordInputHelper(Scanner kb, String type) {
        System.out.println("Please "+type+"enter your password: ");
        while (!kb.hasNext(".{1,2147483646}$")) {
            kb.nextLine(); //clear the invalid input before prompting again
            System.out.print("Please "+type+"enter your password: ");
        }
        return kb.next(".{1,2147483646}$");
    }

    private static File makePasswordFile(String fileName) {
        File returnFile = null;
        File passwordFile = new File(fileName);

        if(!passwordFile.exists()) { // Checks to make sure the file exists
            try { // If the file name works, but the file does not exist, will try to create the file to use.
                passwordFile.createNewFile();
                returnFile = passwordFile;
            } catch (IOException e) {
                System.out.println("Unable to create file, please try again or enter an already existing file.");
            }
        } else { // File already exists
            returnFile = passwordFile;
        }

        return returnFile;
    }

    private static FileOutputStream makeFileWriter(String fileName) {
        FileOutputStream fileWriter = null;

        try {
            fileWriter = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileWriter;
    }

    private static FileInputStream makeFileReader(File passwordFile) {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(passwordFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private static boolean verifyHashesMatch(byte [] firstHash, byte[] secondHash) {
        if(Arrays.equals(firstHash, secondHash)) {
            System.out.println("Passwords match");
            return true;
        }
        else if(!Arrays.equals(firstHash, secondHash)) {
            System.out.println("Passwords don't match");
            return false;
        }
        else {
            System.out.println("verifyPasswordsMatch returning unexpected output");
            return false;
        }
    }

    private static void storeFirstSaltHashHelper(String password, FileOutputStream fileWriter) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        SecretKeyFactory factory = null;
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.write(salt);
            fileWriter.write(hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] makeHashForSecondHelper(File passwordFile, FileInputStream reader, String password) {
        byte [] salt = getSaltFromFileHelper(passwordFile, reader);
        SecretKeyFactory factory = null;
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return hash;
    }

    public static byte[] getSaltFromFileHelper(File passwordFile, FileInputStream reader) {
        byte [] salt = new byte[(int)passwordFile.length()];

        try {
            reader.read(salt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salt;
    }

    public static byte[] getHashFromFileHelper(File passwordFile, FileInputStream reader) {
        byte [] hash = new byte[(int)passwordFile.length()];

        try {
            reader.read(); //TODO this should read over salt but it DOESNT yet
            reader.read(hash);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }

    //#7
    private static void openOutputFile(Scanner kb) {

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
