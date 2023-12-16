package Week_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    public static long lcmOfArray(long[] array) {
        long lcm = array[0];
        for (int i = 1; i < array.length; i++) {
            lcm = lcm(lcm, array[i]);
        }
        return lcm;
    }

    public static long countRoutes(HashMap<String, String> nodes, String directions, String start, String end) {
        // This assumes that each cycle is disjoint, and only has one path (or paths have equal length)
        // This also assumes that each cycle length is divisible by the length of the directions string
        // Finally, this assume that the number of steps from start to end is equal to the cycle length
        Pattern pattern = Pattern.compile(start);

        List<String> startPos = nodes.keySet().stream()
                .filter(pattern.asPredicate())
                .collect(Collectors.toList());

        long[] loopLengths = new long[startPos.size()];

        for (int i = 0; i < startPos.size(); i++) {
            long count = 0;
            String current = startPos.get(i);
            while (!current.matches(end)) {

                int dir_index = (int) (count % directions.length());
                String direction = directions.substring(dir_index, dir_index + 1);
                String[] next_options = nodes.get(current).split(",");

                if (direction.equals("L")) {
                    current = next_options[0];
                } else if (direction.equals("R")) {
                    current = next_options[1];
                } else {
                    throw new IllegalArgumentException("Invalid direction: " + direction);
                }
                count++;
            }
            loopLengths[i] = count;

        }
        return lcmOfArray(loopLengths);
    }

    public static void main(String[] args) throws FileNotFoundException {

        HashMap<String, String> nodes = new HashMap<>();

        File myObj = new File("Week_2/Data/Day8.txt");
        Scanner myReader = new Scanner(myObj);

        String directions = myReader.nextLine().strip();
        myReader.nextLine(); // Skip blank line

        while (myReader.hasNextLine()) {
            String[] line = myReader.nextLine().split("=");
            String key = line[0].strip();
            String value = line[1].strip().replaceAll("[()\s+]", "");
            nodes.put(key, value);
        }

        long stepNum = countRoutes(nodes, directions, "AAA", "ZZZ");
        System.out.println("The number of steps for a human is " + stepNum);

        long stepNumGhost = countRoutes(nodes, directions, "[A-Z0-9][A-Z0-9]A", "[A-Z0-9][A-Z0-9]Z");
        System.out.println("The number of steps for a ghost is " + stepNumGhost);

        myReader.close();
    }
}
