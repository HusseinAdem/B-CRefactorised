/*  Hussein Adem 2024-01-26
    Hussein.adem@hotmail.com

    interface for creating a game
 */

package businesslogic;

import java.sql.SQLException;

public interface Playable {
    Status checkUser(String input);
    String printIntroOutroText();
    Status playGame(String input) throws SQLException;
    Status playAgain(boolean b);
    int currentId();
    String noUserFoundText();



}
