package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1


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

/**
 * LikeEvent generated by hbm2java
 */
@Entity
@Table(name="like_event"
    ,catalog="projekat"
    , uniqueConstraints = @UniqueConstraint(columnNames={"eid", "uid"}) 
)
public class LikeEvent  implements java.io.Serializable {


     private Integer leid;
     private Event event;
     private User user;

    public LikeEvent() {
    }

    public LikeEvent(Event event, User user) {
       this.event = event;
       this.user = user;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="leid", unique=true, nullable=false)
    public Integer getLeid() {
        return this.leid;
    }
    
    public void setLeid(Integer leid) {
        this.leid = leid;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eid", nullable=false)
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="uid", nullable=false)
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }




}


