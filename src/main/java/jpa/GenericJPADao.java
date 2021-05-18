package jpa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Java Persistance API minta.
 * @param <T> T típusú entitás.
 */
public abstract class GenericJPADao<T> {
    protected Class<T> entityClass;
    protected EntityManager entityManager;

    /**
     * A {@link GenericJPADao} settere.
     * @param entityClass T típusú entitás.
     */
    public GenericJPADao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * {@link EntityManager} getter.
     * @return Visszatér egy entityManager-rel.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Setter metódus.
     * @param entityManager Egy entityManagert kap paraméterül.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Adatbázisba történő tárolás.
     * @param entity T típusú entitás.
     */
    public void persist(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    /**
     * Visszatér egy lehetséges eredménnyel.
     * @param primaryKey Elsődleges kulcs.
     * @return A megtalált entitás vagy {@link Optional#empty()}.
     */
    public Optional<T> find(Object primaryKey) {
        return Optional.ofNullable(entityManager.find(entityClass, primaryKey));
    }

    /**
     * Visszatér a T típusú entitás listájával.
     * @return A T típusú entitás listája vagy {@link Collections#emptyList()}.
     */
    public List<T> findAll() {
        TypedQuery<T> typedQuery = entityManager.createQuery("FROM " + entityClass.getSimpleName(), entityClass);
        return typedQuery.getResultList();
    }

    /**
     * Töröl egy entitást az adatbázisból.
     * @param entity T típusú entitás.
     */
    public void remove(T entity) {
        entityManager.getTransaction().begin();
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }
}