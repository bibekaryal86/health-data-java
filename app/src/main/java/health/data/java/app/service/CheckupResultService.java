package health.data.java.app.service;

import health.data.java.app.model.CheckupCategory;
import health.data.java.app.model.CheckupComponent;
import health.data.java.app.model.CheckupResult;
import health.data.java.app.model.CheckupResultRequest;
import health.data.java.app.model.CheckupResultResponse;
import health.data.java.app.model.dto.CheckupResultDto;
import health.data.java.app.repository.CheckupResultRepository;
import health.data.java.app.util.CommonUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import health.data.java.app.util.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CheckupResultService {

    private final CheckupResultRepository checkupResultRepository;
    private final CheckupComponentService checkupComponentService;

    public CheckupResultService(CheckupResultRepository checkupResultRepository,
                                CheckupComponentService checkupComponentService) {
        this.checkupResultRepository = checkupResultRepository;
        this.checkupComponentService = checkupComponentService;
    }

    public CheckupResultResponse findCheckupResults(String username) {
        CheckupResultResponse checkupResultResponse;

        try {
            log.info("Find Checkup Results, Calling Repository");
            List<CheckupResultDto> checkupResultDtoList = checkupResultRepository.findCheckupResultDtoByUsername(username);
            List<CheckupComponent> checkupComponentList = checkupComponentService.findCheckupComponents().getCheckupComponentList();
            List<CheckupResult> checkupResultList = checkupResultDtoList.stream()
                    .map(checkupResultDto -> convertDtoToObject(checkupResultDto, checkupComponentList))
                    .sorted(Comparator.comparing((CheckupResult cr) -> cr.getCheckupComponent().getCheckupCategory().getCategoryName())
                            .thenComparing(cr -> cr.getCheckupComponent().getComponentName())
                            .thenComparing(CheckupResult::getCheckupDate).reversed())
                    .collect(Collectors.toList());

            checkupResultResponse = CheckupResultResponse.builder()
                    .checkupResultList(checkupResultList)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Find Checkup Results", ex);
            checkupResultResponse = CheckupResultResponse.builder()
                    .checkupResultList(Collections.emptyList())
                    .errMsg(ConstantUtils.ERR_MSG_FIND).build();
        }

        return checkupResultResponse;
    }

    public CheckupResultResponse saveCheckupResult(CheckupResult checkupResult) {
        CheckupResultResponse checkupResultResponse;
        CheckupResultDto checkupResultDto = convertObjectToDto(checkupResult);

        try {
            log.info("Save Checkup Result, Calling Repository: {}", checkupResult);
            checkupResultRepository.saveAndFlush(checkupResultDto);
            checkupResultResponse = CheckupResultResponse.builder()
                    .modifiedCount(1)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Save Checkup Result", ex);
            checkupResultResponse = CheckupResultResponse.builder()
                    .errMsg(ConstantUtils.ERR_MSG_SAVE)
                    .build();
        }

        return checkupResultResponse;
    }

    public CheckupResultResponse deleteCheckupResult(Integer id) {
        CheckupResultResponse checkupResultResponse;

        try {
            log.info("Delete Checkup Result, Calling Result: {}", id);
            checkupResultRepository.deleteById(id);
            checkupResultResponse = CheckupResultResponse.builder()
                    .modifiedCount(1)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Delete Checkup Result", ex);
            checkupResultResponse = CheckupResultResponse.builder()
                    .errMsg(ConstantUtils.ERR_MSG_DELETE)
                    .build();
        }

        return checkupResultResponse;
    }

    public boolean isValidCheckupResultRequest(CheckupResultRequest checkupResultRequest, boolean isInsert) {
        if (checkupResultRequest == null ||
                checkupResultRequest.getCheckupResult() == null ||
                checkupResultRequest.getCheckupResult().getCheckupComponent() == null ||
                !StringUtils.hasText(checkupResultRequest.getCheckupResult().getUsername()) ||
                !StringUtils.hasText(checkupResultRequest.getCheckupResult().getCheckupDate()) ||
                !StringUtils.hasText(checkupResultRequest.getCheckupResult().getTestResult()) ||
                !StringUtils.hasText(checkupResultRequest.getCheckupResult().getResultFlag()) ||
                !StringUtils.hasText(checkupResultRequest.getCheckupResult().getCheckupComponent().getId()) ||
                !CommonUtils.isValidDate(checkupResultRequest.getCheckupResult().getCheckupDate())) {
            return false;
        }

        return isInsert || CommonUtils.isValidRequestId(checkupResultRequest.getId());
    }

    private CheckupResult convertDtoToObject(CheckupResultDto checkupResultDto, List<CheckupComponent> checkupComponentList) {
        CheckupComponent checkupComponent = checkupComponentList.stream()
                .filter(checkupComponent1 -> checkupComponent1.getId().equals(checkupResultDto.getComponentId().toString()))
                .findFirst()
                .orElse(CheckupComponent.builder()
                        .checkupCategory(CheckupCategory.builder()
                                .build())
                        .build());

        return CheckupResult.builder()
                .id(checkupResultDto.getId().toString())
                .checkupDate(checkupResultDto.getCheckupDate())
                .testResult(checkupResultDto.getTestResult())
                .resultFlag(checkupResultDto.getResultFlag())
                .checkupComponent(CheckupComponent.builder()
                        .id(checkupResultDto.getComponentId().toString())
                        .componentName(checkupComponent.getComponentName())
                        .componentStandard(checkupComponent.getComponentStandard())
                        .componentComments(checkupComponent.getComponentComments())
                        .checkupCategory(checkupComponent.getCheckupCategory())
                        .build())
                .username(checkupResultDto.getUsername())
                .build();
    }

    private CheckupResultDto convertObjectToDto(CheckupResult checkupResult) {
        return CheckupResultDto.builder()
                .id(CommonUtils.getIntegerId(checkupResult.getId()))
                .componentId(CommonUtils.getIntegerId(checkupResult.getCheckupComponent().getId()))
                .checkupDate(checkupResult.getCheckupDate())
                .testResult(checkupResult.getTestResult())
                .resultFlag(checkupResult.getResultFlag())
                .username(checkupResult.getUsername())
                .build();
    }
}