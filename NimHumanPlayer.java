/**
 * @author Jinnan Li, 975506
 */

import java.util.Scanner;

public class NimHumanPlayer extends NimPlayer implements Testable {
	private Scanner scannerObject=null;
	
	public NimHumanPlayer() {

	}

	public NimHumanPlayer(String[] names,Scanner scannerObject) {
		super(names);
		this.scannerObject=scannerObject;
	}

	// set the number of stone the player want to remove/
	public int removeStone(int upperBoundNo, int currentStone) throws Exception {
		int removeNo = 0;
		System.out.println(super.getGivenName() + "'s turn - remove how many?");
		removeNo = scannerObject.nextInt();
		 scannerObject.nextLine();
		if (currentStone > upperBoundNo) {
			if (removeNo > upperBoundNo||removeNo<1) {
				System.out.println("\nInvalid move. You must remove between 1 and " + upperBoundNo + " stones.");
				return 0;
			}
		} else {
			if (removeNo > currentStone||removeNo<1) {
				System.out.println("\nInvalid move. You must remove between 1 and " + currentStone + " stones.");
				return 0;
			}
		}
		return removeNo;
	}

	// set the position and the number of stone the player want to remove in
	// advanced game/
	public String advancedMove(boolean[] available, String lastMove) {
		String move = "";
		System.out.println(super.getGivenName() + "'s turn - which to remove?");
		move = scannerObject.nextLine();
		return move;
	}
}
