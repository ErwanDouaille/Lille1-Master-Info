/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author erwan
 */
@Entity
@Table(name="Book")
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="title")
    private String title;
  
    @Column(name="author")
    private String author;
   
    @Column(name="date")
    private int date;
   
    @Column(name="price")
    private float price;

    public Book(String title, String author, int date, float price) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public Book() {
    } 
    
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getDate() {
        return date;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(int date) {
        this.date = date;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (title != null ? title.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.title == null && other.title != null) || (this.title != null && !this.title.equals(other.title))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.Book[ id=" + title + " ]";
    }
    
}
