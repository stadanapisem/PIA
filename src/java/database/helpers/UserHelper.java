package database.helpers;

import org.hibernate.Session;
import util.NewHibernateUtil;

/**
 *
 * @author Miljan
 */
public class UserHelper {
    private Session session = null;
    
    public UserHelper() {
        session = NewHibernateUtil.getSessionFactory().getCurrentSession();
    }
}
