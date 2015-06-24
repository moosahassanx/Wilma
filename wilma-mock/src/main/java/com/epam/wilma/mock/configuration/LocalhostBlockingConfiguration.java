package com.epam.wilma.mock.configuration;

/*==========================================================================
 Copyright 2015 EPAM Systems

 This file is part of Wilma.

 Wilma is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Wilma is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Wilma.  If not, see <http://www.gnu.org/licenses/>.
 ===========================================================================*/

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.wilma.mock.domain.LocalhostControl;
import com.epam.wilma.mock.domain.WilmaMockConfig;
import com.epam.wilma.mock.http.WilmaHttpClient;

/**
 * Collects the localhost blocking configuration related commands.
 *
 * @author Tamas_Pinter
 *
 */
public class LocalhostBlockingConfiguration extends AbstractConfiguration {
    private static final Logger LOG = LoggerFactory.getLogger(LocalhostBlockingConfiguration.class);

    private static final String STATUS_GETTER_URL_POSTFIX = "config/public/localhost/status";
    private static final String STATUS_SETTER_URL_POSTFIX_FORMAT = "config/admin/localhost/%s";

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     */
    public LocalhostBlockingConfiguration(WilmaMockConfig config) {
        super(config);
    }

    /**
     * Constructor.
     *
     * @param config the Wilma server configuration
     * @param client the Wilma HTTP client
     */
    public LocalhostBlockingConfiguration(WilmaMockConfig config, WilmaHttpClient client) {
        super(config, client);
    }

    /**
     * Gets the localhost blocking status.
     *
     * @return localhost blocking status in JSONObject
     */
    public JSONObject getLocalhostBlockingStatus() {
        LOG.debug("Call localhost blocking status API.");

        return getterRequest(STATUS_GETTER_URL_POSTFIX);
    }

    /**
     * Sets the localhost blocking status.
     *
     * @param control the new blocking status
     * @return <tt>true</tt> if the request is successful, otherwise return <tt>false</tt>
     */
    public boolean setLocalhostBlockingStatus(LocalhostControl control) {
        LOG.debug("Call localhost blocking setter API with value: " + control);

        String postfix = String.format(STATUS_SETTER_URL_POSTFIX_FORMAT, control.toString().toLowerCase());
        return setterRequest(postfix);
    }

}
