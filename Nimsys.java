
/**
@author Jinnan Li, 975506
*/

import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Scanner;

public class Nimsys {
	private Scanner scannerObject = new Scanner(System.in);
	public static final int MAX_NUMBER_PLAYERS = 100;
	private NimPlayer[] players = new NimPlayer[MAX_NUMBER_PLAYERS];
	private int playersCount = 0;

	public static void main(String[] args) {
		Nimsys commandManage = new Nimsys();
		commandManage.initialize();

		System.out.println("Welcome to Nim");

		String command = "";
		while (true) {
			System.out.print("\n$");
			command = commandManage.scannerObject.nextLine();
			String[] commSplit = command.split(" |,");

			try {
				if (commSplit[0].equals("startgame")) {
					if (commSplit.length < 5)
						throw new Exception("Incorrect number of arguments supplied to command.");
					else
						commandManage.startGame(commSplit);
				} else if (commSplit[0].equals("startadvancedgame")) {
					if (commSplit.length < 4)
						throw new Exception("Incorrect number of arguments supplied to command.");
					else
						commandManage.startAdvanceGame(commSplit);
				} else if (commSplit[0].equals("addplayer")) {
					if (commSplit.length < 4)
						throw new Exception("Incorrect number of arguments supplied to command.");
					else
						commandManage.addPlayer(commandManage.adjustComm(commSplit));
				} else if (commSplit[0].equals("addaiplayer")) {
					if (commSplit.length < 4)
						throw new Exception("Incorrect number of arguments supplied to command.");
					else
						commandManage.addAIPlayer(commandManage.adjustComm(commSplit));
				} else if (commSplit[0].equals("editplayer")) {
					if (commSplit.length < 4)
						throw new Exception("Incorrect number of arguments supplied to command.");
					else
						commandManage.editPlayer(commSplit);
				} else if (commSplit[0].equals("removeplayer")) {
					if (commSplit.length >= 2)
						commandManage.removePlayer(commSplit[1]);
					else if (commSplit.length == 1)
						commandManage.removePlayer();
				} else if (commSplit[0].equals("resetstats")) {
					if (commSplit.length >= 2)
						commandManage.resetStates(commSplit[1]);
					else if (commSplit.length == 1)
						commandManage.resetStates();
				} else if (commSplit[0].equals("displayplayer")) {
					if (commSplit.length >= 2)
						commandManage.displayPlayer(commSplit[1]);
					else if (commSplit.length == 1)
						commandManage.displayPlayer();
				} else if (commSplit[0].equals("rankings")) {
					if (commSplit.length >= 2) {
						String[] commArgs = new String[2];
						for (int i = 0; i < 2; i++)
							commArgs[i] = commSplit[i];
						commandManage.rankPlayer(commArgs);
					} else if (commSplit.length == 1)
						commandManage.rankPlayer(commSplit);
				} else if (commSplit[0].equals("exit")) {
					commandManage.exit();
				} else
					throw new Exception("'" + commSplit[0] + "' is not a valid command.");
			} catch (Exception e) {
				String message = e.getMessage();
				System.out.println(message);
			}
		}
	}

	// initialize the Nim system
	private void initialize() {
		Scanner inputStream = null;
		File target = new File("players.dat");
		if (target.exists()) {
			try {
				FileInputStream file = new FileInputStream(target.getAbsoluteFile());
				inputStream = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.print("File was not found or could not be opened.");
				System.exit(0);
			}
			while (inputStream.hasNextLine()) {
				String[] playerInfo = inputStream.nextLine().split(" ");
				if (playerInfo[0].equals("NimHumanPlayer")) {
					players[playersCount] = new NimHumanPlayer(playerInfo, scannerObject);
					playersCount++;
				} else if (playerInfo[0].equals("NimAIPlayer")) {
					players[playersCount] = new NimAIPlayer(playerInfo);
					playersCount++;
				}
			}
			inputStream.close();
		}
	}

	// start a new game for two exit players
	private void startGame(String[] command) {
		try {
			int initialstones = Integer.parseInt(command[1]);
			int upperBoundNo = Integer.parseInt(command[2]);
			int player1Index = checkPlayerExist(command[3]);
			int player2Index = checkPlayerExist(command[4]);
			if (player1Index < 0 || player2Index < 0)
				System.out.println("One of the players does not exist.");
			else if (initialstones <= 0)
				System.out.println("The initial number of stones must be more than 0 to start the game.");
			else if (upperBoundNo < 1)
				System.out.println("The upper bound number of stones must be more than 1 to start the game.");
			else {
				NimGame newGame = new NimGame(initialstones, upperBoundNo, player1Index, player2Index, players);
				newGame.playGame();
			}
		} catch (NumberFormatException e) {
			System.out.println("Incorrect format of arguments supplied to command.");
		}
	}

	// start an advanced new game for two exit players
	private void startAdvanceGame(String[] command) {
		try {
			int initialstones = Integer.parseInt(command[1]);
			int player1Index = checkPlayerExist(command[2]);
			int player2Index = checkPlayerExist(command[3]);

			if (player1Index < 0 || player2Index < 0)
				System.out.println("One of the players does not exist.");
			else if (initialstones <= 0)
				System.out.println("The initial number of stones must be more than 0 to start the advanced game.");
			else {
				NimGame newGame = new NimGame(initialstones, player1Index, player2Index, players);
				newGame.playAdvanceGame();
			}
		} catch (NumberFormatException e) {
			System.out.println("Incorrect format of arguments supplied to command.");
		}
	}

