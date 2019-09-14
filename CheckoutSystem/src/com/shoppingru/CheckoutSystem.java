package com.shoppingru;

import java.util.ArrayList;
import java.util.Scanner;

import com.shoppingru.beans.ProductDetails;
import com.shoppingru.utils.FileUtils;

public class CheckoutSystem {
	
	private ArrayList<ProductDetails> alProducts = new ArrayList<ProductDetails>();
	
	private ArrayList<String> alScannedItems = new ArrayList<String>();
	
	public static void main(String args[])
	{
		CheckoutSystem objCheckoutSystem = new CheckoutSystem();
		
		// Load the product details from csv file
		FileUtils objFileUtils = new FileUtils();
		ArrayList<String> alRows = objFileUtils.readFile("inputFiles\\catalogue.csv");
		
		objCheckoutSystem.loadProducts(alRows); // Load the products
		objCheckoutSystem.printCatalogue(); // Print the catalogue  
		objCheckoutSystem.scanItems();  // Scan the product items 
		objCheckoutSystem.updateCounter(); // Count each item to apply correct offers
		System.out.println("Grand Total $" + objCheckoutSystem.calculateTotalValue());
	}
	
	public void loadProducts(ArrayList<String> alRows) 
	{
        String sepPipe = "\\|", sepAt = "@", currencySign = "$";

        ProductDetails objProductDetails = null;
        for(int i=2;i<alRows.size();i++)
        {
        	String line = alRows.get(i);
        	String[] columns = line.split(sepPipe);
        	String str[] = null;
        	String offer1 = columns[4].trim();
        	
        	String strProductPrice = columns[3].trim().replace(currencySign, "");
        	double productPrice = Double.valueOf(strProductPrice);
        	
        	int offer1X, offer1Y, offer2X;
        	double offer2P;
        	String offer3sku = null;
        	if(!offer1.isEmpty())
        	{
        		str = offer1.split(sepAt);
        		offer1X = Integer.parseInt(str[0]);
        		offer1Y = Integer.parseInt(str[1]);
        	}
        	else
        	{
        		offer1X = -1;
        		offer1Y = -1;
        	}

        	String offer2 = columns[5].trim();
        	if(!offer2.isEmpty())
        	{
        		str = offer2.split(sepAt);
        		offer2X = Integer.parseInt(str[0].trim());
        		offer2P = Double.valueOf(str[1].trim().replace(currencySign, ""));
        	}
        	else
        	{
        		offer2X = -1;
        		offer2P = -1;
        	}
        	
        	String offer3 = columns[6].trim();
        	if(!offer3.isEmpty())
        		offer3sku = offer3;
        	else
        		offer3sku = null;

            objProductDetails = new ProductDetails(columns[1].trim(),columns[2].trim()
            		,productPrice,offer1X,offer1Y,offer2X,offer2P,offer3sku);
            alProducts.add(objProductDetails);
        }
   	}
	
	public void printCatalogue()
	{
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% Product Catalogue %%%%%%%%%%%%%%%%%%%%%%%%");
		alProducts.stream().forEach(x -> System.out.println(" (SKU) "+ x.getProductSku() 
			+ " (Name) " + x.getProductName()
			+ " (Price) $" + x.getProductPrice()));
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%% Product Catalogue %%%%%%%%%%%%%%%%%%%%%%%%");
	}
	
	public void scanItems()
	{
		String strSKU = "";
		String stopMarker = "xxx";
		try (Scanner scanner = new Scanner(System.in)) 
		{
			while(true)
			{
	            System.out.print("Enter product SKU to scan (xxx to exit) : ");
	            strSKU = scanner.next();  // Read user input
	            if(strSKU!=null && strSKU.equals(stopMarker))
		           	break;
	            else 
	            	alScannedItems.add(strSKU);
			}
		}
	}
	
	// Update item counter for each product
	public void updateCounter()
	{
		for (String sku : alScannedItems) 
		{
			// Find the object 
			ProductDetails objProductDetails = alProducts.stream().filter(x -> x.getProductSku().equals(sku)).findAny().orElse(null);
			if(objProductDetails!=null)
				objProductDetails.setCounter(objProductDetails.getCounter()+1);
		}
	}
	
	// Find the total basket value of all products 
	public double calculateTotalValue()
	{
		double grandTotal = 0;
		for (ProductDetails objProductDetails : alProducts) {
			
			if(objProductDetails.getCounter()>0)
			{
				//System.out.println("Processing " + objProductDetails.getProductName());
				double tempTotal = applyOffer1(objProductDetails);
				if(tempTotal<0)
				{
					tempTotal = applyOffer2(objProductDetails);
					if(tempTotal<0)
					{
						tempTotal = applyOffer3(objProductDetails);
						if(tempTotal<0)
						{
							tempTotal = objProductDetails.getCounter() * objProductDetails.getProductPrice();
							grandTotal += tempTotal;
						}
						else
							grandTotal += tempTotal;
					}	
					else
						grandTotal += tempTotal;
				}
				else
					grandTotal += tempTotal;
			}
		}
		return grandTotal;
	}
	
	// Apply offer1 : Buy X units at the price of Y units
	public double applyOffer1(ProductDetails objProductDetails)
	{
		int offer1X = objProductDetails.getOffer1X();
		double totalProductPrice = -1;
		int quantity = objProductDetails.getCounter();
		double perProductPrice = objProductDetails.getProductPrice();
		if(offer1X > 0)
			totalProductPrice = (quantity - (quantity / offer1X)) * perProductPrice;
		return totalProductPrice;
	}
	
	// Apply offer2 : Bulk buy. Get more products at lower prices than regular
	public double applyOffer2(ProductDetails objProductDetails)
	{
		int offer2X = objProductDetails.getOffer2X();
		double totalProductPrice = -1;
		int quantity = objProductDetails.getCounter();
		if(offer2X > 0 && quantity > offer2X)
			totalProductPrice = quantity * objProductDetails.getOffer2P();
		return totalProductPrice;
	}
	
	// Apply offer3: Get item free when bought with some other items
	public double applyOffer3(ProductDetails objProductDetails)
	{
		String offer3sku = objProductDetails.getOffer3sku();
		double totalProductPrice = -1;
		if(offer3sku!=null)
		{
			int quantity = objProductDetails.getCounter();
			// Find the product tied with this offer
			ProductDetails tiedProduct = alProducts.stream().filter(product -> product.getProductSku().equals(offer3sku)).findAny().orElse(null);
			int tiedProductCounter = tiedProduct.getCounter(); 
			if(tiedProductCounter > 0)
				totalProductPrice = (quantity - tiedProductCounter) * objProductDetails.getProductPrice();
		}
		return totalProductPrice;
	}
}
