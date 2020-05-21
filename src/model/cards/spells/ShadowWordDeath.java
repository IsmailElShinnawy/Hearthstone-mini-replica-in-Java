package model.cards.spells;

import exceptions.InvalidTargetException;
import model.cards.Rarity;
import model.cards.minions.Minion;

public class ShadowWordDeath extends Spell implements MinionTargetSpell {

	public ShadowWordDeath() {
		super("Shadow Word: Death", 3, Rarity.BASIC);

	}

	@Override
	public void performAction(Minion m) throws InvalidTargetException {
		if (m.getAttack() < 5)
			throw new InvalidTargetException("Choose a minion with 5 or more attack");
		m.setCurrentHP(0);

	}
	
	public String getHoverMessage() {
		return "Destroys a minion that his attack is 5 or more even if it has a divine\n" + 
				"shield";
	}

}
