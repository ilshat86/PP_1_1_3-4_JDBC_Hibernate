package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.DbConnectionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = DbConnectionManager.getSessionFactory();
    }

    @Override
    public void createUsersTable() {
        final String CREATE_USER_SQL_TEMPLATE = "create TABLE IF NOT EXISTS User (id INT auto_increment primary key, name VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, age TINYINT)";
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(CREATE_USER_SQL_TEMPLATE);
            query.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
    }

    @Override
    public void dropUsersTable() {
        final String DROP_USER_SQL_TEMPLATE = "drop TABLE IF EXISTS User";
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createSQLQuery(DROP_USER_SQL_TEMPLATE);
            query.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(User.class);
            Root<User> root = cq.from(User.class);
            Query query = session.createQuery(cq);
            List<User> list = query.getResultList();
            tx.commit();
            return list;
        } catch (Exception ex) {
            tx.rollback();
        }

        return new ArrayList<>();
    }

    @Override
    public void cleanUsersTable() {
        final String TRUNCATE_USER_SQL_TEMPLATE = "TRUNCATE TABLE User";
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.createSQLQuery(TRUNCATE_USER_SQL_TEMPLATE).executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
    }
}

