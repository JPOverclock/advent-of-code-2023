package com.jpoverclock.aoc;

import java.util.*;
import java.util.stream.Stream;

public class Day19 {

    public interface Action {

        static Action valueOf(String value) {
            return switch (value) {
                case "A" -> new AcceptAction();
                case "R" -> new RejectAction();
                default -> new JumpAction(value);
            };
        }
    }

    public static class JumpAction implements Action {
        public final String workflow;

        public JumpAction(String workflow) {
            this.workflow = workflow;
        }
    }

    public static class AcceptAction implements Action { }
    public static class RejectAction implements Action { }

    public interface Rule {
        Action apply(Part part);
        boolean matches(Part part);

        static Rule valueOf(String value) {
            if (value.contains("<") || value.contains(">")) {
                String[] parts = value.split(":");

                char component = parts[0].charAt(0);
                char comparison = parts[0].charAt(1);
                int number = Integer.parseInt(parts[0].substring(2));

                return new ComparisonRule(component, comparison, number, Action.valueOf(parts[1]));
            } else {
                return new NoRule(Action.valueOf(value));
            }
        }
    }

    public static class ComparisonRule implements Rule {

        private final char component;

        private final char comparison;

        private final int number;

        private final Action action;

        public ComparisonRule(char component, char comparison, int number, Action action) {
            this.component = component;
            this.number = number;
            this.comparison = comparison;
            this.action = action;
        }

        @Override
        public Action apply(Part part) {
            return action;
        }

        @Override
        public boolean matches(Part part) {
            int componentToMatch = switch (component) {
                case 'x' -> part.x;
                case 'm' -> part.m;
                case 'a' -> part.a;
                case 's' -> part.s;
                default -> throw new RuntimeException("Unmatched component " + component);
            };

            return switch (comparison) {
                case '<' -> componentToMatch < number;
                case '>' -> componentToMatch > number;
                default -> throw new RuntimeException("Unmatched comparison " + comparison);
            };
        }

        public ComparisonRule invert() {
            return new ComparisonRule(component, switch (comparison) {
                case '<' -> '}';
                case '>' -> '{';
                case '}' -> '<';
                case '{' -> '>';
                default -> throw new RuntimeException("Cannot invert!");
            }, number, action);
        }
    }

    public static class NoRule implements Rule {

        private final Action action;

        public NoRule(Action action) {
            this.action = action;
        }

        @Override
        public Action apply(Part part) {
            return action;
        }

        @Override
        public boolean matches(Part part) {
            return true;
        }
    }

    public static class Workflow {

        private final String name;

        private final List<Rule> rules;

        private Workflow(String name, List<Rule> rules) {
            this.name = name;
            this.rules = rules;
        }

        public Action process(Part part) {
            for (Rule rule : rules) {
                if (rule.matches(part)) {
                    return rule.apply(part);
                }
            }

            throw new RuntimeException("Workflow did not match!");
        }

        public static Workflow valueOf(String input) {
            String name = input.substring(0, input.indexOf('{'));
            String rules = input.substring(input.indexOf('{') + 1, input.indexOf('}'));

            return new Workflow(name, Arrays.stream(rules.split(",")).map(Rule::valueOf).toList());
        }
    }

    public static class System {
        private final Map<String, Workflow> workflows;

        public System(List<Workflow> workflows) {
            Map<String, Workflow> flows = new HashMap<>();

            for (var flow : workflows) {
                flows.put(flow.name, flow);
            }

            this.workflows = flows;
        }

        public Long process(List<Part> parts) {
            long sum = 0;

            for (var part : parts) {
                boolean processing = true;
                Workflow workflow = workflows.get("in");

                while (processing) {
                    Action action = workflow.process(part);

                    if (action instanceof AcceptAction) {
                        sum += part.rating();
                        processing = false;
                    } else if (action instanceof RejectAction) {
                        processing = false;
                    } else if (action instanceof JumpAction jump) {
                        workflow = workflows.get(jump.workflow);
                    }
                }
            }

            return sum;
        }
    }

    public record Part(int x, int m, int a, int s) {
        public static Part valueOf(String input) {
            String[] components = input
                    .replaceAll("[{}]", "")
                    .split(",");

            return new Part(
                    Integer.parseInt(components[0].replaceAll("x=", "")),
                    Integer.parseInt(components[1].replaceAll("m=", "")),
                    Integer.parseInt(components[2].replaceAll("a=", "")),
                    Integer.parseInt(components[3].replaceAll("s=", ""))
            );
        }

