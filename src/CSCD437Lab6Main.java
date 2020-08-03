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
    private static byte[] salt= new byte[32];;
    private static String firstName;
    private static String lastName;
    private static int int1;
    private static int int2;
    private static File inpFile;
    private static File outFile;
    private static File errorFile;

    public static void main(String[] args) throws IOException {

        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));
        Scanner kb = new Scanner(System.in);

        nameInput(kb);
        intInput(kb);
        fileNameInput(kb);
        fileNameOutput(kb);

        passwordInput(kb);
        try {
            openOutputFile(kb);
        }catch(IOException e){
            out.write(e.getMessage());
            out.flush();
        }
    }

    //#1
    private static void nameInput(Scanner kb) {
        firstName = nameHelper(kb,"first");
        lastName = nameHelper(kb,"Last");
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
        int1 = intHelper(kb, "first");
        int2= intHelper(kb, "second");
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

    //#3
    private static void fileNameInput(Scanner kb) {
        String fileNameIn = null;
        inpFile = null;
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
    private static void fileNameOutput(Scanner kb) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));
        String fileNameOut = null;
        outFile = null;
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
                        out.write(e.getMessage());
                        out.flush();
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
    private static void passwordInput(Scanner kb) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));


        File passwordFile = new File("Password.txt");
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("Password.txt"));
        FileInputStream fileReader = new FileInputStream(passwordFile);

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
            out.write(e.getMessage());
            out.flush();
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

    private static File makePasswordFile(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));
        File returnFile = null;
        File passwordFile = new File(fileName);

        if(!passwordFile.exists()) { // Checks to make sure the file exists
            try { // If the file name works, but the file does not exist, will try to create the file to use.
                passwordFile.createNewFile();
                returnFile = passwordFile;
            } catch (IOException e) {
                out.write(e.getMessage());
                out.flush();
            }
        } else { // File already exists
            returnFile = passwordFile;
        }
        out.close();
        return returnFile;
    }

    private static FileOutputStream makeFileWriter(String fileName) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));
        FileOutputStream fileWriter = null;

        try {
            fileWriter = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            out.write(e.getMessage());
            out.flush();
        }
        out.close();
        return fileWriter;
    }

    private static FileInputStream makeFileReader(File passwordFile) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("errors-java.txt"));
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(passwordFile);
        } catch (FileNotFoundException e) {
            out.write(e.getMessage());
            out.flush();
        }
        out.close();
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

    private static void storeFirstSaltHashHelper(String password, BufferedWriter fileWriter) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter( "errors-java.txt"));
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        SecretKeyFactory factory = null;
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            out.write(e.getMessage());
            out.flush();
        }
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            out.write(e.getMessage());
            out.flush();
        }
        try {
            System.out.println(Arrays.toString(hash));
            System.out.println(Arrays.toString(salt));
            fileWriter.write(String.valueOf(hash));
            fileWriter.flush();
        } catch (IOException e) {
            out.write(e.getMessage());
            out.flush();
        }

        out.close();
    }

    private static byte[] makeHashForSecondHelper(File passwordFile, FileInputStream reader, String password) throws IOException {

        BufferedWriter out = new BufferedWriter(new FileWriter( "errors-java.txt"));


        SecretKeyFactory factory = null;
        byte[] hash = null;
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 512);

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            out.write(e.getMessage());
            out.flush();
        }
        try {
            if (factory != null) {
                hash = factory.generateSecret(spec).getEncoded();
            }
        } catch (InvalidKeySpecException e) {
            out.write(e.getMessage());
            out.flush();
        }
        out.close();
        return hash;
    }


    public static byte[] getHashFromFileHelper(File passwordFile, FileInputStream reader) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter( "errors-java.txt"));
        byte [] hash = new byte[(int)passwordFile.length()];

        try {
            System.out.println("made it to get hash from file: "+reader.read(hash));
        } catch (IOException e) {
            out.write(e.getMessage());
            out.flush();
        }
        out.close();
        return hash;
    }

    //#7
    private static void openOutputFile(Scanner kb) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter( "errors-java.txt"));
        try {
            BufferedWriter fOut = new BufferedWriter(new FileWriter(outFile));
            fOut.write(firstName + " ");
            fOut.flush();
            fOut.write(lastName + "\n");
            fOut.flush();
            fOut.write(StrictMath.addExact(int1, ((long)int2)) + "\n");
            fOut.flush();
            fOut.write(StrictMath.multiplyFull(int1, int2) + "\n");
            fOut.flush();
            Scanner inf = new Scanner(inpFile);
            while(inf.hasNext()) {
                fOut.write(inf.nextLine() + "\n");
            }

            fOut.close();
        } catch (IOException e) {
            System.out.println("failed?");
            out.write(e.getMessage());
            out.flush();
        }
        out.close();
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