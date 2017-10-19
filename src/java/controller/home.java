package controller;

import database.Conference;
import database.Message;
import database.Moderator;
import database.UserConference;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "home")
@ViewScoped
public class home implements Serializable {

    private List<Conference> allConfs;
    private List<Conference> filteredConfs = new ArrayList<Conference>();
    private String name, area, place, password;
    private Date start, end;
    private List<Conference> myConferences = new ArrayList<>();
    private Integer cid;
    private List<Message> myInbox = new ArrayList<>(), myOutbox = new ArrayList<>();
    private List<Integer> isModerator;
    private String add_event;

    @ManagedProperty(value = "#{login}")
    private login login;
    
    public String signUp() {
        Conference tmp = null;

        for (Conference c : allConfs) {
            if (c.getCid() == cid) {
                tmp = c;
                break;
            }
        }

        if (!checkHash(password, tmp.getPassword(), tmp.getSalt(), tmp.getIterations())) {
            throw new ValidatorException(new FacesMessage("Wrong password!"));
        }

        UserConference uc = new UserConference(tmp, login.getUser());
        if (!UserConference.addUserConference(uc)) {
            return "error";
        }

        return FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
    }

    public void test() {
        cid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("singup_id"));
        //System.out.println(cid);
    }
    
    public String goToMod() {
         Integer cid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cid_tomod"));
         
         return "moderator?cid=" + cid.toString();
    }

    public void search() {
        filteredConfs.clear();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 3);
        Date now = cal.getTime();

        for (Conference c : allConfs) {
            if ((name != null && c.getName().toLowerCase().contains(name.toLowerCase()))
                    || (place != null && c.getLocation().getName().toLowerCase().contains(place.toLowerCase()))
                    || (area != null && c.getArea().toLowerCase().contains(area.toLowerCase())) && (c.getDateBegin().after(now))) {
                filteredConfs.add(c);
            } else if (start != null && end != null && ((c.getDateBegin().before(start) && c.getDateEnd().after(start)) || (c.getDateBegin().before(end)))) {
                filteredConfs.add(c);
            } else if (start != null && end == null && (c.getDateBegin().after(start))) {
                filteredConfs.add(c);
            } else if (start == null && end != null && (c.getDateEnd().before(end))) {
                filteredConfs.add(c);
            }
        }
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
                    if (hash[i] != check[i]) {
                        diff = false;
                    }
                }
            }

            return diff;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean contains(List<String> tmp, String x) {
        for (String a : tmp) {
            if (a.equals(x)) {
                return true;
            }
        }

        return false;
    }

    public List<String> completeName(String query) {
        List<String> ret = new ArrayList<String>(); // TO DO AND NOT ALREADY IN LIST

        for (Conference c : allConfs) {
            if (c.getName().toLowerCase().contains(query.toLowerCase())) {
                if (!contains(ret, c.getName())) {
                    ret.add(c.getName());
                }
            }
        }

        return ret;
    }

    public List<String> completeArea(String query) {
        List<String> ret = new ArrayList<String>();

        for (Conference c : allConfs) {
            if (c.getArea().toLowerCase().contains(query.toLowerCase())) {
                if (!contains(ret, c.getArea())) {
                    ret.add(c.getArea());
                }
            }
        }

        return ret;
    }

    public List<String> completePlace(String query) {
        List<String> ret = new ArrayList<>();

        for (Conference c : allConfs) {
            if (c.getLocation().getName().toLowerCase().contains(query.toLowerCase())) {
                if (!contains(ret, c.getLocation().getName())) {
                    ret.add(c.getLocation().getName());
                }
            }
        }

        return ret;
    }

    public home() {

    }

    @PostConstruct
    public void load() {
        List<Integer> tmp1 = Message.getAllMessagesForUser(login.getUser());
        for (int i = 0; i < tmp1.size(); i++) {
            Message tmpmess = Message.getMessageById(tmp1.get(i));
            if (tmpmess.getUserByUid1().getUid() == login.getUser().getUid()) {
                myInbox.add(Message.getMessageById(tmp1.get(i)));
            } else {
                myOutbox.add(Message.getMessageById(tmp1.get(i)));
            }
        }

        allConfs = Conference.getAllConferences();
        if (login != null) {
            List<Integer> tmp = UserConference.getUserConferences(login.getUser());
            for (Integer id : tmp) {
                Conference asd = Conference.getConferenceById(id);
                myConferences.add(asd);
                for (int i = 0; i < allConfs.size(); i++) {
                    if (allConfs.get(i).getCid() == asd.getCid()) {
                        allConfs.remove(i);
                    }
                }
            }
        }
        
        isModerator = Moderator.getModeratedForUser(login.getUser());
    }

    public login getLogin() {
        return login;
    }

    public void setLogin(login login) {
        this.login = login;
    }

    public List<Conference> getAllConfs() {
        return allConfs;
    }

    public void setAllConfs(List<Conference> allConfs) {
        this.allConfs = allConfs;
    }

    public List<Conference> getFilteredConfs() {
        return filteredConfs;
    }

    public void setFilteredConfs(List<Conference> filteredConfs) {
        this.filteredConfs = filteredConfs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public List<Conference> getMyConferences() {
        return myConferences;
    }

    public void setMyConferences(List<Conference> myConferences) {
        this.myConferences = myConferences;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Message> getMyInbox() {
        return myInbox;
    }

    public void setMyInbox(List<Message> myInbox) {
        this.myInbox = myInbox;
    }

    public List<Message> getMyOutbox() {
        return myOutbox;
    }

    public void setMyOutbox(List<Message> myOutbox) {
        this.myOutbox = myOutbox;
    }

    public List<Integer> getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(List<Integer> isModerator) {
        this.isModerator = isModerator;
    }

    public String getAdd_event() {
        return add_event;
    }

    public void setAdd_event(String add_event) {
        this.add_event = add_event;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
    
}
