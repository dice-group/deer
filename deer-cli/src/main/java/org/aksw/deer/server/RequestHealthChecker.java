/*
 * DEER Command Line Interface - DEER - RDF Dataset Enrichment Framework
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
package org.aksw.deer.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class RequestHealthChecker extends Thread {

  private static final Logger logger = LoggerFactory.getLogger(RequestHealthChecker.class);

  private ConcurrentMap<String, CompletableFuture<Void>> requestsMap;
  private Set<String> visited = new HashSet<>();

  RequestHealthChecker(ConcurrentMap<String, CompletableFuture<Void>> requestsMap) {
    setName("RequestHealthCheckerThread");
    this.requestsMap = requestsMap;
  }

  @Override
  public void run() {
    while(true) {
      requestsMap.forEach((requestId, request) -> {
        if (request.isDone() && !visited.contains(requestId)) {
          try {
            visited.add(requestId);
            request.get();
          } catch (InterruptedException | ExecutionException e) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(baos));
            try {
              MDC.put("requestId", requestId);
              logger.error("Request " + requestId + " completed with exception:" + e.getMessage());
              logger.error(baos.toString("UTF-8"));
              MDC.remove("requestId");
            } catch (UnsupportedEncodingException ee) {
              throw new RuntimeException(ee);
            }
          }
        }
      });
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        return;
      }
    }
  }
}
