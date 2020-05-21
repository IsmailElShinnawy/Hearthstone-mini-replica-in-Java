package engine;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.DivineSpirit;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.KillCommand;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Polymorph;
import model.cards.spells.Pyroblast;
import model.cards.spells.SealOfChampions;
import model.cards.spells.ShadowWordDeath;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import view.GameView;

//controller
public class GameController implements GameListener, ActionListener, MouseMotionListener {

	private Game game; // model
	private boolean started;
	private static boolean AI = true;
	private static boolean drawImages = false;

	private Hero h1;
	private Hero h2;

	private int idx1 = -1;
	private int idx2 = -1;

	private Minion chosenMinion;
	private Minion targetMinion;
	private boolean magePower = false;
	private boolean priestPower = false;

	private Spell spellToBeCasted;

	private GameView view; // view

	private ArrayList<JButton> firstHeroButtons;
	private ArrayList<JButton> secondHeroButtons;

	private ArrayList<JButton> currentHeroHand;
	private ArrayList<Card> cards;
	private ArrayList<JButton> opponentHeroHand;

	private ArrayList<JButton> currentHeroField;
	private ArrayList<Minion> field;
	private ArrayList<JButton> opponentHeroField;
	private ArrayList<Minion> oppField;

	private JButton startGame;
	private JButton endTurn;
	private JButton useHeroPower;
	private JButton attackHeroOpponent;
	private JButton healCurrentHero;

	// private static final String[] HERO_NAMES = { "Hunter\nRexxar", "Mage\nJaina
	// Proudmoore",
	// "Paladin\nUther lightbringer", "Preist\nAnduin Wrynn", "Warlock\nGul'dan" };
	private static final String[] HERO_NAMES = { "Rexxar", "Jaina Proudmoore", "Uther lightbringer", "Anduin Wrynn",
			"Gul'dan" };

	public GameController() {

		view = new GameView("HearthStone Game");

		firstHeroButtons = new ArrayList<JButton>();
		secondHeroButtons = new ArrayList<JButton>();

		currentHeroHand = new ArrayList<JButton>();
		cards = new ArrayList<Card>();
		opponentHeroHand = new ArrayList<JButton>();

		currentHeroField = new ArrayList<JButton>();
		field = new ArrayList<Minion>();
		opponentHeroField = new ArrayList<JButton>();
		oppField = new ArrayList<Minion>();

		startGame = new JButton("Start Game");
		startGame.addActionListener(this);
		view.getContentPane().add(startGame, BorderLayout.CENTER);

		for (int i = 0; i < 5; i++) {
			JButton b1;
			JButton b2;
			if (!drawImages) {
				b1 = new JButton("<html>" + HERO_NAMES[i].replaceAll("\\n", "<br>") + "</html>");
				b2 = new JButton("<html>" + HERO_NAMES[i].replaceAll("\\n", "<br>") + "</html>");
			} else {
				b1 = new JButton();
				b2 = new JButton();
				b1.setIcon(new ImageIcon("res/heros/" + HERO_NAMES[i].toLowerCase().replaceAll(" ", "_") + ".jfif"));
				b2.setIcon(new ImageIcon("res/heros/" + HERO_NAMES[i].toLowerCase().replaceAll(" ", "_") + ".jfif"));
			}
			b1.addActionListener(this);
			b1.setActionCommand(i + "");
			b2.addActionListener(this);
			b2.setActionCommand(i + "");
			firstHeroButtons.add(b1);
			secondHeroButtons.add(b2);
			view.getFirstHeroPanel().addHeroButton(b1);
			view.getSecondHeroPanel().addHeroButton(b2);
		}
		view.repaint();
		view.revalidate();
	}

