package result;

import jpa.GenericJPADao;
import result.model.DataModel;

import javax.persistence.Persistence;
import java.util.List;

public class DataDao extends GenericJPADao<DataModel> {
    private static DataDao instance;

    private DataDao() {
        super(DataModel.class);
    }

    /**
     * Visszaadja az osztály egyetlen létező példányát.
     * @return Az osztály példánya (instance).
     */
    public static DataDao getInstance() {
        if (instance == null) {
            instance = new DataDao();
            instance.setEntityManager(Persistence.createEntityManagerFactory("koltsegek").createEntityManager());
        }
        return instance;
    }

    /**
     * Lekéri az adatbázisban található entitások listáját.
     * @return {@link DataModel} típusú entitások listája.
     */
    public List<DataModel> findAll() {
        return entityManager.createQuery("SELECT r FROM DataModel r order by r.created DESC", DataModel.class)
                .getResultList();
    }

}
