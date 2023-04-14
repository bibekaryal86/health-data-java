package health.data.java.app.controller;

import health.data.java.app.model.CheckupComponentRequest;
import health.data.java.app.model.CheckupComponentResponse;
import health.data.java.app.service.CheckupComponentService;
import health.data.java.app.util.CommonUtils;
import health.data.java.app.util.ConstantUtils;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/checkup_component")
public class CheckupComponentController {

  private final CheckupComponentService checkupComponentService;

  public CheckupComponentController(CheckupComponentService checkupComponentService) {
    this.checkupComponentService = checkupComponentService;
  }

  @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupComponentResponse> find() {
    return response(checkupComponentService.findCheckupComponents());
  }

  @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupComponentResponse> insert(
      @RequestBody CheckupComponentRequest checkupComponentRequest) {
    if (checkupComponentService.isValidCheckupComponentRequest(checkupComponentRequest, true)) {
      ResponseEntity<CheckupComponentResponse> responseEntity =
          response(
              checkupComponentService.saveCheckupComponent(
                  checkupComponentRequest.getCheckupComponent()));
      CompletableFuture.supplyAsync(checkupComponentService::findCheckupComponents);
      return responseEntity;
    }

    return badRequestResponse();
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupComponentResponse> update(
      @RequestBody CheckupComponentRequest checkupComponentRequest) {
    if (checkupComponentService.isValidCheckupComponentRequest(checkupComponentRequest, false)) {
      ResponseEntity<CheckupComponentResponse> responseEntity =
          response(
              checkupComponentService.saveCheckupComponent(
                  checkupComponentRequest.getCheckupComponent()));
      CompletableFuture.supplyAsync(checkupComponentService::findCheckupComponents);
      return responseEntity;
    }

    return badRequestResponse();
  }

  @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupComponentResponse> delete(@RequestParam String id) {
    if (CommonUtils.isValidNumber(id)) {
      ResponseEntity<CheckupComponentResponse> responseEntity =
          response(checkupComponentService.deleteCheckupComponent(Integer.parseInt(id)));
      CompletableFuture.supplyAsync(checkupComponentService::findCheckupComponents);
      return responseEntity;
    }

    return badRequestResponse();
  }

  private ResponseEntity<CheckupComponentResponse> response(
      CheckupComponentResponse checkupCategoryResponse) {
    if (!StringUtils.hasText(checkupCategoryResponse.getErrMsg())) {
      return ResponseEntity.ok(checkupCategoryResponse);
    }

    return new ResponseEntity<>(checkupCategoryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<CheckupComponentResponse> badRequestResponse() {
    return new ResponseEntity<>(
        CheckupComponentResponse.builder()
            .checkupComponentList(Collections.emptyList())
            .errMsg(ConstantUtils.ERR_MSG_MISSING)
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
