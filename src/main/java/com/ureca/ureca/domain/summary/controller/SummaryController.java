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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Summary", description = "상담 요약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summary")
public class SummaryController {

  private final UserService userService;
  private final SummaryService summaryService;

  @Operation(summary = "요약 리스트 조회", description = """
      상담 요약 리스트를 조회합니다.

      - view=all : 전체 내역
      - view=recent : 최근 내역(예: 최근 2개)
      - cursor : 무한스크롤 커서(id 기준, id < cursor)
      """)
  @GetMapping
  public ApiResponse<SummaryListResponseDto> getSummaries(
      @Parameter(hidden = true) Authentication authentication,

      @Parameter(description = "가져올 개수 (기본값: 10 - 서버 정책)",
          example = "10") @RequestParam(value = "limit", required = false) Integer limit,

      @Parameter(description = "무한스크롤 커서(이전 마지막 id)",
          example = "20") @RequestParam(value = "cursor", required = false) Long cursor,

      @Parameter(description = "조회 뷰 타입 (all | recent)", example = "all") @RequestParam(
          value = "view", required = false, defaultValue = "all") String view) {

    String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
    User user = userService.getByFirebaseUid(firebaseUid);

    SummaryListResponseDto response =
        summaryService.getSummaries(user.getId(), limit, cursor, view);

    return ApiResponse.ok("요약 리스트 조회 성공", response);
  }

  @Operation(summary = "요약 상세 조회", description = """
      특정 요약(summaryId)을 상세 조회합니다.

      - bubble=true : 하이라이트(중요 대화부분) 포함
      - bubble=false : 요약(제목/내용/날짜)만 반환
      """)
  @GetMapping("/{summaryId}")
  public ApiResponse<SummaryDetailResponseDto> getSummaryDetail(
      @Parameter(hidden = true) Authentication authentication,

      @Parameter(description = "요약 ID", example = "1",
          required = true) @PathVariable("summaryId") Long summaryId,

      @Parameter(description = "중요 대화(bubble) 포함 여부", example = "true") @RequestParam(
          name = "bubble", defaultValue = "false") boolean includeHighlights) {

    String firebaseUid = (String) authentication.getPrincipal(); // 필터에서 uid 넣어둠
    User user = userService.getByFirebaseUid(firebaseUid);

    SummaryDetailResponseDto response =
        summaryService.getSummaryDetail(user.getId(), summaryId, includeHighlights);
    return ApiResponse.ok("요약내용 상세보기 조회 성공", response);
  }

}
