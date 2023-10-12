package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.example.entities.Customer;
import org.example.entities.Order;
import org.example.entities.Product;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {
        Random rnd = new Random();
        Faker fkr = new Faker();
        File file = new File("src/main/java/org.example/file.txt");

        List<Customer> customerList = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            customerList.add(new Customer(Math.abs(rnd.nextInt()), fkr.name().firstName(), rnd.nextInt(1, 3)));
        }
        for (int i = 0; i < 200; i++) {
            productList.add(new Product(Math.abs(rnd.nextInt()), fkr.commerce().productName(), fkr.commerce().material(), rnd.nextDouble(10, 1000)));
        }
        for (int i = 0; i < 20; i++) {
            int nCustomer = rnd.nextInt(0, customerList.size());
            int nprod = rnd.nextInt(1, 6);
            orderList.add(Order.fillOrder(productList, customerList.get(nCustomer), nprod));
        }

        Map<Customer, List<Order>> es1 = esercizio1(orderList);
        Map<Customer, Double> es2 = esercizio2(orderList);
        List<Product> es3 = esercizio3(productList);
        double es4 = esercizio4(orderList);
        esercizio5(productList);
        try {
            esercizio6(productList, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Product> productListClone = esercizio7(file);

        
        System.out.println(productListClone.equals(productList) ? "Le due liste sono uguali" : "Qualcosa non va le due liste sono diverse");

    }

    public static Map<Customer, List<Order>> esercizio1(List<Order> orders) {
        Map<Customer, List<Order>> mapList = orders.stream().collect(Collectors.groupingBy(Order::getCustomer));
        mapList.forEach((k, v) -> System.out.println("Customer id: " + k.getId() + " ---> " + v));
        return mapList;
    }

    public static Map<Customer, Double> esercizio2(List<Order> orders) {
        Map<Customer, Double> mapList = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(ord -> ord.calcTot())));

        return mapList;
    }

    public static List<Product> esercizio3(List<Product> prod) {
        List<Product> app = prod.stream().sorted(Comparator.comparing(Product::getPrice)).toList();
        return app.stream().filter(elem -> elem.getPrice() == app.get(app.size() - 1).getPrice()).toList();

    }

    public static double esercizio4(List<Order> order) {
        return order.stream().mapToDouble(Order::calcTot).average().orElse(0.0);
    }

    public static void esercizio5(List<Product> products) {
        products.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice))).forEach((k, v) -> System.out.println("Categoria: " + k + " prezzo tot: " + v));


    }

    public static void esercizio6(List<Product> product, File file) throws IOException {
        FileUtils.write(file, "", StandardCharsets.UTF_8);
        product.forEach(elem -> {
            String str = elem.getId() + "@" + elem.getName() + "@" + elem.getCategory() + "@" + elem.getPrice() + "#";
            try {
                FileUtils.write(file, str, StandardCharsets.UTF_8, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static List<Product> esercizio7(File file) {
        List<Product> list = new ArrayList<>();
        try {
            String fileReader = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            List<String> str = new ArrayList<>(List.of(fileReader.split("#")));
            for (int i = 0; i < str.size(); i++) {
                List<String> app = new ArrayList<>(List.of(str.get(i).split("@")));
                list.add(new Product(Long.parseLong(app.get(0)), app.get(1), app.get(2), Double.parseDouble(app.get(3))));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;

    }
}
