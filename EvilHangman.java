import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EvilHangman {

	private ArrayList<String> wordList;
	private ArrayList<String> sameLengthWordList;
	private HashSet<Character> previousGuesses;
	private TreeSet<Character> incorrectGuesses; // behaves like a hash set, but orders the entries!
	private EvilSolution evilSolution;
	private Scanner inputScanner;

	public EvilHangman() {
		this("engDictionary.txt");
	}

	public EvilHangman(String filename) {
		try {
			wordList = dictionaryToList(filename);
		} catch (IOException e) {
			System.out.printf(
					"Couldn't read from the file %s. Verify that you have it in the right place and try running again.",
					filename);
			e.printStackTrace();
			System.exit(0); // stop the program--no point in trying if you don't have a dictionary
		}

		previousGuesses = new HashSet<>();
		incorrectGuesses = new TreeSet<>();

		int randomIndex = new Random().nextInt(wordList.size());
		String target = wordList.get(randomIndex);
		evilSolution = new EvilSolution(target);
		sameLengthWordList = new ArrayList<>();

		for (String s : wordList) { // reduce dictionary word list to only same length words
			if (s.length() == target.length()) {
				sameLengthWordList.add(s);
			}
		}
		inputScanner = new Scanner(System.in);
	}

	public void start() {

		while (!evilSolution.isSolved()) {
			char guess = promptForGuess();

			sameLengthWordList = largestWordFamily(wordFamilies(guess)); // Trim same length word list

			// Generate random word from trimmed word list
			int newRandom = new Random().nextInt(sameLengthWordList.size());
			String newTarget = sameLengthWordList.get(newRandom);

			evilSolution.setTarget(newTarget); // update solution every time
			recordGuess(guess);

		}
		printVictory();
	}

	private char promptForGuess() {
		while (true) {
			System.out.println("Guess a letter.\n");
			evilSolution.printProgress();
			System.out.println("Incorrect guesses:\n" + incorrectGuesses.toString());
			String input = inputScanner.next();
			if (input.length() != 1) {
				System.out.println("Please enter a single character.");
			} else if (previousGuesses.contains(input.charAt(0))) {
				System.out.println("You've already guessed that.");
			} else {
				return input.charAt(0);
			}
		}
	}

	private void recordGuess(char guess) {
		previousGuesses.add(guess);
		boolean isCorrect = evilSolution.addGuess(guess);
		if (!isCorrect) {
			incorrectGuesses.add(guess);
		}
	}

	private void printVictory() {
		System.out.printf("Congrats! The word was %s%n", evilSolution.getTarget());
	}

	private static ArrayList<String> dictionaryToList(String filename) throws IOException {
		FileInputStream fs = new FileInputStream(filename);
		Scanner scnr = new Scanner(fs);

		ArrayList<String> wordList = new ArrayList<>();

		while (scnr.hasNext()) {
			wordList.add(scnr.next());
		}
		return wordList;
	}

	public String wordPattern(String word, char userInput) { // Finding word pattern based on guessed character

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) != userInput) {
				word = word.replace(word.charAt(i), '-');
			}
		}
		return word;
	}

	public TreeMap<String, ArrayList<String>> wordFamilies(char userInput) {
		TreeMap<String, ArrayList<String>> wordFam = new TreeMap<String, ArrayList<String>>();

		for (String s : sameLengthWordList) {
			String wordPattern = wordPattern(s, userInput);

			ArrayList<String> wordListArray = null;
			if (wordFam.containsKey(wordPattern)) {
				wordListArray = wordFam.get(wordPattern); // if wordList already has the same pattern then set to that
															// list
			} else {
				wordListArray = new ArrayList<String>(); // if not, then generate a new list
			}

			if (!wordListArray.contains(s)) {
				wordListArray.add(s);
			}
			wordFam.put(wordPattern, wordListArray);
		}
		return wordFam;
	}

	public ArrayList<String> largestWordFamily(TreeMap<String, ArrayList<String>> wordFams) {

		Set<Map.Entry<String, ArrayList<String>>> families = wordFams.entrySet();

		int largestFamSize = 0;
		ArrayList<String> largestWordFam = new ArrayList<String>();

		for (Map.Entry<String, ArrayList<String>> entry : families) {
			int famSize = entry.getValue().size();

			if (famSize > largestFamSize) {
				largestFamSize = famSize;
				largestWordFam = entry.getValue();
			}
		}
		return largestWordFam;
	}

}
