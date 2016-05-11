/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import ejb.Book;
import ejb.Users;
import java.util.ArrayList;

/**
 *
 * @author erwan
 */
public class Cart {

    private ArrayList<Book> bookList = new ArrayList<Book>();
    private Users user;

    public Cart(Users user) {
        this.user = user;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public Users getUser() {
        return user;
    }

    public void addBook(Book aBook) {
        this.bookList.add(aBook);
    }

    public void removeBook(Book aBook) {
        for (int i = 0; i < this.bookList.size(); i++) {
            if (this.bookList.get(i).equals(aBook)) {
                this.bookList.remove(i);
                return;
            }
        }
    }

    public float totalPrice() {
        float sum = 0;
        for (Book book : bookList) {
            sum += book.getPrice();
        }
        return sum;
    }

}
