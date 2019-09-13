package com.shoppingru.beans;

public class ProductDetails {
	private String productSku;
	
	private String productName;
	
	private double productPrice;

	private int offer1X;
	
	private int offer1Y;
	
	private int offer2X;
	
	private double offer2P;
	
	private String offer3sku;
	
	private int counter = 0;
	
	public ProductDetails(String productSku, String productName, double productPrice, int offer1x, int offer1y,
			int offer2x, double offer2p, String offer3sku) {
		this.productSku = productSku;
		this.productName = productName;
		this.productPrice = productPrice;
		offer1X = offer1x;
		offer1Y = offer1y;
		offer2X = offer2x;
		offer2P = offer2p;
		this.offer3sku = offer3sku;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public int getOffer1X() {
		return offer1X;
	}

	public void setOffer1X(int offer1x) {
		offer1X = offer1x;
	}

	public int getOffer1Y() {
		return offer1Y;
	}

	public void setOffer1Y(int offer1y) {
		offer1Y = offer1y;
	}

	public int getOffer2X() {
		return offer2X;
	}

	public void setOffer2X(int offer2x) {
		offer2X = offer2x;
	}

	public double getOffer2P() {
		return offer2P;
	}

	public void setOffer2P(double offer2p) {
		offer2P = offer2p;
	}

	public String getOffer3sku() {
		return offer3sku;
	}

	public void setOffer3sku(String offer3sku) {
		this.offer3sku = offer3sku;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
}
