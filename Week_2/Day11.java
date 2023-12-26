package Week_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Day11 {

    public static void printNestedArray(ArrayList<ArrayList<Character>> arr) {
        for (ArrayList<Character> array : arr) {
            for (char c : array) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static ArrayList<ArrayList<Character>> expandUniverse(ArrayList<ArrayList<Character>> arr) {
        for (int i = 0; i < arr.size(); i++) {
            // If all elements of row are '.', add a new row of '.'
            if (arr.get(i).stream().allMatch(c -> c == '.')) {
                ArrayList<Character> newRow = new ArrayList<>(Collections.nCopies(arr.get(i).size(), '.'));
                arr.add(i, newRow);
                i++;  // Skip the new row
            }
        }

        for (int j = 0; j < arr.get(0).size(); j++) {
            // If all elements of column are '.', add a new column of '.'
            boolean allDots = true;
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).get(j) != '.') {
                    allDots = false;
                    break;
                }
            }
            if (allDots) {
                for (int i = 0; i < arr.size(); i++) {
                    arr.get(i).add(j, '.');
                }
                j++;  // Skip the new column
            }
        }
        return arr;
    }

    public static ArrayList<int[]> getGalaxyList(ArrayList<ArrayList<Character>> arr) {
        ArrayList<int[]> galaxyList = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            ArrayList<Character> row = arr.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == '#') {
                    int[] pos = { i, j };
                    galaxyList.add(pos);
                }
            }
        }
        return galaxyList;
    }

    public static int getPairSeparation(int[] pos1, int[] pos2) {
        int rowDiff = Math.abs(pos1[0] - pos2[0]);
        int colDiff = Math.abs(pos1[1] - pos2[1]);
        return rowDiff + colDiff;
    }

    public static int getSummedPairSeparation(ArrayList<int[]> galaxyList) {
        int total = 0;
        for (int i = 0; i < galaxyList.size(); i++) {
            for (int j = i + 1; j < galaxyList.size(); j++) {
                total += getPairSeparation(galaxyList.get(i), galaxyList.get(j));
            }
        }
        return total;
    }

    public static long getPairSeparation(int[] pos1, int[] pos2, ArrayList<ArrayList<Character>> arr, int expansion) {
        int rowCount = 0; int colCount = 0;
        int startRow = Math.min(pos1[0], pos2[0]);
        int endRow = Math.max(pos1[0], pos2[0]);
        int startCol = Math.min(pos1[1], pos2[1]);
        int endCol = Math.max(pos1[1], pos2[1]);

        for (int i =  startRow; i < endRow; i++) {
            if (arr.get(i).stream().allMatch(c -> c == '.')) {
                rowCount++; 
            }
        }

        for (int j = startCol; j < endCol; j++) {
            boolean allDots = true;
            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).get(j) != '.') {
                    allDots = false;
                    break;
                }
            }
            colCount += allDots ? 1 : 0;
        }
        long baseDist = getPairSeparation(pos1, pos2);
        return baseDist + (rowCount * (expansion - 1)) + (colCount * (expansion - 1));
    }

    public static long getSummedPairSeparation(ArrayList<ArrayList<Character>> arr, int expansion) {
        long total = 0;
        ArrayList<int[]> galaxyList = getGalaxyList(arr);
        for (int i = 0; i < galaxyList.size(); i++) {
            for (int j = i + 1; j < galaxyList.size(); j++) {
                total += getPairSeparation(galaxyList.get(i), galaxyList.get(j), arr, expansion);
            }
        }
        return total;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<ArrayList<Character>> inputArr = new ArrayList<>();

        File myObj = new File("Week_2/Data/Day11.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            ArrayList<Character> charList = new ArrayList<>();
            for (char c : data.toCharArray()) {
                charList.add(c);
            }
            inputArr.add(charList);
        }
        myReader.close();
        
        ArrayList<ArrayList<Character>> newArr = new ArrayList<>();
        for (ArrayList<Character> innerList : inputArr) {
            ArrayList<Character> copyList = new ArrayList<>(innerList);
            newArr.add(copyList);
        }
        
        ArrayList<int[]> galaxyList = getGalaxyList(expandUniverse(newArr));

        int sumDist = getSummedPairSeparation(galaxyList);
        System.out.println("Summed distance between all galaxies: " + sumDist);

        long newDist = getSummedPairSeparation(inputArr, 1000000);
        System.out.println("Summed distance (generic expansion) between all galaxies: " + newDist);
    }
}
