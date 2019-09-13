package com.shoppingru.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileUtils 
{
	public ArrayList<String> readFile(String filePath)
	{
        BufferedReader br = null;
        String line = "";
        ArrayList<String> alRows = new ArrayList<String>();
        
        try 
        {
            br = new BufferedReader(new FileReader(filePath));
            // Skip first 2 lines as they are headers
            while ((line = br.readLine()) != null) 
        	{
               	alRows.add(line);
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.out.println("File not found at given location " + filePath + ". Exception message : " + e.getMessage());
        } 
        catch (IOException e) 
        {
        	System.out.println("IOException while reading file " + filePath + ". Exception message : " + e.getMessage());
        } 
        finally 
        {
            if (br != null) 
            {
                try 
                {
                    br.close();
                } 
                catch (IOException e) 
                {
                	System.out.println("IOException while closing the file connection. Exception message : " + e.getMessage());
                } 
            }
        }
        return alRows;
	}
}
