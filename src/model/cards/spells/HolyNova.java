package model.cards.spells;

import java.util.ArrayList;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class HolyNova extends Spell implements AOESpell {

	public HolyNova() {
		super("Holy Nova", 5, Rarity.BASIC);

	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField) {
		for (int i = 0; i < oppField.size(); i++) {
			Minion m = oppField.get(i);
			if (m.isDivine())
				m.setDivine(false);
			else {
				m.setCurrentHP(m.getCurrentHP() - 2);
				if (m.getCurrentHP() == 0)
					i--;
			}
		}
		for (int i = 0; i < curField.size(); i++) {
			Minion m = curField.get(i);
			m.setCurrentHP(m.getCurrentHP() + 2);
		}

	}
	
	public String getHoverMessage() {
		return "Deals 2 damage to all enemy minions. Restores 2 health to all friendly minions."; 
	}

}
