package engine;

import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import model.cards.Card;
import model.cards.minions.Minion;
import model.heroes.Hero;
import model.heroes.HeroListener;

//model
public class Game implements ActionValidator, HeroListener, Cloneable {
	private Hero firstHero;
	private Hero secondHero;
	private Hero currentHero;
	private Hero opponent;

	private GameListener listener;

	public Game(Hero p1, Hero p2) throws FullHandException, CloneNotSupportedException {
		firstHero = p1;
		secondHero = p2;
		firstHero.setListener(this);
		secondHero.setListener(this);
		firstHero.setValidator(this);
		secondHero.setValidator(this);
		int coin = (int) (Math.random() * 2);
		currentHero = coin == 0 ? firstHero : secondHero;
		opponent = currentHero == firstHero ? secondHero : firstHero;
		currentHero.setCurrentManaCrystals(1);
		currentHero.setTotalManaCrystals(1);
		for (int i = 0; i < 3; i++) {
			currentHero.drawCard();
		}
		for (int i = 0; i < 4; i++) {
			opponent.drawCard();
		}
	}

	@Override
	public void validateTurn(Hero user) throws NotYourTurnException {
		if (user == opponent)
			throw new NotYourTurnException("You can not do any action in your opponent's turn");
	}

	public void validateAttack(Minion a, Minion t)
			throws TauntBypassException, InvalidTargetException, NotSummonedException, CannotAttackException {
		if (a.getAttack() <= 0)
			throw new CannotAttackException("This minion Cannot Attack");
		if (a.isSleeping())
			throw new CannotAttackException("Give this minion a turn to get ready");
		if (a.isAttacked())
			throw new CannotAttackException("This minion has already attacked");
		if (!currentHero.getField().contains(a))
			throw new NotSummonedException("You can not attack with a minion that has not been summoned yet");
		if (currentHero.getField().contains(t))
			throw new InvalidTargetException("You can not attack a friendly minion");
		if (!opponent.getField().contains(t))
			throw new NotSummonedException("You can not attack a minion that your opponent has not summoned yet");
		if (!t.isTaunt()) {
			for (int i = 0; i < opponent.getField().size(); i++) {
				if (opponent.getField().get(i).isTaunt())
					throw new TauntBypassException("A minion with taunt is in the way");
			}

		}

	}

	public void validateAttack(Minion m, Hero t)
			throws TauntBypassException, NotSummonedException, InvalidTargetException, CannotAttackException {
		if (m.getAttack() <= 0)
			throw new CannotAttackException("This minion Cannot Attack");
		if (m.isSleeping())
			throw new CannotAttackException("Give this minion a turn to get ready");
		if (m.isAttacked())
			throw new CannotAttackException("This minion has already attacked");
		if (!currentHero.getField().contains(m))
			throw new NotSummonedException("You can not attack with a minion that has not been summoned yet");
		if (t.getField().contains(m))
			throw new InvalidTargetException("You can not attack yourself with your minions");
		for (int i = 0; i < opponent.getField().size(); i++) {
			if (opponent.getField().get(i).isTaunt())
				throw new TauntBypassException("A minion with taunt is in the way");
		}
	}

	public void validateManaCost(Card c) throws NotEnoughManaException {
		if (currentHero.getCurrentManaCrystals() < c.getManaCost())
			throw new NotEnoughManaException("I don't have enough mana !!");
	}

	public void validatePlayingMinion(Minion m) throws FullFieldException {
		if (currentHero.getField().size() == 7)
			throw new FullFieldException("No space for this minion");
	}

	public void validateUsingHeroPower(Hero h) throws NotEnoughManaException, HeroPowerAlreadyUsedException {
		if (h.getCurrentManaCrystals() < 2)
			throw new NotEnoughManaException("I don't have enough mana !!");
		if (h.isHeroPowerUsed())
			throw new HeroPowerAlreadyUsedException(" I already used my hero power");
	}

	@Override
	public void onHeroDeath() {
		if(listener!=null) {
			listener.onGameOver();
		}
	}

	@Override
	public void damageOpponent(int amount) {

		opponent.setCurrentHP(opponent.getCurrentHP() - amount);
	}

	public Hero getCurrentHero() {
		return currentHero;
	}

	public void setListener(GameListener listener) {
		this.listener = listener;
	}

	@Override
	public void endTurn() throws FullHandException, CloneNotSupportedException {
		Hero temp = currentHero;
		currentHero = opponent;
		opponent = temp;
		currentHero.setTotalManaCrystals(currentHero.getTotalManaCrystals() + 1);
		currentHero.setCurrentManaCrystals(currentHero.getTotalManaCrystals());
		currentHero.setHeroPowerUsed(false);
		for (Minion m : currentHero.getField()) {
			m.setAttacked(false);
			m.setSleeping(false);
		}
		currentHero.drawCard();
	}

	public Hero getOpponent() {
		return opponent;
	}
	
	public Game clone() throws CloneNotSupportedException{
		Game clonedGame = (Game)super.clone();
		
		clonedGame.currentHero = (Hero)this.currentHero.clone();
		clonedGame.currentHero.setListener(clonedGame);
		clonedGame.currentHero.setValidator(clonedGame);
		
		clonedGame.opponent = (Hero)this.opponent.clone();
		clonedGame.opponent.setListener(clonedGame);
		clonedGame.opponent.setValidator(clonedGame);
		
		clonedGame.listener = null;
//		return (Game)(super.clone());
		return clonedGame;
	}

}
