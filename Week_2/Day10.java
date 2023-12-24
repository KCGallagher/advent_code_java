package Week_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Day10 {

    public static void printNestedArray(ArrayList<char[]> arr) {
        for (char[] array : arr) {
            for (char c : array) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static Boolean isPipe(char c) {
        String c_str = Character.toString(c);
        return c_str.matches("[7FLJ|-]");
    }

    public static ArrayList<char[]> convertToNestedCharArray(ArrayList<String> arr) {
        ArrayList<char[]> nestedArr = new ArrayList<>();

        for (String str : arr) {
            char[] charArr = str.toCharArray();
            nestedArr.add(charArr);
        }
        return nestedArr;
    }

    public static int[] findChar(ArrayList<char[]> arr, char x) {
        int[] startIndices = new int[2];
        for (int i = 0; i < arr.size(); i++) {
            char[] row = arr.get(i);
            for (int j = 0; j < row.length; j++) {
                if (row[j] == x) {
                    startIndices[0] = i;
                    startIndices[1] = j;
                    return startIndices;
                }
            }
        }
        return new int[] { -1, -1 };
    }

    public static ArrayList<int[]> generateNeighbourIndices(int[] pos) {
        ArrayList<int[]> neighbourIndices = new ArrayList<>();

        for (int i = pos[0] - 1; i <= pos[0] + 1; i += 2) {
            int[] sideNeighbour = { i, pos[1] };
            neighbourIndices.add(sideNeighbour);
        }

        for (int i = pos[1] - 1; i <= pos[1] + 1; i += 2) {
            int[] vertNeighbour = { pos[0], i };
            neighbourIndices.add(vertNeighbour);
        }
        return neighbourIndices;
    }

    public static HashMap<int[], String> getNextIndicesSearch(ArrayList<char[]> arr, int[] currIndices,
            Boolean overrideValue) {
        // For when the direction of the next step is not known (such as at the start)
        HashMap<int[], String> allowedValues = new HashMap<>();
        allowedValues.put(new int[] { 0, 1 }, "[7J-]");
        allowedValues.put(new int[] { 1, 0 }, "[JL|]");
        allowedValues.put(new int[] { 0, -1 }, "[LF-]");
        allowedValues.put(new int[] { -1, 0 }, "[F7|]");

        int[] nextIndices = new int[2];
        HashMap<int[], String> nextPipe = new HashMap<>();

        for (var entry : allowedValues.entrySet()) {
            int[] pos = entry.getKey();
            String regex = entry.getValue();

            int[] newIndices = { currIndices[0] + pos[0], currIndices[1] + pos[1] };

            try {
                char pipeChar = arr.get(newIndices[0])[newIndices[1]];
                if (regex.contains(Character.toString(pipeChar))) {
                    if (overrideValue) { // Replace char with units digit of step tracking
                        int newVal = Integer.parseInt(arr.get(currIndices[0])[currIndices[1]] + "", 10) + 1;
                        char unitsVal = (char) (newVal % 5 + '0');
                        arr.get(newIndices[0])[newIndices[1]] = unitsVal;
                    }
                    nextIndices = newIndices;
                    nextPipe.put(nextIndices, Character.toString(pipeChar));
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return nextPipe;
    }

    public static HashMap<int[], String> getNextIndicesStep(ArrayList<char[]> arr, HashMap<int[], String> currPipe) {
        // For when the next step is defined by the current state
        // Consider check both ends of current pipe, and pick end with new pipe, not int

        HashMap<String, int[][]> nextSteps = new HashMap<>();
        nextSteps.put("-", new int[][] { { 0, 1 }, { 0, -1 } });
        nextSteps.put("|", new int[][] { { 1, 0 }, { -1, 0 } });
        nextSteps.put("L", new int[][] { { -1, 0 }, { 0, 1 } });
        nextSteps.put("F", new int[][] { { 0, 1 }, { 1, 0 } });
        nextSteps.put("7", new int[][] { { 1, 0 }, { 0, -1 } });
        nextSteps.put("J", new int[][] { { 0, -1 }, { -1, 0 } });

        // Extract key and value from currIndices
        int[] currIndices = currPipe.keySet().iterator().next();
        String currValue = currPipe.get(currIndices);

        int[][] possibleSteps = nextSteps.get(currValue);

        HashMap<int[], String> nextPipe = new HashMap<>();
        for (int i = 0; i < 2; i++) {
            int[] newIndices = { currIndices[0] + possibleSteps[i][0], currIndices[1] + possibleSteps[i][1] };
            char newValue = arr.get(newIndices[0])[newIndices[1]];
            if (isPipe(newValue)) {
                nextPipe.put(newIndices, Character.toString(newValue));

                int newVal = Integer.parseInt(arr.get(currIndices[0])[currIndices[1]] + "", 10) + 1;
                char unitsVal = (char) (newVal % 5 + '0');
                arr.get(newIndices[0])[newIndices[1]] = unitsVal;
                return nextPipe;
            }
        }
        return nextPipe; // Should not reach here!
    }

    public static int getMaxLoopDist(ArrayList<char[]> arr, ArrayList<char[]> arrOriginal) {
        // Find indices of S in nested array
        int[] startIndices = findChar(arr, 'S');

        arr.get(startIndices[0])[startIndices[1]] = '0'; // Replace S with 0 (step 0)
        arrOriginal.get(startIndices[0])[startIndices[1]] = (char) '-'; // Replace S with - (connect loop)

        HashMap<int[], String> nextPipeA = getNextIndicesSearch(arr, startIndices, true);
        HashMap<int[], String> nextPipeB = getNextIndicesSearch(arr, startIndices, true);

        List<HashMap<int[], String>> currPipes = new ArrayList<>();
        currPipes.add(nextPipeA);
        currPipes.add(nextPipeB);

        // Run next index twice to get both dir of loop
        // After this, we can run it once per step as there should only be one
        // connecting pipe
        int stepNum = 1;
        outerLoop: while (true) {

            int[][] nextIndices = new int[2][2];

            for (int i = 0; i < 2; i++) {
                HashMap<int[], String> currPipe = currPipes.get(i);
                currPipes.set(i, getNextIndicesStep(arr, currPipe));
                try {
                    nextIndices[i] = currPipes.get(i).keySet().iterator().next();
                } catch (NoSuchElementException e) {
                    stepNum++;
                    break outerLoop;
                }
                nextIndices[i] = currPipes.get(i).keySet().iterator().next();
            }
            stepNum++;
        }
        int loopArea = countLoopArea(arr, arrOriginal);
        System.out.println("Loop area: " + loopArea);

        return stepNum;
    }

    public static int countLoopArea(ArrayList<char[]> arr, ArrayList<char[]> arrOriginal) {
        // Filter non-loop pipes out of original array
        for (int i = 0; i < arr.size(); i++) {
            char[] row = arr.get(i);
            for (int j = 0; j < row.length; j++) {
                if (isPipe(arrOriginal.get(i)[j]) && isPipe(row[j])) {
                    arrOriginal.get(i)[j] = '.';
                }
            }
        }

        int area = 0;
        for (int i = 0; i < arrOriginal.size(); i++) {
            String str = new String(arrOriginal.get(i));
            str = str.replaceAll("F-*J", "|");
            str = str.replaceAll("L-*7", "|");

            boolean enclosed = false;
            for (int j = 0; j < str.length(); j++) {
                char c = str.charAt(j);
                if (c == '|') {
                    enclosed = !enclosed;
                } else if (c == '.' && enclosed) {
                    area++;
                }
            }
        }
        return area;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> inputArr = new ArrayList<String>();

        File myObj = new File("Week_2/Data/Day10.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            inputArr.add(data);
        }
        myReader.close();

        ArrayList<char[]> modifyArr = convertToNestedCharArray(inputArr);
        ArrayList<char[]> originalArr = convertToNestedCharArray(inputArr);

        int maxLoopDist = getMaxLoopDist(modifyArr, originalArr);
        System.out.println("Max loop distance: " + maxLoopDist);
    }
}
