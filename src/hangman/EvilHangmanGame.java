package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    TreeSet<String> words;
    TreeSet<Character> guessedLetters = new TreeSet<Character>();
    String subSetKey;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        words = populateSet(dictionary, wordLength);

        //create initial blank subSetKey
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < wordLength; ++i)
        {
            stringBuilder.append("-");
        }
        subSetKey = stringBuilder.toString();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        char myChar = Character.toLowerCase(guess);
        if (guessedLetters.contains(myChar))
        {
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(myChar);
        TreeMap<String, TreeSet<String>> partitionMap = new TreeMap<String, TreeSet<String>>();
        for (String word : words)
        {
            String subSetKey = getSubSetKey(word, myChar);
            if (!partitionMap.containsKey(subSetKey))
            {
                partitionMap.put(subSetKey, new TreeSet<String>());
            }
            partitionMap.get(subSetKey).add(word);
        }

        //find out biggest subset in the partition map
        int size = 0;
        String winner = "";
        for (Map.Entry<String, TreeSet<String>> entry : partitionMap.entrySet())
        {
            if (entry.getValue().size() > size)
            {
                size = entry.getValue().size();
                winner = entry.getKey();
            }
        }
        StringBuilder stringBuilder = new StringBuilder(subSetKey);
        for (int i = 0; i < winner.length(); ++i)
        {
            if (winner.charAt(i) != '-' && winner.charAt(i) != subSetKey.charAt(i))
            {
                stringBuilder.setCharAt(i, winner.charAt(i));
            }
        }
        subSetKey = stringBuilder.toString();
        words = partitionMap.get(winner);
        return partitionMap.get(winner);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    private TreeSet<String> populateSet(File dictionary, int wordLength) throws  IOException, EmptyDictionaryException
    {
        TreeSet<String> words = new TreeSet<String>();
        Scanner scanner = new Scanner(dictionary);
        //populate my set
        while (scanner.hasNext())
        {
            String str = scanner.next();
            if (str.length() == wordLength) {
                words.add(str);
            }
        }
        if (words.size() == 0)
        {
            throw new EmptyDictionaryException();
        }
        return words;
    }

    private String getSubSetKey(String word, char guessedLetter)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < word.length(); ++i)
        {
            if (guessedLetter == word.charAt(i))
            {
                stringBuilder.append(word.charAt(i));
            }
            else
            {
                stringBuilder.append("-");
            }
        }
        return stringBuilder.toString();
    }
}