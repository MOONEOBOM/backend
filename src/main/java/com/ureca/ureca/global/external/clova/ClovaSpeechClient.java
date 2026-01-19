package com.ureca.ureca.global.external.clova;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.ureca.ureca.global.common.exception.BusinessException;
import com.ureca.ureca.global.common.exception.ErrorCode;
import jakarta.annotation.PreDestroy;

@Component
public class ClovaSpeechClient {

  private final ClovaSpeechProperties properties;
  private final CloseableHttpClient httpClient = HttpClients.createDefault();
  private final Gson gson = new Gson();

  public ClovaSpeechClient(ClovaSpeechProperties properties) {
    this.properties = properties;
  }

  @PreDestroy
  public void cleanup() throws IOException {
    if (httpClient != null) {
      httpClient.close();
    }
  }

  private Header[] headers() {
    return new Header[] {new BasicHeader("Accept", "application/json"),
        new BasicHeader("X-CLOVASPEECH-API-KEY", properties.getSecret()),};
  }

  public static class Boosting {
    private String words;

    public String getWords() {
      return words;
    }

    public void setWords(String words) {
      this.words = words;
    }
  }

  public static class Diarization {
    private Boolean enable = Boolean.FALSE;
    private Integer speakerCountMin;
    private Integer speakerCountMax;

    public Boolean getEnable() {
      return enable;
    }

    public void setEnable(Boolean enable) {
      this.enable = enable;
    }

    public Integer getSpeakerCountMin() {
      return speakerCountMin;
    }

    public void setSpeakerCountMin(Integer speakerCountMin) {
      this.speakerCountMin = speakerCountMin;
    }

    public Integer getSpeakerCountMax() {
      return speakerCountMax;
    }

    public void setSpeakerCountMax(Integer speakerCountMax) {
      this.speakerCountMax = speakerCountMax;
    }
  }

  public static class Sed {
    private Boolean enable = Boolean.FALSE;

    public Boolean getEnable() {
      return enable;
    }

    public void setEnable(Boolean enable) {
      this.enable = enable;
    }
  }

  public static class NestRequestEntity {
    private String language = "ko-KR";
    // completion optional, sync/async (응답 결과 반환 방식(sync/async) 설정, 필수 파라미터 아님)
    private String completion = "sync";
    // optional, used to receive the analyzed results (분석된 결과 조회 용도, 필수 파라미터 아님)
    private String callback;
    // optional, any data (임의의 Callback URL 값 입력, 필수 파라미터 아님)
    private Map<String, Object> userdata;
    private Boolean wordAlignment = Boolean.TRUE;
    private Boolean fullText = Boolean.TRUE;
    // boosting object array (키워드 부스팅 객체 배열)
    private List<Boosting> boostings;
    // comma separated words (쉼표 구분 키워드)
    private String forbiddens;
    private Diarization diarization;
    private Sed sed;

    public Sed getSed() {
      return sed;
    }

    public void setSed(Sed sed) {
      this.sed = sed;
    }

    public String getLanguage() {
      return language;
    }

    public void setLanguage(String language) {
      this.language = language;
    }

    public String getCompletion() {
      return completion;
    }

    public void setCompletion(String completion) {
      this.completion = completion;
    }

    public String getCallback() {
      return callback;
    }

    public Boolean getWordAlignment() {
      return wordAlignment;
    }

    public void setWordAlignment(Boolean wordAlignment) {
      this.wordAlignment = wordAlignment;
    }

    public Boolean getFullText() {
      return fullText;
    }

    public void setFullText(Boolean fullText) {
      this.fullText = fullText;
    }

    public void setCallback(String callback) {
      this.callback = callback;
    }

    public Map<String, Object> getUserdata() {
      return userdata;
    }

    public void setUserdata(Map<String, Object> userdata) {
      this.userdata = userdata;
    }

    public String getForbiddens() {
      return forbiddens;
    }

    public void setForbiddens(String forbiddens) {
      this.forbiddens = forbiddens;
    }

    public List<Boosting> getBoostings() {
      return boostings;
    }

    public void setBoostings(List<Boosting> boostings) {
      this.boostings = boostings;
    }

    public Diarization getDiarization() {
      return diarization;
    }

    public void setDiarization(Diarization diarization) {
      this.diarization = diarization;
    }
  }

