package models;

import java.math.BigInteger;

public class Product {
	private String productID;
	private String productName;
	private BigInteger productPrice;
	private String productDes;
	
	public Product(String productID, String productName, BigInteger productPrice, String productDes) {
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.productDes = productDes;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigInteger getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigInteger productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductDes() {
		return productDes;
	}

	public void setProductDes(String productDes) {
		this.productDes = productDes;
	}
	
}
