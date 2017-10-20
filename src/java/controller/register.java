package controller;

import database.User;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "register")
@ViewScoped
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
    private String cid;
    private String eid;
    
    @ManagedProperty(value = "#{login}")
    private login login;

    public void UserValidator(FacesContext fc, UIComponent c, Object value) {

        if (value == null || ((String)value).isEmpty()) {
            throw new ValidatorException(new FacesMessage("Field Required!"));
        }
        if (!User.checkExistance((String) value)) {
            throw new ValidatorException(new FacesMessage("Username already in use!"));
        }
    }

    public void PassValidator(FacesContext fc, UIComponent c, Object value) {
        String pass = (String) value;
        System.out.println(fc.getAttributes().size());
        String pattern = "^(?!.*(.)\\1{2})([A-Za-z0-9])(?=(.*[a-z].*){3,})(?=.*\\d.*)(?=.*\\W.*)[a-zA-Z0-9\\S]{7,11}$";

        if (!pass.matches(pattern)) {
            throw new ValidatorException(new FacesMessage("Password needs to contain between 8 and 12 characters. Among them: at least 1 uppercase, at least 3 lowercase, at least 1 numercal and at least 1 special. Password cannot contain 2 same characters after oneanother!"));
        }
    }

    public void handleUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        byte[] contents = uploadedFile.getContents();
        try {
            BufferedImage bufferedImg = ImageIO.read(new ByteArrayInputStream(contents));
            // if(bufferedImg.getHeight() > 300 || bufferedImg.getWidth() > 300)
            //     throw new ValidatorException(new FacesMessage("Image dimensions too large"));

            Path folder = Paths.get("C:\\Users\\Miljan\\Documents\\NetBeansProjects\\PIApouksaj8\\web\\pics");
            String extention = fileName.substring(fileName.indexOf('.'));
            Path file = Files.createTempFile(folder, fileName + "-", extention);
            this.profilePicture = file.toString().substring(file.toString().indexOf("pics"));
            InputStream in = uploadedFile.getInputstream();
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded Successfully"));
        } catch (Exception e) {
            e.printStackTrace();
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

    public String send() {
        if (!User.checkExistance(username)) {
            throw new ValidatorException(new FacesMessage("Username already in use!"));
        }

        byte[] salt_bytes = getSaltB();
        int iter = 10000;
        String tmp_pass = generateHash(password, salt_bytes, iter);

        User user = new User(username, tmp_pass, Base64.getEncoder().encodeToString(salt_bytes), iter, name, surname, email, institution, gender, profilePicture, shirtSize, linkedin, (byte) 0);
        boolean ret = User.addUser(user);

        if (ret) {
            if (cid != null && !cid.isEmpty()) {
                login.setUser(user);
                FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("cid", cid);
                return "home";
            }
            
            if(eid != null && !eid.isEmpty()) {
                login.setUser(user);
                return "agenda?eid=" + eid + "&id=" + cid;
            }

            return "index";
        }

        return "error";
    }

    public register() {
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public login getLogin() {
        return login;
    }

    public void setLogin(login login) {
        this.login = login;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

}
