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
 * Comment generated by hbm2java
 */
@Entity
@Table(name="comment"
    ,catalog="projekat"
    , uniqueConstraints = @UniqueConstraint(columnNames={"cid", "eid", "uid"}) 
)
public class Comment  implements java.io.Serializable {


     private Integer cid;
     private Event event;
     private User user;
     private String text;

    public Comment() {
    }

    public Comment(Event event, User user, String text) {
       this.event = event;
       this.user = user;
       this.text = text;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="cid", unique=true, nullable=false)
    public Integer getCid() {
        return this.cid;
    }
    
    public void setCid(Integer cid) {
        this.cid = cid;
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

    
    @Column(name="text", nullable=false, length=65535)
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }




}


