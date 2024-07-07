package ru.clevertec.check.services;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.CSVWorker;

import java.util.*;

public class ProductService {
    private static final String PRODUCTS_CSV = "./src/main/resources/products.csv";

    public static List<Product> getProductsByIds(Set<Integer> integers) {
        List<String[]> productsData = CSVWorker.readCSV(PRODUCTS_CSV, ";");
        List<Product> products = new ArrayList<>();
        for (String[] productData : productsData) {
            int id = Integer.parseInt(productData[0]);
            if (!integers.contains(id))
                continue;
            String description = productData[1];
            double price = Double.parseDouble(productData[2]);
            int quantityInStock = Integer.parseInt(productData[3]);
            boolean isWholesale = Boolean.parseBoolean(productData[4]);
            products.add(new Product(id, description, price, isWholesale, quantityInStock));
        }
        return products;
    }
}
