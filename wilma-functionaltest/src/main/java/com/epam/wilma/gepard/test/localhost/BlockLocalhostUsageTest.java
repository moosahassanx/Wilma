package com.epam.wilma.gepard.test.localhost;
/*==========================================================================
Copyright 2013-2015 EPAM Systems

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

import com.epam.gepard.annotations.TestClass;
import com.epam.gepard.annotations.TestParameter;
import com.epam.wilma.gepard.WilmaTestCase;
import com.epam.wilma.gepard.testclient.RequestParameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Tests the localhost request blocking functionality.
 *
 * @author Adam_Csaba_Kiraly
 */
@TestClass(id = "BlockLocalhostUsage", name = "Localhost request blocking")
public class BlockLocalhostUsageTest extends WilmaTestCase {

    private static final String REQUEST = "resources/example2.xml";

    @TestParameter(id = "PAR0")
    private String tcName;
    @TestParameter(id = "PAR1")
    private String tcTargetUrl;
    @TestParameter(id = "PAR2")
    private String tcState;
    @TestParameter(id = "PAR3")
    private String tcStubOperationState;
    @TestParameter(id = "PAR4")
    private String tcExpectedCode;

    public void testLocalhostBlocking() throws Exception {
        //given
        setOperationModeTo(tcStubOperationState);
        setLocalhostBlockingTo(tcState);
        RequestParameters requestParameters = createRequestParameters();
        //when
        callWilmaWithGetMethod(requestParameters);
        //then
        checkResponseCode(Integer.valueOf(tcExpectedCode));
    }

    protected RequestParameters createRequestParameters() throws FileNotFoundException {
        String testServerUrl = tcTargetUrl;
        String wilmaHost = getClassData().getEnvironment().getProperty("wilma.host");
        Integer wilmaPort = Integer.parseInt(getClassData().getEnvironment().getProperty("wilma.port.external"));
        String contentType = "application/xml";
        String acceptHeader = "application/xml";
        String contentEncoding = "no";
        String acceptEncoding = "no";
        return new RequestParameters().testServerUrl(testServerUrl).useProxy(true).wilmaHost(wilmaHost).wilmaPort(wilmaPort)
                .xmlIS(new FileInputStream(REQUEST)).contentType(contentType).acceptHeader(acceptHeader).contentEncoding(contentEncoding)
                .acceptEncoding(acceptEncoding);
    }
}