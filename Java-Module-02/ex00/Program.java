import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        String resultPath = "result.txt";
        String inputPath = "signatures.txt";

        byte[]  fileHeader = new byte[16];
        Map<String, byte[]> magicNumbers = createMagicNumbersMap(inputPath);
        
        Scanner scanner = new Scanner(System.in);
        boolean matched = false;
        String  path;

        try {
            while (true) {
                path = scanner.next();
                if ("42".equals(path)) {
                    break;
                }

                fileHeader = readFileHeader(path);
                if (fileHeader.length == 0) {
                    System.out.println("UNDEFINED");
                    continue;
                }

                for (Map.Entry<String, byte[]> entry : magicNumbers.entrySet()) {
                    matched = matchesSignature(fileHeader, entry.getValue());
                    if (matched) {
                        try (FileOutputStream fos = new FileOutputStream(resultPath, true)) {
                            fos.write((entry.getKey() + "\n").getBytes());
                            System.out.println("PROCESSED");
                        } catch (IOException e) {
                            System.err.println("Error writing to result file: " + e.getMessage());
                        }

                        break;
                    }
                }

                if (!matched) {
                    System.out.println("UNDEFINED");
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static byte[] readFileHeader(String filePath) {
        byte[] buffer = new byte[16];

        try (FileInputStream fis = new FileInputStream(filePath)) {
            int bytesRead = fis.read(buffer);
            
            if (bytesRead == -1) {
                return new byte[0]; 
            } if (bytesRead < buffer.length) {
                return Arrays.copyOf(buffer, bytesRead);
            }
            
            return buffer;
        } catch (IOException e) {
            System.err.print("Error reading file: " + e.getMessage() + ": ");
            return new byte[0];
        }
    }

    private static Map<String, byte[]> createMagicNumbersMap(String inputPath) {
        Map<String, byte[]> magicNumbers = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(inputPath);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    String extension = parts[0].trim();
                    String hexString = parts[1].trim();

                    byte[] signatureBytes = parseHex(hexString);
                    if (signatureBytes.length <= 0) {
                        System.err.println("Invalid signature for extension: " + extension);
                    } else {
                        magicNumbers.put(extension, signatureBytes);
                    }
                }
            }
        } catch (IOException e) {
            System.err.print("I/O Error: " + e.getMessage() + ": ");
        }

        return magicNumbers;
    }

    private static byte[] parseHex(String hexString) {
        try {
            String[] hexValues = hexString.split("\\s+");
            byte[] bytes = new byte[hexValues.length];
            
            for (int i = 0; i < hexValues.length; i++) {
                bytes[i] = (byte) Integer.parseInt(hexValues[i], 16);
            }
            
            return bytes;
        } catch (NumberFormatException e) {
            System.err.println("Error parsing hex string: " + e.getMessage());
        }

        return new byte[0];
    }

    private static boolean matchesSignature(byte[] fileHeader, byte[] signature) {
        if (fileHeader.length < signature.length) {
            return false;
        }

        for (int i = 0; i < signature.length; i++) {
            if (fileHeader[i] != signature[i]) {
                return false;
            }
        }
        
        return true;
    }
}