	public void onGameStart(int idx1, int idx2) {
		try {
			h1 = getHero(idx1);
			h2 = getHero(idx2); // AI
			game = new Game(h1, h2);
		} catch (IOException e) {
			// probably won't be thrown
			view.displayError(e.getMessage());
		} catch (CloneNotSupportedException e) {
			view.displayError(e.getMessage());
		} catch (FullHandException e) {
			view.displayError(e.getMessage() + "\n" + e.getBurned().toString());
		}
		game.setListener(this);
		view.getContentPane().removeAll();

		view.onGameStart();
		updateView();

		if (game.getCurrentHero() == h2) {
			if (AI) {
				try {
					System.out.println("AI turn...computing best move...");
					MiniMax miniMax = new MiniMax(game.clone());
					ArrayList<Integer[]> actions = miniMax.getBestActions();
//					if(actions!=null) {
//						for (Integer[] action : actions) {
//							System.out.println(Arrays.toString(action));
//						}
//					}
//					System.out.println("-------------------");
					playAIMove(actions);
					updateView();
					view.repaint();
					view.revalidate();
				} catch (CloneNotSupportedException exc) {
					System.out.println("clone problem");
				} 
				catch (NotYourTurnException | NotEnoughManaException | FullFieldException | CannotAttackException
						| TauntBypassException | NotSummonedException | InvalidTargetException | FullHandException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		view.repaint();
		view.revalidate();
	}

	public void updateView() {
		
		endTurn = new JButton("End Turn");
		useHeroPower = new JButton("Use Hero Power");
		attackHeroOpponent = new JButton("Attack Opponent");
		healCurrentHero = new JButton("Heal Hero");

		healCurrentHero.setEnabled(false);

		endTurn.addActionListener(this);
		useHeroPower.addActionListener(this);
		useHeroPower.addMouseMotionListener(this);
		attackHeroOpponent.addActionListener(this);
		healCurrentHero.addActionListener(this);
		
		view.getMiddleGamePanel().removeAll();
		
		view.getMiddleGamePanel().add(endTurn);
		view.getMiddleGamePanel().add(useHeroPower);
		view.getMiddleGamePanel().add(attackHeroOpponent);
		view.getMiddleGamePanel().add(healCurrentHero);
		
		currentHeroHand = new ArrayList<JButton>();
		cards = new ArrayList<Card>();
		opponentHeroHand = new ArrayList<JButton>();

		currentHeroField = new ArrayList<JButton>();
		field = new ArrayList<Minion>();
		opponentHeroField = new ArrayList<JButton>();
		oppField = new ArrayList<Minion>();

		view.getOpponentHeroGamePanel().getHandPanel().removeAll();
		view.getCurrentHeroGamePanel().getHandPanel().removeAll();

		view.getOpponentHeroGamePanel().getFieldPanel().removeAll();
		view.getCurrentHeroGamePanel().getFieldPanel().removeAll();

		view.getCurrentHeroGamePanel().getDetailsPanel().setText("");
		view.getOpponentHeroGamePanel().getDetailsPanel().setText("");

		view.getCurrentHeroGamePanel().getDetailsPanel().append("Current Hero: " + game.getCurrentHero().toString());
		view.getOpponentHeroGamePanel().getDetailsPanel().append("Opponent Hero: " + game.getOpponent().toString());

		int i = 0;
		for (Card c : game.getCurrentHero().getHand()) {
			JButton inHand;
			if (!drawImages) {
				inHand = new JButton("<html>" + c.toString().replaceAll("\\n", "<br>") + "</html>");
				inHand.setFont(new Font("times new roman", Font.PLAIN, 10));
			} else {
				inHand = new JButton();
				if (c instanceof Spell) {
					inHand.setIcon(
							new ImageIcon("res/spells/" + c.getName().toLowerCase().replaceAll(" ", "_") + ".jfif"));
				} else {
					inHand.setIcon(
							new ImageIcon("res/minions/" + c.getName().toLowerCase().replaceAll(" ", "_") + ".jfif"));
				}
			}
			inHand.addActionListener(this);
			inHand.addMouseMotionListener(this);
			inHand.setActionCommand((i++) + "");
			cards.add(c);
			currentHeroHand.add(inHand);
			view.getCurrentHeroGamePanel().getHandPanel().add(inHand);
		}

		for (i = 0; i < game.getOpponent().getHand().size();) {
			JButton inHand = new JButton("");
			if (drawImages) {
				inHand.setIcon(new ImageIcon("res/utils/card_back.png"));
				inHand.setDisabledIcon(new ImageIcon("res/utils/card_back.png"));
			}
			inHand.setEnabled(false);
			inHand.addActionListener(this);
			inHand.setActionCommand((i++) + "");
			opponentHeroHand.add(inHand);
			view.getOpponentHeroGamePanel().getHandPanel().add(inHand);
		}

		i = 0;
		for (Minion m : game.getCurrentHero().getField()) {
			JButton onField;
			if (!drawImages) {
				onField = new JButton("<html>" + m.getFieldDetail().replaceAll("\\n", "<br>") + "</html>");
				onField.setFont(new Font("times new roman", Font.PLAIN, 10));
			} else {
				onField = new JButton();
				onField.setIcon(
						new ImageIcon("res/minions/" + m.getName().toLowerCase().replaceAll(" ", "_") + ".jfif"));
			}
			onField.addActionListener(this);
			onField.setActionCommand((i++) + "");
			currentHeroField.add(onField);
			field.add(m);
			view.getCurrentHeroGamePanel().getFieldPanel().add(onField);
		}

		i = 0;
		for (Minion m : game.getOpponent().getField()) {
			JButton onField;
			if (!drawImages) {
				onField = new JButton("<html>" + m.getFieldDetail().replaceAll("\\n", "<br>") + "</html>");
				onField.setFont(new Font("times new roman", Font.PLAIN, 10));
			} else {
				onField = new JButton();
				onField.setIcon(
						new ImageIcon("res/minions/" + m.getName().toLowerCase().replaceAll(" ", "_") + ".jfif"));
			}
			onField.addActionListener(this);
			onField.setActionCommand((i++) + "");
			opponentHeroField.add(onField);
			oppField.add(m);
			view.getOpponentHeroGamePanel().getFieldPanel().add(onField);
		}

		view.repaint();
		view.revalidate();
	}

	public void onGameOver() {
		updateView();
		if (game.getCurrentHero().getCurrentHP() == 0) {
			view.displayWinner(game.getOpponent().getName());
		} else {
			view.displayWinner(game.getCurrentHero().getName());
		}
		System.exit(0);
	}

	public void playAIMove(ArrayList<Integer[]> actions) throws NotYourTurnException, NotEnoughManaException,
			FullFieldException, FullHandException, CloneNotSupportedException, CannotAttackException,
			TauntBypassException, NotSummonedException, InvalidTargetException {
		for (Integer[] action : actions) {
			if (action[0] == 0) { // play minion
				System.out.println("playing minion...");
				game.getCurrentHero().playMinion((Minion) (game.getCurrentHero().getHand().get(action[1])));
			} else if (action[0] == 1) { // attack with minion
				System.out.println("attacking with minion...");
				if (action[2] == -2) {
					game.getCurrentHero().attackWithMinion((Minion) (game.getCurrentHero().getField().get(action[1])),
							game.getOpponent());
				} else {
					game.getCurrentHero().attackWithMinion((Minion) (game.getCurrentHero().getField().get(action[1])),
							game.getOpponent().getField().get(action[2]));
				}
			} else if (action[0] == 2) { // cast spell
				System.out.println("casting spell...");
				Spell s = (Spell) (game.getCurrentHero().getHand().get(action[1]));
				if (action[2] == -1) { // Field Spell
					game.getCurrentHero().castSpell((FieldSpell) s);
				} else if (action[2] == -2) { // Hero Target Spell
					game.getCurrentHero().castSpell((HeroTargetSpell) s, game.getOpponent());
				} else if (action[2] == -3) { // AOE spell
					game.getCurrentHero().castSpell((AOESpell) s, game.getOpponent().getField());
				} else { // leeching or Minion Target Spell
					if (s instanceof LeechingSpell) {
						game.getCurrentHero().castSpell((LeechingSpell) s,
								game.getOpponent().getField().get(action[2]));
					} else if (s instanceof MinionTargetSpell) {
						if (!(s instanceof KillCommand || s instanceof Polymorph || s instanceof Pyroblast
								|| s instanceof ShadowWordDeath)) {
							game.getCurrentHero().castSpell((MinionTargetSpell) s,
									game.getCurrentHero().getField().get(action[2]));
						} else if (!(s instanceof DivineSpirit || s instanceof SealOfChampions)) {
							game.getCurrentHero().castSpell((MinionTargetSpell) s,
									game.getOpponent().getField().get(action[2]));
						}
					}
				}
			} else if(action[0]==3) {
				System.out.println("using hero power...");
				if(game.getCurrentHero() instanceof Hunter) {
					Hunter h = (Hunter) game.getCurrentHero();
					try {
						h.useHeroPower();
					} catch (HeroPowerAlreadyUsedException e) {
						
					}
				}else if(game.getCurrentHero() instanceof Mage) {
					Mage h = (Mage) game.getCurrentHero();
					if(action[1]==-2) {
						try {
							h.useHeroPower(game.getOpponent());
						} catch (HeroPowerAlreadyUsedException e) {
						}
					}else {
						try {
							h.useHeroPower(game.getOpponent().getField().get(action[1]));
						}catch(Exception e) {
							
						}
					}
				} else if(game.getCurrentHero() instanceof Paladin) {
					Paladin h = (Paladin) game.getCurrentHero();
					try {
						h.useHeroPower();
					} catch (HeroPowerAlreadyUsedException e) {
						
					}
				} else if(game.getCurrentHero() instanceof Priest) {
					Priest h = (Priest) game.getCurrentHero();
					if(action[1]==-2) {
						try {
							h.useHeroPower(game.getCurrentHero());
						} catch (HeroPowerAlreadyUsedException e) {
						}
					}else {
						try {
							h.useHeroPower(game.getCurrentHero().getField().get(action[1]));
						}catch(Exception e) {
							
						}
					}
				}else {
					Warlock h = (Warlock) game.getCurrentHero();
					try {
						h.useHeroPower();
					} catch (HeroPowerAlreadyUsedException e) {
						
					}
				}
			}
		}
		game.getCurrentHero().endTurn();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (!started) {
			if (b.getActionCommand().equals("Start Game")) {
				if (idx1 != -1 && idx2 != -1) {
					onGameStart(idx1, idx2);
					started = true;
				}
			} else if (Integer.parseInt(b.getActionCommand()) == firstHeroButtons.indexOf(b)) {
				idx1 = Integer.parseInt(b.getActionCommand());
			} else if (Integer.parseInt(b.getActionCommand()) == secondHeroButtons.indexOf(b)) {
				idx2 = Integer.parseInt(b.getActionCommand());
			}
		} else {
			if (b == endTurn) {
				// reset variables
				chosenMinion = null;
				spellToBeCasted = null;
				targetMinion = null;
				magePower = false;

				try {
					game.endTurn();
				} catch (CloneNotSupportedException e1) {
					// probably won't be thrown
					view.displayError(e1.getMessage());
				} catch (FullHandException e1) {
					view.displayError(e1.getMessage() + "\nBurned Card:\n" + e1.getBurned().toString());
				}

				if (game.getCurrentHero() == h2) { // AI turn?
					if (AI) {
						try {
							System.out.println("AI turn...computing best move...");
							MiniMax miniMax = new MiniMax(game.clone());
							ArrayList<Integer[]> actions = miniMax.getBestActions();
//							if(actions!=null) {
//								for (Integer[] action : actions) {
//									System.out.println(Arrays.toString(action));
//								}
//							}
//							System.out.println("------------------");
							playAIMove(actions);
						} catch (CloneNotSupportedException exc) {
							System.out.println("clone problem");
						} 
						catch (NotYourTurnException | NotEnoughManaException | FullFieldException | FullHandException
								| CannotAttackException | TauntBypassException | NotSummonedException
								| InvalidTargetException e1) {
							System.out.println("in");
							e1.printStackTrace();
						}
					}
				}
				updateView();
			} else if (b == useHeroPower) {
				try {
					if (game.getCurrentHero() instanceof Mage) {
						// choose minion or hero to deal damage
						magePower = true;
					} else if (game.getCurrentHero() instanceof Hunter) {
						game.getCurrentHero().useHeroPower();
					} else if (game.getCurrentHero() instanceof Priest) {
						// choose minion or hero to restore health
						priestPower = true;
						healCurrentHero.setEnabled(true);
					} else if (game.getCurrentHero() instanceof Warlock) {
						game.getCurrentHero().useHeroPower();
					} else if (game.getCurrentHero() instanceof Paladin) {
						game.getCurrentHero().useHeroPower();
					}
					updateView();
				} catch (FullHandException e1) {
					view.displayError(e1.getMessage() + "\nBurned Card\n" + e1.getBurned().toString());
				} catch (Exception e1) {
					view.displayError(e1.getMessage());
				}

			} else if (b == attackHeroOpponent) {
				attackOpponent();
			} else if (b == healCurrentHero) {
				if (priestPower) {
					try {
						((Priest) game.getCurrentHero()).useHeroPower(game.getCurrentHero());
					} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException
							| FullHandException | FullFieldException | CloneNotSupportedException e1) {
						view.displayError(e1.getMessage());
					}
					healCurrentHero.setEnabled(false);
					priestPower = false;
				}
				updateView();
			} else if (Integer.parseInt(b.getActionCommand()) == currentHeroHand.indexOf(b)) {
				chooseCardFromHand(b);
			} else if (Integer.parseInt(b.getActionCommand()) == currentHeroField.indexOf(b)) {
				chooseMinionFromField(b);
			} else if (Integer.parseInt(b.getActionCommand()) == opponentHeroField.indexOf(b)) {
				chooseTarget(b);
			}
		}
	}

	public void attackOpponent() {
		if (chosenMinion != null) {
			try {
				game.getCurrentHero().attackWithMinion(chosenMinion, game.getOpponent());
				chosenMinion = null;
			} catch (CannotAttackException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (NotYourTurnException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (TauntBypassException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (NotSummonedException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (InvalidTargetException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			}
			updateView();
		} else if (spellToBeCasted != null && spellToBeCasted instanceof HeroTargetSpell) {
			try {
				game.getCurrentHero().castSpell((HeroTargetSpell) spellToBeCasted, game.getOpponent());
				spellToBeCasted = null;
			} catch (NotYourTurnException e) {
				view.displayError(e.getMessage());
				spellToBeCasted = null;
			} catch (NotEnoughManaException e) {
				view.displayError(e.getMessage());
				spellToBeCasted = null;
			}
			updateView();

		} else if (magePower) {
			try {
				((Mage) game.getCurrentHero()).useHeroPower(game.getOpponent());
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e) {
				view.displayError(e.getMessage());
			}
			updateView();
		} else { // no chosen attacker
			view.displayError("choose attacker minion or spell");
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (started) {
			for (int i = 0; i < currentHeroHand.size(); i++) {
				if (game.getCurrentHero().getHand().get(i) instanceof Spell) {
					Spell s = (Spell) (game.getCurrentHero().getHand().get(i));
					if (currentHeroHand.get(i).contains(e.getPoint())) {
						currentHeroHand.get(i).setToolTipText(s.getHoverMessage());
					}
				}
			}
			if(useHeroPower.contains(e.getPoint())) {
				useHeroPower.setToolTipText(game.getCurrentHero().getHoverMessage());
			}
		}

	}

	public void chooseCardFromHand(JButton b) {
		if (cards.get(currentHeroHand.indexOf(b)) instanceof Minion) {
			spellToBeCasted = null;
			try {
				game.getCurrentHero().playMinion((Minion) (cards.get(currentHeroHand.indexOf(b))));
			} catch (NotYourTurnException e) {
				view.displayError(e.getMessage());
			} catch (NotEnoughManaException e) {
				view.displayError(e.getMessage());
			} catch (FullFieldException e) {
				view.displayError(e.getMessage());
			}
			updateView();
		} else { // if spell
			spellToBeCasted = (Spell) (cards.get(currentHeroHand.indexOf(b)));
			chosenMinion = null; // cannot choose a spell and an attacker minion at the same time
			try {
				if (spellToBeCasted instanceof AOESpell) {
					game.getCurrentHero().castSpell((AOESpell) spellToBeCasted, game.getOpponent().getField());
					spellToBeCasted = null;
				} else if (spellToBeCasted instanceof FieldSpell) {
					game.getCurrentHero().castSpell((FieldSpell) spellToBeCasted);
					spellToBeCasted = null;
				}
			} catch (NotYourTurnException e) {
				view.displayError(e.getMessage());
				spellToBeCasted = null;
			} catch (NotEnoughManaException e) {
				view.displayError(e.getMessage());
				spellToBeCasted = null;
			}
			updateView();
		}
	}

	public void chooseMinionFromField(JButton b) {
		chosenMinion = field.get(currentHeroField.indexOf(b));
		if (spellToBeCasted != null) {
			if (spellToBeCasted instanceof MinionTargetSpell)
				try {
					game.getCurrentHero().castSpell((MinionTargetSpell) spellToBeCasted, chosenMinion);
					spellToBeCasted = null;
				} catch (NotYourTurnException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				} catch (NotEnoughManaException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				} catch (InvalidTargetException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				}
			updateView();
		} else if (priestPower) {
			try {
				((Priest) game.getCurrentHero()).useHeroPower(chosenMinion);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e) {
				view.displayError(e.getMessage());
			}
			priestPower = false;
			healCurrentHero.setEnabled(false);
			updateView();
		}
	}

	public void chooseTarget(JButton b) {
		targetMinion = oppField.get(opponentHeroField.indexOf(b));
		if (chosenMinion != null) {
			try {
				game.getCurrentHero().attackWithMinion(chosenMinion, targetMinion);
				chosenMinion = null;
			} catch (CannotAttackException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (NotYourTurnException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (TauntBypassException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (InvalidTargetException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			} catch (NotSummonedException e) {
				view.displayError(e.getMessage());
				chosenMinion = null;
			}
		} else if (spellToBeCasted != null) {
			if (spellToBeCasted instanceof LeechingSpell) {
				try {
					game.getCurrentHero().castSpell((LeechingSpell) spellToBeCasted, targetMinion);
					spellToBeCasted = null;
				} catch (NotYourTurnException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				} catch (NotEnoughManaException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				}
			} else if (spellToBeCasted instanceof MinionTargetSpell) {
				try {
					game.getCurrentHero().castSpell((MinionTargetSpell) spellToBeCasted, targetMinion);
					spellToBeCasted = null;
				} catch (NotYourTurnException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				} catch (NotEnoughManaException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				} catch (InvalidTargetException e) {
					view.displayError(e.getMessage());
					spellToBeCasted = null;
				}
			}
		} else if (magePower) {
			try {
				((Mage) game.getCurrentHero()).useHeroPower(targetMinion);
			} catch (NotEnoughManaException | HeroPowerAlreadyUsedException | NotYourTurnException | FullHandException
					| FullFieldException | CloneNotSupportedException e) {
				view.displayError(e.getMessage());
			}
			magePower = false;
		} else { // no chosen attacker or spell
			view.displayError("Choose attacker minion or spell");
		}
		updateView();
	}

	public Hero getHero(int idx) throws IOException, CloneNotSupportedException {
		switch (idx) {
		case 0:
			return new Hunter();
		case 1:
			return new Mage();
		case 2:
			return new Paladin();
		case 3:
			return new Priest();
		case 4:
			return new Warlock();
		default:
			System.out.println("No such hero");
			return null;
		}
	}

	public static void main(String[] args) {
		new GameController();
	}
}
