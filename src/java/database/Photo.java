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

/**
 * Photo generated by hbm2java
 */
@Entity
@Table(name="photo"
    ,catalog="projekat"
)
public class Photo  implements java.io.Serializable {


     private Integer pid;
     private Event event;
     private String path;

    public Photo() {
    }

    public Photo(Event event, String path) {
       this.event = event;
       this.path = path;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="pid", unique=true, nullable=false)
    public Integer getPid() {
        return this.pid;
    }
    
    public void setPid(Integer pid) {
        this.pid = pid;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="eid", nullable=false)
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }

    
    @Column(name="path", nullable=false, length=150)
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }




}


