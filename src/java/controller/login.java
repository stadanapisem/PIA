package controller;

import database.Agenda;
import database.Conference;
import database.User;
import java.util.Base64;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "login")
@SessionScoped
public class login {

    private String username;
    private String password;
    private boolean loggedin = false;
    private String message;
    private User user = null;
    
    public Set<Agenda> getConference() {
        if(user != null)
            return user.getAgendas();
        
        return null;
    }
    
    public String process() {
        user = User.findByUsername(username);
        
        if(user == null) {
            message = "Username not found!";
            return "index";
        }
        
        if(!checkHash(password, user.getPassword(), user.getSalt(), user.getIterations())) {
            message = "Wrong password!";
            return "index";
        }
        
        loggedin = true;
        
        if(user.getType() == 0)
            return "home";
        else return "admin";
       
    }
    
    private static boolean checkHash(String pass, String p64, String salt, int iter) {
        try {
            byte[] salt_byte = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt_byte, iter, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            byte[] check = Base64.getDecoder().decode(p64);

            boolean diff = hash.length == check.length;
            if (diff) {
                for (int i = 0; i < hash.length; i++) {
                    if(hash[i] != check[i])
                        diff = false;
                }
            }
            
            return diff;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public String logout() {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        return "index";
    }
    
    public login() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLoggedin() {
        return loggedin;
    }

    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
