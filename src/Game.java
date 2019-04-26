import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class Game {
	String wordList;
	String word;
	char guess;
	int mode;
	int numberOfGuesses = 0;
	int correctGuesses = 0;
	int [] correctGuessArray = new int[26];
	int timesToRun;
	String [] previousGuessArray = new String[26];
	boolean guessedWord = false;
	ArrayList<Character> guessed = new ArrayList<>();
	ArrayList<Character> notGuessed = new ArrayList<>();
	String currentGuess;
	AI gameAI = new AI(31, 32, 32, 1);
	double[] newInputs = new double[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};
	double[] newOutput = new double[] {0};
	

	public static void main(String[] args)
	{
		
	}
	
	public void setUpGame () {
		newOutput = new double[] {0};
		newInputs = new double[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		guessedWord = false;
		correctGuesses = 0;
		correctGuessArray = new int[] {26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26,26};
		currentGuess = "";
		numberOfGuesses= 0;
		notGuessed = new ArrayList<>();
		for  (int i = 25; i > -1; i--) {
			notGuessed.add((char) ('a' + i));
		}
		guessed = new ArrayList<>();
		numberOfGuesses = 0;
		word = this.chooseWord(wordList);
		if (mode == 1 || mode == 4) {
			System.out.println(wordList + " has been used to find the word '" 
					+ word + "'. This will be used in the game.");
			System.out.print("guessing ");
		}
		for (int i = word.length() ; i > 0 ; i --) {
			currentGuess = currentGuess + "*";
		}
	}
	
	public Game(String nWordList, int nMode, int nTimesToRun) {		
		wordList = nWordList;
		mode = nMode;
		timesToRun = nTimesToRun;
		while (timesToRun > 0) {
			this.setUpGame();
			timesToRun --;
			while (!guessedWord) {
				this.guessing();				
			}
		}
	}

	// Has a go at guessing the word, the method used to generate the letter chose will depend on the mode of the game.
	private void guessing() {
		numberOfGuesses ++;
		if (mode == 3 || mode == 4) {
			makeGameStateTwo();
			guess = gameAI.getGuess(newInputs, newOutput);
		}
		else {
			guess = notGuessed.get((getRand(notGuessed.size())));			
		}
		if (mode == 1 || mode == 4 ) {
			System.out.print(numberOfGuesses + " " + guess + " / ");
		}
		guessLetter(guess);
		this.checkGameWon();
	}
	
	private void checkGameWon () {
		if (currentGuess.indexOf('*') == -1) {
			if (mode == 1 || mode == 4) {
				System.out.println("Word has been guessed correctly in " + numberOfGuesses + " guesses.");
			}
			if (mode == 2) {
				this.mode2print();
				}
			guessedWord = true;
		}			
	}
	
	
	private void mode2print () {
		StringBuilder toWrite = makeGameStateOne();
		System.out.println();		
		try {
			this.writeOutput(toWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(toWrite);
	}
	
	private StringBuilder makeGameStateOne () {
		StringBuilder toWrite = new StringBuilder();		
		for (int i = 0 ; i < guessed.size() ; i ++) {
			toWrite.append(guessed.get(i)+0 + ",");
		}
		for (int i = 0 ; i < notGuessed.size() ; i++) {
			toWrite.append(notGuessed.get(i)+0 + ",");
		}
		for (int i = 0 ; i < 26 ; i ++) {
			toWrite.append(correctGuessArray[i] + ",");
		}
		return toWrite;
	}

	private void makeGameStateTwo () {		
		for (int i = 0 ; i < guessed.size() ; i ++) {
			newInputs[i] = guessed.get(i)+0;
		}
		for (int i = 0 ; i < notGuessed.size() ; i++) {
			newInputs[i+guessed.size()] = notGuessed.get(i)+0;
			
		}
		for (int i = 0 ; i < currentGuess.length() ; i++) {
			newInputs[26+i] = currentGuess.charAt(i)+0;
		}
		newInputs[30] = numberOfGuesses;
		newOutput[0]=correctGuesses; 
	}
	
	
	

	private void writeOutput(StringBuilder toWrite) throws IOException {
		BufferedWriter writer = new BufferedWriter (new FileWriter("output.txt", true));
			writer.newLine();
			writer.write(toWrite.toString());
			writer.close();
		}
	private void guessLetter(char guess) {
		int indexToRemove = notGuessed.indexOf(guess);
		int lastIndex = -1;
		notGuessed.remove(indexToRemove);
		guessed.add(guess);
		if (word.indexOf(guess) >-1 ) {
			correctGuesses ++;
			if (mode == 1 || mode == 4) {
				System.out.println();
				System.out.println("The word " + word + " contains '" + guess + "' ");
			}
			this.correctLetter(guess, word.indexOf(guess));

			lastIndex = word.indexOf(guess, word.indexOf(guess)+1);
			while (lastIndex > -1) {
				this.correctLetter(guess, lastIndex);				
				lastIndex = word.indexOf(guess, lastIndex+1);
			}
			if (mode == 1 || mode == 4) {
				System.out.println("Current guess is " + currentGuess );
				
			}
					
		}		
		correctGuessArray[numberOfGuesses-1] = correctGuesses;
		previousGuessArray[numberOfGuesses-1] = currentGuess;
	}
	
	private void correctLetter(char correctChar, int charPosition ) {
		StringBuilder newGuess = new StringBuilder(currentGuess);
		newGuess.setCharAt(charPosition, correctChar);
		currentGuess = newGuess.toString();

	}

	private int getRand(int max) {
		Random rand = new Random();
		int n = rand.nextInt(max);
		return n;
		
	}

	private String chooseWord(String wordList) {
		String word = "";
		try {
			word = Files.readAllLines(Paths.get("src/"+wordList)).get(getRand((int)wordListLength(wordList)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return word;
	}
	
	private long wordListLength (String wordList) {
		long words = 0;
		Path path = Paths.get("src/"+wordList);
		try {
			words = Files.lines(path).count();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return words;
		
	}

	
	
}
