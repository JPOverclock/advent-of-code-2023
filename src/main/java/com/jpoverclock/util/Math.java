package com.jpoverclock.util;

import java.util.Stack;

public class Math {
    public static long lcm(Stack<Long> numbers) {
        if (numbers.size() == 2) {
            return lcm(numbers.pop(), numbers.pop());
        } else {
            return lcm(numbers.pop(), lcm(numbers));
        }
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }

        long absNumber1 = java.lang.Math.abs(number1);
        long absNumber2 = java.lang.Math.abs(number2);

        long absHigherNumber = java.lang.Math.max(absNumber1, absNumber2);
        long absLowerNumber = java.lang.Math.min(absNumber1, absNumber2);

        long lcm = absHigherNumber;

        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
