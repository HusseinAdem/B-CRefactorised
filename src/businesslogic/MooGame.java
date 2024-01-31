package businesslogic;

import dao.GameDAO;

public class MooGame implements Game {
    GameDAO connector;
    public int nGuesses = 1;
    private int currentId = 0;
    public String goalNumber = "";
    public String currentFeedback = "";

    public MooGame(GameDAO gDao) {
        this.connector = gDao;
    }
    public Status checkUser(String input) {
        Status status;
        try {
            int id;
            id = connector.getUserId(input);
            if (id != 0) {
                currentId = id;
                generateNumber();
                status = Status.VERIFIED;
            } else if (input.equals("exit")) {
                status = Status.EXIT;
            } else {
                status = Status.NO_USER_FOUND;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return status;
    }
    public Status playGame(String userInput) {
        if (currentId == 0){
           return checkUser(userInput);
        } else {
            String InputToCheck = generateFeedback(goalNumber, userInput);
            return checkOutcome(InputToCheck, currentId());
        }

    }
    public Status playAgain(boolean b) {
        if (b){
            nGuesses = 1;
            currentFeedback = "";
            generateNumber();
            return Status.CONTINUEGAME;

        } else {
            return Status.EXIT;
        }
    }
    public String printIntroOutroText(){
        if (currentId == 0){
            return "Enter your user name:\n";
        } else if (currentFeedback.equals(goalNumber + "    ")) {
            return "Correct, it took " + nGuesses + " guesses\nContinue?";
        } else if (currentFeedback.isEmpty()){
            return "New game:\nFor practice, number is: " + goalNumber + "\n";
        } else {
            return currentFeedback + "\n";
        }

    }

    public Status checkOutcome(String userFeedback, int id) {
        Status status;
        int nGuess = nGuesses;

        if (!userFeedback.equals("BBBB,")) {
            currentFeedback += "\n" + userFeedback;
            nGuess++;
            nGuesses = nGuess;
            status = Status.PLAYING_GAME;
        } else {
            status = Status.OK;
        }
        connector.saveResult(nGuess,id);
        return status;
    }
    public int currentId(){
        return currentId;
    }
    public String noUserFoundText(){
        return "User not in database, please register with admin";
    }

    public String generateNumber() {
        String goal = "";
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            String randomDigit = "" + random;
            while (goal.contains(randomDigit)) {
                random = (int) (Math.random() * 10);
                randomDigit = "" + random;
            }
            goal = goal + randomDigit;
        }
        goalNumber = goal;
        return goal;
    }
    public String generateFeedback(String goal, String userGuess) {
        userGuess += "    ";
        int cows = 0, bulls = 0;

        StringBuilder resultBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (goal.charAt(i) == userGuess.charAt(j)) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }
        currentFeedback = userGuess;
        resultBuilder.append("B".repeat(bulls)).append(",").append("C".repeat(cows));

        return resultBuilder.toString();
    }
}