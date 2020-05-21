package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ChooseHeroPanel extends JPanel {

	public ChooseHeroPanel(String title, int pWidth, int pHeight) {
		setVisible(true);
		this.setBorder(BorderFactory.createTitledBorder(title));
//		setPreferredSize(new Dimension(1000, 300));
		setPreferredSize(new Dimension(pWidth, (int)(pHeight*0.3)));
		setLayout(new GridLayout(1, 5));
	}

	public void addHeroButton(JButton b) {
		add(b);
	}

}
