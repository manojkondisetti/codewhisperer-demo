/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2006-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.dao.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opennms.netmgt.model.HeatMapElement;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsOutage;
import org.opennms.netmgt.model.ServiceSelector;
import org.opennms.netmgt.model.monitoringLocations.OnmsMonitoringLocation;
import org.opennms.netmgt.model.outage.CurrentOutageDetails;
import org.opennms.netmgt.model.outage.OutageSummary;


/**
 * <p>OutageDao interface.</p>
 */
public interface OutageDao extends LegacyOnmsDao<OnmsOutage, Integer> {

    /**
     * <p>currentOutageCount</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    Integer currentOutageCount();

    /**
     * <p>currentOutages</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    Collection<OnmsOutage> currentOutages();

    /**
     * Open outages grouped by {@link OnmsMonitoredService} id.
     */
    Map<Integer, Set<OnmsOutage>> currentOutagesByServiceId();

    /**
     * Return the current open outage for the service or if the service
     * is up and has no open outage, return null.
     */
    OnmsOutage currentOutageForService(OnmsMonitoredService service);

    OnmsOutage currentOutageForServiceFromPerspective(final OnmsMonitoredService service, final OnmsMonitoringLocation perspective);

    /**
     * Return all current open outages for the given service be it detected from Perspective Poller.
     */
    Collection<OnmsOutage> currentOutagesForServiceFromPerspectivePoller(OnmsMonitoredService service);

    /**
     * Finds the latest (unresolved) outages that match the given services.
     *
     * @param services a list of services
     * @return a {@link java.util.Collection} of outages
     */
    Collection<CurrentOutageDetails> newestCurrentOutages(List<String> services);

    /**
     * Finds all current (unresolved) outages that match the given service selector.
     *
     * @param selector a service selector (filter + service list)
     * @return a {@link java.util.Collection} of outages
     */
    Collection<OnmsOutage> matchingCurrentOutages(ServiceSelector selector);

    /**
     * <p>findAll</p>
     *
     * @param offset a {@link java.lang.Integer} object.
     * @param limit a {@link java.lang.Integer} object.
     * @return a {@link java.util.Collection} object.
     */
    Collection<OnmsOutage> findAll(Integer offset, Integer limit);

    /**
     * Get the number of nodes with outages.
     * @return the number of nodes with outages.
     */
    int countOutagesByNode();

    /**
     * Get the list of current outages, one per node.  If a node has more than one outage, the
     * oldest outstanding outage is returned.
     * @param rows The maximum number of outages to return.
     * @return A list of outages.
     */
    List<OutageSummary> getNodeOutageSummaries(int rows);

    /**
     * Retrieves heatmap elements for a given combination of database columns.
     *
     * @param entityNameColumn the entity's name column
     * @param entityIdColumn the entity's id column
     * @param restrictionColumn a column used for a restriction of the results
     * @param restrictionValue the value that must match against the restrictionColumn
     * @param groupByColumns columns used for the SQL group-by clause
     * @return the heatmap elements for this query
     */
    List<HeatMapElement> getHeatMapItemsForEntity(String entityNameColumn, String entityIdColumn, String restrictionColumn, String restrictionValue, String... groupByColumns);

    Collection<OnmsOutage> getStatusChangesForApplicationIdBetween(final Date startDate, final Date endDate, final Integer applicationId);
}
