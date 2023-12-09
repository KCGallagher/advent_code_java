public class ComplexDataTypes {
    public static void main(String[] args) {
        int[] myNum = {10, 20, 30, 40};
        for (int i = 0; i < myNum.length; i++) {
            myNum[i]++;
        }
        for (int i : myNum) {
            System.out.print(String.valueOf(i) + ", ");
        }

        System.out.println();
        int[][] myNumbers = { {1, 2, 3, 4}, {5, 6, 7} };
        for (int i = 0; i < myNumbers.length; ++i) {
          for(int j = 0; j < myNumbers[i].length; ++j) {
            if (myNumbers[i][j] > 5) {
                System.out.print(myNumbers[i][j] + ", ");
            }
            
          }
        }
    }
}
