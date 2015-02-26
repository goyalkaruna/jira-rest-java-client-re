package ru.retverd.jira.helpers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Worklog;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Nullable;
import com.atlassian.util.concurrent.Promise;

public class JiraSimplifiedClient {
	private JiraRestClient client;

	public JiraSimplifiedClient(String JiraURL, String login, String password) throws IOException, URISyntaxException {
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		URI uri = new URI(JiraURL);
		client = (JiraRestClient) factory.createWithBasicHttpAuthentication(uri, login, password);
	}

	public SearchResult searchByJQLExpr(String jqlString, @Nullable Integer maxResults, @Nullable Integer startAt) {
		Set<String> fields = new HashSet<String>();
		fields.add("summary");
		fields.add("created");
		fields.add("updated");
		fields.add("project");
		fields.add("status");
		fields.add("issuetype");
		fields.add("versions");
		fields.add("worklog");
		fields.add("fixVersions");
		fields.add("components");

		Promise<SearchResult> promise = client.getSearchClient().searchJql(jqlString, maxResults, startAt, fields);
		SearchResult searchResults = promise.claim();
		return searchResults;
	}

	public Issue getIssueByKey(String issueKey) {
		Promise<Issue> promise = client.getIssueClient().getIssue(issueKey);
		Issue issue = promise.claim();
		return issue;
	}

	public Iterator<Worklog> getWorklogs(BasicIssue issue) throws URISyntaxException {
		Promise<Iterable<Worklog>> promise = client.getIssueClient().getWorklogs(issue.getSelf(), issue.getKey());
		Iterable<Worklog> worklogs = promise.claim();
		return worklogs.iterator();
	}

	public void closeConnection() throws IOException {
		client.close();
	}
}