/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.onc.pdti.statistics.service.impl;

import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import gov.hhs.onc.pdti.statistics.service.PdtiAuditLog;
import java.util.Date;

/**
 *
 * @author Wilmertech
 */
public class PdtiAuditLogImplTest {
    public static void main(String args[])
    {
        try
        {
            PdtiAuditLog pdtiAuditLogService = PdtiAuditLogImpl.getInstance();
            if(pdtiAuditLogService != null)
            {
                PDTIStatisticsEntity entity = new PDTIStatisticsEntity();                
                entity.setBaseDn("testDn");
                entity.setCreationDate(new Date());
                entity.setPdRequestType("BatchRequest");
                entity.setSourceDomain("testSrcDomain");
                entity.setStatus("Success");
                pdtiAuditLogService.save(entity);
                System.out.println("Spring Context Bean Initialized...");
            }
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }    
}
