
/**
 * @author Jinnan Li, 975506
 */

import java.util.Random;

public class NimAIPlayer extends NimPlayer implements Testable {

	public NimAIPlayer() {

	}

	public NimAIPlayer(String[] names) {
		super(names);
	}

	// set the number of stone the aiplayer want to remove/
	public int removeStone(int upperBoundNo, int currentStone) throws Exception {
		int removeNo = 0, k = 0;
		Random rand = new Random();
		System.out.println(super.getGivenName() + "'s turn - remove how many?");
		for (k = 0; k * (upperBoundNo + 1) + 1 <= currentStone; k++) {
			if (k * (upperBoundNo + 1) + 1 == currentStone) {
				removeNo = rand.nextInt(upperBoundNo) + 1;
				return removeNo;
			}
		}
		removeNo = currentStone - ((k - 1) * (upperBoundNo + 1) + 1);
		return removeNo;
	}

	/*
	 * set the position and the number of stone the player want to remove in
	 * advanced game, using brute-force algorithm to give the response
	 */
	public String advancedMove(boolean[] available, String lastMove) {
		int moveNo = 0, moveIndex = 0;
		System.out.println(super.getGivenName() + "'s turn - which to remove?");

		int start = 0;
		int end = available.length - 1;
		while (!available[start])
			start++;
		while (!available[end])
			end--;

		boolean[] temp = available.clone();

		/*
		 * if the stones are symmetric, and the middle stones are available, choose the middle stones,
		 * if the stones are not symmetric, try to make them to be symmetric, 
		 * otherwise move randomly
		 */
		if (symmetric(temp, start, end) && checkMiddle(temp, start, end)) {
			moveIndex = (start + 2 + end) / 2;
			if ((end - start + 1) % 2 == 0) {
				moveNo = 2;
			} else {
				moveNo = 1;
			}
			return moveIndex + " " + moveNo;
		} else {
			String[] lastRemove = lastMove.split(" ");
			int lastRemoveIndex = Integer.parseInt(lastRemove[0]);
			int lastRemoveNo = Integer.parseInt(lastRemove[1]);
			boolean[] lastAvailable = available.clone();
			for (int i = (lastRemoveIndex - 1); i < (lastRemoveIndex + lastRemoveNo - 1); i++)
				lastAvailable[i] = true;

			int lastStart = 0;
			int lastEnd = available.length - 1;
			while (!lastAvailable[lastStart])
				lastStart++;
			while (!lastAvailable[lastEnd])
				lastEnd--;

			if (symmetric(lastAvailable, lastStart, lastEnd) && !checkMiddle(lastAvailable, lastStart, lastEnd)) {
				if (lastRemoveNo == 1)
					moveIndex = lastStart + lastEnd - lastRemoveIndex + 2;
				else
					moveIndex = lastStart + lastEnd - lastRemoveIndex + 1;
				moveNo = lastRemoveNo;
				return moveIndex + " " + moveNo;
			}

			boolean[] temp2 = available.clone();
			if (!checkMiddle(temp, start, end)) {
				int i = start;
				while (i <= (start + end) / 2) {
					if (temp[i] == temp[start + end - i])
						i++;
					else {
						if (temp[i + 1] == temp[start + end - i - 1]) {
							if (temp[i])
								moveIndex = i + 1;
							else
								moveIndex = start + end - i + 1;
							moveNo = 1;
							temp2[i] = temp[i] ? false : true;
						} else {
							if (temp[i] == temp[i + 1]) {
								if (temp[i])
									moveIndex = i + 1;
								else
									moveIndex = start + end - i;
								moveNo = 2;
								temp2[i] = temp[i] ? false : true;
								temp2[i + 1] = temp2[i];
							}
						}
						if (symmetric(temp2, start, end))
							return moveIndex + " " + moveNo;
						else
							break;
					}
				}
				return moveRandom(temp);
			} else {
				boolean[] temp3 = available.clone();
				int newStart, newEnd;

				temp2[start] = false;
				newStart = 0;
				newEnd = end;
				while (!temp2[newStart])
					newStart++;
				if (symmetric(temp2, newStart, newEnd) && !checkMiddle(temp2, newStart, newEnd)) {
					moveIndex = start + 1;
					moveNo = 1;
					return moveIndex + " " + moveNo;
				}

				if (temp2[start + 1]) {
					temp2[start + 1] = false;
					newStart = 0;
					newEnd = end;
					while (!temp2[newStart])
						newStart++;
					if ((symmetric(temp2, newStart, newEnd) && !checkMiddle(temp2, newStart, newEnd))) {
						moveIndex = start + 1;
						moveNo = 2;
						return moveIndex + " " + moveNo;
					}
				}

				temp3[end] = false;
				newStart = start;
				newEnd = temp3.length - 1;
				while (!temp3[newEnd])
					newEnd--;
				if (symmetric(temp3, newStart, newEnd) && !checkMiddle(temp3, newStart, newEnd)) {
					moveIndex = end + 1;
					moveNo = 1;
					return moveIndex + " " + moveNo;
				}

				if (temp3[end - 1]) {
					temp3[end - 1] = false;
					newStart = start;
					newEnd = temp3.length - 1;
					while (!temp3[newEnd])
						newEnd--;
					if ((symmetric(temp3, newStart, newEnd) && !checkMiddle(temp3, newStart, newEnd))) {
						moveIndex = end;
						moveNo = 2;
						return moveIndex + " " + moveNo;
					}
				}
				return moveRandom(temp);
			}
		}
	}

	// check whether the stones string is symmetric
	private boolean symmetric(boolean[] available, int start, int end) {
		for (int i = start; i <= (start + end) / 2; i++)
			if (available[i] != available[start + end - i])
				return false;
		return true;
	}

	// check whether the middle of the giving stones string has already taken
	private boolean checkMiddle(boolean[] available, int start, int end) {
		if ((end - start + 1) % 2 == 0) {
			if (!available[(start + end) / 2] && !available[(start + end) / 2 + 1])
				return false;
			else
				return true;
		} else {
			if (available[(start + end) / 2])
				return true;
			else
				return false;
		}
	}

	// give a randomly move
	private String moveRandom(boolean[] available) {
		int moveIndex = 0, moveNo = 0;
		Random rand = new Random();
		while (true) {
			int i = rand.nextInt(available.length);
			if (i < available.length - 1) {
				if (available[i])
					if (available[i + 1]) {
						moveIndex = i + 1;
						moveNo = rand.nextInt(2) + 1;
						return moveIndex + " " + moveNo;
					} else {
						moveIndex = i + 1;
						moveNo = 1;
						return moveIndex + " " + moveNo;
					}
			} else {
				if (available[i]) {
					moveIndex = i + 1;
					moveNo = 1;
					return moveIndex + " " + moveNo;
				}
			}
		}
	}
}
