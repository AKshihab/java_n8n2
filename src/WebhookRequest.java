/**
 * WebhookRequest — immutable data model (POJO) for the webhook payload.
 * Responsibility: Hold the user's name, ID, and Email as a plain Java object.
 *
 * This class has no logic — it is serialized to JSON by JsonUtil.
 */
public class WebhookRequest {

    private final String name;
    private final String id;
    private final String email;

    public WebhookRequest(String name, String id, String email) {
        if (name  == null || name.isBlank())  throw new IllegalArgumentException("name must not be blank");
        if (id    == null || id.isBlank())    throw new IllegalArgumentException("id must not be blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email must not be blank");
        this.name  = name;
        this.id    = id;
        this.email = email;
    }

    public String getName()  { return name;  }
    public String getId()    { return id;    }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "WebhookRequest{name='" + name + "', id='" + id + "', email='" + email + "'}";
    }
}