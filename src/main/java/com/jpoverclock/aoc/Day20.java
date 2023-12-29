package com.jpoverclock.aoc;

import com.jpoverclock.util.Math;

import java.math.BigInteger;
import java.util.*;

public class Day20 {

    enum Pulse {
        LOW,
        HIGH
    }

    record PulseAction(String origin, String destination, Pulse pulse) { }

    static abstract class Module {

        private final String name;

        private final List<String> dependents;

        private final List<String> incoming;

        public Module(String name, List<String> dependents) {
            this.name = name;
            this.dependents = dependents;
            this.incoming = new ArrayList<>();
        }

        abstract List<PulseAction> pulse(String from, Pulse pulse);

        void consolidate(Map<String, Module> moduleMap) {
            for (var dependentName : dependents) {
                if (moduleMap.containsKey(dependentName)) {
                    moduleMap.get(dependentName).hasIncomingSignalFrom(this.name);
                }
            }
        }

        void hasIncomingSignalFrom(String name) {
            incoming.add(name);
        }

        protected List<PulseAction> issuePulses(Pulse pulse) {
            return dependents.stream().map(d -> new PulseAction(this.name, d, pulse)).toList();
        }

        public static Module valueOf(String value) {
            String[] parts = value.split(" -> ");
            char type = parts[0].charAt(0);
            String name = parts[0].substring(1);
            List<String> dependents = Arrays.stream(parts[1].replaceAll("\\s+", "").split(",")).toList();

            return switch (type) {
                case '%' -> new FlipFlop(name, dependents);
                case '&' -> new Conjunction(name, dependents);
                case 'b' -> new Broadcast(parts[0], dependents);
                default -> throw new RuntimeException("Invalid type " + type);
            };
        }
    }

    static class FlipFlop extends Module {

        private Pulse status = Pulse.LOW;

        public FlipFlop(String name, List<String> dependents) {
            super(name, dependents);
        }

        @Override
        List<PulseAction> pulse(String from, Pulse pulse) {
            if (pulse.equals(Pulse.LOW)) {
                status = (status == Pulse.LOW) ? Pulse.HIGH : Pulse.LOW;
                return issuePulses(status);
            }

            return List.of();
        }
    }

    static class Conjunction extends Module {

        private final Map<String, Pulse> pulses = new HashMap<>();

        public Conjunction(String name, List<String> dependents) {
            super(name, dependents);
        }

        @Override
        void hasIncomingSignalFrom(String name) {
            super.hasIncomingSignalFrom(name);
            pulses.put(name, Pulse.LOW);
        }

        @Override
        List<PulseAction> pulse(String from, Pulse pulse) {
            // Implement conjunction
            pulses.put(from, pulse);

            if (pulses.values().stream().allMatch(p -> p.equals(Pulse.HIGH))) {
                return issuePulses(Pulse.LOW);
            } else {
                return issuePulses(Pulse.HIGH);
            }
        }
    }

    static class Broadcast extends Module {

        public Broadcast(String name, List<String> dependents) {
            super(name, dependents);
        }

        @Override
        List<PulseAction> pulse(String from, Pulse pulse) {
            return issuePulses(pulse);
        }
    }



    public static String part1(String input) {
        List<Module> modules = Arrays.stream(input.split("\n")).map(Module::valueOf).toList();
        Map<String, Module> moduleMap = new HashMap<>();

        // Consolidate
        for (var module : modules) {
            moduleMap.put(module.name, module);
        }

        for (var module : modules) {
            module.consolidate(moduleMap);
        }

        // Pulse the machine 1000 times
        BigInteger low = new BigInteger("1000");
        BigInteger high = BigInteger.ZERO;

        for (int i = 0; i < 1000; i++) {

            List<PulseAction> actions = new ArrayList<>(moduleMap.get("broadcaster").pulse("button", Pulse.LOW));

            while (!actions.isEmpty()) {
                int actionsToProcess = actions.size();
                for (int j = 0; j < actionsToProcess; j++) {
                    PulseAction action = actions.remove(0);

                    //System.out.println(action.origin + " -" + (action.pulse.equals(Pulse.LOW) ? "low" : "high") + "-> " + action.destination);

                    if (action.pulse.equals(Pulse.LOW)) {
                        low = low.add(BigInteger.ONE);
                    } else {
                        high = high.add(BigInteger.ONE);
                    }

                    Module module = moduleMap.get(action.destination);

                    if (module != null) {
                        actions.addAll(module.pulse(action.origin, action.pulse));
                    }
                }
            }
        }

        return low.multiply(high).toString();
    }

    public static String part2(String input) {
        List<Module> modules = Arrays.stream(input.split("\n")).map(Module::valueOf).toList();
        Map<String, Module> moduleMap = new HashMap<>();

        // Consolidate
        for (var module : modules) {
            moduleMap.put(module.name, module);
        }

        for (var module : modules) {
            module.consolidate(moduleMap);
        }

        // `dn` is the only input to `rx`, a conjunction module
        // `dn` contains 4 inputs: `dd`, `fh`, `xp` and `fc`
        // `dn` pulses high whenever any of its inputs pulses `low`
        // So the result is LCM of whenever its inputs pulse low

        long ddPulsedLow = Long.MAX_VALUE;
        long fhPulsedLow = Long.MAX_VALUE;
        long xpPulsedLow = Long.MAX_VALUE;
        long fcPulsedLow = Long.MAX_VALUE;

        for (long i = 0;; i++) {

            if (ddPulsedLow != Long.MAX_VALUE &&
                    fcPulsedLow != Long.MAX_VALUE &&
                    xpPulsedLow != Long.MAX_VALUE &&
                    fhPulsedLow != Long.MAX_VALUE
            ) break;

            List<PulseAction> actions = new ArrayList<>(moduleMap.get("broadcaster").pulse("button", Pulse.LOW));

            while (!actions.isEmpty()) {
                int actionsToProcess = actions.size();
                for (int j = 0; j < actionsToProcess; j++) {
                    PulseAction action = actions.remove(0);

                    switch (action.destination) {
                        case "dd" ->
                                ddPulsedLow = action.pulse.equals(Pulse.LOW) ? i + 1 : ddPulsedLow;
                        case "fh" ->
                                fhPulsedLow = action.pulse.equals(Pulse.LOW) ? i + 1 : fhPulsedLow;
                        case "xp" ->
                                xpPulsedLow = action.pulse.equals(Pulse.LOW) ? i + 1 : xpPulsedLow;
                        case "fc" ->
                                fcPulsedLow = action.pulse.equals(Pulse.LOW) ? i + 1 : fcPulsedLow;
                    }

                    Module module = moduleMap.get(action.destination);

                    if (module != null) {
                        actions.addAll(module.pulse(action.origin, action.pulse));
                    }
                }
            }
        }

        Stack<Long> values = new Stack<>();
        values.addAll(List.of(fcPulsedLow, fhPulsedLow, xpPulsedLow, ddPulsedLow));

        return Long.toString(Math.lcm(values));
    }
}
