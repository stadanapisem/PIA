/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.Agenda;
import database.Conference;
import database.Location;
import database.Moderator;
import database.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "admin")
@RequestScoped
public class admin implements Serializable {

    private String name, password, area;
    
    @ManagedProperty(value="#{param.cancel_id}")
    private Integer cancel_id;
    
    private Integer location_id;
    private Date start, end;
    private List<Conference> conferences;
    private List<Location> locations;
    private List<User> users;
    private String[] mods;
    private String[] areas = {"Electrical and Computer Engineering", "Architecture", "Civil Engineering and Geodesy", "Mechanical and Industrial Engineering", "Medicine", "Phisyics - Chemistry", "Biology", "Environmental safety"};

    // TODO delete Conf, send messages
    
    public String cancelConf() {
        System.out.println(cancel_id);
        Conference to_del = null;
        
        for(Conference c : conferences)
            if(c.getCid() == cancel_id) {
                to_del = c;
                conferences.remove(c);
            }
        
        List<User> to_send = Agenda.allUsersForConference(to_del);
        
        if(to_del == null)
            return "error";
        
        Conference.deleteConference(to_del);
        
        return null;
    }
    
    public String send() {
        byte[] salt_bytes = getSaltB();
        int iter = 10000;
        String tmp_pass = generateHash(password, salt_bytes, iter);

        Conference conf = new Conference(getById(location_id), name, tmp_pass, Base64.getEncoder().encodeToString(salt_bytes), iter, start, end, area);
        if (!Conference.addConference(conf)) {
            return "error";
        }

        for (String i : mods) {
            int uid = Integer.parseInt(i);
            User tmp = null;

            for (User u : users) {
                if (u.getUid() == uid) {
                    tmp = u;
                }
            }

            Moderator m = new Moderator(conf, tmp);
            if (!Moderator.addModerator(m)) {
                return "error";
            }
        }

        conferences.add(conf);
        
        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
    }

    public Location getById(Integer loc_id) {
        for (Location i : locations) {
            if (i.getLid() == loc_id) {
                return i;
            }
        }

        return null;
    }

    public void PassValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        System.out.println(fc.getAttributes().size());
        String pattern = "^(?!.*(.)\\1{2})([A-Za-z0-9])(?=(.*[a-z].*){3,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{7,11}$";

        if (!pass.matches(pattern)) {
            throw new ValidatorException(new FacesMessage("Password needs to contain between 8 and 12 characters. Among them: at least 1 uppercase, at least 3 lowercase, at least 1 numercal and at least 1 special. Password cannot contain 2 same characters after oneanother!"));
        }
    }

    public void PassMatchValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;

        if (!pass.equals(password)) {
            throw new ValidatorException(new FacesMessage("Passwords don't match!"));
        }

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

    public admin() {
        conferences = Conference.getAllConferences();
        locations = Location.getAllLocations();
        users = User.getAllUsers();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Conference> getConferences() {
        return conferences;
    }

    public void setConferences(List<Conference> conferences) {
        this.conferences = conferences;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String[] getAreas() {
        return areas;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Integer getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Integer location_id) {
        this.location_id = location_id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String[] getMods() {
        return mods;
    }

    public void setMods(String[] mods) {
        this.mods = mods;
    }

    public void setCancel_id(Integer cancel_id) {
        this.cancel_id = cancel_id;
    }

}
