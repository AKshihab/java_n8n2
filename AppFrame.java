import javax.swing.*;
import java.awt.*;

/**
 * AppFrame — the root JFrame window.
 * Responsibility: Compose the main layout and wire child panels together.
 * Uses BorderLayout: FormPanel in CENTER, ResultPanel in SOUTH.
 */
public class AppFrame extends JFrame {

    private final FormPanel   formPanel;
    private final ResultPanel resultPanel;

    public AppFrame() {
        super("Welcome System");

        // ── Window settings ─────────────────────────────────────────────────
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(480, 320));
        setLocationRelativeTo(null);   // center on screen

        // ── Build child panels ───────────────────────────────────────────────
        resultPanel = new ResultPanel();
        formPanel   = new FormPanel(resultPanel);

        // ── Assemble layout ──────────────────────────────────────────────────
        setLayout(new BorderLayout());

        JPanel wrapper = buildWrapperPanel();
        wrapper.add(formPanel,   BorderLayout.CENTER);
        wrapper.add(resultPanel, BorderLayout.SOUTH);

        add(wrapper, BorderLayout.CENTER);

        pack();
    }

    /** Adds a uniform inset margin around the two child panels. */
    private JPanel buildWrapperPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(Color.WHITE);
        return panel;
    }
}
