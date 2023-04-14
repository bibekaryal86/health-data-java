package health.data.java.app.controller;

import health.data.java.app.model.CheckupCategoryRequest;
import health.data.java.app.model.CheckupCategoryResponse;
import health.data.java.app.service.CheckupCategoryService;
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
@RequestMapping("/checkup_category")
public class CheckupCategoryController {

  private final CheckupCategoryService checkupCategoryService;

  public CheckupCategoryController(CheckupCategoryService checkupCategoryService) {
    this.checkupCategoryService = checkupCategoryService;
  }

  @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupCategoryResponse> find() {
    return response(checkupCategoryService.findCheckupCategories());
  }

  @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupCategoryResponse> insert(
      @RequestBody CheckupCategoryRequest checkupCategoryRequest) {
    if (checkupCategoryService.isValidCheckupCategoryRequest(checkupCategoryRequest, true)) {
      ResponseEntity<CheckupCategoryResponse> responseEntity =
          response(
              checkupCategoryService.saveCheckupCategory(
                  checkupCategoryRequest.getCheckupCategory()));
      CompletableFuture.supplyAsync(checkupCategoryService::findCheckupCategories);
      return responseEntity;
    }

    return badRequestResponse();
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupCategoryResponse> update(
      @RequestBody CheckupCategoryRequest checkupCategoryRequest) {
    if (checkupCategoryService.isValidCheckupCategoryRequest(checkupCategoryRequest, false)) {
      ResponseEntity<CheckupCategoryResponse> responseEntity =
          response(
              checkupCategoryService.saveCheckupCategory(
                  checkupCategoryRequest.getCheckupCategory()));
      CompletableFuture.supplyAsync(checkupCategoryService::findCheckupCategories);
      return responseEntity;
    }

    return badRequestResponse();
  }

  @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CheckupCategoryResponse> delete(@RequestParam String id) {
    if (CommonUtils.isValidNumber(id)) {
      ResponseEntity<CheckupCategoryResponse> responseEntity =
          response(checkupCategoryService.deleteCheckupCategory(Integer.parseInt(id)));
      CompletableFuture.supplyAsync(checkupCategoryService::findCheckupCategories);
      return responseEntity;
    }

    return badRequestResponse();
  }

  private ResponseEntity<CheckupCategoryResponse> response(
      CheckupCategoryResponse checkupCategoryResponse) {
    if (!StringUtils.hasText(checkupCategoryResponse.getErrMsg())) {
      return ResponseEntity.ok(checkupCategoryResponse);
    }

    return new ResponseEntity<>(checkupCategoryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private ResponseEntity<CheckupCategoryResponse> badRequestResponse() {
    return new ResponseEntity<>(
        CheckupCategoryResponse.builder()
            .checkupCategoryList(Collections.emptyList())
            .errMsg(ConstantUtils.ERR_MSG_MISSING)
            .build(),
        HttpStatus.BAD_REQUEST);
  }
}
