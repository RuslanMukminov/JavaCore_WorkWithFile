import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

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

    public static Map<String, List<String>> getConfig(File file) throws IOException,
            SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        Map<String, List<String>> config = new HashMap<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;
                NodeList subNodeList = element.getChildNodes();
                List<String> param = new ArrayList<>();
                for (int j = 0; j < subNodeList.getLength(); j++) {
                    Node subNode = subNodeList.item(j);
                    if (Node.ELEMENT_NODE == subNode.getNodeType()) {
                        param.add(subNode.getTextContent());
                    }
                }
                config.put(element.getTagName(), param);
            }
        }
        return config;
    }

    public static void main(String[] args) throws IOException,
            ParserConfigurationException, SAXException {
        Scanner scanner = new Scanner(System.in);

        File fileConfig = new File("shop.xml");

        String[] products = {"Хлеб", "Яблоки", "Молоко", "Гречневая крупа"};
        long[] prices = {50, 100, 150, 90};

        Basket basket;
        ClientLog clientLog = new ClientLog();

        Map<String, List<String>> config = getConfig(fileConfig);
        int indexEnabled = 0;
        int indexFileName = 1;
        int indexFormat = 2;
        boolean paramLoad = config.get("load").get(indexEnabled).equals("true");
        boolean paramSave = config.get("save").get(indexEnabled).equals("true");
        boolean paramLog = config.get("log").get(indexEnabled).equals("true");
        String formatLoad = config.get("load").get(indexFormat);
        String formatSave = config.get("save").get(indexFormat);

        if (paramLoad) {
            File fileLoad = new File(config.get("load").get(indexFileName));
            if (formatLoad.equals("json")) {
                basket = fromJson(fileLoad);
            } else {
                basket = Basket.loadFromTxtFile(fileLoad);
            }
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

            if (paramSave) {
                File fileBasket = new File(config.get("save").get(indexFileName));
                if (formatSave.equals("json")) {
                    toJson(fileBasket, basket);
                } else {
                    basket.saveTxt(fileBasket);
                }
            }
        }
        if (paramLog) {
            File fileLog = new File(config.get("log").get(indexFileName));
            clientLog.exportAsCSV(fileLog);
        }
        basket.printCart();
    }
}
