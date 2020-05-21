package model.cards.spells;

import java.util.ArrayList;

import model.cards.minions.Minion;

public interface FieldSpell {
	public void performAction(ArrayList<Minion> field);
}
