/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.User;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Miljan
 */
@Named(value = "register")
@Dependent
public class register {

    private Integer uid;
    private String username;
    private String password;
    private String password2;
    private String salt;
    private int iterations;
    private String name;
    private String surname;
    private String email;
    private String institution;
    private Byte gender;
    private String profilePicture;
    private String shirtSize;
    private String linkedin;
    private byte type;
    private boolean valid = false, username_valid = false, password_valid = false, passwords_match = false;

    public void UserValidator(FacesContext fc, UIComponent c, Object value) {
        username_valid = User.checkExistance((String) value);
        update_validity();
        if (!username_valid) {
            throw new ValidatorException(new FacesMessage("Username already in use!"));
        }
    }

    public void PassValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        System.out.println(fc.getAttributes().size());
        String pattern = "^(?!.*(.)\\1{2})([A-Za-z0-9]{1})((?=.*[\\d])(?=.*[A-Za-z])|(?=.*[^\\w\\d\\s])(?=.*[A-Za-z])).{7,11}$";

        if (!pass.matches(pattern)) {
            password_valid = false;
            update_validity();
            throw new ValidatorException(new FacesMessage("Password needs to contain between 8 and 12 characters. Among them: at least 1 uppercase, at least 3 lowercase, at least 1 numercal and at least 1 special. Password cannot contain 2 same characters after oneanother!"));
        }

        password_valid = true;
        update_validity();
    }

    public void PassMatchValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        
        if(!pass.equals(password)) {
            passwords_match = false;
            update_validity();
            throw new ValidatorException(new FacesMessage("Passwords don't match!"));
        }
        
        passwords_match = true;
        update_validity();
            
    }
    
    public String send() {
        return null;
    }

    public register() {
    }

    public void update_validity() {
        valid = username_valid && password_valid && passwords_match;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

}
