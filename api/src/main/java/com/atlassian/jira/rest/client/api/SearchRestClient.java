/*
 * Copyright (C) 2011 Atlassian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.jira.rest.client.api;

import com.atlassian.jira.rest.client.api.domain.Filter;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;

import javax.annotation.Nullable;
import java.net.URI;

/**
 * The client handling search REST resource
 *
 * @since 2.0 client, 4.3 server
 */
public interface SearchRestClient {
	/**
	 * Performs a JQL search and returns issues matching the query
	 *
	 * @param jql a valid JQL query (will be properly encoded by JIRA client). Restricted JQL characters (like '/') must be properly escaped.
	 * @return issues matching given JQL query
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid JQL query, etc.)
	 */
	Promise<SearchResult> searchJql(@Nullable String jql);

	/**
	 * Performs a JQL search and returns issues matching the query using default maxResults (as configured in JIRA - usually 50) and startAt=0
	 *
	 * @param jql        a valid JQL query (will be properly encoded by JIRA client). Restricted JQL characters (like '/') must be properly escaped.
	 * @param maxResults maximum results (page/window size) for this search. The page will contain issues from
	 *                   <code>startAt div maxResults</code> (no remnant) and will include at most <code>maxResults</code> matching issues.
	 * @param startAt    starting index (0-based) defining the page/window for the results. It will be aligned by the server to the beginning
	 *                   on the page (startAt = startAt div maxResults). For example for startAt=5 and maxResults=3 the results will include matching issues
	 *                   with index 3, 4 and 5. For startAt = 6 and maxResults=3 the issues returned are from position 6, 7 and 8.
	 * @return issues matching given JQL query
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid JQL query, etc.)
	 */
	Promise<SearchResult> searchJql(@Nullable String jql, int maxResults, int startAt);

	/**
	 * Performs a JQL search and returns issues matching the query using default maxResults (as configured in JIRA - usually 50)
	 * and startAt=0. Optional fields parameter allow to specify list of issue fields which should be included in search result.
	 *
	 * @param jql        a valid JQL query (will be properly encoded by JIRA client). Restricted JQL characters (like '/') must be properly escaped.
	 * @param maxResults maximum results (page/window size) for this search. The page will contain issues from
	 *                   <code>startAt div maxResults</code> (no remnant) and will include at most <code>maxResults</code> matching issues.
	 * @param startAt    starting index (0-based) defining the page/window for the results. It will be aligned by the server to the beginning
	 *                   on the page (startAt = startAt div maxResults). For example for startAt=5 and maxResults=3 the results will include matching issues
	 *                   with index 3, 4 and 5. For startAt = 6 and maxResults=3 the issues returned are from position 6, 7 and 8.
	 * @param fields     comma separated list of fields which should be retrieved. You can specify *all for all fields
	 *                   or *navigable (which is the default value) which will cause to include just navigable fields in result.
	 *                   To ignore specific field you can use "-" before field's name.
	 * @return issues matching given JQL query
	 * @throws RestClientException in case of problems (connectivity, malformed messages, invalid JQL query, etc.)
	 */
	Promise<SearchResult> searchJql(@Nullable String jql, @Nullable Integer maxResults, @Nullable Integer startAt, @Nullable String fields);

	/**
	 * Retrieves list of your favourite filters.
	 *
	 * @return list of your favourite filters
	 * @since 2.0 client, 5.0 server
	 */
	Promise<Iterable<Filter>> getFavouriteFilters();

	/**
	 * Retrieves filter for given URI.
	 *
	 * @param filterUri URI to filter resource (usually get from <code>self</code> attribute describing component elsewhere)
	 * @return filter
	 * @since 2.0 client, 5.0 server
	 */
	Promise<Filter> getFilter(URI filterUri);

	/**
	 * Retrieves filter for given id.
	 *
	 * @param id ID of the filter
	 * @return filter
	 * @since 2.0 client, 5.0 server
	 */
	Promise<Filter> getFilter(long id);
}