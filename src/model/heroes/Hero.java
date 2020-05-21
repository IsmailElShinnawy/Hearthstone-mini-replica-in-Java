package model.heroes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import engine.ActionValidator;
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
import model.cards.Rarity;
import model.cards.minions.Icehowl;
import model.cards.minions.Minion;
import model.cards.minions.MinionListener;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;

public abstract class Hero implements MinionListener, Cloneable {
	private String name;
	private int currentHP;
	private boolean heroPowerUsed;
	private int totalManaCrystals;
	private int currentManaCrystals;
	private ArrayList<Card> deck;
	private ArrayList<Minion> field;
	private ArrayList<Card> hand;
	private int fatigueDamage;
	private HeroListener listener;
	private ActionValidator validator;

	public Hero(String name) throws IOException, CloneNotSupportedException {
		this.name = name;
		currentHP = 30;
		deck = new ArrayList<Card>();
		field = new ArrayList<Minion>(7);
		hand = new ArrayList<Card>(10);
		buildDeck();
	}

	public abstract void buildDeck() throws IOException, CloneNotSupportedException;

	public static final ArrayList<Minion> getAllNeutralMinions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		ArrayList<Minion> minions = new ArrayList<Minion>();
		String current = br.readLine();
		while (current != null) {
			String[] line = current.split(",");
			Minion minion = null;
			String n = line[0];
			int m = Integer.parseInt(line[1]);
			Rarity r = null;
			switch (
				(line[2])
			) {
			case "b":
				r = Rarity.BASIC;
				break;
			case "c":
				r = Rarity.COMMON;
				break;
			case "r":
				r = Rarity.RARE;
				break;
			case "e":
				r = Rarity.EPIC;
				break;
			case "l":
				r = Rarity.LEGENDARY;
				break;
			}
			int a = Integer.parseInt(line[3]);
			int p = Integer.parseInt(line[4]);
			boolean t = line[5].equals("TRUE") ? true : false;
			boolean d = line[6].equals("TRUE") ? true : false;
			boolean c = line[7].equals("TRUE") ? true : false;
			if (!n.equals("Icehowl"))
				minion = new Minion(n, m, r, a, p, t, d, c);
			else
				minion = new Icehowl();
			minions.add(minion);
			current = br.readLine();
		}
		br.close();
		return minions;
	}

	public static final ArrayList<Minion> getNeutralMinions(ArrayList<Minion> minions, int count)
			throws CloneNotSupportedException {
		ArrayList<Minion> res = new ArrayList<Minion>();
		int i = 0;
		while (i < count) {

			int index = (int) (Math.random() * minions.size());
			Minion minion = minions.get(index);
			int occ = 0;
			for (int j = 0; j < res.size(); j++) {
				if (res.get(j).getName().equals(minion.getName()))
					occ++;
			}
			if (occ == 0) {
				res.add(minion.clone());
				i++;
			} else if (occ == 1 && minion.getRarity() != Rarity.LEGENDARY) {

				res.add(minion.clone());
				i++;
			}
		}
		return res;
	}

	public void listenToMinions() {
		for (Card c : deck) {
			if (c instanceof Minion)
				((Minion) c).setListener(this);
		}
	}

	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, CloneNotSupportedException, FullFieldException {
		validator.validateTurn(this);
		validator.validateUsingHeroPower(this);
		currentManaCrystals -= 2;
		heroPowerUsed = true;
	}

	public void playMinion(Minion m) throws NotYourTurnException, NotEnoughManaException, FullFieldException {
		validator.validateTurn(this);
		validator.validateManaCost(m);
		validator.validatePlayingMinion(m);
		currentManaCrystals -= m.getManaCost();
		hand.remove(m);
		field.add(m);

	}

	public void attackWithMinion(Minion attacker, Minion target) throws CannotAttackException, NotYourTurnException,
			TauntBypassException, InvalidTargetException, NotSummonedException {
		validator.validateTurn(this);
		validator.validateAttack(attacker, target);
		attacker.attack(target);
	}

	public void attackWithMinion(Minion attacker, Hero target) throws CannotAttackException, NotYourTurnException,
			TauntBypassException, NotSummonedException, InvalidTargetException {
		validator.validateTurn(this);
		validator.validateAttack(attacker, target);
		attacker.attack(target);
	}

	public void castSpell(FieldSpell s) throws NotYourTurnException, NotEnoughManaException {
		validator.validateTurn(this);
		validator.validateManaCost((Spell) s);

		s.performAction(field);
		castSpellCommons((Spell) s);

	}

	public void castSpell(MinionTargetSpell s, Minion m)
			throws NotYourTurnException, NotEnoughManaException, InvalidTargetException {
		validator.validateTurn(this);
		validator.validateManaCost((Spell) s);
		s.performAction(m);
		castSpellCommons((Spell) s);

	}

	public void castSpell(LeechingSpell s, Minion m) throws NotYourTurnException, NotEnoughManaException {
		validator.validateTurn(this);
		validator.validateManaCost((Spell) s);
		int v = s.performAction(m);
		setCurrentHP(currentHP + v);
		castSpellCommons((Spell) s);
	}

	public void castSpell(HeroTargetSpell s, Hero h) throws NotYourTurnException, NotEnoughManaException {
		validator.validateTurn(this);
		validator.validateManaCost((Spell) s);
		s.performAction(h);
		castSpellCommons((Spell) s);

	}

	public void castSpell(AOESpell s, ArrayList<Minion> oppField) throws NotYourTurnException, NotEnoughManaException {
		validator.validateTurn(this);
		validator.validateManaCost((Spell) s);
		s.performAction(oppField, field);
		castSpellCommons((Spell) s);

	}

	private void castSpellCommons(Spell s) {
		currentManaCrystals -= s.getManaCost();
		hand.remove(s);
	}

	public void endTurn() throws FullHandException, CloneNotSupportedException {
		listener.endTurn();
	}

	public Card drawCard() throws FullHandException, CloneNotSupportedException {
		if (fatigueDamage > 0) {
			setCurrentHP(currentHP - fatigueDamage);
			fatigueDamage++;
			return null;
		}

		Card c = deck.remove(0);

		if (deck.size() == 0)
			fatigueDamage = 1;
		if (hand.size() == 10)
			throw new FullHandException("My hand is too full !!!", c);
		hand.add(c);
		if (fieldContains("Chromaggus") && hand.size() < 10)
			hand.add(c.clone());
		return c;

	}

	public int getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(int hp) {
		this.currentHP = hp;
		if (this.currentHP > 30)
			this.currentHP = 30;
		else if (this.currentHP <= 0) {
			this.currentHP = 0;
			listener.onHeroDeath();
		}
	}

	public int getTotalManaCrystals() {
		return totalManaCrystals;
	}

	public void setTotalManaCrystals(int totalManaCrystals) {
		this.totalManaCrystals = totalManaCrystals;
		if (this.totalManaCrystals > 10)
			this.totalManaCrystals = 10;
	}

	public int getCurrentManaCrystals() {
		return currentManaCrystals;
	}

	public void setCurrentManaCrystals(int currentManaCrystals) {
		this.currentManaCrystals = currentManaCrystals;
		if (this.currentManaCrystals > 10)
			this.currentManaCrystals = 10;
	}

	public void onMinionDeath(Minion m) {
		field.remove(m);
	}

	public HeroListener getListener() {
		return listener;
	}

	public ArrayList<Minion> getField() {
		return field;
	}

	public void setListener(HeroListener listener) {
		this.listener = listener;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public boolean isHeroPowerUsed() {
		return heroPowerUsed;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public void setHeroPowerUsed(boolean powerUsed) {
		this.heroPowerUsed = powerUsed;
	}

	public boolean fieldContains(String n) {
		for (Minion m : field) {
			if (m.getName().equals(n))
				return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name+", Remaining Cards in Deck: "+deck.size()+"\nHealth: "+currentHP+"/30"+"\nMana Crystals: "+currentManaCrystals+"/"+totalManaCrystals;
	}
	
	public abstract String getHoverMessage(); 

	public void setValidator(ActionValidator validator) {
		this.validator = validator;
	}
	
	public Hero clone() throws CloneNotSupportedException{
		Hero clonedHero = (Hero)super.clone();
		clonedHero.deck = new ArrayList<Card>();
		for(Card c: this.deck) {
			clonedHero.deck.add(c.clone());
		}
		clonedHero.hand = new ArrayList<Card>();
		for(Card c: this.hand) {
			clonedHero.hand.add(c.clone());
		}
		clonedHero.field = new ArrayList<Minion>();
		for(Minion m: this.field) {
			clonedHero.field.add(m.clone());
		}
		return clonedHero;
	}

}
