package baekjoon.p10699;

import java.text.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String result = new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
        ).format(new Date());

        System.out.println(result);
    }

}