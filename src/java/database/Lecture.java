package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.Query;
import org.hibernate.Transaction;
import util.NewHibernateUtil;

/**
 * Lecture generated by hbm2java
 */
@Entity
@Table(name = "lecture",
        catalog = "projekat"
)
public class Lecture implements java.io.Serializable {

    private Integer lid;
    private Session session;
    private String title;
    private int duration;
    private String author1;
    private String author2;
    private String author3;
    private String author4;
    private String file1;
    private String file2;
    private Set<Grade> grades = new HashSet<Grade>(0);
    private Set<LectureCeremony> lectureCeremonies = new HashSet<LectureCeremony>(0);

    public static boolean sameLectureInConference(String title, Conference C) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT L.lid FROM Lecture L, Session S, Event E, Programme P WHERE L.lid = S.lid AND S.eid = E.eid AND P.eid = E.eid AND P.cid = :cid AND L.title = :title");
            query.setParameter("title", title);
            query.setParameter("cid", C.getCid());
            
            List<Integer> res = query.list();
            
            if(res != null && res.size() > 0)
                ret = false;
            else if(res != null && res.size() == 0)
                ret = true;
            
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ret;
    }
    
    public static boolean updateLecture(Lecture c) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            session.update(c);
            tx.commit();
            ret = true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ret;
    }

    public static Lecture getLectureById(Integer id) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Lecture ret = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Lecture L WHERE L.lid = :lid");
            query.setParameter("lid", id);

            List<Lecture> ans = query.list();

            if (ans != null && ans.size() > 0) {
                ret = ans.get(0);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ret;
    }

    public static boolean addLecture(Lecture c) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            session.save(c);
            tx.commit();
            ret = true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ret;
    }

    public static List<Lecture> getAllLecturesForSession(Session s) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Lecture> ret = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Lecture L WHERE L.session = :eid");
            query.setParameter("eid", s);

            ret = query.list();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return ret;
    }

    public Lecture() {
    }

    public Lecture(Session session, String title, int duration, String author1, String author2, String author3, String author4) {
        this.session = session;
        this.title = title;
        this.duration = duration;
        this.author1 = author1;
        this.author2 = author2;
        this.author3 = author3;
        this.author4 = author4;
    }

    public Lecture(Session session, String title, int duration, String file1, String file2) {
        this.session = session;
        this.title = title;
        this.duration = duration;
        this.file1 = file1;
        this.file2 = file2;
    }

    public Lecture(Session session, String title, int duration, String author1, String author2, String author3, String author4, String file1, String file2, Set<Grade> grades, Set<LectureCeremony> lectureCeremonies) {
        this.session = session;
        this.title = title;
        this.duration = duration;
        this.author1 = author1;
        this.author2 = author2;
        this.author3 = author3;
        this.author4 = author4;
        this.file1 = file1;
        this.file2 = file2;
        this.grades = grades;
        this.lectureCeremonies = lectureCeremonies;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "lid", unique = true, nullable = false)
    public Integer getLid() {
        return this.lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eid", nullable = false)
    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "duration", nullable = false)
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Column(name = "author1", length = 60)
    public String getAuthor1() {
        return this.author1;
    }

    public void setAuthor1(String author1) {
        this.author1 = author1;
    }

    @Column(name = "author2", length = 60)
    public String getAuthor2() {
        return this.author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }

    @Column(name = "author3", length = 60)
    public String getAuthor3() {
        return this.author3;
    }

    public void setAuthor3(String author3) {
        this.author3 = author3;
    }

    @Column(name = "author4", length = 60)
    public String getAuthor4() {
        return this.author4;
    }

    public void setAuthor4(String author4) {
        this.author4 = author4;
    }

    @Column(name = "file1", length = 200)
    public String getFile1() {
        return this.file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    @Column(name = "file2", length = 200)
    public String getFile2() {
        return this.file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lecture")
    public Set<Grade> getGrades() {
        return this.grades;
    }

    public void setGrades(Set<Grade> grades) {
        this.grades = grades;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lecture")
    public Set<LectureCeremony> getLectureCeremonies() {
        return this.lectureCeremonies;
    }

    public void setLectureCeremonies(Set<LectureCeremony> lectureCeremonies) {
        this.lectureCeremonies = lectureCeremonies;
    }

}
