/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2016-2016 The OpenNMS Group, Inc.
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

package org.opennms.netmgt.jetty;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.deploy.App;
import org.eclipse.jetty.deploy.providers.WebAppProvider;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.webapp.WebAppContext;

@ManagedObject("Provider for start-up deployement of webapps based on presence in directory")
public class OpenNMSWebAppProvider extends WebAppProvider {

    @Override
    public ContextHandler createContextHandler(final App app) throws Exception {
        final ContextHandler handler = super.createContextHandler(app);

        /*
         * Add an alias check that accepts double slashes in our resource paths.
         */
        handler.addAliasCheck(new ApproveAbsolutePathAliases());

        /*
         * Pulled from: http://bengreen.eu/fancyhtml/quickreference/jettyjsp9error.html
         *
         * Configure the application to support the compilation of JSP files.
         * We need a new class loader and some stuff so that Jetty can call the
         * onStartup() methods as required.
         */
        if (handler instanceof WebAppContext) {
            WebAppContext context = (WebAppContext)handler;
            context.setAttribute(AnnotationConfiguration.CONTAINER_INITIALIZERS, jspInitializers());
            context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
            context.addBean(new ServletContainerInitializersStarter(context), true);
            context.setThrowUnavailableOnStartupException(true);
        }

        return handler;
    }

    private static List<ContainerInitializer> jspInitializers() {
        final List<ContainerInitializer> initializers = new ArrayList<>();
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        initializers.add(initializer);
        return initializers;
    }
}
