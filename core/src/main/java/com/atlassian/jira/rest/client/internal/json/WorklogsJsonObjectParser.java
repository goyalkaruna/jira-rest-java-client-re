package com.atlassian.jira.rest.client.internal.json;

import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.api.domain.Worklog;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
* andrey@polyakov.co 6/8/14 5:35 PM
*/
public class WorklogsJsonObjectParser implements JsonObjectParser<Iterable<Worklog>> {
    private final WorklogJsonParserV5 jsonParser;

    public WorklogsJsonObjectParser(WorklogJsonParserV5 jsonParser) {
        this.jsonParser = jsonParser;
    }

    @Override
    public Iterable<Worklog> parse(JSONObject json) throws JSONException {
        final JSONArray worklogsArray = JsonParseUtil.getNestedArray(json, IssueFieldId.WORKLOGS_FIELD.id);
        if (worklogsArray != null) {
            final ArrayList<Worklog> worklogs = new ArrayList<Worklog>(worklogsArray.length());
            for (int i = 0; i < worklogsArray.length(); i++) {
                worklogs.add(jsonParser.parse((JSONObject) worklogsArray.get(i)));
            }

            return worklogs;
        }
        return  Collections.emptyList();
    }
}
