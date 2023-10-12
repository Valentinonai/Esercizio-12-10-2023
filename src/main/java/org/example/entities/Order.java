package org.example.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order {

    private static String[] statusorder = {"pending", "shipped", "delivered"};
    private long id;
    private String status;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private List<Product> products;
    private Customer customer;

    public Order(long id, String status, LocalDate orderDate, List<Product> products, Customer customer) {
        this.id = id;
        this.status = status;
        this.orderDate = orderDate;
        this.setDeliveryDate(orderDate);
        this.products = products;
        this.customer = customer;
    }

    public static Order fillOrder(List<Product> product, Customer customer, int n) {
        Random rnd = new Random();
        List<Product> app = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            app.add(product.get(rnd.nextInt(0, product.size())));
        }
        int month = rnd.nextInt(1, 5);
        int day;
        if (month == 2)
            day = rnd.nextInt(1, 29);
        else if (month == 11 || month == 4 || month == 6 || month == 9) {
            day = rnd.nextInt(1, 31);
        } else day = rnd.nextInt(1, 32);
        int year = rnd.nextInt(2021, 2022);
        return new Order(Math.abs(rnd.nextInt()), statusorder[rnd.nextInt(0, 3)], LocalDate.of(year, month, day), app, customer);
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public long getId() {
        return id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setDeliveryDate(LocalDate orderDate) {
        this.deliveryDate = orderDate.plus(Period.ofDays(3));
    }

    public double calcTot() {
        return this.products.stream().map(Product::getPrice).reduce(0.0, (sub, elem) -> sub + elem);
       /* double tot = 0;
        for (int i = 0; i < this.products.size(); i++) {
            tot += this.products.get(i).getPrice();
        }
        return tot;*/
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", products=" + products +
                ", customer=" + customer +
                '}';
    }
}
