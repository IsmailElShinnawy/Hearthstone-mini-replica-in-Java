package model.cards.minions;

import exceptions.InvalidTargetException;
import model.cards.Rarity;
import model.heroes.Hero;

public class Icehowl extends Minion {
	public Icehowl() {
		super("Icehowl", 9, Rarity.LEGENDARY, 10, 10, false, false, true);
	}

	public void attack(Hero target) throws InvalidTargetException {
		if (getName().equals("Icehowl"))
			throw new InvalidTargetException("this minion can not attack heroes");
		else
			super.attack(target);
	}
}
