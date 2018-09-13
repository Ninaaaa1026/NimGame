/**
 * @author Jinnan Li, 975506
 */

public abstract class NimPlayer implements Testable {
	protected String userName;
	protected String givenName;
	protected String familyName;
	protected int playedNumber = 0;
	protected int wonNumber = 0;
	protected double wonRatio = 0;

	public NimPlayer() {

	}

	// initialize the player's information
	public NimPlayer(String[] playerInfo) {
		try {
			this.userName = playerInfo[1];
			this.familyName = playerInfo[2];
			this.givenName = playerInfo[3];
			if (playerInfo.length > 4) {
				this.playedNumber = Integer.parseInt(playerInfo[4]);
				this.wonNumber = Integer.parseInt(playerInfo[5]);
			}
		} catch (NumberFormatException e) {
			System.out.println("Incorrect format of arguments supplied to command.");
		}
	}

	// set user name,family name and given name for a player
	public void setNames(String[] names) {
		this.userName = names[1];
		this.familyName = names[2];
		this.givenName = names[3];
	}

	// set the given name for a player
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	// set the family name for a player
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	// add the number of played times of a player
	public void setPlayedNumber() {
		this.playedNumber++;
	}

	// add the number of won times of a player
	public void setWonNumber() {
		this.wonNumber++;
	}

	// reset the player's played number
	public void resetPlayedNumber() {
		this.playedNumber = 0;
	}

	// reset the won number of a player
	public void resetWonNumber() {
		this.wonNumber = 0;
	}

	// return the user name
	public String getUserName() {
		return this.userName;
	}

	// return the given name of a player
	public String getGivenName() {
		return this.givenName;
	}

	// return the family name of a player
	public String getFamilyName() {
		return this.familyName;
	}

	// return the full name of a player
	public String getFullName() {
		return this.givenName + " " + this.familyName;
	}

	// return the played number of a player
	public int getPlayedNumber() {
		return this.playedNumber;
	}

	// return the won number of a player
	public int getWonNumber() {
		return this.wonNumber;
	}

	// return the won ratio of a player
	public double getWonRatio() {
		if (this.playedNumber != 0)
			this.wonRatio = (double) this.wonNumber / (double) this.playedNumber;
		else
			wonRatio = 0;
		return wonRatio;
	}

	// set the number of stone the player want to remove/
	public abstract int removeStone(int upperBoundNo, int currentStone) throws Exception;

	// return a string containing all the information of a player
	public String toString() {
		return userName + "," + givenName + "," + familyName + "," + playedNumber + " games," + wonNumber + " wins";
	}

	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;
		else if (getClass() != otherObject.getClass())
			return false;
		else {
			NimPlayer player = (NimPlayer) otherObject;
			return (userName.equals(player.getUserName()) && familyName.equals(player.familyName)
					&& givenName.equals(player.givenName) && (wonNumber == player.wonNumber)
					&& (playedNumber == player.getPlayedNumber() && (this.getWonRatio() == player.getWonRatio())));
		}
	}

}
