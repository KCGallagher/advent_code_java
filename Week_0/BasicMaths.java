public class BasicMaths {
    // Code to display basic arithmetic operations
    public static void main (String[] args) {
        int a = 5;
        int b = 10;
        int sum = a + b;
        float avg = sum / 2;

        String output = String.format("The sum is %d, and the average is %.1f", sum, avg);
        System.out.println(output);
    }
}
