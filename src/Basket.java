import java.io.*;

public class Basket {

    private String[] products;
    private long[] prices;
    private int[] cart;

    public Basket(String[] products, long[] prices) {
        this.products = products;
        this.prices = prices;
        cart = new int[products.length];
    }

    public Basket(String[] products, long[] prices, int[] cart) {
        this.products = products;
        this.prices = prices;
        this.cart = cart;
    }

    public void addToCart(int productNum, int amount) {
        cart[productNum] += amount;
    }

    public void printCart() {
        long sumProducts = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < cart.length; i++) {
            if (cart[i] != 0) {
                System.out.println(products[i] + " " + cart[i] + " шт " + prices[i]
                        + " руб/шт " + (cart[i] * prices[i]) + " руб в сумме");
                sumProducts += prices[i] * cart[i];
            }
        }
        System.out.println("Итого " + sumProducts + " руб");
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (String s : products) {
                out.print(s + ",");
            }
            out.print('\n');
            for (long l : prices) {
                out.print(l + " ");
            }
            out.print('\n');
            for (int i : cart) {
                out.print(i + " ");
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        String[] products = new String[0];
        long[] prices = new long[0];
        int[] cart = new int[0];
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            products = br.readLine().split(",");
            String[] s = br.readLine().split(" ");
            prices = new long[s.length];
            for (int i = 0; i < prices.length; i++) {
                prices[i] = Long.parseLong(s[i]);
            }
            s = br.readLine().split(" ");
            cart = new int[s.length];
            for (int i = 0; i < cart.length; i++) {
                cart[i] = Integer.parseInt(s[i]);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new Basket(products, prices, cart);
    }

    public String[] getProducts() {
        return products;
    }

    public long[] getPrices() {
        return prices;
    }
}
