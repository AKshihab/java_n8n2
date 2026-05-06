import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * WebhookService — handles all HTTP communication with the n8n webhook.
 * Responsibility: Serialize a WebhookRequest to JSON and POST it to n8n.
 *
 * NOTE: This method is designed to be called from a background thread
 *       (e.g., SwingWorker.doInBackground). It blocks until the server
 *       responds or the connection times out.
 */
public class WebhookService {

    // ── Configuration ────────────────────────────────────────────────────────
    /**
     * Paste your n8n PRODUCTION webhook URL here.
     * Format: https://<your-instance>/webhook/<webhook-id>
     * Example: https://myworkflow.app.n8n.cloud/webhook/a1b2c3d4-e5f6-...
     *
     * NOTE: Make sure your n8n workflow is ACTIVE (not just in test mode).
     * The workflow toggle must be ON so it listens 24/7.
     */
    private static final String WEBHOOK_URL = "https://siam175.app.n8n.cloud/webhook-test/913827e7-2bb5-4638-8205-28a4d877ac6b";

    private static final int TIMEOUT_SECONDS = 15;

    // ── Shared HttpClient (thread-safe, reusable) ─────────────────────────────
    private final HttpClient httpClient;

    public WebhookService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Sends the request as a JSON POST to the configured webhook URL.
     *
     * @param webhookRequest the data model to serialize
     * @return {@code true} if the server responded with HTTP 2xx, {@code false} otherwise
     */
    public boolean send(WebhookRequest webhookRequest) {
        try {
            String json = JsonUtil.toJson(webhookRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(WEBHOOK_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            System.out.printf("[WebhookService] Response %d — %s%n", statusCode, response.body());

            return statusCode >= 200 && statusCode < 300;

        } catch (Exception e) {
            System.err.println("[WebhookService] Request failed: " + e.getMessage());
            return false;
        }
    }
}
