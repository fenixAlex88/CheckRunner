package ru.clevertec.check.model;

public class ProductItem {
    private final Product product;
    private final int productQuantity;

    private ProductItem(Builder builder) {
        this.product = builder.product;
        this.productQuantity = builder.productQuantity;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public static class Builder{
        private Product product;
        private int productQuantity;

        public Builder setProduct(Product product) {
            this.product = product;
            return this;
        }
        public Builder setProductQuantity(int productQuantity) {
            this.productQuantity = productQuantity;
            return this;
        }
        public ProductItem build() {
            return new ProductItem(this);
        }
    }


}
