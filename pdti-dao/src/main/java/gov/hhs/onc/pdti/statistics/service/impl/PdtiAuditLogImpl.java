package gov.hhs.onc.pdti.statistics.service.impl;

import gov.hhs.onc.pdti.statistics.dao.PDTIStatisticsDAO;
import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import gov.hhs.onc.pdti.statistics.service.PdtiAuditLog;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PdtiAuditLogImpl implements PdtiAuditLog {

	@Autowired
    PDTIStatisticsDAO pdtiStatisticsDAO;

    @Override
    public void save(PDTIStatisticsEntity oPDTIStatisticsEntity) {
        pdtiStatisticsDAO.save(oPDTIStatisticsEntity);
    }

    @Override
    public void update(PDTIStatisticsEntity ogetPDTIStatisticsEntityId) {
        pdtiStatisticsDAO.update(ogetPDTIStatisticsEntityId);
    }

    @Override
    public void delete(String strPDTIStatisticsEntityId) {
        pdtiStatisticsDAO.delete(strPDTIStatisticsEntityId);
    }

    @Override
    public List<PDTIStatisticsEntity> getAllPDTIStatisticsEntity() {
        return pdtiStatisticsDAO.getAllPDTIStatisticsEntity();
    }

    @Override
    public PDTIStatisticsEntity getPDTIStatisticsEntityById(String PDTIStatisticsEntityId) {
        return pdtiStatisticsDAO.getPDTIStatisticsEntityById(PDTIStatisticsEntityId);
    }
}
