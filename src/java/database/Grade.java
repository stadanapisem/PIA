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
 * Grade generated by hbm2java
 */
@Entity
@Table(name = "grade",
        catalog = "projekat",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lid", "uid"})
)
public class Grade implements java.io.Serializable {

    private Integer gid;
    private Lecture lecture;
    private User user;
    private byte grade;

    public static boolean updateGrade(Grade g) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("UPDATE Grade SET grade = :grade WHERE gid = :gid");
            query.setParameter("grade", g.getGrade());
            query.setParameter("gid", g.getGid());

            int res = query.executeUpdate();

            if (res == 0) {
                ret = false;
            } else {
                ret = true;
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

    public static Grade getGrade(User u, Lecture l) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Grade ret = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Grade G WHERE G.user = :user AND G.lecture = :lecture");
            query.setParameter("user", u);
            query.setParameter("lecture", l);

            List<Grade> grades = query.list();

            if (grades != null && grades.size() > 0) {
                ret = grades.get(0);
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

    public static boolean addGrade(Grade g) {
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean ret = false;

        try {
            tx = session.beginTransaction();
            session.save(g);
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

    public Grade() {
    }

    public Grade(Lecture lecture, User user, byte grade) {
        this.lecture = lecture;
        this.user = user;
        this.grade = grade;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "gid", unique = true, nullable = false)
    public Integer getGid() {
        return this.gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lid", nullable = false)
    public Lecture getLecture() {
        return this.lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "grade", nullable = false)
    public byte getGrade() {
        return this.grade;
    }

    public void setGrade(byte grade) {
        this.grade = grade;
    }

}
