package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        //set my arguments in their labeled places
        String dictionary = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        File file = new File(dictionary);
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        try {
            evilHangmanGame.startGame(file, wordLength);
        }
        catch (EmptyDictionaryException e)
        {
            System.out.print("You have provided an empty dictionary or a wordLength too large for any word in the dicionary");
        }
        catch (IOException e)
        {
            System.out.print(e);
        }

        Scanner reader = new Scanner(System.in);
        boolean winning = false;
        while (guesses != 0)
        {
            String tempSubSetKey = evilHangmanGame.subSetKey;
            System.out.print("You have " + guesses + " guesses left\n");
            System.out.print("Used letters: ");
            for (char c : evilHangmanGame.guessedLetters)
            {
                System.out.print(c + " ");
            }
            System.out.print("\n");
            System.out.print("Word: " + evilHangmanGame.subSetKey + "\n");
            boolean loop = true;
            String string = "";
            while (loop) {
                System.out.print("Enter guess: ");
                string = reader.next();
                if (string.length() > 1 || !Character.isAlphabetic(string.charAt(0))) {
                    System.out.print("Invalid input!");
                    continue;
                }
                char guess = string.charAt(0);
                try
                {
                    evilHangmanGame.makeGuess(guess);
                }
                catch (GuessAlreadyMadeException e)
                {
                    System.out.print("Guess already made! ");
                    continue;
                }
                loop = false;
            }
            if (tempSubSetKey.equals(evilHangmanGame.subSetKey)) {
                System.out.print("Sorry, there are no " + string + "\n\n");
                guesses = guesses - 1;
            }
            else
            {
                int sum = 0;
                for (int i = 0; i< tempSubSetKey.length(); ++i)
                {
                    if (tempSubSetKey.charAt(i) != evilHangmanGame.subSetKey.charAt(i))
                    {
                        sum = sum + 1;
                    }
                }
                System.out.print("Yes, there is " + sum + " " +string + "\n\n");
            }

            //if they win then cut out early
            winning = true;
            for (int i = 0; i < evilHangmanGame.subSetKey.length(); ++i)
            {
                if (evilHangmanGame.subSetKey.charAt(i) == '-')
                {
                    winning = false;
                }
            }
            if (winning)
            {
                System.out.print("You win! You guessed the word: " + evilHangmanGame.subSetKey + "\n");
                break;
            }
        }
        if (!winning)
        {
            String finalWord = "";
            for (String string : evilHangmanGame.words) {
                finalWord = string;
                break;
            }
            System.out.print("Sorry, you lost! The word was: " + finalWord);
        }
    }
}
