package restGateWay;

import javax.ejb.Singleton;

/**
 * NameStorageBean is a singleton containing login and password information of the current user
 * 
 * @author DOUAILLE ERWAN & DOUYLLIEZ MAXIME
 */
@Singleton
public class NameStorageBean {

    private String login ;
    private String password ;
    
    public String getLogin(){
        return this.login;
    }
    
    public String getPassword(){
        return this.password;
    }
    
    public void setLogin(String login){
        this.login = login;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
 
}
