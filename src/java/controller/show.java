package controller;

import database.Agenda;
import database.Ceremony;
import database.Comment;
import database.Conference;
import database.Event;
import database.Favourites;
import database.Grade;
import database.Lecture;
import database.LectureCeremony;
import database.LikeEvent;
import database.Message;
import database.Moderator;
import database.Photo;
import database.User;
import database.UserConference;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RateEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "show")
@ViewScoped
public class show {

    private Integer cid, day, eid;

    private Conference c;
    private Event gal_e;
    private List<Event> events = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<Lecture> lectures;// = new ArrayList<>();
    private List<Event> myAgenda = new ArrayList<>();
    private List<Integer> ratings = new ArrayList<>();
    private List<Comment> comments;
    private List<String> photos = new ArrayList<>();
    private List<String> linksForGallery;
    private Integer rating;
    private String comment_text;
    private Integer lid;
    private String prof_name, prof_pic, prof_email, prof_inst, prof_link;

    @ManagedProperty(value = "#{login}")
    private login login;

    public void updateProfileDialog(String author) {
        List<User> users = User.getSomeUsers(author);
        
        if(users.size() == 1) {
            prof_name = users.get(0).getName() + " " + users.get(0).getSurname();
            prof_pic = users.get(0).getProfilePicture();
            prof_email = users.get(0).getEmail();
            prof_inst = users.get(0).getInstitution();
            prof_link = users.get(0).getLinkedin();
        }
    }
    
    public String getEventName(String url) {
        Integer id = Integer.parseInt(url.substring(url.indexOf("id=") + 3, url.indexOf("&cid=")));
        Event ev = Event.getEventById(id);

        if (ev != null) {
            if (ev.getCeremony() != null && ev.getCeremony().getEid() == ev.getEid()) {
                return ev.getStartTime().toString();
            }

            if (ev.getSession() != null && ev.getSession().getEid() == ev.getEid()) {
                return ev.getSession().getTitle();
            }

            if (ev.getWorkshop() != null && ev.getWorkshop().getEid() == ev.getEid()) {
                return ev.getWorkshop().getTitle();
            }
        }
        return "Default";
    }

    public String AddressForFacebook() {
        return "https://www.facebook.com/plugins/share_button.php?href=http://localhost:8080/PIApouksaj8/faces/agenda.xhtml&layout=button&size=small&mobile_iframe=true&width=58&height=20&appId";
    }

    public boolean shouldExpand(Integer eid) {
        //System.out.println(eid);

        if (this.eid != null) {
            if (this.eid == eid) {
                return true;
            }

            return false;
        }

        return false;
    }

