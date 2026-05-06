import javax.swing.SwingUtilities;

/**
 * Main entry point for the Welcome System application.
 * Responsibility: Bootstrap only — launch the GUI on the Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppFrame frame = new AppFrame();
            frame.setVisible(true);
        });
    }
}
