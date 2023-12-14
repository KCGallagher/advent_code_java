package Week_1;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Arrays;

public class Day6 {

    public static long computeErrorMargin(long race_time, long record_dist) {
        long waysToBeatRecord = 0;
        for (long time = 0; time <= race_time; time++) {
            waysToBeatRecord += (time * (race_time - time) > record_dist) ? 1 : 0;
        }
        return waysToBeatRecord;
    }

    public static long computeErrorMarginSmart(long race_time, long record_dist) {
        long upper = (long) Math.floor((race_time + Math.sqrt(race_time*race_time - 4*record_dist)) / 2);
        long lower = (long) Math.ceil((race_time - Math.sqrt(race_time*race_time - 4*record_dist)) / 2);
        return upper - lower;
    }

    public static long computeErrorProduct(long[] timeValues, long[] distValues) {
        long errorMargin = 1;
        for (int i = 0; i < timeValues.length; i++) {
            errorMargin *= computeErrorMarginSmart(timeValues[i], distValues[i]);
        }
        return errorMargin;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myObj = new File("Week_1/Data/Day6.txt");
        Scanner myReader = new Scanner(myObj);

        String timeData = myReader.nextLine().split(":")[1].strip();
        long[] timeValues = Arrays.stream(timeData.split("\\s+")).mapToLong(Long::parseLong).toArray();
        long[] timeValueUnkerned = { Long.parseLong(timeData.replaceAll("[\s+]", "")) };

        String distData = myReader.nextLine().split(":")[1].strip();
        long[] distValues = Arrays.stream(distData.split("\\s+")).mapToLong(Long::parseLong).toArray();
        long[] distValueUnkerned = { Long.parseLong(distData.replaceAll("[\s+]", "")) };
        myReader.close();

        long errorProduct = computeErrorProduct(timeValues, distValues);
        long errorProductUnkerned = computeErrorProduct(timeValueUnkerned, distValueUnkerned);
        System.out.println("The total product of error margins is " + errorProduct);
        System.out.println("The error margin for single (unkerned) race is " + errorProductUnkerned);
    }
}
