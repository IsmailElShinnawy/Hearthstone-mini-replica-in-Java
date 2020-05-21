package model.heroes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import model.cards.Rarity;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.Flamestrike;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Polymorph;
import model.cards.spells.Pyroblast;
import model.cards.spells.Spell;

public class Mage extends Hero {

	public Mage() throws IOException, CloneNotSupportedException {
		super("Jaina Proudmoore");
	}

	@Override
	public void buildDeck() throws IOException, CloneNotSupportedException {
		ArrayList<Minion> neutrals = getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"), 13);
		getDeck().addAll(neutrals);
		for (int i = 0; i < 2; i++) {
			getDeck().add(new Polymorph());
			getDeck().add(new Flamestrike());
			getDeck().add(new Pyroblast());
		}
		Minion kalycgos = (new Minion("Kalycgos", 10, Rarity.LEGENDARY, 4, 12, false, false, false));
		getDeck().add(kalycgos);
		listenToMinions();
		Collections.shuffle(getDeck());

	}

	public void useHeroPower(Minion m) throws NotEnoughManaException, HeroPowerAlreadyUsedException,
			NotYourTurnException, FullHandException, CloneNotSupportedException, FullFieldException {
		super.useHeroPower();
		if (m.isDivine())
			m.setDivine(false);
		else
			m.setCurrentHP(m.getCurrentHP() - 1);
	}

	public void useHeroPower(Hero h) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, CloneNotSupportedException, FullFieldException {
		super.useHeroPower();
		h.setCurrentHP(h.getCurrentHP() - 1);
	}

	public void castSpell(AOESpell s, ArrayList<Minion> oppField) throws NotYourTurnException, NotEnoughManaException {
		if (fieldContains("Kalycgos")) {
			if (((Spell) s).getManaCost() - 4 > this.getCurrentManaCrystals())
				throw new NotEnoughManaException("I don't have enough Mana");
			((Spell) s).setManaCost(((Spell) s).getManaCost() - 4);
		}
		super.castSpell(s, oppField);
	}

	public void castSpell(MinionTargetSpell s, Minion m)
			throws NotYourTurnException, NotEnoughManaException, InvalidTargetException {
		if (fieldContains("Kalycgos")) {
			if (((Spell) s).getManaCost() - 4 > this.getCurrentManaCrystals())
				throw new NotEnoughManaException("I don't have enough Mana");
			((Spell) s).setManaCost(((Spell) s).getManaCost() - 4);
		}
		super.castSpell(s, m);
	}

	public void castSpell(HeroTargetSpell s, Hero h) throws NotYourTurnException, NotEnoughManaException {
		if (fieldContains("Kalycgos")) {
			if (((Spell) s).getManaCost() - 4 > this.getCurrentManaCrystals())
				throw new NotEnoughManaException("I don't have enough Mana");
			((Spell) s).setManaCost(((Spell) s).getManaCost() - 4);
		}
		super.castSpell(s, h);
	}
	
	public String getHoverMessage() {
		return "Inflict one damage point to a specific target (a hero or a minion).";
	}

}
