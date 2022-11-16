import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Scanner;

class Main {
    public static void menu(Basket basket) {
        String[] products = basket.getProducts();
        long[] prices = basket.getPrices();
        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }
        System.out.println("Выберите товар и количество или введите `end`");
    }

    public static void toJson(File fileJson, Basket basket) {
        try (Writer writer = new FileWriter(fileJson)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            gson.toJson(basket, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket fromJson(File fileJson) {
        Basket basket = null;
        try (BufferedReader br = new BufferedReader(new FileReader(fileJson))) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            basket = gson.fromJson(br, Basket.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return basket;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        File clientLogFile = new File("log.csv");
        File fileJson = new File("basket.json");

        String[] products = {"Хлеб", "Яблоки", "Молоко", "Гречневая крупа"};
        long[] prices = {50, 100, 150, 90};
        Basket basket;
        ClientLog clientLog = new ClientLog();

        if (fileJson.exists()) {
            basket = fromJson(fileJson);
            basket.printCart();
        } else {
            System.out.println("Ваша корзина пуста");
            basket = new Basket(products, prices);
        }
        menu(basket);
        while (true) {
            String input = scanner.nextLine();
            if ("end".equals(input)) {
                break;
            }
            String[] inputArr = input.split(" ");
            int productNum = Integer.parseInt(inputArr[0]) - 1;
            int amount = Integer.parseInt(inputArr[1]);
            basket.addToCart(productNum, amount);

            clientLog.log(productNum, amount);
            toJson(fileJson, basket);
        }
        basket.printCart();

        clientLog.exportAsCSV(clientLogFile);
    }
}
