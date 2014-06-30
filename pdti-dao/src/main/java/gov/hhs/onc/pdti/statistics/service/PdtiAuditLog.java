/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.onc.pdti.statistics.service;

import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import java.util.List;

/**
 *
 * @author Wilmertech
 */
public interface PdtiAuditLog {
    public void save(PDTIStatisticsEntity oPDTIStatisticsEntity);
    public void update(PDTIStatisticsEntity ogetPDTIStatisticsEntityId);
    public void delete(String strPDTIStatisticsEntityId);
    public List<PDTIStatisticsEntity> getAllPDTIStatisticsEntity();
    public PDTIStatisticsEntity getPDTIStatisticsEntityById(String PDTIStatisticsEntityId);    
}
