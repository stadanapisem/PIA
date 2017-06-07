package database;
// Generated Jun 7, 2017 5:07:06 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Ceremony generated by hbm2java
 */
@Entity
@Table(name="ceremony"
    ,catalog="projekat"
)
public class Ceremony  implements java.io.Serializable {


     private int eid;
     private Event event;
     private int duration;
     private Set<LectureCeremony> lectureCeremonies = new HashSet<LectureCeremony>(0);

    public Ceremony() {
    }

	
    public Ceremony(Event event, int duration) {
        this.event = event;
        this.duration = duration;
    }
    public Ceremony(Event event, int duration, Set<LectureCeremony> lectureCeremonies) {
       this.event = event;
       this.duration = duration;
       this.lectureCeremonies = lectureCeremonies;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="event"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="eid", unique=true, nullable=false)
    public int getEid() {
        return this.eid;
    }
    
    public void setEid(int eid) {
        this.eid = eid;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }

    
    @Column(name="duration", nullable=false)
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="ceremony")
    public Set<LectureCeremony> getLectureCeremonies() {
        return this.lectureCeremonies;
    }
    
    public void setLectureCeremonies(Set<LectureCeremony> lectureCeremonies) {
        this.lectureCeremonies = lectureCeremonies;
    }




}


