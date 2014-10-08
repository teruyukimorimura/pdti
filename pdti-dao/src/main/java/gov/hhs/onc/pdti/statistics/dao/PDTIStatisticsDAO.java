package gov.hhs.onc.pdti.statistics.dao;

import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
@Scope("prototype")
public class PDTIStatisticsDAO {

	/**
	 * entity manager.
	 */
	@Autowired
	@PersistenceContext(unitName = "persistenceUnit")
	private EntityManager entityManager;

    /**
     *
     * @param PDTIStatisticsEntityId
     * @return PDTIStatisticsEntity
     */
    public PDTIStatisticsEntity getPDTIStatisticsEntityById(String PDTIStatisticsEntityId) {
		return entityManager.find(PDTIStatisticsEntity.class, PDTIStatisticsEntityId);
    }

    /**
     *
     * @return List
     */
    public List<PDTIStatisticsEntity> getAllPDTIStatisticsEntity() {
		TypedQuery<PDTIStatisticsEntity> namedQuery =
				entityManager.createNamedQuery("pdtiauditlog.getAll", PDTIStatisticsEntity.class);
		return namedQuery.getResultList();
    }

    /**
     *
     * @param oPDTIStatisticsEntity
     */
	@Transactional
    public void save(PDTIStatisticsEntity oPDTIStatisticsEntity) {
		entityManager.persist(oPDTIStatisticsEntity);
		entityManager.flush();
    }

    /**
     *
     * @param ogetPDTIStatisticsEntityId
     */
	@Transactional
    public void update(PDTIStatisticsEntity ogetPDTIStatisticsEntityId) {
		entityManager.merge(ogetPDTIStatisticsEntityId);
		entityManager.flush();
    }

    /**
     *
     * @param strPDTIStatisticsEntityId
     */
	@Transactional
    public void delete(String strPDTIStatisticsEntityId) {
		PDTIStatisticsEntity pdtiStatisticsEntity =
				entityManager.find(PDTIStatisticsEntity.class, strPDTIStatisticsEntityId);
		entityManager.remove(pdtiStatisticsEntity);
		entityManager.flush();
    }
}
