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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import org.aksw.deer.DeerController;
import org.aksw.deer.io.AbstractModelIO;
import org.aksw.faraday_cage.engine.CompiledExecutionGraph;
import org.aksw.faraday_cage.engine.FaradayCageContext;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static spark.Spark.*;

/**
 */
public class Server {

  private static final Logger logger = LoggerFactory.getLogger(Server.class);

  public static final String STORAGE_DIR_PATH = ".server-storage/";
  public static final String LOG_DIR_PATH = STORAGE_DIR_PATH + "logs/";

  private static final Gson GSON = new GsonBuilder().create();
  private static Server instance = null;

  private static Model SHAPES_MODEL = DeerController.getShapes();

  private final ConcurrentMap<String, CompletableFuture<Void>> requests = new ConcurrentHashMap<>();
  private final File uploadDir = new File(STORAGE_DIR_PATH);
  private int port = -1;

  public static Server getInstance() {
    if (instance == null) {
      instance = new Server();
    }
    return instance;
  }

  public void run(int port) {
    if (this.port > 0) {
      throw new IllegalStateException("Server already running on port " + port + "!");
    } else {
      this.port = port;
    }
    if (!uploadDir.exists()) {
      uploadDir.mkdir();
    }
    FaradayCageContext.addForkListener(runId -> MDC.put("requestId", runId));
    AbstractModelIO.takeWorkingDirectoryFrom(()-> STORAGE_DIR_PATH + FaradayCageContext.getRunId() + "/");
    staticFiles.expireTime(0);
    staticFiles.location("/gui");
    threadPool(100, 1, 30000);
    port(port);
    enableCORS("*","GET, POST, OPTIONS","");
    post("/submit", this::handleSubmit);
    get("/shapes", this::handleShapes);
    get("/status/:id", this::handleStatus);
    get("/logs/:id", this::handleLogs);
    get("/results/:id", this::handleResults);
    get("/result/:id/:file", this::handleResult);
    exception(Exception.class, (e, req, res) -> {
      logger.error("Error in processing request" + req.uri(), e);
      res.status(500);
      res.type("application/json");
      res.body(GSON.toJson(new ErrorMessage(e)));
    });
    notFound((req, res) -> {
      res.redirect("/");
      return "";
    });
    init();
    awaitInitialization();
  }

  private Server(){
    new RequestHealthChecker(requests).start();
  }

