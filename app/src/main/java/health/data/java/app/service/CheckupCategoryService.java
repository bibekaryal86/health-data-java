package health.data.java.app.service;

import health.data.java.app.model.CheckupCategory;
import health.data.java.app.model.CheckupCategoryRequest;
import health.data.java.app.model.CheckupCategoryResponse;
import health.data.java.app.model.dto.CheckupCategoryDto;
import health.data.java.app.repository.CheckupCategoryRepository;
import health.data.java.app.util.CommonUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import health.data.java.app.util.ConstantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CheckupCategoryService {

    private final CheckupCategoryRepository checkupCategoryRepository;

    public CheckupCategoryService(CheckupCategoryRepository checkupCategoryRepository) {
        this.checkupCategoryRepository = checkupCategoryRepository;
    }

    @Cacheable(value = "checkupCategories", unless = "#result == null || #result.checkupCategoryList.size() == 0")
    public CheckupCategoryResponse findCheckupCategories() {
        CheckupCategoryResponse checkupCategoryResponse;

        try {
            log.info("Find Checkup Categories, Calling Repository");
            List<CheckupCategoryDto> checkupCategoryDtoList = checkupCategoryRepository.findAll();
            List<CheckupCategory> checkupCategoryList = checkupCategoryDtoList.stream()
                    .map(this::convertDtoToObject)
                    .sorted(Comparator.comparing(CheckupCategory::getCategoryName))
                    .collect(Collectors.toList());

            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .checkupCategoryList(checkupCategoryList)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Find Checkup Category", ex);
            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .checkupCategoryList(Collections.emptyList())
                    .errMsg(ConstantUtils.ERR_MSG_FIND)
                    .build();
        }

        return checkupCategoryResponse;
    }

    @CacheEvict(value = "checkupCategories", allEntries = true, beforeInvocation = true)
    public CheckupCategoryResponse saveCheckupCategory(CheckupCategory checkupCategory) {
        CheckupCategoryResponse checkupCategoryResponse;
        CheckupCategoryDto checkupCategoryDto = convertObjectToDto(checkupCategory);

        try {
            log.info("Save Checkup Category, Calling Repository: {}", checkupCategory);
            checkupCategoryRepository.saveAndFlush(checkupCategoryDto);
            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .modifiedCount(1)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Save Checkup Category", ex);
            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .errMsg(ConstantUtils.ERR_MSG_SAVE)
                    .build();
        }

        return checkupCategoryResponse;
    }

    @CacheEvict(value = "checkupCategories", allEntries = true, beforeInvocation = true)
    public CheckupCategoryResponse deleteCheckupCategory(Integer id) {
        CheckupCategoryResponse checkupCategoryResponse;

        try {
            log.info("Delete Checkup Category, Calling Repository: {}", id);
            checkupCategoryRepository.deleteById(id);
            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .modifiedCount(1)
                    .build();
        } catch (Exception ex) {
            log.error("Exception in Delete Checkup Category", ex);
            checkupCategoryResponse = CheckupCategoryResponse.builder()
                    .errMsg(ConstantUtils.ERR_MSG_DELETE)
                    .build();
        }

        return checkupCategoryResponse;
    }

    public boolean isValidCheckupCategoryRequest(CheckupCategoryRequest checkupCategoryRequest, boolean isInsert) {
        if (checkupCategoryRequest == null ||
                checkupCategoryRequest.getCheckupCategory() == null ||
                !StringUtils.hasText(checkupCategoryRequest.getCheckupCategory().getCategoryName())) {
            return false;
        }

        return isInsert || CommonUtils.isValidNumber(checkupCategoryRequest.getId());
    }

    private CheckupCategory convertDtoToObject(CheckupCategoryDto checkupCategoryDto) {
        return CheckupCategory.builder()
                .id(checkupCategoryDto.getId().toString())
                .categoryName(checkupCategoryDto.getCategoryName())
                .build();
    }

    private CheckupCategoryDto convertObjectToDto(CheckupCategory checkupCategory) {
        return CheckupCategoryDto.builder()
                .id(CommonUtils.getIntegerId(checkupCategory.getId()))
                .categoryName(checkupCategory.getCategoryName())
                .build();
    }
}
