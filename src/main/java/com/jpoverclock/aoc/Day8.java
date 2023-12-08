package com.jpoverclock.aoc;

import com.jpoverclock.util.Math;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {

    private final String input;

    public Day8(String input) {
        this.input = input;
    }

    public String part1() {
        String[] parts = input.split("\n\n");

        char[] instructions = parts[0].toCharArray();
        String[] nodeLines = parts[1].split("\n");

        Map<String, String[]> graph = new HashMap<>();
        Pattern nodeRegex = Pattern.compile("^(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

        for (String nodeLine : nodeLines) {
            Matcher matcher = nodeRegex.matcher(nodeLine);
            matcher.find();

            String node = matcher.group(1);
            String left = matcher.group(2);
            String right = matcher.group(3);

            graph.put(node, new String[] {left, right});
        }

        // Simulate instructions
        long steps = 0;
        int currentInstruction = 0;
        String currentNode = "AAA";

        while (!currentNode.equals("ZZZ")) {
            char instruction = instructions[currentInstruction];

            switch (instruction) {
                case 'L' -> currentNode = graph.get(currentNode)[0];
                case 'R' -> currentNode = graph.get(currentNode)[1];
            }

            steps++;
            currentInstruction++;
            if (currentInstruction >= instructions.length) currentInstruction = 0;
        }

        return Long.toString(steps);
    }

    public String part2() {
        String[] parts = input.split("\n\n");

        char[] instructions = parts[0].toCharArray();
        String[] nodeLines = parts[1].split("\n");

        Map<String, String[]> graph = new HashMap<>();
        Pattern nodeRegex = Pattern.compile("^(\\w{3}) = \\((\\w{3}), (\\w{3})\\)");

        for (String nodeLine : nodeLines) {
            Matcher matcher = nodeRegex.matcher(nodeLine);
            matcher.find();

            String node = matcher.group(1);
            String left = matcher.group(2);
            String right = matcher.group(3);

            graph.put(node, new String[] {left, right});
        }

        // Follow instructions
        int currentInstruction = 0;
        Set<String> currentNodes = new HashSet<>();

        // Initial value is all nodes ending with A
        graph.keySet().stream().filter(s -> s.endsWith("A")).forEach(currentNodes::add);

        Set<Long> multiples = new HashSet<>();

        for (String node : currentNodes) {
            System.out.println("Node = " + node);
            String currentNode = node;
            currentInstruction = 0;
            long steps = 0;

            Set<Long> foundSteps = new HashSet<>();
            boolean searching = true;

            while (searching) {
                if (currentNode.endsWith("Z")) {
                    // Try to find multiples
                    for (Long foundStep : foundSteps) {
                        if (steps % foundStep == 0) {
                            // Step is a multiple of some other, we've found a loop!!
                            searching = false;
                            break;
                        }
                    }

                    if (searching) foundSteps.add(steps);
                }

                // Try to find Z
                char instruction = instructions[currentInstruction];
                switch (instruction) {
                    case 'L' -> currentNode = graph.get(currentNode)[0];
                    case 'R' -> currentNode = graph.get(currentNode)[1];
                }

                steps++;
                currentInstruction++;
                if (currentInstruction >= instructions.length) currentInstruction = 0;
            }

            multiples.addAll(foundSteps);
        }

        // LCM the common steps
        Stack<Long> numbers = new Stack<>();
        numbers.addAll(multiples);

        long steps = Math.lcm(numbers);
        return Long.toString(steps);
    }
}
