

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author marlon
 */
public class ShoppingCartSelection extends JFrame
{
    private JPanel bookPanel;
    private JPanel selectedBookPanel;
    private JPanel buttonPanel;
    private JList<String> bookList;
    private JList<String> selectedBookList;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JButton addToCartButton;
    private JButton removeFromCartButton;
    private JButton clearCartButton;
    private JButton checkoutButton;
    private DefaultListModel<String> bookListModel = new DefaultListModel<String>();
    private DefaultListModel<String> selectedBookListModel = new DefaultListModel<String>();
    private ArrayList<Book> books;
    
    DecimalFormat decimalFormat = new DecimalFormat("####.##");
    ArrayList<Book> booksInCart = new ArrayList<>();
    String[] booksInCartNames;
    
    double SALES_TAX_PERCENTAGE = 0.06;
    double subtotal;
    double salesTax;
    double total;

    public ShoppingCartSelection(ArrayList<Book> books)
    {
        this.books = books;

        // Populates bookListModel
        for(int index = 0; index < books.size(); index++)
        {
            bookListModel.add(index, books.get(index).getName());
        }

        // Configures basics of window
        setTitle("Book Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Builds panels
        buildBookPanel();
        buildSelectedBookPanel();
        buildButtonPanel();

        // Adds the panels to the content pane
        add(bookPanel, BorderLayout.NORTH);
        add(selectedBookPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Packs and displays the window
        pack();
        setVisible(true);
    }


    private void buildBookPanel()
    {
        bookPanel = new JPanel();
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        bookList.setVisibleRowCount(6);
        scrollPane1 = new JScrollPane(bookList);
        bookPanel.add(scrollPane1);
    }


    private void buildSelectedBookPanel()
    {
        selectedBookPanel = new JPanel();
        selectedBookList = new JList<>(selectedBookListModel);
        selectedBookList.setVisibleRowCount(6);
        scrollPane2 = new JScrollPane(selectedBookList);
        selectedBookPanel.add(scrollPane2);
    }

    /**
     * Adds buttons to the buttonPanel and assigns action listeners..
     */
    private void buildButtonPanel()
    {
        buttonPanel = new JPanel();
        addToCartButton = new JButton("Add");
        removeFromCartButton = new JButton("Remove");
        clearCartButton = new JButton("Clear");
        checkoutButton = new JButton("Checkout");
        addToCartButton.addActionListener(new AddToListButtonListener());
        removeFromCartButton.addActionListener(new RemoveFromCartButtonListener());
        clearCartButton.addActionListener(new ClearCartButtonListener());
        checkoutButton.addActionListener(new CheckoutButtonListener());
        buttonPanel.add(addToCartButton);
        buttonPanel.add(removeFromCartButton);
        buttonPanel.add(clearCartButton);
        buttonPanel.add(checkoutButton);
    }


    private class AddToListButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(bookList.getSelectedIndices().length > 0)
            {
                int[] selectedIndices = bookList.getSelectedIndices();

                for(int index = selectedIndices.length - 1; index >= 0; index--)
                {
                    selectedBookListModel.add(0, bookListModel.get(selectedIndices[index]));
                    bookListModel.removeElementAt(selectedIndices[index]);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "You must select something first!",
                        "No Selections Found",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private class RemoveFromCartButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            // If the user has selected something, then add it to the book list and remove it from the selected book list
            if(selectedBookList.getSelectedIndices().length > 0)
            {
                int[] selectedIndices = selectedBookList.getSelectedIndices();

                for(int index = selectedIndices.length - 1; index >= 0; index--)
                {
                    bookListModel.add(0, selectedBookListModel.get(selectedIndices[index]));
                    selectedBookListModel.removeElementAt(selectedIndices[index]);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(
                        null,
                        "You must select something first!",
                        "No Selections Found",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    /**
     * If there are values in the selected book list model, then it adds them to the book list model and removes them
     * from the selected book list model, effectively clearing the cart.
     */
    private class ClearCartButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(selectedBookListModel.getSize() != 0)
            {
                int selectedBookListModelSize = selectedBookListModel.getSize();

                for(int index = 0; index < selectedBookListModelSize; index++)
                {
                    bookListModel.add(0, selectedBookListModel.get(0));
                    selectedBookListModel.removeElementAt(0);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "The cart is empty!", "Empty Cart", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class CheckoutButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(selectedBookListModel.size() != 0)
            {
                // Sets booksInCartNames to the contents of selectedBookListModel
                booksInCartNames = Arrays.copyOf(selectedBookListModel.toArray(), selectedBookListModel.toArray().length,
                        String[].class);

                // Adds book objects to booksInCart
                for(String booksInCartName : booksInCartNames)
                {
                    for(Book book : books)
                    {
                        if(booksInCartName.equals(book.getName()))
                        {
                            booksInCart.add(book);
                        }
                    }
                }

                // Calculates subtotal from books in booksInCart
                for(Book aBooksInCart : booksInCart)
                {
                    subtotal += aBooksInCart.getRetailPrice();
                }

                // Calculates the salesTax
                salesTax = subtotal * SALES_TAX_PERCENTAGE;

                // Calculates total
                total = subtotal + salesTax;

                // Displays message dialog with subtotal, salesTax, and total
                JOptionPane.showMessageDialog(
                        null,
                        "Subtotal: $" + decimalFormat.format(subtotal) +
                        "\nSales Tax: $" + decimalFormat.format(salesTax) +
                        "\nTotal: $" + decimalFormat.format(total),
                        "Purchase Successful!", JOptionPane.INFORMATION_MESSAGE
                );
            }
            else
            {
                JOptionPane.showMessageDialog(null, "The cart is empty!", "Empty Cart", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}