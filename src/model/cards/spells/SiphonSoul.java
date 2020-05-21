package model.cards.spells;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class SiphonSoul extends Spell implements LeechingSpell {

	public SiphonSoul() {
		super("Siphon Soul", 6, Rarity.RARE);
	}

	@Override
	public int performAction(Minion m) {
		m.setCurrentHP(0);
		return 3;
	}
	
	public String getHoverMessage() {
		return "Destroys a minion even if it has a divine shield and restores 3 health points to the\n" + 
				"hero.";
	}
	
}
