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
import model.cards.spells.LevelUp;
import model.cards.spells.SealOfChampions;

public class Paladin extends Hero {
	public Paladin() throws IOException, CloneNotSupportedException {
		super("Uther Lightbringer");
	}

	public void useHeroPower() throws NotEnoughManaException, HeroPowerAlreadyUsedException, NotYourTurnException,
			FullHandException, CloneNotSupportedException, FullFieldException {
		if (getField().size() < 7) {
			super.useHeroPower();
			Minion silverHand = new Minion("Silver Hand Recruit", 1, Rarity.BASIC, 1, 1, false, false, false);
			silverHand.setListener(this);
			getField().add(silverHand);
		} else
			throw new FullFieldException("There is no place for this minion");
	}

	@Override
	public void buildDeck() throws IOException, CloneNotSupportedException {
		ArrayList<Minion> neutrals = getNeutralMinions(getAllNeutralMinions("neutral_minions.csv"), 15);
		getDeck().addAll(neutrals);
		for (int i = 0; i < 2; i++) {
			getDeck().add(new SealOfChampions());
			getDeck().add(new LevelUp());
		}
		Minion tirion = new Minion("Tirion Fordring", 4, Rarity.LEGENDARY, 6, 6, true, true, false);
		getDeck().add(tirion);
		listenToMinions();
		Collections.shuffle(getDeck());
	}
	
	public String getHoverMessage() {
		return "Spawns a Silver Hand Reqruit to the field";
	}

}
