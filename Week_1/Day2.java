package Week_1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Day2 {
    public static String[] colors = { "red", "green", "blue" };
    public static Integer[] max_counts = { 12, 13, 14 }; // Hard coded in question definition

    public static int[] getGameCounts(String game) {
        int[] game_counts = { 0, 0, 0 };
        String[] split_counts = game.split(",");
        for (String count : split_counts) {
            String[] count_details = count.strip().split("\\s+");
            Integer color_index = List.of(colors).indexOf(count_details[1]);
            game_counts[color_index] = Integer.parseInt(count_details[0]);
        }
        return game_counts;
    }

    public static int addAllowedGameID(String[] split_games, int game_index) {
        // Returns 0 if game is not allowed, otherwise returns game ID

        for (String game : split_games) {
            int[] game_counts = getGameCounts(game);

            // Check if all counts are less than max counts
            boolean isAllTrue = IntStream.range(0, game_counts.length)
                    .allMatch(i -> game_counts[i] <= max_counts[i]);

            if (!isAllTrue) {
                return 0; // No need to check other results from this game
            }
        }
        return game_index;
    }

    public static int computeGamePower(String[] split_games) {
        int[] min_counts = { 0, 0, 0 };

        for (String game : split_games) {
            int[] game_counts = getGameCounts(game);

            for (int i = 0; i < game_counts.length; i++) {
                min_counts[i] = Math.max(game_counts[i], min_counts[i]);
            }
        }
        // System.out.println(java.util.Arrays.toString(min_counts));
        return Arrays.stream(min_counts).reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Integer possible_id_sum = 0;
        Integer game_power_sum = 0;

        File myObj = new File("Week_1/Data/Day2.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] split_line = data.split(":");

            String game_index_str = split_line[0].replaceAll("[^0-9]", "");
            int game_index = Integer.parseInt(game_index_str);
            String[] split_games = split_line[1].split(";");

            possible_id_sum += addAllowedGameID(split_games, game_index);
            game_power_sum += computeGamePower(split_games);
        }
        myReader.close();
        System.out.println("The sum of allowed game IDs is " + possible_id_sum);
        System.out.println("The sum of game powers is " + game_power_sum);
    }
}
