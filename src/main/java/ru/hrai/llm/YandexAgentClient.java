package ru.hrai.llm;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.hrai.config.YandexProperties;
@Component
public class YandexAgentClient implements LlmClient {
 private static final Logger log = LoggerFactory.getLogger(YandexAgentClient.class);
 private final RestClient client; private final YandexProperties p;
 public YandexAgentClient(RestClient client, YandexProperties p) { this.client=client; this.p=p; }
 @Override @SuppressWarnings("unchecked") public String generate(String input) {
  if (p.apiKey()==null || p.apiKey().isBlank()) throw new IllegalStateException("YANDEX_API_KEY is not configured");
  if (p.agentId()==null || p.agentId().isBlank()) throw new IllegalStateException("YANDEX_AGENT_ID is not configured");
  log.info("Calling Yandex AI Studio Agent; prompt content is intentionally not logged");
  Map<String,Object> body=Map.of("prompt",Map.of("id",p.agentId()),"input",input,"tools",List.of(Map.of("type","web_search","filters",Map.of("allowed_domains",List.of()),"search_context_size","medium")));
  Map<String,Object> response=client.post().uri("/responses").header(HttpHeaders.AUTHORIZATION,"Bearer "+p.apiKey()).header("OpenAI-Project",p.projectId()).header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE).body(body).retrieve().body(Map.class);
  if(response==null) throw new IllegalStateException("Empty response from Yandex AI Studio");
  Object out=response.get("output_text"); if(out instanceof String s && !s.isBlank()) return s.trim();
  Object output=response.get("output"); if(output instanceof List<?> items) { StringBuilder text=new StringBuilder(); for(Object item:items) if(item instanceof Map<?,?> m && m.get("content") instanceof List<?> blocks) for(Object b:blocks) if(b instanceof Map<?,?> block && block.get("text") instanceof String s) { if(!text.isEmpty()) text.append("\n"); text.append(s); } if(!text.isEmpty()) return text.toString().trim(); }
  throw new IllegalStateException("Yandex AI Studio returned no text output");
 }
}
