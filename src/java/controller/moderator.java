package controller;

import database.Ceremony;
import database.Conference;
import database.Event;
import database.Hall;
import database.Lecture;
import database.LectureCeremony;
import database.Programme;
import database.Session;
import database.Workshop;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "moderator")
@ViewScoped
public class moderator implements Serializable {

    private String add_event, day_to_add, start_time;
    private String cid;
    private Conference c;
    private Integer cer_duration, conf_dur;
    private List<Event> currentAgenda = new ArrayList<>();
    private Set<Hall> halls;
    private List<Session> sessionList = new ArrayList<>();
    private List<Ceremony> ceremonyList = new ArrayList<>();

    private String session_title, session_hall, affiliation_session, affiliation_cer;

    private String author1, author2, author3, author4;

    private void resetVariables() {
        author1 = author2 = author3 = author4 = null;
        session_hall = session_title = null;
        cer_duration = 0;
        start_time = day_to_add = null;
        affiliation_session = affiliation_cer = null;
    }

    public String newLecture() {

        if (affiliation_session == null || affiliation_session.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "You must affiliate lecture with a session!"));
            return null;
        }

        Lecture lecture = new Lecture(Session.getSessionsById(Integer.parseInt(affiliation_session)), session_title, cer_duration, author1, author2, author3, author4);

        if (!Lecture.addLecture(lecture)) {
            return "error";
        }

        if (affiliation_cer != null && !affiliation_cer.isEmpty()) {
            Ceremony cer = Ceremony.getCeremonyById(Integer.parseInt(affiliation_cer));
            LectureCeremony lc = new LectureCeremony(cer, lecture);

            if (!LectureCeremony.addLectureCeremony(lc)) {
                return "error";
            }
        }

        updateFields();
        resetVariables();
        return "moderator?faces-redirect=true";
    }

    public String newWorkshop() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(c.getDateBegin());
        cal.add(Calendar.DATE, Integer.parseInt(day_to_add));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(start_time.substring(0, start_time.indexOf(':'))));
        cal.set(Calendar.MINUTE, Integer.parseInt(start_time.substring(start_time.indexOf(':') + 1)));
        Date now = cal.getTime();

        Event event = new Event(Integer.parseInt(day_to_add), now);

        if (!Event.addEvent(event)) {
            return "error";
        }

        Workshop ws = new Workshop(event, session_title, author1, author2, author3, author4, cer_duration);

        if (!Workshop.addWorkshop(ws)) {
            return "error";
        }

        Programme prog = new Programme(c, event);
        if (!Programme.addProgramme(prog)) {
            return "error";
        }

        updateFields();
        resetVariables();
        return "moderator";
    }

    public String newSession() { // TODO Session overlaping
        Calendar cal = Calendar.getInstance();
        cal.setTime(c.getDateBegin());
        cal.add(Calendar.DATE, Integer.parseInt(day_to_add));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(start_time.substring(0, start_time.indexOf(':'))));
        cal.set(Calendar.MINUTE, Integer.parseInt(start_time.substring(start_time.indexOf(':') + 1)));
        Date now = cal.getTime();

        Event event = new Event(Integer.parseInt(day_to_add), now);

        if (!Event.addEvent(event)) {
            return "error";
        }

        Hall hall = null;
        for (Hall tmp : halls) {
            if (tmp.getHid() == Integer.parseInt(session_hall)) {
                hall = tmp;
                break;
            }
        }

        Session session = new Session(event, hall, session_title);

        if (!Session.addSession(session)) {
            return "error";
        }

        Programme prog = new Programme(c, event);
        if (!Programme.addProgramme(prog)) {
            return "error";
        }

        updateFields();
        resetVariables();
        return "moderator?faces-redirect=true";
    }

    public String newCeremony() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(c.getDateBegin());
        cal.add(Calendar.DATE, Integer.parseInt(day_to_add));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(start_time.substring(0, start_time.indexOf(':'))));
        cal.set(Calendar.MINUTE, Integer.parseInt(start_time.substring(start_time.indexOf(':') + 1)));
        Date now = cal.getTime();

        Event event = new Event(Integer.parseInt(day_to_add), now);

        if (!Event.addEvent(event)) {
            return "error";
        }

        Ceremony cer = new Ceremony(event, cer_duration);

        if (!Ceremony.addCeremony(cer)) {
            return "error";
        }

        Programme prog = new Programme(c, event);
        if (!Programme.addProgramme(prog)) {
            return "error";
        }

        updateFields();
        resetVariables();
        return "moderator?faces-redirect=true";
    }

    private void updateFields() {
        currentAgenda.clear();
        c = Conference.getConferenceById(Integer.parseInt(cid));
        conf_dur = Conference.getNumberOfDays(c);
        Set<Programme> programmes = c.getProgrammes();
        for (Programme tmp : programmes) {
            currentAgenda.add(tmp.getEvent());
        }

        Collections.sort(currentAgenda, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                return e1.getStartTime().compareTo(e2.getStartTime());
            }
        });

        halls = c.getLocation().getHalls();

        sessionList.clear();
        List<Integer> tmp_sess = Session.getSessionsForConference(c);
        for (int i = 0; i < tmp_sess.size(); i++) {
            sessionList.add(Session.getSessionsById(tmp_sess.get(i)));
        }

        ceremonyList.clear();
        tmp_sess = Ceremony.getCeremonyForConference(c);
        for (int i = 0; i < tmp_sess.size(); i++) {
            ceremonyList.add(Ceremony.getCeremonyById(tmp_sess.get(i)));
        }
    }

    public moderator() {
        //cid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nesto");
        if(cid != null)
            updateFields();
    }

    
    public void load() {
        if(cid != null)
            updateFields();
    }
    
    public void getParameter() {

    }

    public String getAdd_event() {
        return add_event;
    }

    public void setAdd_event(String add_event) {
        this.add_event = add_event;
    }

    public String getDay_to_add() {
        return day_to_add;
    }

    public void setDay_to_add(String day_to_add) {
        this.day_to_add = day_to_add;
    }

    public Integer getConf_dur() {
        return conf_dur;
    }

    public void setConf_dur(Integer conf_dur) {
        this.conf_dur = conf_dur;
    }

    public Integer getCer_duration() {
        return cer_duration;
    }

    public void setCer_duration(Integer cer_duration) {
        this.cer_duration = cer_duration;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<Integer> getNumbers() {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < conf_dur; i++) {
            values.add(i);
        }

        return values;
    }

    public List<Event> getCurrentAgenda() {
        return currentAgenda;
    }

    public void setCurrentAgenda(List<Event> currentAgenda) {
        this.currentAgenda = currentAgenda;
    }

    public Conference getC() {
        return c;
    }

    public void setC(Conference c) {
        this.c = c;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public Set<Hall> getHalls() {
        return halls;
    }

    public void setHalls(Set<Hall> halls) {
        this.halls = halls;
    }

    public String getSession_title() {
        return session_title;
    }

    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    public String getSession_hall() {
        return session_hall;
    }

    public void setSession_hall(String session_hall) {
        this.session_hall = session_hall;
    }

    public String getAuthor1() {
        return author1;
    }

    public void setAuthor1(String author1) {
        this.author1 = author1;
    }

    public String getAuthor2() {
        return author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }

    public String getAuthor3() {
        return author3;
    }

    public void setAuthor3(String author3) {
        this.author3 = author3;
    }

    public String getAuthor4() {
        return author4;
    }

    public void setAuthor4(String author4) {
        this.author4 = author4;
    }

    public List<Session> getSessionList() {
        return sessionList;
    }

    public void setSessionList(List<Session> sessionList) {
        this.sessionList = sessionList;
    }

    public List<Ceremony> getCeremonyList() {
        return ceremonyList;
    }

    public void setCeremonyList(List<Ceremony> ceremonyList) {
        this.ceremonyList = ceremonyList;
    }

    public String getAffiliation_session() {
        return affiliation_session;
    }

    public void setAffiliation_session(String affiliation_session) {
        this.affiliation_session = affiliation_session;
    }

    public String getAffiliation_cer() {
        return affiliation_cer;
    }

    public void setAffiliation_cer(String affiliation_cer) {
        this.affiliation_cer = affiliation_cer;
    }

}
