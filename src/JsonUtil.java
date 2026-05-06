import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonUtil — lightweight JSON serializer / deserializer.
 * Handles flat objects only (no arrays or nested objects).
 */
public class JsonUtil {

    private JsonUtil() {}

    /** Converts a WebhookRequest into a JSON string including email. */
    public static String toJson(WebhookRequest request) {
        return buildObject(
                "name",  request.getName(),
                "id",    request.getId(),
                "email", request.getEmail()
        );
    }

    public static String buildObject(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0)
            throw new IllegalArgumentException("keyValuePairs must be alternating key/value pairs");
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(escapeJson(keyValuePairs[i])).append("\"");
            sb.append(":");
            sb.append("\"").append(escapeJson(keyValuePairs[i + 1])).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    public static Map<String, String> parseObject(String json) {
        Map<String, String> result = new LinkedHashMap<>();
        if (json == null) return result;
        String trimmed = json.trim();
        if (!trimmed.startsWith("{") || !trimmed.endsWith("}"))
            throw new IllegalArgumentException("Input is not a JSON object: " + json);
        String body = trimmed.substring(1, trimmed.length() - 1).trim();
        if (body.isEmpty()) return result;
        for (String pair : splitOnTopLevelCommas(body)) {
            int colonIdx = findColonOutsideQuotes(pair);
            if (colonIdx < 0) continue;
            result.put(unquote(pair.substring(0, colonIdx).trim()),
                       unquote(pair.substring(colonIdx + 1).trim()));
        }
        return result;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private static String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2)
            s = s.substring(1, s.length() - 1);
        return s.replace("\\\"", "\"").replace("\\\\", "\\")
                .replace("\\n", "\n").replace("\\r", "\r").replace("\\t", "\t");
    }

    private static String[] splitOnTopLevelCommas(String body) {
        java.util.List<String> parts = new java.util.ArrayList<>();
        boolean inQuotes = false;
        int start = 0;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '"' && (i == 0 || body.charAt(i - 1) != '\\')) inQuotes = !inQuotes;
            if (c == ',' && !inQuotes) { parts.add(body.substring(start, i)); start = i + 1; }
        }
        parts.add(body.substring(start));
        return parts.toArray(new String[0]);
    }

    private static int findColonOutsideQuotes(String pair) {
        boolean inQuotes = false;
        for (int i = 0; i < pair.length(); i++) {
            char c = pair.charAt(i);
            if (c == '"' && (i == 0 || pair.charAt(i - 1) != '\\')) inQuotes = !inQuotes;
            if (c == ':' && !inQuotes) return i;
        }
        return -1;
    }
}