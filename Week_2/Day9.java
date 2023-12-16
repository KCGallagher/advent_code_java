package Week_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Day9 {

    public static ArrayList<Integer> getElementWiseDifference(int[] lineInts) {
        ArrayList<Integer> diff = new ArrayList<>();
        for (int i = 0; i < lineInts.length - 1; i++) {
            diff.add(lineInts[i + 1] - lineInts[i]);
        }
        return diff;
    }

    public static int getAdjValue(List<Integer> lineInts, Boolean forwardsDirection) {

        List<List<Integer>> listRecord = new ArrayList<>();
        listRecord.add(lineInts);

        List<Integer> workingList = lineInts;
        while (!workingList.stream().allMatch(i -> i == 0)) {
            workingList = getElementWiseDifference(workingList.stream().mapToInt(i -> i).toArray());
            listRecord.add(workingList);
        }

        for (int i = listRecord.size() - 2; i >= 0; i--) {

            List<Integer> currList = listRecord.get(i);
            List<Integer> prevList = listRecord.get(i + 1);

            if (forwardsDirection) {
                int newVal = currList.get(currList.size() - 1) + prevList.get(prevList.size() - 1);
                listRecord.get(i).add(newVal);
            } else {
                int newVal = currList.get(0) - prevList.get(0);
                listRecord.get(i).add(0, newVal);
            }
        }
        return forwardsDirection ? listRecord.get(0).get(listRecord.get(0).size() - 1) : listRecord.get(0).get(0);
    }

    public static void main(String[] args) throws FileNotFoundException {
        int nextTermsSum = 0;
        int prevTermsSum = 0;

        File myObj = new File("Week_2/Data/Day9.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String[] line = myReader.nextLine().split("[\\s]+");
            List<Integer> lineInts = Arrays.stream(line).mapToInt(Integer::parseInt).boxed()
                    .collect(Collectors.toList());

            nextTermsSum += getAdjValue(lineInts, true);
            prevTermsSum += getAdjValue(lineInts, false);
        }
        myReader.close();

        System.out.println("The sum of the next terms is " + nextTermsSum);
        System.out.println("The sum of the previous terms is " + prevTermsSum);
    }
}