	// check whether the player is exist
	private int checkPlayerExist(String userName) {
		for (int i = 0; i < playersCount; i++)
			if (userName.equals(players[i].getUserName()))
				return i;
		return -1;
	}

	// add player to the system
	private void addPlayer(String[] names) {
		if (checkPlayerExist(names[1]) < 0) {
			if (playersCount < 100) {
				players[playersCount] = new NimHumanPlayer(names, scannerObject);
				playersCount++;
			} else
				System.out.println(
						"Sorry, the number of players has reached the upper bound. Please try the game later.");
		} else
			System.out.println("The player already exists.");
	}

	private void addAIPlayer(String[] names) {
		if (checkPlayerExist(names[1]) < 0) {
			if (playersCount < 100) {
				players[playersCount] = new NimAIPlayer(names);
				playersCount++;
			} else
				System.out.println(
						"Sorry, the number of players has reached the upper bound. Please try the game later.");
		} else
			System.out.println("The player already exists.");
	}

	// remove all players from the system
	private void removePlayer() {
		System.out.println("Are you sure you want to remove all players? (y/n)");
		if (scannerObject.nextLine().equalsIgnoreCase("y")) {
			for (int i = 0; i < playersCount; i++)
				players[i] = null;
			playersCount = 0;
		}
	}

	// remove specific player from the system
	private void removePlayer(String userName) {
		int i = checkPlayerExist(userName);
		if (i >= 0) {
			// move the last item in players array to the location where the player is going
			// to remove
			players[i] = players[playersCount - 1];
			players[playersCount - 1] = null;
			playersCount--;
		} else
			System.out.println("The player does not exist.");
	}

	// edit specific player given name or family name
	private void editPlayer(String[] names) {
		int i = checkPlayerExist(names[1]);
		if (i >= 0) {
			players[i].setGivenName(names[3]);
			players[i].setFamilyName(names[2]);
		} else
			System.out.println("The player does not exist.");
	}

	// reset all players' statistics
	private void resetStates() {
		System.out.println("Are you sure you want to reset all player statistics? (y/n)");
		if (scannerObject.nextLine().equalsIgnoreCase("y"))
			for (int i = 0; i < playersCount; i++) {
				players[i].resetPlayedNumber();
				players[i].resetWonNumber();
			}
	}

	// reset specific player's statistics
	private void resetStates(String userName) {
		int i = checkPlayerExist(userName);
		if (i >= 0) {
			players[i].resetPlayedNumber();
			players[i].resetWonNumber();
		} else
			System.out.println("The player does not exist.");
	}

	// display all players' information
	private void displayPlayer() {
		if (playersCount > 0)
			Arrays.sort(players, 0, playersCount, new Comparator<NimPlayer>() {
				// override the compare method to sort by user name
				public int compare(NimPlayer player1, NimPlayer player2) {
					return player1.getUserName().compareTo(player2.getUserName());
				}
			});
		for (int i = 0; i < playersCount; i++) {
			System.out.println(players[i].toString());
		}
	}

	// display specific player's information
	private void displayPlayer(String userName) {
		int i = checkPlayerExist(userName);
		if (i >= 0)
			System.out.println(players[i].toString());
		else
			System.out.println("The player does not exist.");
	}

	// rank players according to the command and display the statistics
	private void rankPlayer(String[] command) {
		if (playersCount > 0)
			if (command.length == 2 && command[1].equals("asc"))
				Arrays.sort(players, 0, playersCount, new Comparator<NimPlayer>() {
					// override the compare method to sort by won ratio in ascending order
					public int compare(NimPlayer player1, NimPlayer player2) {
						if (player1.getWonRatio() > player2.getWonRatio())
							return 1;
						else if (player1.getWonRatio() == player2.getWonRatio())
							return player1.getUserName().compareTo(player2.getUserName());
						else
							return -1;
					}
				});
			else if ((command.length == 2 && command[1].equals("desc")) || command.length == 1) {
				Arrays.sort(players, 0, playersCount, new Comparator<NimPlayer>() {
					// override the compare method to sort by won ratio in descending order
					public int compare(NimPlayer player1, NimPlayer player2) {
						if (player1.getWonRatio() < player2.getWonRatio())
							return 1;
						else if (player1.getWonRatio() == player2.getWonRatio())
							return player1.getUserName().compareTo(player2.getUserName());
						else
							return -1;
					}
				});
			}
		for (int i = 0; i < 10 && i < playersCount; i++)
			System.out.printf("%-5s| %02d games | %s\n", (int) (Math.round(players[i].getWonRatio() * 100)) + "%",
					players[i].getPlayedNumber(), players[i].getFullName());
	}

	// exit the system
	private void exit() {
		File target = new File("players.dat");
		if (!target.exists())
			try {
				target.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		PrintWriter outputStream = null;
		try {
			outputStream = new PrintWriter(new FileOutputStream(target.getAbsoluteFile()));
		} catch (FileNotFoundException e) {
			System.out.println("File players.dat was not found or could not be opened.");
			System.exit(0);
		}
		for (int i = 0; i < playersCount; i++) {
			outputStream.println(players[i].getClass().getName() + " " + players[i].getUserName() + " "
					+ players[i].getFamilyName() + " " + players[i].getGivenName() + " " + players[i].getPlayedNumber()
					+ " " + players[i].getWonNumber());
		}
		outputStream.close();
		System.out.println();
		System.exit(0);
	}

	// control the excessive input of arguments of players
	private String[] adjustComm(String[] commSplit) {
		String[] playerInfo = new String[4];
		for (int i = 0; i < 4; i++)
			playerInfo[i] = commSplit[i];
		return playerInfo;
	}
}