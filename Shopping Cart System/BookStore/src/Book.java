/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author marlon
 */
public class Book {

    private String name;
    private double retailPrice;

    public Book(String name, double retailPrice)
    {
        setName(name);
        setRetailPrice(retailPrice);
    }

    /** Returns the book's name */
    public String getName()
    {
        return name;
    }

    /** Returns the book's retail price */
    public double getRetailPrice()
    {
        return retailPrice;
    }

    /** Sets the book's name to the value given */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Sets the book's retail price to the value given */
    public void setRetailPrice(double retailPrice)
    {
        this.retailPrice = retailPrice;
    }

    public static boolean loadBooks(ArrayList<Book> books, String fileName)
    {
        try(Scanner scanner = new Scanner(new File(fileName)))
        {
            String currentLine;
            String deliminators = ", ";
            String[] tokens;

            scanner.useDelimiter(", ");

            while(scanner.hasNext())
            {
                currentLine = scanner.nextLine();
                tokens = currentLine.split(deliminators);
                books.add(new Book(tokens[0], Double.parseDouble(tokens[1])));
            }

            return true;
        }
        catch(FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(
                    null,
                    "Error opening " + fileName + "! \n\nMake sure that it is in the same directory as the program.",
                    "File Not Found", JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }
    
}
