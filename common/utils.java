package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class utils {
    public static int generateId(int size) {
        int randomNum = 0;

        // generate a random number of 'size' digits
        for (int i = 0; i < size; i++) {
            randomNum = (randomNum * 10) + (int) (Math.random() * 10);
        }

        return randomNum;
    }

    public static String generateInvoiceNumber() {
        return "INV-" + getEpochTime();
    }

    public static String generateCustomerNumber() {
        return "CUS-" + generateId(4);
    }

    public static String generateProductNumber() {
        return "PROD-" + generateId(4);
    }

    public static <T> void saveData(String fileName, List<T> al) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < al.size(); i++) {
                String content = al.get(i).toString();
                writer.write(content);
                writer.write("\n");
            }
            System.out.println("WRITING IN FILE DONE");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static Stream<String> readData(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        Stream<String> stream = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            stream = reader.lines().collect(Collectors.toList()).stream();
            return stream;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            throw new Error("Error reading file: " + e.getMessage());
        }
    }

    public static long getEpochTime() {
        return Instant.now().toEpochMilli();
    }

    public static String convertEpochToDateTime(long number) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(number),
                ZoneId.systemDefault());
        return dateTime.toString();
    }

    public static long getDueDate(long invoiceDate, PaymentTerms paymentTerms) {
        String days = paymentTerms.toString().split("_")[1];
        return Instant.ofEpochMilli(invoiceDate)
                .plus(Duration.ofDays(Long.parseLong(days)))
                .toEpochMilli();
    }

    public static long getDiscountAmount(long totalAmount, int discountPercentage) {
        return (totalAmount * discountPercentage) / 100;
    }
}
