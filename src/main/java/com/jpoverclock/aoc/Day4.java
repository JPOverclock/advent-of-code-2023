package com.jpoverclock.aoc;

import java.math.BigInteger;
import java.util.*;

public class Day4 {

    record ScratchCard(int id, Set<Integer> winningNumbers, Set<Integer> myNumbers) {

        public Long points() {
            long points = 1;

            for (Integer number : winningNumbers) {
                if (myNumbers.contains(number)) {
                    points <<= 1;
                }
            }

            return points >> 1;
        }

        public Integer matches() {
            int count = 0;

            for (int number : winningNumbers) {
                if (myNumbers.contains(number)) {
                    count++;
                }
            }

            return count;
        }

        static ScratchCard parse(String input) {
            String[] cardIdContents = input.split(":");

            int id = Integer.parseInt(cardIdContents[0].replace("Card", "").trim());

            String[] contents = cardIdContents[1].split("\\|");

            Set<Integer> winningNumbers = new HashSet<>();
            String[] rawWinningNumbers = contents[0].trim().split("\\s+");

            for (String rawWinningNumber : rawWinningNumbers) {
                winningNumbers.add(Integer.parseInt(rawWinningNumber));
            }

            Set<Integer> myNumbers = new HashSet<>();
            String[] rawMyNumbers = contents[1].trim().split("\\s+");

            for (String rawMyNumber : rawMyNumbers) {
                myNumbers.add(Integer.parseInt(rawMyNumber));
            }

            return new ScratchCard(id, winningNumbers, myNumbers);
        }
    }

    private final List<ScratchCard> cards;

    public Day4(String input) {
        cards = Arrays.stream(input.split("\n")).map(ScratchCard::parse).toList();
    }

    public String part1() {
        long totalPoints = cards.stream().mapToLong(ScratchCard::points).sum();
        return String.valueOf(totalPoints);
    }

    public String part2() {
        Map<Integer, Day4.ScratchCard> cardMap = new HashMap<>();
        Map<Integer, Boolean> winningCards = new HashMap<>();
        Stack<Integer> pile = new Stack<>();

        // Set up our lookups for the subsequent game
        for (var card : cards) {
            cardMap.put(card.id, card);
            pile.add(card.id);
            winningCards.put(card.id, card.matches() > 0);
        }

        BigInteger count = BigInteger.ZERO;

        while (true) {
            Stack<Integer> newPile = new Stack<>();
            count = count.add(new BigInteger(String.valueOf(pile.size())));

            while (!pile.isEmpty()) {
                int id = pile.pop();
                if (!winningCards.get(id)) continue;

                ScratchCard card = cardMap.get(id);
                int matches = card.matches();

                for (int nextId = id + 1; nextId <= id + matches; nextId++) {
                    if (cardMap.containsKey(nextId)) {
                        newPile.add(nextId);
                    }
                }
            }

            if (newPile.isEmpty()) {
                return count.toString();
            } else {
                pile = newPile;
            }
        }
    }
}
