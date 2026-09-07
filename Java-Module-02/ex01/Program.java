import java.io.*;
import java.util.*;

public class Program {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error: Please provide exactly two file paths as arguments.");
            System.exit(1);
        }

        String fileAPath = args[0];
        String fileBPath = args[1];

        SortedSet<String> dictionary = new TreeSet<>();
        Map<String, Integer> freqA = new HashMap<>();
        Map<String, Integer> freqB = new HashMap<>();

        try {
            parseFile(fileAPath, dictionary, freqA);
            parseFile(fileBPath, dictionary, freqB);
            writeDictionary(dictionary);

            double similarity = calculateSimilarity(dictionary, freqA, freqB);
            double truncated = Math.floor(similarity * 100) / 100.0;
            System.out.printf("Similarity = %.2f\n", truncated);
        } catch (IOException e) {
            System.err.println("Error reading or writing files: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void parseFile(String path, Set<String> dictionary, Map<String, Integer> freqMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^a-zA-Z0-9]+");

                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }

                    dictionary.add(word);
                    freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
                }
            }
        }
    }

    private static void writeDictionary(Set<String> dictionary) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dictionary.txt"))) {
            for (String word : dictionary) {
                writer.write(word);
                writer.newLine();
            }
        }
    }

    private static double calculateSimilarity(Set<String> dictionary, Map<String, Integer> freqA, Map<String, Integer> freqB) {
        double aFinalCount = 0.0;
        double bFinalCount = 0.0;
        double dividend = 0.0;
        double divisor = 0.0;
        
        for (String word : dictionary) {
            int countA = freqA.getOrDefault(word, 0);
            int countB = freqB.getOrDefault(word, 0);
            
            dividend += countA * countB;
            aFinalCount += countA * countA;
            bFinalCount += countB * countB;
        }
        
        // System.out.println("aFinalCount: " + aFinalCount + ", bFinalCount: " + bFinalCount);
        divisor = Math.sqrt(aFinalCount) * Math.sqrt(bFinalCount);
        // System.out.println("dividend: " + dividend + ", divisor: " + divisor);

        if (divisor == 0.0) {
            return 0.0;
        } else return dividend / divisor;
    }
}
