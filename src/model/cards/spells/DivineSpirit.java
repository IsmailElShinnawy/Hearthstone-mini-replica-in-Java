package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class DivineSpirit extends Spell implements MinionTargetSpell {

	public DivineSpirit() {
		super("Divine Spirit", 3, Rarity.BASIC);

	}

	@Override
	public void performAction(Minion m) {
		m.setMaxHP(m.getMaxHP() * 2);
		m.setCurrentHP(m.getCurrentHP() * 2);
	}
	
	public String getHoverMessage() {
		return "Doubles the current and max HP of a minion.";
	}

}
