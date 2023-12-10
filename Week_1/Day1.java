package Week_1;

import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Map;

public class Day1 {
    static Map<String, String> numberTexts = Map.ofEntries(
            Map.entry("zero", "0"), Map.entry("one", "1"), Map.entry("two", "2"), Map.entry("three", "3"),
            Map.entry("four", "4"), Map.entry("five", "5"), Map.entry("six", "6"), Map.entry("seven", "7"),
            Map.entry("eight", "8"), Map.entry("nine", "9"));
    static String name_regex = "(?=(" + String.join("|", numberTexts.keySet()) + "))";

    static int getIndexOfFirstFound(String text, String[] words) {
        return Arrays.stream(words)
                .mapToInt(text::indexOf)
                .filter(i -> i >= 0)
                .sorted()
                .findFirst()
                .orElse(-1);
    }

    public static String convertTextToNums(String text) {
        Pattern pattern = Pattern.compile(name_regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) { // Find the first match
            String key = matcher.group(1);
            String value = Day1.numberTexts.get(key);
            text = text.replace(key, key + value + key);

            String end_key = "*";
            String end_value = "*";
            while (matcher.find()) { // Update to get final match
                end_key = matcher.group(1);
                end_value = Day1.numberTexts.get(end_key);
            }
            if (end_key != "*") { // Another number was found
                text = text.replace(end_key, end_key + end_value + end_key);
            }
        }
        return text;
    }

    public static void main(String[] args) {
        Integer total = 0;
        Boolean check_textual_nums = true; // Set to false for Part A of problem
        try {
            File myObj = new File("Week_1/Data/Day1.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                if (check_textual_nums) {
                    data = convertTextToNums(data);
                }

                String numData = data.replaceAll("[^0-9]", "");
                Integer value = Integer.parseInt("" + numData.charAt(0) + numData.charAt(numData.length() - 1));
                total += value;
            }
            
            myReader.close();
            System.out.println("The total value is " + total);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
