import java.io.File;
import java.io.IOException;
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

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        File textFile = new File("basket.txt");
        File file = new File("basket.bin");
        String[] products = {"Хлеб", "Яблоки", "Молоко", "Гречневая крупа"};
        long[] prices = {50, 100, 150, 90};
        Basket basket;
        if (file.exists()) {
            basket = Basket.loadFromBinFile(file);
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
            basket.saveBin(file, basket);
        }
        basket.printCart();
    }
}