        public int rating() {
            return x + m + a + s;
        }
    }

    public static String part1(String input) {
        String[] sections = input.split("\n\n");

        var workflows = Arrays.stream(sections[0].split("\n")).map(Workflow::valueOf).toList();
        var parts = Arrays.stream(sections[1].split("\n")).map(Part::valueOf).toList();

        var system = new System(workflows);

        long result = system.process(parts);

        return Long.toString(result);
    }

    public record Node(String name, List<Rule> rules) { }

    public static String part2(String input) {
        String[] sections = input.split("\n\n");

        var workflows = Arrays.stream(sections[0].split("\n")).map(Workflow::valueOf).toList();
        var parts = Arrays.stream(sections[1].split("\n")).map(Part::valueOf).toList();

        var system = new System(workflows);

        // We need to DFS or BFS to find all "A" nodes and how we get there
        Stack<Node> stack = new Stack<Node>();
        stack.push(new Node("in", List.of()));

        List<List<Rule>> acceptRules = new LinkedList<>();

        while (!stack.isEmpty()) {
            Node node = stack.pop();

            // Go through all rules
            var workflow = system.workflows.get(node.name);

            var invertedRules = new LinkedList<Rule>();

            for (var rule : workflow.rules) {
                Action action = null;
                var nextRules = Stream.concat(Stream.concat(node.rules.stream(), invertedRules.stream()), Stream.of(rule)).toList();
                Rule invertedRule = null;

                if (rule instanceof NoRule noRule) {
                    action = noRule.action;
                } else if (rule instanceof ComparisonRule comparisonRule) {
                    action = comparisonRule.action;
                    invertedRule = comparisonRule.invert();
                }

                if (action != null) {
                    if (action instanceof AcceptAction accept) {
                        // Add to results
                        acceptRules.add(nextRules);
                    } else if (action instanceof JumpAction jump) {
                        // Push to stack
                        stack.push(new Node(jump.workflow, nextRules));
                    } else {
                        // Reject action, ignore
                    }
                }

                if (invertedRule != null) {
                    invertedRules.add(invertedRule);
                }
            }
        }

        long sum = 0;

        for (var acceptRule : acceptRules) {
            long minX = 1;
            long maxX = 4000;
            long minM = 1;
            long maxM = 4000;
            long minA = 1;
            long maxA = 4000;
            long minS = 1;
            long maxS = 4000;

            for (var rule : acceptRule) {
                if (rule instanceof ComparisonRule comparison) {
                    switch (comparison.comparison) {
                        case '>' -> {
                            switch (comparison.component) {
                                case 'x' -> minX = Math.max(minX, comparison.number + 1);
                                case 'm' -> minM = Math.max(minM, comparison.number + 1);
                                case 'a' -> minA = Math.max(minA, comparison.number + 1);
                                case 's' -> minS = Math.max(minS, comparison.number + 1);
                            }
                        }
                        case '<' -> {
                            switch (comparison.component) {
                                case 'x' -> maxX = Math.min(maxX, comparison.number - 1);
                                case 'm' -> maxM = Math.min(maxM, comparison.number - 1);
                                case 'a' -> maxA = Math.min(maxA, comparison.number - 1);
                                case 's' -> maxS = Math.min(maxS, comparison.number - 1);
                            }
                        }
                        case '}' -> {
                            switch (comparison.component) {
                                case 'x' -> minX = Math.max(minX, comparison.number);
                                case 'm' -> minM = Math.max(minM, comparison.number);
                                case 'a' -> minA = Math.max(minA, comparison.number);
                                case 's' -> minS = Math.max(minS, comparison.number);
                            }
                        }
                        case '{' -> {
                            switch (comparison.component) {
                                case 'x' -> maxX = Math.min(maxX, comparison.number);
                                case 'm' -> maxM = Math.min(maxM, comparison.number);
                                case 'a' -> maxA = Math.min(maxA, comparison.number);
                                case 's' -> maxS = Math.min(maxS, comparison.number);
                            }
                        }
                    }
                }
            }

            sum += ((maxX - minX + 1) * (maxM - minM + 1) * (maxA - minA + 1) * (maxS - minS + 1));
        }

        return Long.toString(sum);
    }
}
