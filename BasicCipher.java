import java.util.*;

/*
Tyler Davis
ICSI-426

    This program encrypts a message with three forms of standard encryption:
    -   substitution
    -   transposition
    -   product

    It will take an input message and output the encrypted message

    Instructions for running:
        Insert source code into IntelliJ IDEA or Eclipse idea and run as normal. Inputs are taken through terminal.
 */

public class BasicCipher {

    public static void main(String[] args) {

        char[] letters = new char[200];

        char[] step1;
        char[] step2;
        char[] step3;

        String result;
        int counter = 0;

        //the keys are defined
        String key = "GYBNQKURP";
        int intkey = 3;

        Scanner sc = new Scanner(System.in);

        //takes in the string
        System.out.println("Insert your string to encrypt: ");
        result = sc.nextLine();

        //insert the letters into our cipher
        for (int i = 0; i < result.length(); i++) {
            letters[i] = result.charAt(i);
            counter++;
        }

        //take input for a 3 x 3 matrix (9 consecutive numbers) to be used
        //in our product cipher
        int[][] keyMatrix = new int[3][3];
        System.out.println("Enter the 3x3 key matrix (row-wise):");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                keyMatrix[i][j] = sc.nextInt();
            }
        }


        //perform the encryption steps
        /*
        step1 - Viginere Cipher
        step2 - Rail Fence Cipher
        step3 - Hill Cipher
         */

        step1 = substitution(letters, counter, key);
        step2 = transposition(step1, intkey, counter);
        step3 = product(step2, keyMatrix, counter);

        //prints out the resulting text
        for (int i = 0; i < result.length(); i++) {
            System.out.print(step3[i]);
        }
    }

    //mimics a Viginere Cipher
    public static char[] substitution(char[] input, int count, String key) {
        char[] ciphertextArray = new char[200];

        //initialize the ciphertest array
        for (int i = 0; i < 199; i++) {
            ciphertextArray[i] = ' ';
        }


        for (int i = 0, j = 0; i < count; i++) {

            //uppercase the plaintext matrix for calculation purposes
            char c = Character.toUpperCase(input[i]);

            //check to see if the character is within the bounds of the alphabet
            if (c < 'A' || c > 'Z')
                continue;

            //calculate the new ciphertext
            ciphertextArray[i] += (char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
            j = ++j % key.length();
        }

        return ciphertextArray;
    }

    //mimics a Rail Fence Cipher
    public static char[] transposition(char[] input, int key, int count) {

        //creating the matrix used to cipher the plaintext
        char[][] rail = new char[key][count];
        String output;

        //initializing the rail matrix ensuring to distinguish between
        //filled spaces and blank ones
        for (char[] row : rail) {
            Arrays.fill(row, '\n');
        }

        //determining the direction
        boolean dir_down = false;
        int row = 0, col = 0;

        //move the reader and initialize the rail matrix
        for (int i = 0; i < count; i++) {
            if (row == 0 || row == key - 1) {
                dir_down = !dir_down;
            }

            rail[row][col++] = input[i];

            if (dir_down) {
                row++;
            } else {
                row--;
            }
        }

        //build a string from the rail matrix
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < count; j++) {
                if (rail[i][j] != '\n') {
                    result.append(rail[i][j]);
                }
            }
        }

        //return the output
        output = result.toString();
        return output.toCharArray();
    }

    // Encrypts the message using the provided key matrix
    public static char[] product(char[] message, int[][] keyMatrix, int count) {
        StringBuilder encryptedText = new StringBuilder();

        //We will encrypt the message block by block
        for (int i = 0; i < count; i += 3) {

            String block = Arrays.toString(message).substring(i, i + 3);

            //initialize the plaintext and ciphertext matrices
            int[][] messageVector = new int[3][1];
            int[][] cipherMatrix = new int[3][1];

                //initializing the plaintext matrix
                for (int j = 0; j < 3; j++) {
                    messageVector[j][0] = block.charAt(j) - 'A'; // Convert characters to numeric values
                }

                // Multiplying the key matrix with the plaintext matrix and initializes the ciphertext matrix
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        cipherMatrix[j][0] += keyMatrix[j][k] * messageVector[k][0];
                    }
                    cipherMatrix[j][0] %= 26;
                }

                //build the encrypted message
                for (int j = 0; j < 3; j++) {
                    encryptedText.append((char) (cipherMatrix[j][0] + 'A')); // Convert numeric values back to characters
                }
            }

            //return encrypted method
            return encryptedText.toString().toCharArray();
        }
}