  private Object handleSubmit(Request req, Response res) throws IOException, ServletException {
    String runId = FaradayCageContext.newRunId();
    File workingDir = new File(uploadDir.getAbsoluteFile(), runId);
    if (!workingDir.mkdirs()) {
      throw new IOException("Not able to create directory " + workingDir.getAbsolutePath());
    }
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(workingDir.getAbsolutePath()));
    Path configFile = null;
    if (req.contentType().contains("multipart/form-data")) {
      for (Part part : req.raw().getParts()) {
        try (InputStream is = part.getInputStream()) {
          Path partFileName = Paths.get(part.getSubmittedFileName()).getFileName();
          Path destinationPath = workingDir.toPath().resolve(partFileName);
          Files.copy(is, destinationPath, StandardCopyOption.REPLACE_EXISTING);
          if (part.getName().equals("config")) {
            configFile = destinationPath;
          }
          logger.info("Uploaded file '{}' to '{}'", partFileName, destinationPath);
        }
      }
    } else {
      return GSON.toJson(new ErrorMessage(1, "Only Requests of type \"multipart/form-data\" are allowed"));
    }
    if (Objects.isNull(configFile)) {
      return GSON.toJson(new ErrorMessage(2, "No configuration was submitted"));
    }
    MDC.put("requestId", runId);
    CompiledExecutionGraph compiledExecutionGraph =
      DeerController.compileDeer(configFile.toString(), runId);
    compiledExecutionGraph.andThen(() -> DeerController.writeAnalytics(workingDir.toPath().resolve("deer-analytics.json")));
    requests.put(runId, CompletableFuture.completedFuture(null).thenAcceptAsync($->{
      MDC.put("requestId", runId);
      compiledExecutionGraph.run();
      compiledExecutionGraph.join();
    }));
    MDC.remove("requestId");
    res.status(200);
    return GSON.toJson(new SubmitMessage(runId));
  }

  private Object handleShapes(Request req, Response res) throws IOException {
    res.type("text/turtle");
    res.header("Content-Disposition", "attachment; filename=shapes.ttl");
    res.status(200);
    SHAPES_MODEL.write(res.raw().getOutputStream(), "TTL");
    return "";
  }

  private Object handleStatus(Request req, Response res) {
    String id = sanitizeId(req.params("id"));
    StatusMessage result;
    if (!requests.containsKey(id)) {
      result = new StatusMessage(-1, "Request ID not found");
    } else if (!requests.get(id).isDone()) {
      result = new StatusMessage(0, "Request is being processed");
    } else if (requests.get(id).isCompletedExceptionally()){
      result = new StatusMessage(1, "Request completed exceptionally");
    } else {
      result = new StatusMessage(2, "Request has been processed successfully");
    }
    res.status(200);
    return GSON.toJson(result);
  }

  private Object handleLogs(Request req, Response res) throws Exception {
    String id = sanitizeId(req.params("id"));
    File requestedFile = new File(LOG_DIR_PATH + id + ".log");
    if (requestedFile.exists()) {
      res.type("text/plain");
      res.header("Content-Disposition", "attachment; filename=log.txt");
      res.status(200);
      OutputStream os = res.raw().getOutputStream();
      FileInputStream fs = new FileInputStream(requestedFile);
      final byte[] buffer = new byte[1024];
      int count;
      boolean finish = !requests.containsKey(id) || requests.get(id).isDone();
      while (true) {
        while ((count = fs.read(buffer)) >= 0) {
          os.write(buffer, 0, count);
        }
        os.flush();
        if (finish) break;
        Thread.sleep(500);
        finish = requests.get(id).isDone();
      }
      fs.close();
      os.close();
      return "";
    } else {
      res.status(404);
      return GSON.toJson(new ErrorMessage(1, "Logfile not found"));
    }
  }

  private Object handleResult(Request req, Response res) throws Exception {
    String id = sanitizeId(req.params("id"));
    File file = new File(req.params("file"));
    File requestedFile = new File(STORAGE_DIR_PATH + id + "/" + file.getName());
    // is the file available?
    if (requestedFile.exists()) {
      MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
      Collection mimeTypes = MimeUtil.getMimeTypes(requestedFile, new MimeType("text/plain"));
      res.type(mimeTypes.iterator().next().toString());
      res.header("Content-Disposition", "attachment; filename=" + file.getName());
      res.status(200);
      OutputStream os = res.raw().getOutputStream();
      FileInputStream fs = new FileInputStream(requestedFile);
      final byte[] buffer = new byte[1024];
      int count;
      while ((count = fs.read(buffer)) >= 0) {
        os.write(buffer, 0, count);
      }
      os.flush();
      fs.close();
      os.close();
      return "";
    } else {
      // 404 - Not Found
      res.status(404);
      return GSON.toJson(new ErrorMessage(1, "Result file not found"));
    }
  }

  private Object handleResults(Request req, Response res) {
    String id = sanitizeId(req.params("id"));
    File dir = new File(STORAGE_DIR_PATH + id);
    if (dir.exists() && dir.isDirectory()) {
      List<String> availableFiles = Arrays
        .stream(Objects.requireNonNull(dir.listFiles()))
        .map(File::getName).collect(Collectors.toList());
      return GSON.toJson(new ResultsMessage(availableFiles));
    } else {
      res.status(404);
      return GSON.toJson(new ErrorMessage(1, "Request ID not found"));
    }
  }

  private static String sanitizeId(String id) {
    return id.replaceAll("[^0-9a-f\\-]", "");
  }

  private static void enableCORS(final String origin, final String methods, final String headers) {
    options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }
      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }
      return "OK";
    });

    before((request, response) -> {
      response.header("Access-Control-Allow-Origin", origin);
      response.header("Access-Control-Request-Method", methods);
      response.header("Access-Control-Allow-Headers", headers);
      response.type("application/json");
    });
  }

  private static class ServerMessage {

    protected boolean success = true;

  }

  private static class ErrorMessage extends ServerMessage {

    private Error error;

    private static class Error {
      private int code;
      private String message;
    }

    ErrorMessage(Throwable e) {
      this(-1, e.getMessage());
    }

    ErrorMessage(int code, String message) {
      this.success = false;
      this.error = new Error();
      this.error.code = code;
      this.error.message = message;
    }

  }

  private static class StatusMessage {

    private Status status;

    private static class Status {
      int code;
      String description;
    }

    private StatusMessage(int status, String description) {
      this.status = new Status();
      this.status.code = status;
      this.status.description = description;
    }
  }

  private static class ResultsMessage {

    private List<String> availableFiles;

    private ResultsMessage(List<String> availableFiles) {
      this.availableFiles = availableFiles;
    }
  }

  private static class SubmitMessage {

    private String requestId;

    private SubmitMessage(String requestId) {
      this.requestId = requestId;
    }
  }

}
