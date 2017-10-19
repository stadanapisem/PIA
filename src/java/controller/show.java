package controller;

import database.Ceremony;
import database.Conference;
import database.Event;
import database.Lecture;
import database.LectureCeremony;
import database.User;
import database.UserConference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "show")
@ViewScoped
public class show {

    private Integer cid;

    private Conference c;
    private List<Event> events = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Lecture> lectures;// = new ArrayList<>();

    public void onRowToggle(ToggleEvent tog) {
        if (tog.getVisibility() == Visibility.VISIBLE) {
            System.out.println(tog.getData().toString());
            // TODO Get all lectures for this event
//            lectures.clear();
            Event ev = (Event) tog.getData();
            if(ev.getCeremony() != null) {
                Ceremony cer = ev.getCeremony();
                lectures = LectureCeremony.getAllLecturesForCeremony(cer);
            }
            
            if(ev.getSession() != null) {
                lectures = Lecture.getAllLecturesForSession(ev.getSession());
            }
        }
    }

    public show() {
    }

    public void load() {
        c = Conference.getConferenceById(cid);
        List<Integer> res = Event.getAllEventsForConference(c);

        events.clear();
        for (Integer tmp : res) {
            events.add(Event.getEventById(tmp));
        }

        res = UserConference.getUsersConference(c);
        users.clear();
        for (Integer tmp : res) {
            users.add(User.getUserById(tmp));
        }
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

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

}
