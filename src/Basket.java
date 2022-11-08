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
        int i = 0;
        String[] list0 = new String[0];
        long[] list1 = new long[0];
        int[] list2 = new int[0];
        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String s;
            while ((s = br.readLine()) != null) {
                switch (i) {
                    case 0:
                        list0 = s.split(",");
                        i++;
                        break;
                    case 1:
                        String[] list = s.split(" ");
                        list1 = new long[list.length];
                        for (int j = 0; j < list.length; j++) {
                            list1[j] = Long.parseLong(list[j]);
                        }
                        i++;
                        break;
                    case 2:
                        list = s.split(" ");
                        list2 = new int[list.length];
                        for (int j = 0; j < list.length; j++) {
                            list2[j] = Integer.parseInt(list[j]);
                        }
                        i++;
                        break;
                }

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return new Basket(list0, list1, list2);
    }

    public String[] getProducts() {
        return products;
    }

    public long[] getPrices() {
        return prices;
    }

    public void saveBin(File file, Basket basket) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(basket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            basket = (Basket) ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return basket;
    }
}
