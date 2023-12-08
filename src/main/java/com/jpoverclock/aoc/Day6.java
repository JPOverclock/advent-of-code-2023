package com.jpoverclock.aoc;

import java.util.ArrayList;
import java.util.List;

public class Day6 {

    private final String input;

    record Race(int time, int distance) {
        int numberOfPossibleWaysToBeatRecord() {
            int count = 0;

            for (int i = 0; i <= time; i++) {
                int distance = i * (time - i);
                if (distance > this.distance) {
                    count++;
                }
            }

            return count;
        }
    }

    record BigRace(long time, long distance) {
        long numberOfPossibleWaysToBeatRecord() {
            long count = 0L;

            for (long i = 0; i <= time; i++) {
                long distance = i * (time - i);
                if (distance > this.distance) {
                    count++;
                }
            }

            return count;
        }
    }

    public Day6(String input) {
        this.input = input;
    }

    public String part1() {
        List<Race> races = parseInput();

        long result = races.stream()
                .mapToLong(Race::numberOfPossibleWaysToBeatRecord)
                .reduce(1L, (a,b) -> a * b);

        return Long.toString(result);
    }

    public String part2() {
        BigRace race = parseCorrectInput();

        return Long.toString(race.numberOfPossibleWaysToBeatRecord());
    }

    private List<Race> parseInput() {
        String[] lines = input.split("\n");
        String[] times = lines[0].replace("Time:", "").trim().split("\\s+");
        String[] distances = lines[1].replace("Distance:", "").trim().split("\\s+");

        List<Race> races = new ArrayList<>(times.length);

        for (int i = 0; i < times.length; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }

        return races;
    }

    private BigRace parseCorrectInput() {
        String[] lines = input.split("\n");
        String time = lines[0].replace("Time:", "").trim().replaceAll("\\s+", "");
        String distance = lines[1].replace("Distance:", "").trim().replaceAll("\\s+", "");

        return new BigRace(Long.parseLong(time), Long.parseLong(distance));
    }
}
