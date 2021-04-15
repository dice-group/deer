/*
 * DEER Core Library - DEER - RDF Dataset Enrichment Framework
 * Copyright © 2013 Data Science Group (DICE) (kevin.dressler@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.deer;


import org.apache.jena.rdf.model.Resource;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *
 */
public class DeerAnalyticsStore {

  private static Map<String, JSONObject> backend = new HashMap<>();

  public static synchronized void write(String jobId, Resource nodeId, JSONObject nodeJson) {
    if (nodeId == null) {
      writeGlobal(jobId, nodeJson);
      return;
    }
    String nodeName = nodeId.toString();
    JSONObject jobJson = backend.get(jobId);
    if (jobJson == null) {
      jobJson = new JSONObject();
      backend.put(jobId, jobJson);
    }
    JSONObject insertJson = jobJson.optJSONObject("operatorStats");
    if (insertJson == null) {
      jobJson.put("operatorStats", new JSONObject());
      insertJson = jobJson.getJSONObject("operatorStats");
    }
    JSONObject oldNodeJson = insertJson.optJSONObject(nodeName);
    if (oldNodeJson == null) {
      insertJson.put(nodeName, nodeJson);
    } else {
      insertJson.put(nodeName, mergeJSONObjects(oldNodeJson, nodeJson));
    }
  }

  private static void writeGlobal(String jobId, JSONObject json) {
    JSONObject jobJson = backend.get(jobId);
    if (jobJson == null) {
      jobJson = new JSONObject();
      backend.put(jobId, jobJson);
    }
    JSONObject oldJson = jobJson.optJSONObject("globalStats");
    if (oldJson == null) {
      jobJson.put("globalStats", json);
    } else {
      jobJson.put("globalStats", mergeJSONObjects(oldJson, json));
    }
  }

  public static synchronized JSONObject getAnalyticsForJob(String jobId) {
    return backend.get(jobId);
  }

  private static JSONObject mergeJSONObjects(JSONObject json1, JSONObject json2) {
    JSONObject mergedJSON;
    try {
      mergedJSON = new JSONObject(json1, JSONObject.getNames(json1));
      for (String crunchifyKey : JSONObject.getNames(json2)) {
        mergedJSON.put(crunchifyKey, json2.get(crunchifyKey));
      }

    } catch (JSONException e) {
      throw new RuntimeException("JSON Exception" + e);
    }
    return mergedJSON;
  }

}
