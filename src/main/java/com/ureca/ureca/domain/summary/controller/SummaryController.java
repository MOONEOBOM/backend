package com.ureca.ureca.domain.summary.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ureca.ureca.domain.summary.dto.SummaryDetailResponseDto;
import com.ureca.ureca.domain.summary.dto.SummaryListResponseDto;
import com.ureca.ureca.domain.summary.service.SummaryService;
import com.ureca.ureca.domain.user.dto.User;
import com.ureca.ureca.domain.user.service.UserService;
import com.ureca.ureca.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summary")
public class SummaryController {

  private final UserService userService;
  private final SummaryService summaryService;

  @GetMapping
  public ApiResponse<SummaryListResponseDto> getSummaries(Authentication authentication,
      @RequestParam(value = "limit", required = false) Integer limit,
      @RequestParam(value = "cursor", required = false) Long cursor,
      @RequestParam(value = "view", required = false, defaultValue = "all") String view) {

    String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
    User user = userService.getByFirebaseUid(firebaseUid);

    SummaryListResponseDto response =
        summaryService.getSummaries(user.getId(), limit, cursor, view);

    return ApiResponse.ok("요약 리스트 조회 성공", response);
  }

  @GetMapping("/{summaryId}")
  public ApiResponse<SummaryDetailResponseDto> getSummaryDetail(Authentication authentication,
      @PathVariable("summaryId") Long summaryId,
      @RequestParam(name = "bubble", defaultValue = "false") boolean includeHighlights) {

    String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
    User user = userService.getByFirebaseUid(firebaseUid);

    SummaryDetailResponseDto response =
        summaryService.getSummaryDetail(user.getId(), summaryId, includeHighlights);
    return ApiResponse.ok("요약내용 상세보기 조회 성공", response);
  }

}
