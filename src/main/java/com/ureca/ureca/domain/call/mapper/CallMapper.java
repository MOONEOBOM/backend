package com.ureca.ureca.domain.call.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ureca.ureca.domain.call.dto.CallCounselListResponseDto;
import com.ureca.ureca.domain.call.dto.CallMessagesResponseDto;

@Mapper
public interface CallMapper {

  // 전화상담 목록조회
  List<CallCounselListResponseDto> findCallCounselList();

  // 특정 전화 상담 조회
  List<CallMessagesResponseDto> findMessagesByCallId(@Param("callId") Long callId);

  // 1번 최신화
  int updateStartedAtToNow();
}

