package Week_1;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {

    public static long[] extractSeedNums(String text) {
        String seedText = text.split("[:]")[1].strip();
        String[] seedNums = seedText.split("\\s+");
        return Arrays.stream(seedNums).mapToLong(Long::parseLong).toArray();
    }

    public static long[] mapSeedNums(long[] seedNums, List<List<long[]>> maps) {
        for (int i = 0; i < seedNums.length; i++) {
            for (List<long[]> map : maps) {
                for (long[] row : map) {
                    if ((row[1] <= seedNums[i]) && (seedNums[i] < row[1] + row[2])) {
                        seedNums[i] = row[0] + (seedNums[i] - row[1]);
                        break;
                    }
                }
            }
        }
        return seedNums;
    }

    public static long[] mapSeedRanges(long[] seedNums, List<List<long[]>> maps) {
        // Brute force approach - only took ~4 minutes to run
        List<Long> minPerRange = new ArrayList<Long>();
        for (int i = 0; i < seedNums.length; i += 2) {
            long min_value = Long.MAX_VALUE;
            for (long n = seedNums[i]; n < seedNums[i] + seedNums[i + 1]; n++) {
                long seedNum = n;
                for (int j = 0; j < maps.size(); j++) {
                    List<long[]> map = maps.get(j);
                    for (long[] row : map) {
                        if ((row[1] <= seedNum) && (seedNum < row[1] + row[2])) {
                            seedNum = row[0] + (seedNum - row[1]);
                            if (j == maps.size() - 1) { // Last map (Location)
                                min_value = Math.min(min_value, seedNum);
                            }
                            break;
                        }
                    }
                    if (j == maps.size() - 1) { // In case value unchanged in final map
                        min_value = Math.min(min_value, seedNum);
                    }
                }
            }
            minPerRange.add(min_value);
            // System.out.println((i/2)+1 + "/" + seedNums.length / 2 + " pairs completed");
        }
        return minPerRange.stream().mapToLong(i -> i).toArray();
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myObj = new File("Week_1/Data/Day5.txt");
        Scanner myReader = new Scanner(myObj);

        List<List<long[]>> maps = new ArrayList<>();
        long[] seedNums = new long[0];

        Boolean startNewMap = false;
        while (myReader.hasNextLine()) {

            String line = myReader.nextLine();
            if (line.contains("seeds")) {
                seedNums = extractSeedNums(line);
            }
            if (line.isBlank()) {
                continue;
            }
            if (line.contains("map:")) {
                startNewMap = true;
                continue;
            }

            if (line.matches("[0-9\\s]+")) {
                long[] nums = Arrays.stream(line.strip().split("\\s+")).mapToLong(Long::parseLong).toArray();
                if (startNewMap) {
                    List<long[]> new_map = new ArrayList<long[]>();
                    new_map.add(nums);
                    maps.add(new_map);
                    startNewMap = false;
                } else {
                    maps.get(maps.size() - 1).add(nums);
                }
            }
        }
        myReader.close();

        // Make deep copy of seedNums so that original is not modified
        long[] seedNumsCopy = Arrays.copyOf(seedNums, seedNums.length);

        long[] finalSeedNums = mapSeedNums(seedNumsCopy, maps);
        System.out.println("Minimum seed number from list is " + Arrays.stream(finalSeedNums).min().getAsLong());

        long[] minRangeValues = mapSeedRanges(seedNums, maps);
        System.out.println("Minimum seed number from ranges is " + Arrays.stream(minRangeValues).min().getAsLong());
    }
}
