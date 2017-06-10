package controller;

import database.Conference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "home")
@RequestScoped
public class home {

    private List<Conference> allConfs;
    private List<Conference> filteredConfs = new ArrayList<Conference>();
    private String name, area, place;
    private Date start, end;

    @ManagedProperty(value = "#{login}")
    private login login;

    public String signUp() {
        
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
            } else if(start != null && end != null && ((c.getDateBegin().before(start) && c.getDateEnd().after(start)) || (c.getDateBegin().before(end))) ) {
                filteredConfs.add(c);
            } else if(start != null && end == null && (c.getDateBegin().after(start))) {
                filteredConfs.add(c);
            } else if(start == null && end != null && (c.getDateEnd().before(end))) {
                filteredConfs.add(c);
            }
        }
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
        allConfs = Conference.getAllConferences();
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

}
