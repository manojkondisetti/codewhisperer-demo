/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.web.svclayer.catstatus.support;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opennms.netmgt.config.categories.Category;
import org.opennms.netmgt.config.viewsdisplay.Section;
import org.opennms.netmgt.config.viewsdisplay.View;
import org.opennms.netmgt.dao.api.OutageDao;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsOutage;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.netmgt.model.OnmsSnmpInterface;
import org.opennms.netmgt.model.ServiceSelector;
import org.opennms.web.svclayer.catstatus.model.StatusCategory;
import org.opennms.web.svclayer.catstatus.model.StatusNode;
import org.opennms.web.svclayer.catstatus.model.StatusSection;
import org.opennms.web.svclayer.dao.CategoryConfigDao;
import org.opennms.web.svclayer.dao.ViewDisplayDao;

import junit.framework.TestCase;


public class DefaultCategoryStatusServiceTest extends TestCase {

	private DefaultCategoryStatusService categoryStatusService;
	private ViewDisplayDao viewDisplayDao;
	private CategoryConfigDao categoryDao;
	private OutageDao outageDao;
	
        @Override
	protected void setUp() throws Exception {
		super.setUp();
		viewDisplayDao = mock(ViewDisplayDao.class);
		categoryDao = mock(CategoryConfigDao.class);
		outageDao = mock(OutageDao.class);
		categoryStatusService = new DefaultCategoryStatusService();	 
		categoryStatusService.setViewDisplayDao(viewDisplayDao);
		categoryStatusService.setCategoryConfigDao(categoryDao);
		categoryStatusService.setOutageDao(outageDao);
	}

	
	public void testCategoryGroupsReturnedWhenNoneExist() {
		
		
		View view = new View();
		
		
		when(viewDisplayDao.getView()).thenReturn(view);
		
		Collection<StatusSection> categories = categoryStatusService.getCategoriesStatus();
	
		assertTrue("Collection Should Be Empty", categories.isEmpty());

		verify(viewDisplayDao, atLeastOnce()).getView();
	}
	
	
	public void testGetCategoriesStatus(){
	
		View view = new View();
		Section section = new Section();
		
		section.setSectionName("Section One");
		section.addCategory("Category One");
		
		OnmsOutage outage = new OnmsOutage();
		Collection<OnmsOutage> outages = new ArrayList<>();
		
		outage.setId(300);
		
		
		OnmsServiceType svcType = new OnmsServiceType();
		svcType.setId(3);
		svcType.setName("HTTP");
		OnmsNode node = new OnmsNode();
		node.setId(1);
		node.setLabel("superLabel");
		OnmsSnmpInterface snmpIface = new OnmsSnmpInterface(node, 1);
		OnmsIpInterface iface = new OnmsIpInterface("192.168.1.1", node);
		iface.setSnmpInterface(snmpIface);
		//iface.setId(9);
		OnmsMonitoredService monSvc = new OnmsMonitoredService(iface, svcType);

		outage.setMonitoredService(monSvc);
		
		outages.add(outage);

		view.addSection(section);
		List <String>services = new ArrayList<>();
		services.add("HTTP");
//		ServiceSelector selector = new ServiceSelector("isHTTP",(List<String>) services);
		
		
		
		when(viewDisplayDao.getView()).thenReturn(view);
		when(categoryDao.getCategoryByLabel("Category One")).thenReturn(createCategoryFromLabel("Category One"));
		when(outageDao.matchingCurrentOutages(isA(ServiceSelector.class))).thenReturn(outages);

		Collection<StatusSection> statusSections = categoryStatusService.getCategoriesStatus();
		
		assertEquals("Wrong Number of StatusSections",view.getSections().size(),statusSections.size());
		
		
		for (StatusSection statusSection : statusSections) {
		
			
			assertEquals("StatusSection Name Does Not Match","Section One",statusSection.getName());
				
			Collection <StatusCategory> statusCategorys = statusSection.getCategories();  
			
			for(StatusCategory statusCategory : statusCategorys){
				
				assertEquals("StatusCategoryName does not match","Category One",statusCategory.getLabel());
				//assertEquals("Category Comment Does not match","Category One Comment",statusCategory.getComment());				
				assertTrue("Nodes >= 1",statusCategory.getNodes().size() >= 1);	
				
				for(StatusNode statusNode : statusCategory.getNodes()){
				
					assertEquals("Label does not match","superLabel",statusNode.getLabel());
				}
			}
			
		}

                verify(viewDisplayDao, atLeastOnce()).getView();
                verify(categoryDao, atLeastOnce()).getCategoryByLabel(anyString());
                verify(outageDao, atLeastOnce()).matchingCurrentOutages(any(ServiceSelector.class));
	}


	private Category createCategoryFromLabel(String label) {
		
		Category category = new Category();
		
		category.setLabel(label);
		category.setNormalThreshold(0d);
		category.setWarningThreshold(0d);
		category.setRule("isHTTP");
		category.addService("HTTP");
		
		
		return category;
	}
	
	
	
	
}
