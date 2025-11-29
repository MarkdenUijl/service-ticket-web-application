package nl.helvar.servicetickets.helpers;

public class StringUtilsHelper {
    private StringUtilsHelper() {
        // Prevent instantiation – utility class
    }

    public static String capitalizeWords(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }

        String[] words = input.trim().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                String capitalized =
                        word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                result.append(capitalized).append(" ");
            }
        }

        // Remove trailing space
        return result.toString().trim();
    }
}
