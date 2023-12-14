package Week_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Day7 {
    public static Boolean useJokers = true;
    public static ArrayList<Character> cardCharsFull = new ArrayList<>(
            List.of('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'));

    public static int getCardPriority(char card) {
        ArrayList<Character> cardChars = new ArrayList<>(cardCharsFull);
        if (useJokers) {
            cardChars.remove(Character.valueOf('J'));
            cardChars.add(0, 'J');
        }
        return cardChars.indexOf(card);
    }

    public static int getHandPriority(String hand) {
        // Get counts of unique characters in string
        Map<String, Long> cardCounts = hand.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Integer> cardCountsList = new ArrayList<>(cardCounts.values())
                .stream()
                .map(Long::intValue)
                .collect(Collectors.toList());
        Collections.sort(cardCountsList, Collections.reverseOrder());

        Map<List<Integer>, Integer> priorityMap = new HashMap<>();
        priorityMap.put(List.of(5), 7);
        priorityMap.put(List.of(4, 1), 6);
        priorityMap.put(List.of(3, 2), 5);
        priorityMap.put(List.of(3, 1, 1), 4);
        priorityMap.put(List.of(2, 2, 1), 3);
        priorityMap.put(List.of(2, 1, 1, 1), 2);
        priorityMap.put(List.of(1, 1, 1, 1, 1), 1);
        return priorityMap.getOrDefault(cardCountsList, 0);
    }

    public static int getJokerHandPriority(String hand) {
        ArrayList<Character> posValues = new ArrayList<>(cardCharsFull);
        posValues.remove(Character.valueOf('J'));
        Map<Character, Integer> posPriority = new HashMap<>();

        for (char val : posValues) {
            posPriority.put(val, getHandPriority(hand.replace('J', val)));
        }
        return Collections.max(posPriority.values());
    }

    // Write custom comparator class to sort by key length
    public static Comparator<String> myComparator = new Comparator<String>() {
        public int compare(String h1, String h2) {

            int result = getHandPriority(h1) - getHandPriority(h2);
            if (useJokers) {
                result = getJokerHandPriority(h1) - getJokerHandPriority(h2);
            }

            int cardIndex = 0;
            while (result == 0) {
                result = getCardPriority(h1.charAt(cardIndex)) - getCardPriority(h2.charAt(cardIndex));
                cardIndex++;
                if (cardIndex == h1.length()) {
                    break;
                }
            }
            return result;
        }
    };

    public static int scoreHands(TreeMap<String, String> cards) {
        int score = 0;
        List<String> values = new ArrayList<>(cards.values());
        for (int i = 1; i <= values.size(); i++) {
            score += i * Integer.parseInt(values.get(i - 1));
        }
        return score;
    }

    public static void main(String[] args) throws FileNotFoundException {
        TreeMap<String, String> hands = new TreeMap<String, String>(myComparator);

        File myObj = new File("Week_1/Data/Day7.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String[] cards = myReader.nextLine().split("\s+");
            hands.put(cards[0], cards[1]);
        }
        myReader.close();

        System.out.println("Total score of all hands: " + scoreHands(hands));
    }
}
