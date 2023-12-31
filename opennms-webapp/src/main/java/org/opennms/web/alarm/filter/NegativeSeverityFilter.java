/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2002-2022 The OpenNMS Group, Inc.
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

package org.opennms.web.alarm.filter;

import org.opennms.netmgt.model.OnmsSeverity;
import org.opennms.web.filter.NotEqualsFilter;
import org.opennms.web.filter.SQLType;

/**
 * Encapsulates negative severity filtering functionality, that is filtering OUT
 * this value instead of only filtering IN this value.
 */
public class NegativeSeverityFilter extends NotEqualsFilter<OnmsSeverity> {
    /** Constant <code>TYPE="severitynot"</code> */
    public static final String TYPE = "severitynot";

    public NegativeSeverityFilter(final OnmsSeverity severity) {
        super(TYPE, SQLType.SEVERITY, "ALARMS.SEVERITY", "severity", severity);
    }

    @Override
    public String getTextDescription() {
        return (TYPE + " is not " + getValue().getLabel());
    }

    @Override
    public String toString() {
        return ("<AlarmFactory.NegativeSeverityFilter: " + this.getDescription() + ">");
    }

    public int getSeverity() {
        return getValue().getId();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof NegativeSeverityFilter)) return false;
        return (this.toString().equals(obj.toString()));
    }
}
