# java_n8n2

A Java Swing desktop application that collects user information through a form and sends it to an n8n webhook. The n8n workflow can then send a Gmail confirmation email.

This project uses pure Java only. No Maven, Gradle, or external libraries are required.

---

## Features

- Java Swing desktop GUI
- Collects full name, email, and query/message
- Sends data to an n8n webhook using HTTP POST
- Sends JSON payload to n8n
- Can trigger Gmail confirmation email through n8n
- Non-blocking GUI using background processing

---

## OOP Structure

| Class | Responsibility |
|---|---|
| `Main` | Entry point of the application |
| `AppFrame` | Main Swing window |
| `FormPanel` | Input form for name, email, and query |
| `ResultPanel` | Shows success or error messages |
| `WebhookRequest` | Data model for webhook request |
| `WebhookService` | Sends data to the n8n webhook |
| `JsonUtil` | Creates JSON payload manually |

---

## Project Structure

```text
java_n8n2/
├── LICENSE
├── README.md
├── AppFrame.java
├── FormPanel.java
├── JsonUtil.java
├── Main.java
├── ResultPanel.java
├── WebhookRequest.java
└── WebhookService.java