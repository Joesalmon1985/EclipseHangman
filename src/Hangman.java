

/*
 * This class runs a game of hangman with a given word list, either in mode 1 with written updates on how the game / games go or in mode 2 
 * where updates on how the game goes provided as numbers that can be easily converted for use in a neural network (I think!). Finally this will 
 * repeat a given number of times. 
 */
public class Hangman {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String wordList = args[0];
		int mode = Integer.parseInt(args[1]);
		int timesToRun = Integer.parseInt(args[2]);		
		Game game = new Game(wordList, mode, timesToRun);

	}

}
