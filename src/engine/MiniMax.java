package engine;

import java.util.ArrayList;

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
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;

public class MiniMax {

	// private HashMap<String, Integer> actionMap = new HashMap<String, Integer>();
	private ArrayList<Integer[]> actions = new ArrayList<Integer[]>();
	private ArrayList<ArrayList<Integer[]>> allActions = new ArrayList<ArrayList<Integer[]>>();
	private ArrayList<Game> allGames = new ArrayList<Game>();

	private Game game;

	private static final int DEPTH = 1;

	public MiniMax(Game game) {

		this.game = game;

	}

	public void permute(Game game) {
		Game turnEndGame = null;
		try {
			turnEndGame = game.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		try {
			turnEndGame.endTurn();
		} catch (Exception e) {
			// System.out.println("in");
		}
		allActions.add((ArrayList<Integer[]>) actions.clone());
		try {
			allGames.add(turnEndGame.clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 4; i++) {
			if (i == 0) { // play minion from hand
				for (int j = 0; j < game.getCurrentHero().getHand().size(); j++) {
					try {
						if ((game.getCurrentHero().getHand().get(j)) instanceof Minion) {
							Game clone = game.clone();
							Minion m = (Minion) clone.getCurrentHero().getHand().get(j);
							clone.getCurrentHero().playMinion(m);
							// actions.add("0 " + m);
							actions.add(new Integer[] { 0, j }.clone()); // 0->play minion, j->position of played minion
							// in AI hand
							permute(clone);
							actions.remove(actions.size() - 1);
						}
					} catch (Exception exc) {
						// do nothing, invalid action
					}
				}
			} else if (i == 1) { // attack w/ minion
				for (int j = 0; j < game.getCurrentHero().getField().size(); j++) {
					for (int k = 0; k < game.getOpponent().getField().size(); k++) { // attack other minions
						try {
							Game clone = game.clone();
							Minion m = clone.getCurrentHero().getField().get(j);
							Minion mOpp = clone.getOpponent().getField().get(k);
							clone.getCurrentHero().attackWithMinion(m, mOpp);
							// actions.add("1 " + m + "\n" + mOpp);
							actions.add(new Integer[] { 1, j, k }.clone()); // 1->attack with minion, j->position of
							// attacker, k->position of target
							permute(clone);
							actions.remove(actions.size() - 1);
						} catch (Exception exc) {

						}
					}
					try { // attack opponent minion
						Game clone = game.clone();
						Minion m = clone.getCurrentHero().getField().get(j);
						clone.getCurrentHero().attackWithMinion(m, clone.getOpponent());
						// actions.add("1 " + m + "\n" + "opp hero");
						actions.add(new Integer[] { 1, j, -2 }.clone()); // 1->attack with minion, j->position of
						// attacker, (-2)->opponent hero
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception exc) {

					}
				}
			} else if (i == 2) { // cast spell
				for (int j = 0; j < game.getCurrentHero().getHand().size(); j++) {
					if (game.getCurrentHero().getHand().get(j) instanceof Spell) {
						Spell spell = (Spell) game.getCurrentHero().getHand().get(j);
						if (spell instanceof FieldSpell) {
							try {
								Game clone = game.clone();
								clone.getCurrentHero().castSpell((FieldSpell) clone.getCurrentHero().getHand().get(j));
								// actions.add("2 " + spell);
								actions.add(new Integer[] { 2, j, -1 }.clone()); // 2->cast spell, j->position of spell,
								// (-1)->no argumment
								permute(clone);
								actions.remove(actions.size() - 1);
							} catch (Exception exc) {
								//
							}
						} else if (spell instanceof MinionTargetSpell) {
							if (!(spell instanceof KillCommand || spell instanceof Polymorph
									|| spell instanceof Pyroblast || spell instanceof ShadowWordDeath)) {
								for (int k = 0; k < game.getCurrentHero().getField().size(); k++) {
									try {
										Game clone = game.clone();
										Minion m = clone.getCurrentHero().getField().get(k);
										clone.getCurrentHero().castSpell(
												(MinionTargetSpell) clone.getCurrentHero().getHand().get(j), m);
										// actions.add("2 " + spell + "\n" + m);
										actions.add(new Integer[] { 2, j, k }.clone()); // 2->cast spell, j->position of
										// spell, k->position of target
										// minion in current field
										permute(clone);
										actions.remove(actions.size() - 1);
									} catch (Exception exc) {
										//
									}
								}
							}

							if (!(spell instanceof DivineSpirit || spell instanceof SealOfChampions)) {
								for (int k = 0; k < game.getOpponent().getField().size(); k++) {
									try {
										Game clone = game.clone();
										Minion m = clone.getOpponent().getField().get(k);
										clone.getCurrentHero().castSpell(
												(MinionTargetSpell) clone.getCurrentHero().getHand().get(j), m);
										// actions.add("2 " + spell + "\n" + m);
										actions.add(new Integer[] { 2, j, k }.clone()); // 2->cast spell, j->position of
										// spell, k->position of target
										// minion in opp field
										// size of current field should be remove first
										permute(clone);
										actions.remove(actions.size() - 1);
									} catch (Exception exc) {

									}
								}
							}
						} else if (spell instanceof LeechingSpell) {
							for (int k = 0; k < game.getOpponent().getField().size(); k++) {
								try {
									Game clone = game.clone();
									Minion m = clone.getOpponent().getField().get(k);
									clone.getCurrentHero()
											.castSpell((LeechingSpell) clone.getCurrentHero().getHand().get(j), m);
									// actions.add("2 " + spell + "\n" + m);
									actions.add(new Integer[] { 2, j, k }.clone());
									permute(clone);
									actions.remove(actions.size() - 1);
								} catch (Exception exc) {
								}
							}
						} else if (spell instanceof HeroTargetSpell) {
							try {
								if (!(spell instanceof Pyroblast)) {
									Game clone = game.clone();
									clone.getCurrentHero().castSpell(
											(HeroTargetSpell) clone.getCurrentHero().getHand().get(j),
											clone.getOpponent());
									// actions.add("2 " + spell + "\nOpp Hero");
									actions.add(new Integer[] { 2, j, -2 }.clone());
									permute(clone);
									actions.remove(actions.size() - 1);
								}
							} catch (Exception exc) {
								//
							}
						} else {
							try {
								Game clone = game.clone();
								clone.getCurrentHero().castSpell((AOESpell) clone.getCurrentHero().getHand().get(j),
										clone.getOpponent().getField());
								actions.add(new Integer[] { 2, j, -3 }.clone());
								permute(clone);
								actions.remove(actions.size() - 1);
							} catch (Exception exc) {
								//
							}
						}
					}
				}
			} else if (i == 3) {
				if (game.getCurrentHero() instanceof Hunter) {
					try {
						Game clone = game.clone();
						Hunter clonedHunter = (Hunter) clone.getCurrentHero();
						clonedHunter.useHeroPower();
						actions.add(new Integer[] { 3, -1 });
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception e) {

					}
				} else if (game.getCurrentHero() instanceof Mage) {
					// Mage clonedMage = (Mage) clone.getCurrentHero();
					for (int j = 0; j < game.getOpponent().getField().size(); j++) {
						try {
							Game clone = game.clone();
							Mage clonedMage = (Mage) clone.getCurrentHero();
							clonedMage.useHeroPower(clone.getOpponent().getField().get(j));
							actions.add(new Integer[] { 3, j });
							permute(clone);
							actions.remove(actions.size() - 1);
						} catch (Exception e) {

						}
					}
					try {
						Game clone = game.clone();
						Mage clonedMage = (Mage) clone.getCurrentHero();
						clonedMage.useHeroPower(clone.getOpponent());
						actions.add(new Integer[] { 3, -2 });
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception e) {

					}
				} else if (game.getCurrentHero() instanceof Paladin) {
					try {
						Game clone = game.clone();
						Paladin clonedMage = (Paladin) clone.getCurrentHero();
						clonedMage.useHeroPower();
						actions.add(new Integer[] { 3, -1 });
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception e) {

					}
				} else if (game.getCurrentHero() instanceof Priest) {
					for (int j = 0; j < game.getCurrentHero().getField().size(); j++) {
						try {
							Game clone = game.clone();
							Priest clonedPaladin = (Priest) clone.getCurrentHero();
							clonedPaladin.useHeroPower(clone.getCurrentHero().getField().get(j));
							actions.add(new Integer[] { 3, j });
							permute(clone);
							actions.remove(actions.size() - 1);
						} catch (Exception e) {

						}
					}
					try {
						Game clone = game.clone();
						Priest clonedMage = (Priest) clone.getCurrentHero();
						clonedMage.useHeroPower(clone.getCurrentHero());
						actions.add(new Integer[] { 3, -2 });
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception e) {

					}
				} else {
					try {
						Game clone = game.clone();
						Warlock clonedWarlock = (Warlock) clone.getCurrentHero();
						clonedWarlock.useHeroPower();
						actions.add(new Integer[] { 3, -1 });
						permute(clone);
						actions.remove(actions.size() - 1);
					} catch (Exception e) {

					}
				}
			}
		}

	}

	public ArrayList<Integer[]> getBestActions() {
		// returns an arrayList representing the sequence of actions performed by the AI
		permute(game);
		ArrayList<Game> clonedAllGames = (ArrayList<Game>) this.allGames.clone();
		ArrayList<ArrayList<Integer[]>> clonedAllActions = (ArrayList<ArrayList<Integer[]>>) this.allActions.clone();
		int bestScore = Integer.MIN_VALUE;
		int bestMove = 0;
		for (int i = 0; i < clonedAllGames.size(); i++) {
			int score = minimax(clonedAllGames.get(i), 0, false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
			// System.out.println(score);
			if (score > bestScore) {
				bestScore = score;
				bestMove = i;
			}
		}
		ArrayList<Integer[]> res = null;
		if (bestMove < clonedAllActions.size()) {
			res = (ArrayList<Integer[]>) clonedAllActions.get(bestMove);
		}
		return (ArrayList<Integer[]>) res.clone();
	}

	public int minimax(Game gameState, int depth, boolean maximizing, int hero, int alpha, int beta) {
		if (gameState.getCurrentHero().getCurrentHP() == 0) {
			if (hero == 0) {
				return Integer.MAX_VALUE;
			} else {
				return Integer.MIN_VALUE;
			}
		} else if (gameState.getOpponent().getCurrentHP() == 0) {
			if (hero == 0) {
				return Integer.MIN_VALUE;
			} else {
				return Integer.MAX_VALUE;
			}
		} else if (depth == DEPTH) {
			if (hero == 0) {
				return (gameState.getOpponent().getCurrentHP() + game.getOpponent().getField().size())
						- (gameState.getCurrentHero().getCurrentHP() + game.getCurrentHero().getField().size());
			} else {
				return (gameState.getCurrentHero().getCurrentHP() + game.getCurrentHero().getField().size())
						- (gameState.getOpponent().getCurrentHP() + game.getOpponent().getField().size());
			}
		}

		if (maximizing) {
			this.allGames = new ArrayList<Game>();
			this.allActions = new ArrayList<ArrayList<Integer[]>>();
			permute(gameState);
			ArrayList<Game> clonedAllGames = (ArrayList<Game>) this.allGames.clone();
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < clonedAllGames.size(); i++) {
				int score = minimax(clonedAllGames.get(i), depth + 1, false, (hero + 1) % 2, alpha, beta);
				bestScore = Math.max(bestScore, score);
				alpha = Math.max(alpha, score);
				if (beta <= alpha) {
					break;
				}
			}
			return bestScore;
		} else {
			this.allGames = new ArrayList<Game>();
			this.allActions = new ArrayList<ArrayList<Integer[]>>();
			permute(gameState);
			ArrayList<Game> clonedAllGames = (ArrayList<Game>) this.allGames.clone();
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < clonedAllGames.size(); i++) {
				int score = minimax(clonedAllGames.get(i), depth + 1, true, (hero + 1) % 2, alpha, beta);
				bestScore = Math.min(bestScore, score);
				beta = Math.min(beta, score);
				if (beta <= alpha) {
					break;
				}
			}
			return bestScore;
		}
	}

}
