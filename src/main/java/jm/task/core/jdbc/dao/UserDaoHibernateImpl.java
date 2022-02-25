package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DbConnectionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;

    private static final String DROP_USER_SQL_TEMPLATE = "drop TABLE IF EXISTS User";
    private static final String CREATE_USER_SQL_TEMPLATE = "create TABLE IF NOT EXISTS User (id INT auto_increment primary key, name VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, age TINYINT)";
    private static final String DELETE_USER_SQL_TEMPLATE = "DELETE FROM User where id = :id";
    private static final String TRUNCATE_USER_SQL_TEMPLATE = "TRUNCATE TABLE User";

    public UserDaoHibernateImpl() {
        this.sessionFactory = DbConnectionManager.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createSQLQuery(CREATE_USER_SQL_TEMPLATE);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createSQLQuery(DROP_USER_SQL_TEMPLATE);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = new User(name, lastName, age);
        session.save(user);
        session.getTransaction().commit();
        //session.close();
    }

    @Override
    public void removeUserById(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.remove(user);
        session.getTransaction().commit();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        Query query = session.createQuery(cq);
        List<User> list = query.getResultList();
        session.getTransaction().commit();
        return list;
    }

    @Override
    public void cleanUsersTable() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.createSQLQuery(TRUNCATE_USER_SQL_TEMPLATE).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
