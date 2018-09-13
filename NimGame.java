/**
 * @author Jinnan Li, 975506
 */

public class NimGame {
	private int upperBoundNo;
	private int currentStone;
	private int initialstones;
	private NimPlayer player1;
	private NimPlayer player2;

	NimGame() {
	}

	// initialize a new game
	NimGame(int initialstones, int upperBoundNo, int player1Index, int player2Index, NimPlayer[] players) {
		this.upperBoundNo = upperBoundNo;
		this.currentStone = initialstones;
		this.initialstones = initialstones;
		player1 = players[player1Index];
		player2 = players[player2Index];
	}

	// initialize a new game
	NimGame(int initialstones, int player1Index, int player2Index, NimPlayer[] players) {
		this.currentStone = initialstones;
		this.initialstones = initialstones;
		player1 = players[player1Index];
		player2 = players[player2Index];
	}

	// begin a game for two players
	public void playGame() {
		System.out.println("\nInitial stone count: " + currentStone);
		System.out.println("Maximum stone removal: " + upperBoundNo);
		System.out.println("Player 1: " + player1.getFullName());
		System.out.println("Player 2: " + player2.getFullName());

		NimPlayer currentPlayer = player1;

		for (int removeNo = 0; currentStone > 0;) {// control whether one game is over/
			printStone();
			try {
				removeNo = currentPlayer.removeStone(upperBoundNo, currentStone);
				if (removeNo != 0) {
					currentStone = currentStone - removeNo;
					if (currentPlayer == player1)// change turns between player1 and player2/
						currentPlayer = player2;
					else
						currentPlayer = player1;
				}
			} catch (Exception e) {
				String message = e.getMessage();
				System.out.println(message);
			}
		}
		System.out.println("\nGame Over");
		System.out.println(currentPlayer.getFullName() + " wins!");
		currentPlayer.setWonNumber();
		player1.setPlayedNumber();
		player2.setPlayedNumber();
	}

	// begin an advanced game for two players
	public void playAdvanceGame() {
		boolean[] available = new boolean[initialstones];
		for (int i = 0; i < initialstones; i++)
			available[i] = true;

		String lastMove = "";
		String move = "";
		int removeIndex = 0;
		int removeNo = 0;
		String[] remove;

		System.out.println("\nInitial stone count: " + currentStone);
		System.out.print("Stone display:");
		printOption(available);
		System.out.println("Player 1: " + player1.getFullName());
		System.out.println("Player 2: " + player2.getFullName());

		NimPlayer currentPlayer = player1;

		while (currentStone > 0) {// control whether one game is over/
			System.out.print("\n" + currentStone + " stones left:");
			printOption(available);

			move = currentPlayer.advancedMove(available, lastMove);
			remove = move.split(" ");
			try {
				if (remove.length == 2) {
					try {
						removeIndex = Integer.parseInt(remove[0]);
						removeNo = Integer.parseInt(remove[1]);

						if (removeNo >= 1 && removeNo <= 2 && removeIndex <= initialstones && removeIndex >= 1
								&& checkAvailable(available, removeIndex, removeNo)) {
							for (int i = removeIndex - 1; i < removeIndex + removeNo - 1; i++)
								available[i] = false;
							currentStone = currentStone - removeNo;
							if (currentPlayer == player1)// change turns between player1 and player2/
								currentPlayer = player2;
							else
								currentPlayer = player1;
							lastMove = move;
						} else
							throw new Exception("\nInvalid move.");
					} catch (NumberFormatException e) {
						System.out.println("\nInvalid move.");
					}
				} else
					throw new Exception("\nInvalid move.");
			} catch (Exception e) {
				String message = e.getMessage();
				System.out.println(message);
			}
		}
		if (currentPlayer == player1)// change turns between player1 and player2/
			currentPlayer = player2;
		else
			currentPlayer = player1;
		System.out.println("\nGame Over");
		System.out.println(currentPlayer.getFullName() + " wins!");
		currentPlayer.setWonNumber();
		player1.setPlayedNumber();
		player2.setPlayedNumber();
	}

	// print the star string according to number of stones/
	private void printStone() {
		String stoneNo = "";
		for (int i = 0; i < currentStone; i++)
			stoneNo = stoneNo + " *";
		System.out.println("\n" + currentStone + " stones left:" + stoneNo);
	}

	// print the star string according to number of stones/
	private void printOption(boolean[] available) {
		String optionString = "";
		for (int i = 0; i < available.length; i++)
			if (available[i] == true)
				optionString = optionString + " <" + (i + 1) + ",*>";
			else
				optionString = optionString + " <" + (i + 1) + ",x>";
		System.out.println(optionString);
	}

	private boolean checkAvailable(boolean[] available, int removeIndex, int removeNo) {
		for (int i = removeIndex - 1; i < removeIndex + removeNo - 1;)
			if (available[i] == true)
				i++;
			else
				return false;
		return true;
	}
}
