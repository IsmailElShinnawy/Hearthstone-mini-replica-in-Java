package model.cards.spells;

import model.cards.Card;
import model.cards.Rarity;

public abstract class Spell extends Card {

	public Spell(String n, int m, Rarity r) {
		super(n, m, r);
	}
	
	public String toString() {
		return getName()+"\n"+getRarity()+"\nMana Cost: "+getManaCost();
	}
	
	public abstract String getHoverMessage();

}
