/*
 * Copyright (C) 2010 Atlassian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.jira.restjavaclient.jersey;

import com.atlassian.jira.restjavaclient.AuthenticationHandler;
import com.atlassian.jira.restjavaclient.IssueRestClient;
import com.atlassian.jira.restjavaclient.JiraRestClient;
import com.atlassian.jira.restjavaclient.SessionRestClient;
import com.sun.jersey.api.client.*;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.ApacheHttpClientHandler;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * TODO: Document this class / interface here
 *
 * @since v0.1
 */
public class JerseyJiraRestClient implements JiraRestClient {

    private final URI baseUri;
    private final IssueRestClient issueRestClient;
    private SessionRestClient sessionRestClient;


    public JerseyJiraRestClient(final URI serverUri, final AuthenticationHandler authenticationHandler) {
        this.baseUri = UriBuilder.fromUri(serverUri).path("/rest/api/latest").build();
        DefaultApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        authenticationHandler.configure(config);
        final ApacheHttpClient client = new ApacheHttpClient(createDefaultClientHander(), config) {
            @Override
            public WebResource resource(URI u) {
                final WebResource resource = super.resource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public AsyncWebResource asyncResource(URI u) {
                final AsyncWebResource resource = super.asyncResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public ViewResource viewResource(URI u) {
                final ViewResource resource = super.viewResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }

            @Override
            public AsyncViewResource asyncViewResource(URI u) {
                final AsyncViewResource resource = super.asyncViewResource(u);
                authenticationHandler.configure(resource, this);
                return resource;
            }
        };
        issueRestClient = new JerseyIssueRestClient(baseUri, client);
        sessionRestClient = new JerseySessionRestClient(client, serverUri);
    }

    @Override
    public IssueRestClient getIssueClient() {
        return issueRestClient;
    }

    @Override
    public SessionRestClient getSessionClient() {
        return sessionRestClient;
    }

    private static ApacheHttpClientHandler createDefaultClientHander() {
        final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
        return new ApacheHttpClientHandler(client);
    }

}
