package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.Query;
import org.hibernate.Transaction;
import util.NewHibernateUtil;

/**
 * Conference generated by hbm2java
 */
@Entity
@Table(name = "conference",
        catalog = "projekat"
)
public class Conference implements java.io.Serializable {

    private Integer cid;
    private Location location;
    private String name;
    private String password;
    private String salt;
    private int iterations;
    private Date dateBegin;
    private Date dateEnd;
    private String area;
    private Set<Programme> programmes = new HashSet<Programme>(0);
    private Set<Moderator> moderators = new HashSet<Moderator>(0);

    public static Conference getConferenceById(Integer id) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Conference ret = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Conference C WHERE C.cid = :cid");
            query.setParameter("cid", id);

            List<Conference> result = query.list();
            if (result != null && result.size() > 0) {
                ret = (result.get(0));
            }
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

    public static boolean deleteConference(Conference c) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery("DELETE FROM Conference WHERE cid = :cid");
            query.setParameter("cid", c.getCid());
            
            query.executeUpdate();
            
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

    public static List<Conference> getAllConferences() {
        List<Conference> conf = null;
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Conference C");
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

    public static boolean addConference(Conference c) {
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

    public static int getNumberOfDays(Conference c) {
        long diff = c.dateEnd.getTime() - c.dateBegin.getTime();
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public Conference() {
    }

    public Conference(Location location, String name, String password, String salt, int iterations, Date dateBegin, Date dateEnd, String area) {
        this.location = location;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.iterations = iterations;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.area = area;
    }

    public Conference(Location location, String name, String password, String salt, int iterations, Date dateBegin, Date dateEnd, String area, Set<Programme> programmes, Set<Moderator> moderators) {
        this.location = location;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.iterations = iterations;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.area = area;
        this.programmes = programmes;
        this.moderators = moderators;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "cid", unique = true, nullable = false)
    public Integer getCid() {
        return this.cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lid", nullable = false)
    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "salt", nullable = false, length = 30)
    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Column(name = "iterations", nullable = false)
    public int getIterations() {
        return this.iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date_begin", nullable = false, length = 10)
    public Date getDateBegin() {
        return this.dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date_end", nullable = false, length = 10)
    public Date getDateEnd() {
        return this.dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Column(name = "area", nullable = false, length = 50)
    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conference")
    public Set<Programme> getProgrammes() {
        return this.programmes;
    }

    public void setProgrammes(Set<Programme> programmes) {
        this.programmes = programmes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conference")
    public Set<Moderator> getModerators() {
        return this.moderators;
    }

    public void setModerators(Set<Moderator> moderators) {
        this.moderators = moderators;
    }

}
