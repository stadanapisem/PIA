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
 * Favourites generated by hbm2java
 */
@Entity
@Table(name = "favourites",
         catalog = "projekat",
         uniqueConstraints = @UniqueConstraint(columnNames = {"fid", "uid1", "uid2"})
)
public class Favourites implements java.io.Serializable {

    private Integer fid;
    private User userByUid2;
    private User userByUid1;
    
    public static boolean addFavourite(Favourites f) {
        boolean ret = false;
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            session.save(f);
            tx.commit();
            ret = true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

        return ret;
    }

    public static boolean isInMyFavourites(User me, User u) {
        boolean ret = false;
        org.hibernate.Session session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Favourites F WHERE F.userByUid1 = :user1 AND F.userByUid2 = :user2");
            query.setParameter("user1", me);
            query.setParameter("user2", u);
            
            List<Favourites> ag = query.list();
            
            if(ag != null && ag.size() > 0)
                ret = true;

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }

        return ret;
    }
    
    public Favourites() {
    }

    public Favourites(User userByUid2, User userByUid1) {
        this.userByUid2 = userByUid2;
        this.userByUid1 = userByUid1;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "fid", unique = true, nullable = false)
    public Integer getFid() {
        return this.fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid2", nullable = false)
    public User getUserByUid2() {
        return this.userByUid2;
    }

    public void setUserByUid2(User userByUid2) {
        this.userByUid2 = userByUid2;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid1", nullable = false)
    public User getUserByUid1() {
        return this.userByUid1;
    }

    public void setUserByUid1(User userByUid1) {
        this.userByUid1 = userByUid1;
    }

}
