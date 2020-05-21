package model.heroes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.NotEnoughManaException;
import exceptions.NotYourTurnException;
import model.cards.Rarity;
import model.cards.minions.Minion;
import model.cards.spells.DivineSpirit;
import model.cards.spells.HolyNova;
import model.cards.spells.ShadowWordDeath;

public class Priest extends Hero {

	public Priest() throws IOException, CloneNotSupportedException {
		super("Anduin Wrynn");
	}

	@Override
	public void buildDeck() throws IOException, CloneNotSupportedException {
		ArrayList<Minion> neutrals = getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"), 13);
		getDeck().addAll(neutrals);
		for (int i = 0; i < 2; i++) {
			getDeck().add(new DivineSpirit());
			getDeck().add(new HolyNova());
			getDeck().add(new ShadowWordDeath());
		}
		Minion velen = new Minion("Prophet Velen", 7, Rarity.LEGENDARY, 7, 7, false, false, false);
		getDeck().add(velen);
		listenToMinions();
		Collections.shuffle(getDeck());

	}

	public void useHeroPower(Minion m) throws NotEnoughManaException, HeroPowerAlreadyUsedException,
			NotYourTurnException, FullHandException, CloneNotSupportedException, FullFieldException {
		super.useHeroPower();
		if (fieldContains("Prophet Velen"))
			m.setCurrentHP(m.getCurrentHP() + 8);
		else
			m.setCurrentHP(m.getCurrentHP() + 2);
	}

	public void useHeroPower(Hero h) throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, CloneNotSupportedException, FullFieldException {
		super.useHeroPower();
		if (fieldContains("Prophet Velen"))
			h.setCurrentHP(h.getCurrentHP() + 8);
		else
			h.setCurrentHP(h.getCurrentHP() + 2);
	}

	public String getHoverMessage() {
		return "Restore two health points to a specific target (a hero or a minion).";
	}
	
}
