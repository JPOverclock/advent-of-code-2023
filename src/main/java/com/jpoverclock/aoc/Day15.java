package com.jpoverclock.aoc;

import java.util.*;

public class Day15 {

    private static int hash(String value) {
        int result = 0;

        for (int i = 0; i < value.length(); i++) {
            int number = value.codePointAt(i);
            result += number;
            result *= 17;
            result = result % 256;
        }

        return result;
    }

    record Lens(String name, int focalLength) { }

    record Box(int number, List<Lens> lens) {

        void dropLens(String name) {
            lens.removeIf(lens -> lens.name.equals(name));
        }

        void addLens(String name, int focalLength) {
            Optional<Lens> candidate = lens.stream().filter(lens -> lens.name.equals(name)).findFirst();

            if (candidate.isPresent()) {
                int index = lens.indexOf(candidate.get());
                lens.set(index, new Lens(name, focalLength));
            } else {
                lens.add(new Lens(name, focalLength));
            }
        }

        public int focusingPower() {
            int power = 0;

            for (int i = 0; i < lens.size(); i++) {
                power += ((number + 1) * (i + 1) * lens.get(i).focalLength);
            }

            return power;
        }
    }

    public static String part1(String input) {
        String[] steps = input.split(",");

        long result = Arrays.stream(steps).mapToLong(Day15::hash).sum();

        return Long.toString(result);
    }

    public static String part2(String input) {
        Map<Integer, Box> boxes = new HashMap<>();

        String[] steps = input.split(",");

        // Perform initialization steps
        for (String step : steps) {
            // Check which operation to perform
            if (step.endsWith("-")) {
                // Removal step
                String lensName = step.substring(0, step.length() - 1);

                int index = hash(lensName);

                if (!boxes.containsKey(index)) {
                    boxes.put(index, new Box(index, new LinkedList<>()));
                }

                Box box = boxes.get(index);
                box.dropLens(lensName);
            } else {
                // Addition step
                String[] components = step.split("=");
                String lensName = components[0];
                int focalLength = Integer.parseInt(components[1]);

                int index = hash(lensName);

                if (!boxes.containsKey(index)) {
                    boxes.put(index, new Box(index, new LinkedList<>()));
                }

                Box box = boxes.get(index);
                box.addLens(lensName, focalLength);
            }
        }

        // Compute the focusing power
        long sum = boxes.values().stream().mapToLong(Box::focusingPower).sum();

        return Long.toString(sum);
    }
}
