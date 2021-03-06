package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import util.NewHibernateUtil;

/**
 * Agenda generated by hbm2java
 */
@Entity
@Table(name = "user_conference",
        catalog = "projekat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "cid"})
)
public class UserConference implements java.io.Serializable {

    private Integer ucid;
    private Conference conference;
    private User user;

    public static boolean addUserConference(UserConference uc) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            session.save(uc);
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

    public static List<Integer> getUserConferences(User u) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Integer> ret = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            //Query query = session.createQuery("FROM Conference C, UserConference UC WHERE C.cid = UC.cid AND UC.uid = :uid");
            //query.setParameter("uid", u.getUid());
            Query query = session.createSQLQuery("SELECT DISTINCT(C.cid) FROM conference C, user_conference uc WHERE C.cid = uc.cid AND UC.uid = :uid");
            query.setParameter("uid", u.getUid());
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

    public static List<Integer> getUsersConference(Conference c) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        List<Integer> ret = new ArrayList<>();

        try {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT DISTINCT(U.uid) FROM User U, user_conference uc WHERE U.uid = uc.uid AND uc.cid = :cid");
            query.setParameter("cid", c.getCid());
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

    public UserConference() {
    }

    public UserConference(Conference event, User user) {
        this.conference = event;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "ucid", unique = true, nullable = false)
    public Integer getUcid() {
        return this.ucid;
    }

    public void setUcid(Integer agid) {
        this.ucid = agid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cid", nullable = false)
    public Conference getConference() {
        return this.conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
