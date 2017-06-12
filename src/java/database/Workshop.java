package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import util.NewHibernateUtil;

/**
 * Workshop generated by hbm2java
 */
@Entity
@Table(name = "workshop",
        catalog = "projekat"
)
public class Workshop implements java.io.Serializable {

    private int eid;
    private Event event;
    private String title;
    private String author1;
    private String author2;
    private String author3;
    private String author4;
    private int duration;

    public static boolean addWorkshop(Workshop c) {
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
    
    public Workshop() {
    }

    public Workshop(Event event, String title) {
        this.event = event;
        this.title = title;
    }

    public Workshop(Event event, String title, String author1, String author2, String author3, String author4, Integer duration) {
        this.event = event;
        this.title = title;
        this.author1 = author1;
        this.author2 = author2;
        this.author3 = author3;
        this.author4 = author4;
        this.duration = duration;
    }

    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "event"))
    @Id
    @GeneratedValue(generator = "generator")

    @Column(name = "eid", unique = true, nullable = false)
    public int getEid() {
        return this.eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Column(name = "title", nullable = false, length = 100)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Column(name = "duration", nullable = false)
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