    public StreamedContent getDownloadFile(String path) {
        try {
            InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/" + path);
            String name = path.substring(5, path.indexOf('.'));
            String contentType = Files.probeContentType(Paths.get(path));
            if (contentType != null) {
                return new DefaultStreamedContent(stream, contentType, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setLidForUpload() {
        lid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lid"));
    }

    public void handleUploadFile(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        String fileName = uploadedFile.getFileName();
        byte[] contents = uploadedFile.getContents();
        try {

            Path folder = Paths.get("C:\\Users\\Miljan\\Documents\\NetBeansProjects\\PIApouksaj8\\web\\docs");
            String extention = fileName.substring(fileName.indexOf('.')); // Uradi isto sto imas i u home strani da prosledis argument pa update query
            Path file = Files.createTempFile(folder, fileName + "-", extention);
            String path_to_insert = file.toString().substring(file.toString().indexOf("docs"));
            InputStream in = uploadedFile.getInputstream();
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded Successfully"));

            Lecture lec = Lecture.getLectureById(lid);
            if (lec.getFile1() == null) {
                lec.setFile1(path_to_insert);
            } else if (lec.getFile2() == null) {
                lec.setFile2(path_to_insert);
            }

            if (!Lecture.updateLecture(lec)) {
                throw new ValidatorException(new FacesMessage("Error during upload!"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isItMe(String query) { // It's a me Mario
        List<User> tmpusers = User.getSomeUsers(query);

        if (tmpusers.size() == 1 && tmpusers.get(0).getUid() == login.getUser().getUid()) {
            return true;
        }

        return false;
    }

    public boolean isAuthorInSystem(String query) {
        List<User> tmpusers = User.getSomeUsers(query);

        if (tmpusers.size() == 1) {
            return true;
        }

        return false;
    }

    public String addToFavourites() {
        Integer uid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("uid"));
        Favourites fav = new Favourites(User.getUserById(uid), login.getUser());

        if (!Favourites.addFavourite(fav)) {
            return "error";
        }

        return null;
    }

    public String addComment() {
        Integer eid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eid"));
        Event event = Event.getEventById(eid);

        Comment comm = new Comment(event, login.getUser(), comment_text, new Date());

        if (!Comment.addComment(comm)) {
            return "error";
        }

        comment_text = "";
        return null;
    }

    public String unlike() {
        Integer eid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eid"));

        Event ev = Event.getEventById(eid);
        LikeEvent lev = LikeEvent.getLikeForUserAndEvent(login.getUser(), ev);
        LikeEvent.deleteLike(lev);

        return null;
    }

    public String like() {
        Integer eid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eid"));

        Event ev = Event.getEventById(eid);
        LikeEvent le = new LikeEvent(ev, login.getUser());

        if (!LikeEvent.addLike(le)) {
            return "error";
        }

        return null;
    }

    public String onRate(RateEvent rateEvent) {
        Integer lid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lid"));
        Integer grade = (Integer) rateEvent.getRating();

        Lecture l = Lecture.getLectureById(lid);
        for (int i = 0; i < lectures.size(); i++) {
            if (lectures.get(i).getLid() == lid) {
                ratings.set(i, grade);
                break;
            }
        }
        /*Grade g_old = Grade.getGrade(login.getUser(), l);
        if(g_old.getGrade() != 0) {
            g_old.setGid(grade);
            if(!Grade.updateGrade(g_old))
                return "error";
            
            return null;
        }*/

        Grade g = new Grade(l, login.getUser(), grade.byteValue());

        if (!Grade.addGrade(g)) {
            return "error";
        }
        rating = 0;
        return null;
    }

    public String addToMyAgenda() {
        Integer eid = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("eid"));
        Event add = null;
        for (Event tmp : events) {
            if (tmp.getEid() == eid) {
                add = tmp;
            }
        }

        Agenda ag = new Agenda(add, login.getUser());

        if (!Agenda.addEventForUser(ag)) {
            return "error";
        }

        return null;
    }

    public void onRowToggle(ToggleEvent tog) {
        if (tog.getVisibility() == Visibility.VISIBLE) {
            //System.out.println(tog.getData().toString());

            Event ev = (Event) tog.getData();
            if (ev.getCeremony() != null) {
                Ceremony cer = ev.getCeremony();
                lectures = LectureCeremony.getAllLecturesForCeremony(cer);
            } else if (ev.getSession() != null) {
                lectures = Lecture.getAllLecturesForSession(ev.getSession());
            } else if (lectures != null) {
                lectures.clear();
            }

            if (lectures != null) {
                for (int i = 0; i < lectures.size(); i++) {
                    Grade tmp = Grade.getGrade(login.getUser(), lectures.get(i));
                    if (tmp != null) {
                        ratings.add((int) tmp.getGrade());
                    } else {
                        ratings.add(0);
                    }
                }
            }

            comments = Comment.getAllCommentsForEvent(ev);
        }
    }

    public show() {
    }

    @PostConstruct
    public void checkLogin() {
        try {
            if (login == null || (login != null && login.getUser() == null)) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void load() {

        try {
            if (login.getUser() == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true&conf_id=" + cid + "&eid=" + eid);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        c = Conference.getConferenceById(cid);
        List<Integer> res = Event.getAllEventsForConference(c);

        events.clear();
        for (Integer tmp : res) {
            events.add(Event.getEventById(tmp));
        }

        Collections.sort(events, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                return e1.getStartTime().compareTo(e2.getStartTime());
            }
        });

        res = UserConference.getUsersConference(c);
        users.clear();
        for (Integer tmp : res) {
            users.add(User.getUserById(tmp));
        }

        myAgenda.clear();
        List<Integer> eventsInConference = Event.getAllEventsForConference(c);
        List<Agenda> aglist = Agenda.getMyAgenda(login.getUser());
        for (Agenda tmp : aglist) {
            for(int i = 0; i < eventsInConference.size(); i++)
                if(eventsInConference.get(i) == tmp.getEvent().getEid())
                    myAgenda.add(tmp.getEvent());
        }

        /**
         * if (eid != null) { pre_expanded = new ArrayList<>();
         * pre_expanded.add(Event.getEventById(eid)); }
         */
    }

    public void loadGallery() {
        gal_e = Event.getEventById(eid);
        List<Photo> photo = Photo.getAllCommentsForEvent(gal_e);
        photos.clear();

        for (Photo p : photo) {
            photos.add(p.getPath());
        }

        //get all events for this day
        if (eid == null && day != null) {
            linksForGallery = new ArrayList<>();
            List<Integer> allForConf = Event.getAllEventsForConference(Conference.getConferenceById(cid));

            for (Integer tmp : allForConf) {
                if (Event.getEventById(tmp).getDay() == day) {
                    linksForGallery.add("gallery?day=" + day + "&id=" + tmp + "&cid=" + cid);
                }
            }
        } else if (eid == null && day == null && cid != null) {
            linksForGallery = new ArrayList<>();
            Integer numofdays = Conference.getNumberOfDays(Conference.getConferenceById(cid));

            for (int i = 1; i <= numofdays; i++) {
                linksForGallery.add("gallery?cid=" + cid + "&day=" + i);
            }
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

            InputStream in = uploadedFile.getInputstream();
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);

            Photo photo = new Photo(gal_e, file.toString().substring(file.toString().indexOf("pics")));
            Photo.addPhoto(photo);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Uploaded Successfully"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInFavourites(Integer uid) {
        if (login.getUser().getUid() == uid) {
            return true;
        }

        return Favourites.isInMyFavourites(login.getUser(), User.getUserById(uid));
    }

    public boolean canAddPhotos() {
        // mod || event in agenda
        boolean ret = true;
        if (!Agenda.isEventInMyAgenda(gal_e, login.getUser())) {
            ret = false;
        }

        List<Integer> modOn = Moderator.getModeratedForUser(login.getUser());

        for (Integer tmp : modOn) {
            if (tmp == cid) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public boolean isLiked(Integer eid) {
        LikeEvent le = LikeEvent.getLikeForUserAndEvent(login.getUser(), Event.getEventById(eid));

        if (le != null) {
            return true;
        }

        return false;
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

    public List<Event> getMyAgenda() {
        return myAgenda;
    }

    public void setMyAgenda(List<Event> myAgenda) {
        this.myAgenda = myAgenda;
    }

    public login getLogin() {
        return login;
    }

    public void setLogin(login login) {
        this.login = login;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Event getGal_e() {
        return gal_e;
    }

    public void setGal_e(Event gal_e) {
        this.gal_e = gal_e;
    }

    public List<String> getLinksForGallery() {
        return linksForGallery;
    }

    public void setLinksForGallery(List<String> linksForGallery) {
        this.linksForGallery = linksForGallery;
    }

    public String getProf_name() {
        return prof_name;
    }

    public void setProf_name(String prof_name) {
        this.prof_name = prof_name;
    }

    public String getProf_pic() {
        return prof_pic;
    }

    public void setProf_pic(String prof_pic) {
        this.prof_pic = prof_pic;
    }

    public String getProf_email() {
        return prof_email;
    }

    public void setProf_email(String prof_email) {
        this.prof_email = prof_email;
    }

    public String getProf_inst() {
        return prof_inst;
    }

    public void setProf_inst(String prof_inst) {
        this.prof_inst = prof_inst;
    }

    public String getProf_link() {
        return prof_link;
    }

    public void setProf_link(String prof_link) {
        this.prof_link = prof_link;
    }

}
