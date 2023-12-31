/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2014 The OpenNMS Group, Inc.
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

import org.opennms.netmgt.model.OnmsHwEntity;

/**
 * The Interface HwEntityDao.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public interface HwEntityDao extends OnmsDao<OnmsHwEntity, Integer> {

    /**
     * Find root by node id.
     *
     * @param nodeId the node id
     * @return the OpenNMS hardware entity
     */
    public OnmsHwEntity findRootByNodeId(Integer nodeId);

    /**
     * Better performant than #findRootByNodeId.
     * Useful when dealing with large trees. See NMS-13256
     * Find root by node id
     *
     * @param nodeId the node id
     * @return the OpenNMS hardware entity
     */

    public OnmsHwEntity findRootEntityByNodeId(Integer nodeId);

    /**
     * Find entity by index.
     *
     * @param nodeId the node id
     * @param entPhysicalIndex the entity physical index
     * @return the OpenNMS hardware entity
     */
    public OnmsHwEntity findEntityByIndex(Integer nodeId, Integer entPhysicalIndex);

    /**
     * Find entity by name.
     *
     * @param nodeId the node id
     * @param entPhysicalName the entity physical name
     * @return the OpenNMS hardware entity
     */
    public OnmsHwEntity findEntityByName(Integer nodeId, String entPhysicalName);

    /**
     * Gets the attribute value.
     *
     * @param nodeId the node id
     * @param entPhysicalIndex the entity physical index
     * @param attributeName the name of the desired attribute
     * @return the attribute value
     */
    public String getAttributeValue(Integer nodeId, Integer entPhysicalIndex, String attributeName);

    /**
     * Gets the attribute value.
     *
     * @param nodeId the node id
     * @param nameSource either the value of entPhysicalName or a regular expression to be applied over the entPhysicalName (should start with '~')
     * @param attributeName the name of the desired attribute
     * @return the attribute value
     */
    public String getAttributeValue(Integer nodeId, String nameSource, String attributeName);

}
