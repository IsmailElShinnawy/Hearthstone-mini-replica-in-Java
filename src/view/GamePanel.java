package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GamePanel extends Container {

	private JPanel fieldPanel;
	private JPanel handPanel;
	private JTextArea detailsPanel;
	// private ArrayList<JButton> cards;
	// private ArrayList<JButton> field;
	// private int deckRemCards;
	// private String heroDetails;
	// private boolean currentHero;

	public GamePanel() {
		// ArrayList<JButton> cards, ArrayList<JButton> field, int deckRemCards, String
		// heroDetails,
		// boolean currentHero) {
		// this.cards = cards;
		// this.field = field;
		// this.deckRemCards = deckRemCards;
		// this.heroDetails = heroDetails;
		// this.currentHero = currentHero;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// setPreferredSize(new Dimension(1000, 350));
		setSize(new Dimension((int) screenSize.getWidth(), (int) (screenSize.getHeight() * 0.4)));
		fieldPanel = new JPanel();
		handPanel = new JPanel();
		// System.out.println(handPanel);

		detailsPanel = new JTextArea();
		detailsPanel.setEditable(false);

		fieldPanel.setLayout(new GridLayout(1, 10));
		handPanel.setLayout(new GridLayout(1, 7));

		// fieldPanel.setPreferredSize(new Dimension(1000, 150));
		// handPanel.setPreferredSize(new Dimension(1000, 150));
		// detailsPanel.setPreferredSize(new Dimension(1000, 50));

		fieldPanel.setPreferredSize(new Dimension(getWidth(), (int) (getHeight() * (3.0 / 7.0))));
		handPanel.setPreferredSize(new Dimension(getWidth(), (int) (getHeight() * (3.0 / 7.0))));
		detailsPanel.setPreferredSize(new Dimension(getWidth(), (int) (getHeight() * (1.0 / 7.0))));

		setLayout(new BorderLayout());
		add(fieldPanel, BorderLayout.NORTH);
		add(handPanel, BorderLayout.CENTER);
		add(detailsPanel, BorderLayout.SOUTH);

		setVisible(true);
		// setPreferredSize(new Dimension(1000, 350));
	}

	public JPanel getFieldPanel() {
		return this.fieldPanel;
	}

	public JPanel getHandPanel() {
		return this.handPanel;
	}

	public JTextArea getDetailsPanel() {
		return this.detailsPanel;
	}

}
