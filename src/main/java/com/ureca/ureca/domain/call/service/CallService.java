package com.ureca.ureca.domain.call.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ureca.ureca.domain.call.dto.CallCounselListResponseDto;
import com.ureca.ureca.domain.call.dto.CallMessagesResponseDto;
import com.ureca.ureca.domain.call.mapper.CallMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CallService {

  private final CallMapper callMapper;


  @Transactional
  public List<CallCounselListResponseDto> getCallCounselList() {
    callMapper.updateStartedAtToNow();
    return callMapper.findCallCounselList();
  }

  public List<CallMessagesResponseDto> getMessages(Long callId) {
    return callMapper.findMessagesByCallId(callId);
  }

}
