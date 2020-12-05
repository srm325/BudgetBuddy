package com.srm325.budgetshop.model_classes;

public class Product {
        private String productName;
        private Double productPrice;
        private String productID;

        public Product() {
        }

        public Product(String productName, Double productPrice, String productID) {
            this.productName = productName;
            this.productPrice = productPrice;
            this.productID = productID;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(Double productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

}
