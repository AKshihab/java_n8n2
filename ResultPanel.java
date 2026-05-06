import javax.swing.*;
import java.awt.*;

/**
 * ResultPanel — JPanel that displays a status message to the user.
 * Responsibility: Render success, error, or neutral info messages
 *                 with appropriate colours.
 */
public class ResultPanel extends JPanel {

    private static final Color COLOR_SUCCESS = new Color(0x16A34A); // green-600
    private static final Color COLOR_ERROR   = new Color(0xDC2626); // red-600
    private static final Color COLOR_INFO    = new Color(0x6B7280); // gray-500

    private final JLabel messageLabel;

    public ResultPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 6));
        setBackground(Color.WHITE);

        messageLabel = new JLabel(" ");   // non-empty to reserve vertical space
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(messageLabel);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /** Green text — request succeeded. */
    public void showSuccess(String message) {
        display(message, COLOR_SUCCESS);
    }

    /** Red text — something went wrong. */
    public void showError(String message) {
        display(message, COLOR_ERROR);
    }

    /** Gray text — neutral status (e.g. "Sending…"). */
    public void showInfo(String message) {
        display(message, COLOR_INFO);
    }

    /** Clears the status area. */
    public void clear() {
        display(" ", COLOR_INFO);
    }

    // ── Internals ─────────────────────────────────────────────────────────────

    private void display(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
        revalidate();
        repaint();
    }
}
