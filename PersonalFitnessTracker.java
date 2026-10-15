
import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.function.Predicate;

class Workout implements Comparable<Workout> {
    private String name;
    private int minutes;
    private double calories;

    Workout(String name, int minutes, double calories) {
        this.name = name;
        this.minutes = minutes;
        this.calories = calories;
    }

    public String getName() { return name; }
    public int getMinutes() { return minutes; }
    public double getCalories() { return calories; }

    public int compareTo(Workout w) {
        return Double.compare(w.calories, calories);
    }

    public String toString() {
        return name + " | " + minutes + " mins | "
                + calories + " calories";
    }
}

public class PersonalFitnessTracker {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Workout> workouts = new ArrayList<>();
    static final int GOAL = 60;

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n1.Add Workout  2.View Workouts");
            System.out.println("3.Fitness Summary  4.Search Workout");
            System.out.println("5.Sort Workouts  6.Stream Report");
            System.out.println("7.Save and Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1: addWorkout(); break;
                case 2: viewWorkouts(); break;
                case 3: summary(); break;
                case 4: search(); break;
                case 5:
                    Collections.sort(workouts);
                    viewWorkouts();
                    break;
                case 6: streamReport(); break;
                case 7: save(); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 7);

        sc.close();
    }

    // Module 1 and 2: Input, operators and conditions
    static void addWorkout() {
        System.out.print("Exercise name: ");
        String name = sc.nextLine().trim();

        System.out.print("Duration in minutes: ");
        int minutes = Integer.parseInt(sc.nextLine());

        System.out.print("Calories burned: ");
        double calories = Double.parseDouble(sc.nextLine());

        if (name.isEmpty() || minutes <= 0 || calories < 0) {
            System.out.println("Invalid details!");
            return;
        }

        workouts.add(new Workout(name, minutes, calories));

        System.out.println(minutes >= GOAL
                ? "Daily goal reached!" : "Keep exercising!");
    }

    // Module 3: Methods, loops and arrays
    static void viewWorkouts() {
        for (Workout w : workouts)
            System.out.println(w);
    }

    static void summary() {
        int totalMinutes = 0;
        double totalCalories = 0;

        for (Workout w : workouts) {
            totalMinutes += w.getMinutes();
            totalCalories += w.getCalories();
        }

        System.out.println("Total minutes: " + totalMinutes);
        System.out.println("Total calories: " + totalCalories);

        double average = workouts.isEmpty() ? 0
                : totalMinutes / (double) workouts.size();

        System.out.println("Average duration: " + average);
        System.out.println("Recursive calories: "
                + recursiveSum(0));
    }

    static double recursiveSum(int i) {
        if (i == workouts.size()) return 0;
        return workouts.get(i).getCalories()
                + recursiveSum(i + 1);
    }

    // Module 5: String comparison and searching
    static void search() {
        System.out.print("Enter exercise to find: ");
        String key = sc.nextLine().trim();

        for (Workout w : workouts) {
            if (w.getName().equalsIgnoreCase(key))
                System.out.println(w);
        }
    }

    // Module 6: Predicate, lambda and streams
    static void streamReport() {
        Predicate<Workout> active = w -> w.getMinutes() >= 30;

        workouts.stream()
                .filter(active)
                .sorted()
                .forEach(System.out::println);

        long count = workouts.stream()
                .filter(w -> w.getCalories() >= 100)
                .count();

        System.out.println("Workouts burning 100+ calories: " + count);

        String names = workouts.stream()
                .map(Workout::getName)
                .distinct()
                .collect(Collectors.joining(", "));

        System.out.println("Exercises: " + names);
    }

    // Module 5: File handling and exceptions
    static void save() {
        try {
            List<String> lines = workouts.stream()
                    .map(Workout::toString)
                    .collect(Collectors.toList());

            Files.write(Path.of("fitness.txt"), lines);
            System.out.println("Workout data saved!");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
