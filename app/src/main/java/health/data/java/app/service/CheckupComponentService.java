package health.data.java.app.service;

import health.data.java.app.model.CheckupCategory;
import health.data.java.app.model.CheckupComponent;
import health.data.java.app.model.CheckupComponentRequest;
import health.data.java.app.model.CheckupComponentResponse;
import health.data.java.app.model.dto.CheckupComponentDto;
import health.data.java.app.repository.CheckupComponentRepository;
import health.data.java.app.util.CommonUtils;
import health.data.java.app.util.ConstantUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CheckupComponentService {

  private final CheckupComponentRepository checkupComponentRepository;
  private final CheckupCategoryService checkupCategoryService;

  public CheckupComponentService(
      CheckupComponentRepository checkupComponentRepository,
      CheckupCategoryService checkupCategoryService) {
    this.checkupComponentRepository = checkupComponentRepository;
    this.checkupCategoryService = checkupCategoryService;
  }

  @Cacheable(
      value = "checkupComponents",
      unless = "#result == null || #result.checkupComponentList.size() == 0")
  public CheckupComponentResponse findCheckupComponents() {
    CheckupComponentResponse checkupComponentResponse;

    try {
      log.info("Find Checkup Components, Calling Repository");
      List<CheckupComponentDto> checkupComponentDtoList = checkupComponentRepository.findAll();
      List<CheckupCategory> checkupCategoryList =
          checkupCategoryService.findCheckupCategories().getCheckupCategoryList();
      List<CheckupComponent> checkupComponentList =
          checkupComponentDtoList.stream()
              .map(
                  checkupComponentDto ->
                      convertDtoToObject(checkupComponentDto, checkupCategoryList))
              .sorted(
                  Comparator.comparing(
                          (CheckupComponent cc) -> cc.getCheckupCategory().getCategoryName())
                      .thenComparing(CheckupComponent::getComponentName))
              .collect(Collectors.toList());

      checkupComponentResponse =
          CheckupComponentResponse.builder().checkupComponentList(checkupComponentList).build();
    } catch (Exception ex) {
      log.error("Exception in Find Checkup Component", ex);
      checkupComponentResponse =
          CheckupComponentResponse.builder()
              .checkupComponentList(Collections.emptyList())
              .errMsg(ConstantUtils.ERR_MSG_FIND)
              .build();
    }

    return checkupComponentResponse;
  }

  @CacheEvict(value = "checkupComponents", allEntries = true, beforeInvocation = true)
  public CheckupComponentResponse saveCheckupComponent(CheckupComponent checkupComponent) {
    CheckupComponentResponse checkupComponentResponse;
    CheckupComponentDto checkupComponentDto = convertObjectToDto(checkupComponent);

    try {
      log.info("Save Checkup Component, Calling Repository: {}", checkupComponent);
      checkupComponentRepository.saveAndFlush(checkupComponentDto);
      checkupComponentResponse = CheckupComponentResponse.builder().modifiedCount(1).build();
    } catch (Exception ex) {
      log.error("Exception in Save Checkup Component", ex);
      checkupComponentResponse =
          CheckupComponentResponse.builder().errMsg(ConstantUtils.ERR_MSG_SAVE).build();
    }

    return checkupComponentResponse;
  }

  @CacheEvict(value = "checkupComponents", allEntries = true, beforeInvocation = true)
  public CheckupComponentResponse deleteCheckupComponent(Integer id) {
    CheckupComponentResponse checkupComponentResponse;

    try {
      log.info("Delete Checkup Component, Calling Repository: {}", id);
      checkupComponentRepository.deleteById(id);
      checkupComponentResponse = CheckupComponentResponse.builder().modifiedCount(1).build();
    } catch (Exception ex) {
      log.error("Exception in Delete Checkup Component", ex);
      checkupComponentResponse =
          CheckupComponentResponse.builder().errMsg(ConstantUtils.ERR_MSG_DELETE).build();
    }
    return checkupComponentResponse;
  }

  public boolean isValidCheckupComponentRequest(
      CheckupComponentRequest checkupComponentRequest, boolean isInsert) {
    if (checkupComponentRequest == null
        || checkupComponentRequest.getCheckupComponent() == null
        || checkupComponentRequest.getCheckupComponent().getCheckupCategory() == null
        || !StringUtils.hasText(checkupComponentRequest.getCheckupComponent().getComponentName())
        || !CommonUtils.isValidNumber(
            checkupComponentRequest.getCheckupComponent().getCheckupCategory().getId())) {
      return false;
    }

    if ((checkupComponentRequest.getCheckupComponent().getStandardLow() != null
            && (checkupComponentRequest.getCheckupComponent().getStandardHigh() == null
                || !StringUtils.hasText(
                    checkupComponentRequest.getCheckupComponent().getMeasureUnit())))
        || (checkupComponentRequest.getCheckupComponent().getStandardHigh() != null
            && (checkupComponentRequest.getCheckupComponent().getStandardLow() == null
                || !StringUtils.hasText(
                    checkupComponentRequest.getCheckupComponent().getMeasureUnit())))
        || (StringUtils.hasText(checkupComponentRequest.getCheckupComponent().getMeasureUnit())
            && (checkupComponentRequest.getCheckupComponent().getStandardLow() == null
                || checkupComponentRequest.getCheckupComponent().getStandardHigh() == null))) {

      log.info(
          "Checkup Component Invalid with Standards and Measure Unit: {}", checkupComponentRequest);
      return false;
    }

    return isInsert || CommonUtils.isValidNumber(checkupComponentRequest.getId());
  }

  private CheckupComponent convertDtoToObject(
      CheckupComponentDto checkupComponentDto, List<CheckupCategory> checkupCategoryList) {
    return CheckupComponent.builder()
        .id(checkupComponentDto.getId().toString())
        .componentName(checkupComponentDto.getComponentName())
        .standardLow(checkupComponentDto.getStandardLow())
        .standardHigh(checkupComponentDto.getStandardHigh())
        .measureUnit(checkupComponentDto.getMeasureUnit())
        .componentComments(checkupComponentDto.getComponentComments())
        .standardRange(
            CommonUtils.findStandardRange(
                checkupComponentDto.getStandardLow(),
                checkupComponentDto.getStandardHigh(),
                checkupComponentDto.getMeasureUnit()))
        .checkupCategory(
            checkupCategoryList.stream()
                .filter(
                    checkupCategory ->
                        checkupCategory
                            .getId()
                            .equals(checkupComponentDto.getCategoryId().toString()))
                .findFirst()
                .orElse(CheckupCategory.builder().build()))
        .build();
  }

  private CheckupComponentDto convertObjectToDto(CheckupComponent checkupComponent) {
    return CheckupComponentDto.builder()
        .id(CommonUtils.getIntegerId(checkupComponent.getId()))
        .categoryId(CommonUtils.getIntegerId(checkupComponent.getCheckupCategory().getId()))
        .componentName(checkupComponent.getComponentName())
        .standardLow(checkupComponent.getStandardLow())
        .standardHigh(checkupComponent.getStandardHigh())
        .measureUnit(checkupComponent.getMeasureUnit())
        .componentComments(checkupComponent.getComponentComments())
        .build();
  }
}
