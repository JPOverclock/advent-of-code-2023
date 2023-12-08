package com.jpoverclock.aoc;

import java.util.*;

public class Day7 {

    private final String input;

    interface Rule {
        Long cardStrength(Character card);

        Integer classifyHand(String cards);
    }

    static class Part1Rules implements Rule {
        private static final Map<Character, Long> CARD_STRENGTHS = Map.ofEntries(
                Map.entry('2', 1L),
                Map.entry('3', 2L),
                Map.entry('4', 3L),
                Map.entry('5', 4L),
                Map.entry('6', 5L),
                Map.entry('7', 6L),
                Map.entry('8', 7L),
                Map.entry('9', 8L),
                Map.entry('T', 9L),
                Map.entry('J', 10L),
                Map.entry('Q', 11L),
                Map.entry('K', 12L),
                Map.entry('A', 13L)
        );


        @Override
        public Long cardStrength(Character card) {
            return CARD_STRENGTHS.get(card);
        }

        @Override
        public Integer classifyHand(String cards) {
            return Day7.classifyHand(cards);
        }
    }

    static class Part2Rules implements Rule {
        private static final Map<Character, Long> CARD_STRENGTHS = Map.ofEntries(
                Map.entry('2', 1L),
                Map.entry('3', 2L),
                Map.entry('4', 3L),
                Map.entry('5', 4L),
                Map.entry('6', 5L),
                Map.entry('7', 6L),
                Map.entry('8', 7L),
                Map.entry('9', 8L),
                Map.entry('T', 9L),
                Map.entry('J', 0L),
                Map.entry('Q', 11L),
                Map.entry('K', 12L),
                Map.entry('A', 13L)
        );


        @Override
        public Long cardStrength(Character card) {
            return CARD_STRENGTHS.get(card);
        }

        @Override
        public Integer classifyHand(String cards) {
            if (cards.contains("J")) {
                // Create a bunch of hands based on the substitutions
                Hand maxHand = null;

                for (Character c : CARD_STRENGTHS.keySet()) {
                    if (c.equals('J')) continue;

                    Hand hand = new Hand(cards.replaceAll("J", "" + c), this);

                    if (maxHand == null) {
                        maxHand = hand;
                    } else {
                        if (hand.compareTo(maxHand) >= 1) maxHand = hand;
                    }
                }

                return Day7.classifyHand(maxHand.cards);
            } else {
                return Day7.classifyHand(cards);
            }
        }
    }

    public static int classifyHand(String cards) {
        Map<Character, Integer> cardCounts = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            char c = cards.charAt(i);

            if (!cardCounts.containsKey(c)) {
                cardCounts.put(c, 1);
            } else {
                cardCounts.put(c, cardCounts.get(c) + 1);
            }
        }

        if (cardCounts.values().size() == 1) {
            // Five of a kind
            return 7;
        } else if (cardCounts.values().size() == 2) {
            if (cardCounts.containsValue(4)) {
                // Four of a kind
                return 6;
            } else {
                // Full house
                return 5;
            }
        } else if (cardCounts.values().size() == 3) {
            if (cardCounts.containsValue(3)) {
                // Three of a kind
                return 4;
            } else {
                // Two pair
                return 3;
            }
        } else if (cardCounts.values().size() == 4) {
            // One pair
            return 2;
        } else {
            // High card
            return 1;
        }
    }

    record Hand(String cards, Rule rules) implements Comparable<Hand> {
        @Override
        public int compareTo(Hand o) {
            if (rules.classifyHand(cards).equals(rules.classifyHand(o.cards))) {
                // Rule out based on relative order of cards
                for (int i = 0; i < 5; i++) {
                    if (cards.charAt(i) == o.cards.charAt(i)) continue;

                    return rules.cardStrength(cards.charAt(i)).compareTo(rules.cardStrength(o.cards.charAt(i)));
                }

                return 0;
            } else {
                // Just compare their relative hand classifications
                return Integer.compare(rules.classifyHand(cards), rules.classifyHand(o.cards));
            }
        }
    }

    public Day7(String input) {
        this.input = input;
    }

    public String part1() {
        return Long.toString(play(new Part1Rules()));
    }

    public String part2() {
        return Long.toString(play(new Part2Rules()));
    }

    private long play(Rule rules) {
        String[] handsAndBidsLines = input.split("\n");
        Map<Hand, Long> handToBid = new HashMap<>();

        for (String line : handsAndBidsLines) {
            String[] parts = line.split("\\s+");
            handToBid.put(new Hand(parts[0], rules), Long.parseLong(parts[1]));
        }

        List<Hand> sortedHands = handToBid.keySet().stream().sorted().toList();

        long result = 0L;

        for (int i = 0; i < sortedHands.size(); i++) {
            result += (i + 1) * handToBid.get(sortedHands.get(i));
        }

        return result;
    }
}
