package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1

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
import org.hibernate.Query;
import org.hibernate.Transaction;
import util.NewHibernateUtil;

/**
 * Agenda generated by hbm2java
 */
@Entity
@Table(name = "agenda",
        catalog = "projekat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "eid"})
)
public class Agenda implements java.io.Serializable {

    private Integer agid;
    private Event event;
    private User user;

    public static List<User> allUsersForConference(Conference c) {
        List<User> u = null;
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Agenda A, Event E, Programme P WHERE A.eid = E.eid AND E.eid = P.eid AND P.cid = :cid");
            query.setParameter("cid", c.getCid());
            u = query.list();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

        return u;
    }

    public static List<Integer> allConferencesForUser(User u) {
        List<Integer> conf = null;
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            //Query query = session.createQuery("FROM Conference C, Agenda A, Event E, Programme P WHERE A.uid = :uid AND A.eid = E.eid AND P.eid = E.eid AND P.cid = C.cid GROUP By C.cid");
            Query query = session.createSQLQuery("SELECT DISTINCT(C.cid) FROM Conference C, Agenda A, Event E, Programme P WHERE A.edi = E.edi AND E.edi = P.edi AND P.cid = C.cid ");
            //query.setParameter("uid", u.getUid());
            conf = query.list();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

        return conf;
    }

    public Agenda() {
    }

    public Agenda(Event event, User user) {
        this.event = event;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "agid", unique = true, nullable = false)
    public Integer getAgid() {
        return this.agid;
    }

    public void setAgid(Integer agid) {
        this.agid = agid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eid", nullable = false)
    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
