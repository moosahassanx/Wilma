package com.epam.wilma.browsermob.proxy;
/*==========================================================================
Copyright 2013-2016 EPAM Systems

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

import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.util.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Test class for BrowserMobProxy with real url calls.
 *
 * @author Tamas Kohegyi
 */
public class BrowserMobProxyRealCallTest {

    /**
     * CloseableHttpClient that will connect through the local proxy running on 127.0.0.1.
     */
    private CloseableHttpClient client;
    private int proxyPort = 19092;
    private int requestTimeout = 30000;
    private BrowserMobProxyServer proxy;

    /**
     * Creates an all-trusting CloseableHttpClient (for tests ONLY!) that will connect to a proxy at 127.0.0.1:proxyPort,
     * with no cookie store.
     *
     * @param proxyPort port of the proxy running at 127.0.0.1
     * @return a new CloseableHttpClient
     */
    public static CloseableHttpClient getNewHttpClient(int proxyPort) {
        return getNewHttpClient(proxyPort, null);
    }

    /**
     * Creates an all-trusting CloseableHttpClient (for tests ONLY!) that will connect to a proxy at 127.0.0.1:proxyPort,
     * using the specified cookie store.
     *
     * @param proxyPort   port of the proxy running at 127.0.0.1
     * @param cookieStore CookieStore for HTTP cookies
     * @return a new CloseableHttpClient
     */
    public static CloseableHttpClient getNewHttpClient(int proxyPort, CookieStore cookieStore) {
        try {
            // Trust all certs -- under no circumstances should this ever be used outside of testing
            SSLContext sslcontext = SSLContexts.custom()
                    .useTLS()
                    .loadTrustMaterial(null, new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            return true;
                        }
                    })
                    .build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setDefaultCookieStore(cookieStore)
                    .setProxy(new HttpHost("127.0.0.1", proxyPort))
                    // disable decompressing content, since some tests want uncompressed content for testing purposes
                    .disableContentCompression()
                    .disableAutomaticRetries()
                    .build();

            return httpclient;
        } catch (Exception e) {
            throw new RuntimeException("Unable to create new HTTP client", e);
        }
    }

    @BeforeClass
    public void startTestClient() throws Exception {
        client = getNewHttpClient(proxyPort);
        proxy = new BrowserMobProxyServer(requestTimeout);
        proxy.start(proxyPort);
    }

    @AfterClass
    public void stopTestClient() throws Exception {
        client.close();
        proxy.stop();
    }

    @Test
    public void testStartCallHttpSuccessfully() throws Exception {
        String body = getResponseBodyFromHost("http://cnn.com");
        Assert.assertTrue(body.contains("International Edition"), "Response is incorrect");
    }

    @Test
    public void testStartCallHttpsSuccessfully() throws Exception {
        String body = getResponseBodyFromHost("https://google.com");
        Assert.assertTrue(body.contains("doctype html"), "Response is incorrect");
    }

    /**
     * Convenience method to perform an HTTP GET to the specified URL and return the response object. The response is not closed, and so
     * MUST be closed by the calling code.
     *
     * @param url URL to HTTP GET
     * @return CloseableHttpResponse from the server
     */
    public CloseableHttpResponse getResponseFromHost(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            return client.execute(httpGet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience method to perform an HTTP GET to the specified URL and return the response body. Closes the response before returning
     * the body.
     *
     * @param url URL to HTTP GET
     * @return response body from the server
     */
    public String getResponseBodyFromHost(String url) {
        try {
            CloseableHttpResponse response = getResponseFromHost(url);

            String body = IOUtils.toStringAndClose(response.getEntity().getContent());

            response.close();

            return body;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
