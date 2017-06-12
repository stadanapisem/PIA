package controller;

import database.Conference;
import database.Event;
import database.User;
import database.UserConference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "show")
@RequestScoped
public class show {
    
    @ManagedProperty(value = "#{param.id}")
    private Integer cid;
    
    private Conference c;
    private List<Event> events = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public show() {
    }
    
    @PostConstruct
    public void load() {
        c = Conference.getConferenceById(cid);
        List<Integer> res = Event.getAllEventsForConference(c);
        
        events.clear();
        for(Integer tmp : res)
            events.add(Event.getEventById(tmp));
        
        res = UserConference.getUsersConference(c);
        users.clear();
        for(Integer tmp : res)
            users.add(User.getUserById(tmp));
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Conference getC() {
        return c;
    }

    public void setC(Conference c) {
        this.c = c;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    
}
