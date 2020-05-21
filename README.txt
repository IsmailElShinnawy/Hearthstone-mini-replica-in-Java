Hearthstone card game replica made by Team 127 (Ismail El Shinnawy 46-4847, Lina Kamel 46-0523 and Nadine Khaled 46-0363)

Instructions on how to play the game:

1-How to select the two heros:

The users we'll be granted with a window where the player choose his/her hero and the AI opponent Hero and once both heros are chosen press the "Start Game" button in the middle of the screen.

2-How to play a minion:

The current hero should press on the corresponding minion button in his/her hand and it will be played automatically to the current hero field if no invalid action was taken.

3-How to cast spells:

The current hero should press on the corresponding spell button in his/her hand and if the spell doesn't require a target (AOESpell or FieldSpell) it will be casted automatically,
otherwise if the player wants to target a minion he/she should press on the button of the corresponding minion in the current hero field or the opponent field 
and if the player wants to target the opponent hero he/she should press on the "Attack Opponent" button in the middle of the screen.

4-How to attack with a minion:

The current hero should press on the corresponding attacker minion button in his/her field and then press on the corresponding target minion button in opponent's field.

5-How to end turn:

User should press "End Turn" button in the middle of the screen.

6-How to use hero power:

User should press "Use Hero Power" button in the middle of the screen, if the hero power doesn't require a "user-chosen target" it will be applied automatically, otherwise the user
should press on the corresponding minion button, "Attack opponent" button or "Heal Hero" button.

7-Screen orientation and heros' positions:

The current hero's hand and field are always displayed in the bottom half of the screen, the opponent's hand (flipped) and field are always displayed in the upper half of the screen,
the "End turn", "Use Hero Power", "Attack Opponent" and "Heal Hero" are concerning and affecting the current hero not the opponent. 

8-Hero status:

Heros' details are displayed in text areas below the hand panel displaying hero name, remaining cards in deck, mana crystals and HP

9-Card details:

Every card detail is displayed on the button representing the card such as card name, rarity, mana cost, etc...

10-Extra details:

-Game controller is the class with the main method and should be run to play the game.
-"Heal Hero" is not enabled unless the current hero is a priest and hero power is used.
-If the mouse is hovered over a spell button or the "Use Hero Power" button a brief description of the spell ability of the hero power will be displayed.
-At the end of the game a window with the winning hero will be displayed and once closed the whole game will exit.
-If an invalid action (Not enough mana, hero power already used, taunt bypass) is taken an error message will be displayed and if a Spell or a Minion where previously selected
they will be deselected and the user should re-select them again if he/she wishes.

11-Bonus details:

-The AI logs its moves to the console if no actions is mentioned then no action is taken by the AI
-MiniMax w/ alpha-beta pruning is used, the permuatation and minimax algorithms can be found in the MiniMax.java file
-The depth of the search tree can be altered using the depth variable at the top of the MiniMax.java file, (0/1) depth works fine but larger depths takes a lot of time to compute moves

enjoy :)