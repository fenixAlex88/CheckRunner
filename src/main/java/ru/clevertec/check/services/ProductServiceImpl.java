package ru.clevertec.check.services;

import ru.clevertec.check.model.Product;
import ru.clevertec.check.utils.CSVWorker;
import ru.clevertec.check.utils.CSVWorkerImpl;

import java.util.*;

public class ProductServiceImpl implements ProductService {
    private final Map<Integer, Product> products = new HashMap<>();

    public ProductServiceImpl(String filePath) {
        loadProducts(filePath);
    }

    private void loadProducts(String filePath) {
        CSVWorker csvWorker = new CSVWorkerImpl();
        List<String[]> productsStrings = csvWorker.readFromCSV(filePath, ";");
        for (String[] productsString : productsStrings) {
            int id = Integer.parseInt(productsString[0]);
            String description = productsString[1];
            double price = Double.parseDouble(productsString[2]);
            int quantityInStock = Integer.parseInt(productsString[3]);
            boolean isWholesale = Boolean.parseBoolean(productsString[4]);
            products.put(id, new Product.Builder()
                    .setId(id)
                    .setDescription(description)
                    .setPrice(price)
                    .setQuantityInStock(quantityInStock)
                    .setWholesale(isWholesale).build());
        }
    }

    @Override
    public Product getProductById(int id) {
        return products.get(id);
    }
}
