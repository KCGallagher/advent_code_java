package Week_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Day3 {

    public static void printArr(ArrayList<String> arr) {
        for (String i : arr) {
            System.out.println(i);
        }
    }

    public static ArrayList<char[]> convertToNestedCharArray(ArrayList<String> arr) {
        ArrayList<char[]> nestedArr = new ArrayList<>();

        for (String str : arr) {
            char[] charArr = str.toCharArray();
            nestedArr.add(charArr);
        }
        return nestedArr;
    }

    public static ArrayList<int[]> generateNeighbourIndices(int i, int j0, int j1) {
        ArrayList<int[]> neighbourIndices = new ArrayList<>();

        int[] leftNeighbour = { i, j0 - 1 };
        neighbourIndices.add(leftNeighbour);

        int[] rightNeighbour = { i, j1 };
        neighbourIndices.add(rightNeighbour);

        for (int j_value = j0 - 1; j_value <= j1; j_value++) {
            int[] topNeighbour = { i - 1, j_value };
            neighbourIndices.add(topNeighbour);

            int[] bottomNeighbour = { i + 1, j_value };
            neighbourIndices.add(bottomNeighbour);
        }
        return neighbourIndices;
    }

    public static Boolean isCharSymbol(char c) {
        String c_str = Character.toString(c);
        return c_str.matches("[^0-9a-zA-Z.\s]");
    }

    public static void countPartNums(ArrayList<String> arr) {
        int partNumSum = 0;
        int gearRatioSum = 0;

        HashMap<List<Integer>, Integer> gearMap = new HashMap<>();

        // Convert array of strings into nested array of chars
        ArrayList<char[]> nestedArr = convertToNestedCharArray(arr);

        for (int i = 0; i < nestedArr.size(); i++) {
            for (int j = 0; j < nestedArr.get(i).length; j++) {
                if (Character.isDigit(nestedArr.get(i)[j])) {
                    // This is the first digit of a number
                    int j0 = j;

                    // Check if the next character is a digit
                    try {
                        while (Character.isDigit(nestedArr.get(i)[j + 1])) {
                            // Keep going until we hit a non-digit
                            j++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        // Do nothing
                    }
                    j++; // Capture the last digit

                    // Search for non digits next to the number
                    ArrayList<int[]> neighbourIndices = generateNeighbourIndices(i, j0, j);

                    for (int[] neighbourIndex : neighbourIndices) {

                        try {
                            if (isCharSymbol(nestedArr.get(neighbourIndex[0])[neighbourIndex[1]])) {
                                int partNum = Integer.parseInt(String.valueOf(nestedArr.get(i)).substring(j0, j));
                                partNumSum += partNum;

                                if (nestedArr.get(neighbourIndex[0])[neighbourIndex[1]] == '*') {
                                    // Convert the neighbourIndex to a list, as these have builtin equality checks
                                    List<Integer> neighbourIndexList = Arrays.stream(neighbourIndex).boxed()
                                            .collect(Collectors.toList());

                                    // Check if the neighbourIndex is already in the gearMap
                                    if (gearMap.containsKey(neighbourIndexList)) {
                                        gearRatioSum += gearMap.get(neighbourIndexList) * partNum;
                                        gearMap.remove(neighbourIndexList);
                                    } else {
                                        // Add the gear ratio to the gearMap
                                        gearMap.put(neighbourIndexList, partNum);
                                    }
                                }
                                break;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            // Do nothing
                        }
                    }
                }
            }
        }
        System.out.println("The sum of part numbers is " + partNumSum);
        System.out.println("The sum of gear ratios is " + gearRatioSum);
    }

    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<String> inputArr = new ArrayList<String>();

        File myObj = new File("Week_1/Data/Day3.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            inputArr.add(data);
        }
        myReader.close();

        countPartNums(inputArr);
    }
}