  /**
   * recognize media using URL (외부 파일 URL로 음성 인식 요청)
   * 
   * @param url required, the media URL (필수 파라미터, 외부 파일 URL)
   * @param nestRequestEntity optional (필수 파라미터가 아님)
   * @return string (문자열 반환)
   */
  public String url(String url, NestRequestEntity nestRequestEntity) {
    HttpPost httpPost = new HttpPost(properties.getInvokeUrl() + "/recognizer/url");
    httpPost.setHeaders(headers());
    Map<String, Object> body = new HashMap<>();
    body.put("url", url);
    body.put("language", nestRequestEntity.getLanguage());
    body.put("completion", nestRequestEntity.getCompletion());
    body.put("callback", nestRequestEntity.getCallback());
    body.put("userdata", nestRequestEntity.getUserdata());
    body.put("wordAlignment", nestRequestEntity.getWordAlignment());
    body.put("fullText", nestRequestEntity.getFullText());
    body.put("forbiddens", nestRequestEntity.getForbiddens());
    body.put("boostings", nestRequestEntity.getBoostings());
    body.put("diarization", nestRequestEntity.getDiarization());
    body.put("sed", nestRequestEntity.getSed());
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(body);

      StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

      httpPost.setEntity(httpEntity);
      return execute(httpPost);

    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Clova 요청 JSON 직렬화 실패", e);
    }
  }

  /**
   * recognize media using Object Storage (네이버 클라우드 픒랫폼의 Object Storage 내 파일 URL로 음성 인식 요청)
   * 
   * @param dataKey required, the Object Storage key (필수 파라미터, Object Storage 키 값)
   * @param nestRequestEntity optional (필수 파라미터가 아님)
   * @return string (문자열 반환)
   */
  public String objectStorage(String dataKey, NestRequestEntity nestRequestEntity) {
    HttpPost httpPost = new HttpPost(properties.getInvokeUrl() + "/recognizer/object-storage");
    httpPost.setHeaders(headers());
    Map<String, Object> body = new HashMap<>();
    body.put("dataKey", dataKey);
    body.put("language", nestRequestEntity.getLanguage());
    body.put("completion", nestRequestEntity.getCompletion());
    body.put("callback", nestRequestEntity.getCallback());
    body.put("userdata", nestRequestEntity.getUserdata());
    body.put("wordAlignment", nestRequestEntity.getWordAlignment());
    body.put("fullText", nestRequestEntity.getFullText());
    body.put("forbiddens", nestRequestEntity.getForbiddens());
    body.put("boostings", nestRequestEntity.getBoostings());
    body.put("diarization", nestRequestEntity.getDiarization());
    body.put("sed", nestRequestEntity.getSed());
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(body);

      StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);

      httpPost.setEntity(httpEntity);
      return execute(httpPost);

    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Clova 요청 JSON 직렬화 실패", e);
    }
  }

  /**
   *
   * recognize media using a file (로컬 파일 업로드 후 음성 인식 요청)
   * 
   * @param file required, the media file (필수 파라미터, 로컬 파일)
   * @param nestRequestEntity optional (필수 파라미터가 아님)
   * @return string (문자열 반환)
   */
  public String upload(File file, NestRequestEntity nestRequestEntity) {
    HttpPost httpPost = new HttpPost(properties.getInvokeUrl() + "/recognizer/upload");
    httpPost.setHeaders(headers());
    HttpEntity httpEntity = MultipartEntityBuilder.create()
        .addTextBody("params", gson.toJson(nestRequestEntity), ContentType.APPLICATION_JSON)
        .addBinaryBody("media", file, ContentType.MULTIPART_FORM_DATA, file.getName()).build();
    httpPost.setEntity(httpEntity);
    return execute(httpPost);
  }

  private String execute(HttpPost httpPost) {
    try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
      int statusCode = httpResponse.getStatusLine().getStatusCode();
      if (statusCode < 200 || statusCode >= 300) {
        String body = EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
        throw new BusinessException(ErrorCode.CLOVA_API_ERROR,
            "Clova API error: " + statusCode + " - " + body);
      }
      final HttpEntity entity = httpResponse.getEntity();
      return EntityUtils.toString(entity, StandardCharsets.UTF_8);
    } catch (Exception e) {
      if (e instanceof BusinessException) {
        throw (BusinessException) e;
      }
      throw new BusinessException(ErrorCode.CLOVA_API_ERROR, e.getMessage());
    }
  }
}

