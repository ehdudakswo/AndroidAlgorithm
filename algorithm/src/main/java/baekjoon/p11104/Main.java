package baekjoon.p11104;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            System.out.println(solve(sc.next()));
        }
    }

    private static int solve(String input) {
        return Integer.parseInt(input, 2);
    }

}