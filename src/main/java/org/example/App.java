package org.example;

import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Locale swedishLocale = Locale.of("sv", "SE");
        Locale.setDefault(swedishLocale);
        Scanner scanner = new Scanner(System.in);
        int[] ePrices = new int[24];
        String[] timeIntervals = new String[24];

        String selection;
        do {
            System.out.print("""
                    Elpriser
                    ========
                    1. Inmatning
                    2. Min, Max och Medel
                    3. Sortera
                    4. Bästa Laddningstid (4h)
                    e. Avsluta
                    """);
            selection = scanner.next();
            selection = selection.toLowerCase();

            try {
                switch (selection) {
                    case "1" -> inputPrices(scanner, ePrices, timeIntervals);
                    case "2" -> minMaxAvg(ePrices, timeIntervals);
                    case "3" -> sortPrices(ePrices, timeIntervals);
                    case "4" -> findOptimalChargingTime(ePrices, timeIntervals);
                    case "e" -> System.out.print("Avslutar programmet.\n");
                    default -> System.out.print("Choose another option or press e to exit.\n");
                }
            } catch (IndexOutOfBoundsException exception){
                System.out.print("Choose the right option to continue");
            }
        } while (!selection.equals("e"));
    }

    public static void inputPrices(Scanner scanner, int[] ePrices, String[] timeIntervals) {
        String startHour;
        String finalHour;

        for (int hour = 0; hour < 24; hour++) {
            startHour = String.format("%02d", hour);
            finalHour = String.format("%02d", (hour + 1) % 25);
            timeIntervals[hour] = startHour + "-" + finalHour;

            System.out.print(timeIntervals[hour] + " ");
            ePrices[hour] = scanner.nextInt();
        }
    }

    public static void minMaxAvg(int[] ePrices, String[] timeIntervals) {
        int minPrice = Integer.MAX_VALUE;
        int maxPrice = Integer.MIN_VALUE;
        int totalPrice = 0;
        String intervalMinPrice = timeIntervals[0];
        String intervalMaxPrice = timeIntervals[0];
        int price;

        for (int hours = 0; hours < ePrices.length; hours++) {
            price = ePrices[hours];
            totalPrice += price;

            if (price < minPrice) {
                minPrice = price;
                intervalMinPrice = timeIntervals[hours];
            }
            if (price > maxPrice) {
                maxPrice = price;
                intervalMaxPrice = timeIntervals[hours];
            }
        }

        System.out.print("Lägsta pris: " + intervalMinPrice + ", " + minPrice + " öre/kWh\n");
        System.out.print("Högsta pris: " + intervalMaxPrice + ", " + maxPrice + " öre/kWh\n");
        System.out.printf("Medelpris: %.2f öre/kWh\n", (float) totalPrice / 24);
    }

    public static void sortPrices(int[] ePrices, String[] timeIntervals) {
        boolean swapped;
        int tempPrice;
        String tempTime;

        for (int i = 0; i < ePrices.length - 1; i++) {
            swapped = false;
            for (int j = 0; j < ePrices.length - 1; j++) {
                if (ePrices[j] < ePrices[j + 1]) {
                    tempPrice = ePrices[j];
                    ePrices[j] = ePrices[j + 1];
                    ePrices[j + 1] = tempPrice;

                    tempTime = timeIntervals[j];
                    timeIntervals[j] = timeIntervals[j + 1];
                    timeIntervals[j + 1] = tempTime;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        for (int i = 0; i < ePrices.length; i++) {
            System.out.printf("%s %d öre\n", timeIntervals[i], ePrices[i]);
        }
    }

    public static void findOptimalChargingTime(int[] ePrices, String[] timeIntervals) {
        float minTotalPrice = Float.MAX_VALUE;
        int startTime = 0;

        for (int i = 0; i <= ePrices.length - 4; i++) {
            int totalPrice = 0;
            for (int j = i; j < i + 4; j++) {
                totalPrice += ePrices[j];
            }
            if (totalPrice < minTotalPrice) {
                minTotalPrice = totalPrice;
                startTime = i;
            }
            String start = timeIntervals[startTime].split("-")[0];
            System.out.print("Påbörja laddning klockan " + start + "\n");
            System.out.printf("Medelpris 4h: %.1f öre/kWh\n", minTotalPrice / 4);
        }
    }
}


