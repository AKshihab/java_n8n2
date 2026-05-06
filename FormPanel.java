import javax.swing.*;
import java.awt.*;

/**
 * FormPanel — JPanel containing the user input fields and Submit button.
 * Now includes Full Name, ID, and Email fields.
 */
public class FormPanel extends JPanel {

    private final JTextField  nameField;
    private final JTextField  idField;
    private final JTextField  emailField;
    private final JButton     submitButton;

    private final ResultPanel    resultPanel;
    private final WebhookService webhookService;

    public FormPanel(ResultPanel resultPanel) {
        this.resultPanel    = resultPanel;
        this.webhookService = new WebhookService();

        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        nameField    = new JTextField(22);
        idField      = new JTextField(22);
        emailField   = new JTextField(22);
        submitButton = new JButton("Submit");

        styleComponents();
        layoutComponents();
        attachListeners();
    }

    private void styleComponents() {
        Font f = new Font("SansSerif", Font.PLAIN, 13);
        nameField.setFont(f);
        idField.setFont(f);
        emailField.setFont(f);

        submitButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        submitButton.setBackground(new Color(0x3B82F6));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
    }

    private void layoutComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 4, 6, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Row 0 — Full Name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        add(makeLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(nameField, gbc);

        // Row 1 — ID
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        add(makeLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(idField, gbc);

        // Row 2 — Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        add(makeLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        add(emailField, gbc);

        // Row 3 — Submit button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill   = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        add(submitButton, gbc);
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private void attachListeners() {
        submitButton.addActionListener(e -> handleSubmit());
    }

    private void handleSubmit() {
        String name  = nameField.getText().trim();
        String id    = idField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
            resultPanel.showError("Please fill in Name, ID, and Email.");
            return;
        }

        setFormEnabled(false);
        resultPanel.showInfo("Sending request…");

        WebhookRequest request = new WebhookRequest(name, id, email);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() {
                return webhookService.send(request);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        resultPanel.showSuccess("Request sent! Check your email.");
                    } else {
                        resultPanel.showError("Error: could not reach webhook.");
                    }
                } catch (Exception ex) {
                    resultPanel.showError("Error: " + ex.getMessage());
                } finally {
                    setFormEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void setFormEnabled(boolean enabled) {
        nameField.setEnabled(enabled);
        idField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        submitButton.setEnabled(enabled);
    }
}