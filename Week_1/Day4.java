package Week_1;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Arrays;

public class Day4 {
   
    public static int[] intersection(int[] a, int[] b) {
        return Arrays.stream(a)
                     .distinct()
                     .filter(x -> Arrays.stream(b).anyMatch(y -> y == x))
                     .toArray();
    }

    public static int scoreCard(int[] winningNums){
        return (int) Math.pow(2, winningNums.length - 1);
    }

    public static void main(String[] args) throws FileNotFoundException{
        File myObj = new File("Week_1/Data/Day4.txt");
        Scanner myReader = new Scanner(myObj);
        
        Integer total = 0;
        int currentCard = 0;
        int[] cardCopies = new int[203];
        
        while (myReader.hasNextLine()) {
            currentCard++;
            cardCopies[currentCard - 1] += 1;

            String[] line = myReader.nextLine().split(":")[1].strip().split("[|]");
            int[] cardNums = Arrays.stream(line[0].strip().split("\s+")).mapToInt(Integer::parseInt).toArray();
            int[] myNums = Arrays.stream(line[1].strip().split("\s+")).mapToInt(Integer::parseInt).toArray();

            int[] winningNums = intersection(myNums, cardNums);
            total += scoreCard(winningNums);

            for (int i = 0; i < winningNums.length; i++){
                cardCopies[currentCard + i] += cardCopies[currentCard - 1];
            }
        }
        
        myReader.close();
        System.out.println("The sum of bingo scores is " + total);
        System.out.println("The total number of cards is " + Arrays.stream(cardCopies).sum());

    }
}
