import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class EvilHangmanTest {

	private String word = "volunteer";
	EvilSolution testCase = new EvilSolution(word);

	@Test
	void testWordPattern() {
		EvilHangman test = new EvilHangman();
		assertEquals("a-a--", test.wordPattern("abaft", 'a'));
	}

	@Test
	void testWordPattern1() {
		EvilHangman test1 = new EvilHangman();
		assertEquals("t-t---t-", test1.wordPattern("totality", 't'));

	}

	@Test
	void testWordPattern2() {
		EvilHangman test2 = new EvilHangman();
		assertEquals("--dd-d", test2.wordPattern("padded", 'd'));
	}

	@Test
	void testWordFamily() {
		EvilHangman testWF = new EvilHangman();

		String key1 = testWF.wordFamilies('a').firstKey();
		int charCount = 0;
		int dashCount = 0;

		for (int i = 0; i < key1.length(); i++) {
			if (key1.charAt(i) == 'a') {
				charCount++;
			} else if (key1.charAt(i) == '-') {
				dashCount++;
			}
		}

		int wordPatternLength = charCount + dashCount;

		assertEquals(key1.length(), wordPatternLength);

	}

	@Test
	void testlargestWordFamily() {
		EvilHangman test3 = new EvilHangman();

		ArrayList<String> fam1 = new ArrayList<>();
		ArrayList<String> fam2 = new ArrayList<>();
		ArrayList<String> fam3 = new ArrayList<>();
		TreeMap<String, ArrayList<String>> wordFam1 = new TreeMap<String, ArrayList<String>>();

		fam1.add("adapt");
		fam1.add("abase");
		fam1.add("aback");
		fam2.add("wean");
		fam2.add("weal");
		fam2.add("wear");
		fam2.add("weak");
		fam2.add("waxy");
		fam3.add("accent");
		fam3.add("accept");

		wordFam1.put("a-a---", fam1);
		wordFam1.put("w---", fam2);
		wordFam1.put("acc---", fam3);

		assertEquals(fam2, test3.largestWordFamily(wordFam1));
	}

	@Test
	void testlargestWordFamily2() {
		EvilHangman test4 = new EvilHangman();

		ArrayList<String> fam4 = new ArrayList<>();
		ArrayList<String> fam5 = new ArrayList<>();
		ArrayList<String> fam6 = new ArrayList<>();
		TreeMap<String, ArrayList<String>> wordFam2 = new TreeMap<String, ArrayList<String>>();

		fam4.add("passive");
		fam4.add("passion");
		fam4.add("passover");
		fam4.add("passmark");
		fam5.add("adapt");
		fam5.add("abase");
		fam5.add("aback");
		fam6.add("paceman");
		fam6.add("pacemen");

		wordFam2.put("--ss---", fam4);
		wordFam2.put("a-a--", fam5);
		wordFam2.put("----m-n", fam6);

		assertEquals(fam4, test4.largestWordFamily(wordFam2));
	}

	@Test
	void testAddGuessAndIsSolved() {

		assertTrue(testCase.addGuess('v'));
		assertTrue(testCase.addGuess('e'));
		assertTrue(testCase.addGuess('t'));
		assertFalse(testCase.addGuess('a'));
		assertFalse(testCase.addGuess('i'));

		assertFalse(testCase.isSolved());
	}

	@Test
	void getTarget() {
		assertEquals("volunteer", testCase.getTarget());
	}

	@Test
	void setTarget() {

		testCase.setTarget("mimick");

		assertEquals("mimick", testCase.getTarget());
	}

}
