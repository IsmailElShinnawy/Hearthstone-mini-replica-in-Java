package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//view
public class GameView extends JFrame {
	
	private ChooseHeroPanel firstHeroPanel;
	private ChooseHeroPanel secondHeroPanel;
	
	private GamePanel opponentHeroGamePanel;
	private GamePanel currentHeroGamePanel;
	private JPanel middleGamePanel;
	
	public GameView(String title) {
		super(title);
		
		setLayout(new BorderLayout());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		System.out.println(screenSize);
//		setMaximumSize(new Dimension(1000, 800));
//		setMinimumSize(new Dimension(1000, 800));
//		setPreferredSize(new Dimension(1000, 800));
		
		setMaximumSize(new Dimension((int)(screenSize.getWidth()*0.6), (int)(screenSize.getHeight()*0.95)));
		setMinimumSize(new Dimension((int)(screenSize.getWidth()*0.6), (int)(screenSize.getHeight()*0.95)));
		setPreferredSize(new Dimension((int)(screenSize.getWidth()*0.6), (int)(screenSize.getHeight()*0.95)));
		
		firstHeroPanel = new ChooseHeroPanel("Choose First Hero", getWidth(), getHeight());
		secondHeroPanel = new ChooseHeroPanel("Choose AI Hero", getWidth(), getHeight());
		
		add(firstHeroPanel, BorderLayout.NORTH);
		add(secondHeroPanel, BorderLayout.SOUTH);
		setVisible(true);
//		setMaximumSize(new Dimension(1000, 800));
//		setMinimumSize(new Dimension(1000, 800));
//		setPreferredSize(new Dimension(1000, 800));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void onGameStart() {
		opponentHeroGamePanel = new GamePanel();
		currentHeroGamePanel = new GamePanel();
		middleGamePanel = new JPanel();
		middleGamePanel.setLayout(new GridLayout(1, 2));
		
		add(middleGamePanel, BorderLayout.CENTER);
		add(opponentHeroGamePanel, BorderLayout.NORTH);
		add(currentHeroGamePanel, BorderLayout.SOUTH);
	}
	
	public void displayError(String message) {
		JOptionPane.showMessageDialog(new JFrame(), message, "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	public void displayWinner(String name) {
		JOptionPane.showMessageDialog(new JFrame(), name+" IS THE WINNER!!!!", "ERROR", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public JPanel getMiddleGamePanel() {
		return middleGamePanel;
	}
	
	public GamePanel getOpponentHeroGamePanel() {
		return opponentHeroGamePanel;
	}
	
	public GamePanel getCurrentHeroGamePanel() {
		return currentHeroGamePanel;
	}
	
	public ChooseHeroPanel getFirstHeroPanel() {
		return firstHeroPanel;
	}
	
	public ChooseHeroPanel getSecondHeroPanel() {
		return secondHeroPanel;
	}	
}
