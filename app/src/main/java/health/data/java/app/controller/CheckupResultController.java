package health.data.java.app.controller;

import health.data.java.app.model.CheckupResultRequest;
import health.data.java.app.model.CheckupResultResponse;
import health.data.java.app.service.CheckupResultService;
import health.data.java.app.util.CommonUtils;
import health.data.java.app.util.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/checkup_result")
public class CheckupResultController {

    private final CheckupResultService checkupResultService;

    public CheckupResultController(CheckupResultService checkupResultService) {
        this.checkupResultService = checkupResultService;
    }

    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckupResultResponse> find(@RequestParam String username) {
        if (StringUtils.hasText(username)) {
            return response(checkupResultService.findCheckupResults(username));
        }

        return badRequestResponse();
    }

    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckupResultResponse> insert(@RequestBody CheckupResultRequest checkupResultRequest) {
        if (checkupResultService.isValidCheckupResultRequest(checkupResultRequest, true)) {
            return response(checkupResultService.saveCheckupResult(checkupResultRequest.getCheckupResult()));
        }

        return badRequestResponse();
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckupResultResponse> update(@RequestBody CheckupResultRequest checkupResultRequest) {
        if (checkupResultService.isValidCheckupResultRequest(checkupResultRequest, false)) {
            return response(checkupResultService.saveCheckupResult(checkupResultRequest.getCheckupResult()));
        }

        return badRequestResponse();
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CheckupResultResponse> delete(@RequestParam String id) {
        if (CommonUtils.isValidRequestId(id)) {
            return response(checkupResultService.deleteCheckupResult(Integer.parseInt(id)));
        }

        return badRequestResponse();
    }

    private ResponseEntity<CheckupResultResponse> response(CheckupResultResponse checkupCategoryResponse) {
        if (!StringUtils.hasText(checkupCategoryResponse.getErrMsg())) {
            return ResponseEntity.ok(checkupCategoryResponse);
        }

        return new ResponseEntity<>(checkupCategoryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<CheckupResultResponse> badRequestResponse() {
        return new ResponseEntity<>(CheckupResultResponse.builder()
                .checkupResultList(Collections.emptyList())
                .errMsg(ConstantUtils.ERR_MSG_MISSING)
                .build(),
                HttpStatus.BAD_REQUEST);
    }
}
