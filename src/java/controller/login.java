package controller;

import database.Agenda;
import database.Conference;
import database.User;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "login")
@SessionScoped
public class login implements Serializable {

    private String username;
    private String password, old_password;
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
    
    public void PassValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        //System.out.println(fc.getAttributes().size());
        String pattern = "^(?!.*(.)\\1{2})([A-Za-z0-9])(?=(.*[a-z].*){3,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{7,11}$";

        if (!pass.matches(pattern)) {
            throw new ValidatorException(new FacesMessage("Password needs to contain between 8 and 12 characters. Among them: at least 1 uppercase, at least 3 lowercase, at least 1 numercal and at least 1 special. Password cannot contain 2 same characters after oneanother!"));
        }
        
    }
    
    public void oldPassValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        if(!checkHash(pass, user.getPassword(), user.getSalt(), user.getIterations())) {
            throw new ValidatorException(new FacesMessage("Wrong password!"));
        }
    }
    
    public String changePassword() {
        
        
        
        byte[] salt_bytes = getSaltB();
        int iter = 10000;
        String tmp_pass = generateHash(password, salt_bytes, iter);
        
        user.setPassword(tmp_pass);
        user.setSalt(Base64.getEncoder().encodeToString(salt_bytes));
        user.setIterations(iter);
        
        if(!User.updateUser(user))
            return "error";
        
        return "home";
    }
    
    private byte[] getSaltB() {
        try {
            SecureRandom sr = new SecureRandom(LocalDateTime.now().toString().getBytes());
            byte[] salt = new byte[16];
            sr.nextBytes(salt);

            return salt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String generateHash(String pass, byte[] salt, int iter) {
        try {
            PBEKeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, iter, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            return Base64.getEncoder().encodeToString((skf.generateSecret(spec).getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }
    
}
