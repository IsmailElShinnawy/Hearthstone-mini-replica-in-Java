 package model.cards.spells;

import java.util.ArrayList;

import model.cards.Rarity;
import model.cards.minions.Minion;

public class MultiShot extends Spell implements AOESpell {

	public MultiShot() {
		super("Multi-Shot", 4, Rarity.BASIC);

	}

	@Override
	public void performAction(ArrayList<Minion> oppField, ArrayList<Minion> curField) {

		if (oppField.size() == 1)
			affectMinion(oppField.get(0));
		else if (oppField.size() > 1) {
			int r1 = 0;
			int r2 = 0;
			ArrayList<Minion> pool = new ArrayList<Minion>();
			pool.addAll(oppField);
			while (r1 == r2) {
				r1 = (int) (Math.random() * pool.size());
				r2 = (int) (Math.random() * pool.size());
			}
			affectMinion(pool.get(r1));
			affectMinion(pool.get(r2));
		}

	}

	private static void affectMinion(Minion m) {
		if (m.isDivine())
			m.setDivine(false);
		else
			m.setCurrentHP(m.getCurrentHP() - 3);
	}
	
	public String getHoverMessage() {
		return "Deals 3 damage to two random enemy minions. If the opponent has only one minion,\n" + 
				"it deals 3 damage once to this minion. If the opponent’s field is empty then nothing happens";
	}

}
