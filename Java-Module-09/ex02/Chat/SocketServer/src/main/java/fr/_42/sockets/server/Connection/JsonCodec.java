// package fr._42.sockets.server.Connection;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.stereotype.Component;

// @Component
// public class JsonCodec {

//     private final ObjectMapper objectMapper;

//     public JsonCodec() {
//         this.objectMapper = new ObjectMapper();
//     }

//     public String serialize(Object object) throws JsonProcessingException {
//         return objectMapper.writeValueAsString(object);
//     }

//     public <T> T deserialize(String json, Class<T> type) throws JsonProcessingException {
//         return objectMapper.readValue(json, type);
//     }
// }
