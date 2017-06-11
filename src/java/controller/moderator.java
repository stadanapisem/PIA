package controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Miljan
 */
@ManagedBean(name = "moderator")
@ViewScoped
public class moderator implements Serializable {

    private String add_event, day_to_add, conf_dur, cer_duration;
    //private Integer cid;
    
    //@ManagedProperty(value = "#{param.cid}")
    private String cid;
    
    public void newCeremony() {
        
    }
    
    public moderator() {
        cid = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nesto");
    }
    
    public void getParameter() {
        
    }
    
    @PostConstruct
    public void load() {
         System.out.println(cid);
    }

    public String getAdd_event() {
        return add_event;
    }

    public void setAdd_event(String add_event) {
        this.add_event = add_event;
    }

    public String getDay_to_add() {
        return day_to_add;
    }

    public void setDay_to_add(String day_to_add) {
        this.day_to_add = day_to_add;
    }

    public String getConf_dur() {
        return conf_dur;
    }

    public void setConf_dur(String conf_dur) {
        this.conf_dur = conf_dur;
    }

    public String getCer_duration() {
        return cer_duration;
    }

    public void setCer_duration(String cer_duration) {
        this.cer_duration = cer_duration;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
    
}
