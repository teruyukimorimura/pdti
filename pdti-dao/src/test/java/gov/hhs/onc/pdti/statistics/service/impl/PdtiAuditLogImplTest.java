package gov.hhs.onc.pdti.statistics.service.impl;

import gov.hhs.onc.pdti.statistics.entity.PDTIStatisticsEntity;
import gov.hhs.onc.pdti.statistics.service.PdtiAuditLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ApplicationContext.xml"})
public class PdtiAuditLogImplTest {

	@Autowired
	PdtiAuditLog pdtiAuditLogService;

	@Test
	public void save() throws Exception {
		PDTIStatisticsEntity entity = new PDTIStatisticsEntity();
		entity.setBaseDn("testDn");
		entity.setCreationDate(new Date());
		entity.setPdRequestType("BatchRequest");
		entity.setStatus("Success");
		pdtiAuditLogService.save(entity);
		List<PDTIStatisticsEntity> allPDTIStatisticsEntity = pdtiAuditLogService.getAllPDTIStatisticsEntity();
		Assert.assertEquals("number of record", 1, allPDTIStatisticsEntity.size());
	}
}
