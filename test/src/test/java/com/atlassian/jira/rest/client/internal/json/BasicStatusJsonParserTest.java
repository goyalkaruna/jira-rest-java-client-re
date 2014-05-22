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

package com.atlassian.jira.rest.client.internal.json;

import com.atlassian.jira.rest.client.api.domain.BasicStatus;
import org.codehaus.jettison.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import static com.atlassian.jira.rest.client.TestUtil.toUri;

public class BasicStatusJsonParserTest {
	@Test
	public void testParseOldFields() throws JSONException {
		final BasicStatusJsonParser parser = new BasicStatusJsonParser();
		final BasicStatus basicStatus = parser.parse(ResourceUtil.getJsonObjectFromResource("/json/status/valid.json"));
		Assert.assertEquals(new BasicStatus(
                toUri("http://localhost:8090/jira/rest/api/latest/status/1"), null, "Open", null, null), basicStatus);
	}

    @Test
    public void testParseAllFields() throws JSONException {
        final BasicStatusJsonParser parser = new BasicStatusJsonParser();
        final BasicStatus basicStatus = parser.parse(ResourceUtil.getJsonObjectFromResource("/json/status/complete.json"));
        Assert.assertEquals(new BasicStatus(toUri("http://localhost:8090/jira/rest/api/latest/status/1"),
                1L, "Open", "The issue is open and ready for the assignee to start work on it.",
                toUri("http://localhost:8090/jira/images/icons/status_open.gif")), basicStatus);
    }
}
