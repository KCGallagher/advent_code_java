package Week_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Day12 {

    public static int[] generateConditionRecord(String status){
        if (status.startsWith(".")){
            status = status.replaceFirst("[.]+", "");
        }
        String[] errors = status.split("[.]+");
        
        int[] records = new int[errors.length];
        for (int i = 0; i < errors.length; i++) {
            if (errors[i].length() != 0){
                records[i] = errors[i].length();
            }
        }
        return records;
    }
    
    private static HashMap<String, Integer> memo = new HashMap<>();

    public static int countCombinations(String status, int[] records) {
        memo.clear();  // Reset memoization for each new line
        return countCombinationsHelper(status, 0, records);
    }

    private static String combineStatus(String status, int[] records) {
        String newStatus = status;
        for (int i = 0; i < records.length; i++) {
            newStatus += "_";
            newStatus += Integer.toString(records[i]);
        }
        return newStatus;
    }

    public static Map<String, int[]> simplifyState(String status, int[] record){
        int recordIndex = 0;

        int[] newRecord = Arrays.copyOf(record, record.length);
        String newStatus = status;
        newStatus = newStatus.replaceAll("[.]+", ".");  // Simplify records for memoization
        
        int i = 0;
        while (i < newStatus.length()) {
            if (newStatus.charAt(i) == '?'){
                break;
            }
            else if (newStatus.charAt(i) == '#'){
                int sectionLen = 1;
                while (i + sectionLen < newStatus.length() - 1 && newStatus.charAt(i + sectionLen) == '#'){
                    sectionLen ++;
                }

                if ((i == newStatus.length() - 1 || newStatus.charAt(i + sectionLen) == '.') 
                && (i == 0 || newStatus.charAt(i-1) == '.')){  // Complete phrase
                    if (newRecord.length == 1){
                        break;  // Cannot simplify further
                    }
                    else if ((sectionLen == newRecord[recordIndex])) {  // Matches record - can simplify
                        newRecord = Arrays.copyOfRange(newRecord, 1, newRecord.length);
                        newStatus = newStatus.substring(i + sectionLen);
                        i = 0; continue;
                    }
                    else {  // Does not match record; impossible status so return null instead
                        Map<String, int[]> emptyState = new HashMap<>();
                        emptyState.put("", new int[1]);
                        return emptyState;
                    } 
                }
                else{
                    break;  // Phrase not complete - cannot simplify with certainty
                }
            }
            i++;
        }
    
        Map<String, int[]> simplifiedState = new HashMap<>();
        simplifiedState.put(newStatus, newRecord);
        return simplifiedState;
    }

    private static Boolean checkSufficientOptions(String status, int[] record) {
        // If there are fewer # and ? characters than the sum of the record,
        // there are not enough options to satisfy record and we can end here
        int count = 0;
        for (char c : status.toCharArray()) {
            if (c == '?' || c == '#') {
                count++;
            }
        }
        int sum = 0;
        for (int num : record) {
            sum += num;
        }
        return count >= sum;
    }

    private static int countCombinationsHelper(String status, int index, int[] record) {
        if ((index == status.length()) || (!status.contains("?"))) {
            // Base case: reached the end of the status string, or no more "?" to replace
            return Arrays.equals(generateConditionRecord(status), record) ? 1 : 0;
        }
        if (!checkSufficientOptions(status, record)) {
            // Not enough options to satisfy record
            return 0;
        }
        
        String stringStatus = combineStatus(status, record);
        if (memo.containsKey(stringStatus)) {
            // If the result is already computed, return it
            return memo.get(stringStatus);
        }
    
        char currentChar = status.charAt(index);
        if (currentChar == '?') {
            // Recursive case: replace "?" with "." and "#" and count the combinations
            String replacedWithDot = status.substring(0, index) + "." + status.substring(index + 1);
            String replacedWithHash = status.substring(0, index) + "#" + status.substring(index + 1);
            int count = 0;

            Map<String, int[]> newStateDot = simplifyState(replacedWithDot, record);
            String newStatusDot = newStateDot.keySet().iterator().next();
            int newIndexDot = newStatusDot.indexOf("?", 0);
            count += (newStatusDot != "") ? countCombinationsHelper(newStatusDot, newIndexDot, newStateDot.get(newStatusDot)) : 0;

            Map<String, int[]> newStateHash = simplifyState(replacedWithHash, record);
            String newStatusHash = newStateHash.keySet().iterator().next();
            int newIndexHash = newStatusHash.indexOf("?", 0);
            count += (newStatusHash != "") ? countCombinationsHelper(newStatusHash, newIndexHash, newStateHash.get(newStatusHash)) : 0;
            
            memo.put(stringStatus, count);  
            return count;

        } else {
            // Recursive case: if the current character is not "?", just move to the next character
            Map<String, int[]> newState = simplifyState(status, record);
            String newStatus = newState.keySet().iterator().next();            
            return countCombinationsHelper(newStatus, index + 1, newState.get(newStatus));
        }
    }

    private static String multiplyInput(String status, int multiplier) {
        String repeatingUnit = "?" + status;
        String newStatus = status + repeatingUnit.repeat(multiplier - 1);
        return newStatus;
    }

    private static int[] multiplyInput(int[] record, int multiplier) {
        int[] newRecord = new int[record.length * multiplier];
        for(int i=0; i<newRecord.length; i++){
            newRecord[i] = record[i%record.length];
        }
        return newRecord;
    }

    public static void main(String[] args) throws FileNotFoundException {
        File myObj = new File("Week_2/Data/Day12.txt");
        Scanner myReader = new Scanner(myObj);
        int multiplier = 5;
        
        long combinationCount = 0;
        while (myReader.hasNextLine()) {
            String[] line = myReader.nextLine().split(" ");
            int[] nums = Arrays.stream(line[1].split(",")).mapToInt(Integer::parseInt).toArray();

            String status = multiplyInput(line[0], multiplier);
            int[] record = multiplyInput(nums, multiplier);

            int newVal = countCombinations(status, record);
            combinationCount += newVal;
        }
        myReader.close();

        System.out.println("The number of combinations is " + combinationCount);
    }
}
