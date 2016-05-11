/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejb;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import utils.Cart;

/**
 *
 * @author erwan
 */
@Entity
@Table(name="Users")
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="login")
    private String login;
  
    @Column(name="password")
    private String password;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "surname")
    private String surname;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "orders")
    private ArrayList<Cart> pastOrders;
    
    //private Collection<CustomerOrder> customerOrderCollection;

    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public ArrayList<Cart> getPastOrders() {
        return pastOrders;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void addCart(Cart cart){
        this.pastOrders.add(cart);
    }

    public Users(String login, String password) {
        this.login = login;
        this.password = password;
        this.pastOrders = new ArrayList<Cart>();
    }

    public Users() {
        this.pastOrders = new ArrayList<Cart>();
    } 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (login != null ? login.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.login == null && other.login != null) || (this.login != null && !this.login.equals(other.login))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.User[ id=" + login + " ]";
    }
    
}