# Sheriff of Nottingham

In Sheriff of Nottingham, you are a merchant trying to deliver your Goods to market. Players take turns assuming the role of Sheriff, who must decide which merchants’ bags to inspect and which to let by. As a merchant, your goal is to convince the Sheriff to let you in—by any means necessary. At the end of the game, the merchant with the most wealth wins. (more about the rules [here](https://gusandcodotnet.files.wordpress.com/2014/09/son_rulebook_ch09_singlepages.pdf))

The implementation isn't as complex as the original game, becuase the assignment was made that way.

## The implementation includes 4 types of player strategies: Base, Bribe, Greedy. 
	* Base: 
		- as a Merchant: he's the honest player. He never lies and never tries to bring in illegal goods, unless he's obliged to do so, by the cards in his hand. If he has to bring in illegale goods he brings one and only one with him and declares it as an apple.
		- as the Sheriff: he searches every other player, even though he risks losing money and he never accepts the bribe.
	
	* Bribe: 
		- as a Merchant: he's the the type of player that tries to trick everyone else. He bribes the sheriff as long as he has the money to do so and he'll always try to bring in as many illegal goods as he can. If he runs out of money he'll play like the Base player.
		- as a Sheriff: he'll always check the players right next to him (on his left and on his right side).
	
	* Greedy: 
		- as a Merchant: he's a base player, with the exception that in even rounds after adding the goods in his merchant's bag he tries adding an illegal card (if he still has space for it).
		- as a Sheriff: he inspects all the players that don't bribe him.
		
* There's one more strategy that isn't described above, because it's my attempt at beating the other 3. 
* The main idea behind it is that the wizard, as the sheriff inspects all the players that declare more than 3 assets in their bag or that declare all their assets as "apples'. As a merchant he tries to play legal assets, and puts in his bag the assets that he has the most. If he has only illegal assets he puts all the goods he can in his bag, declares them as apples and gives a minimum bribe, in an attempt to pass unverified.
			
