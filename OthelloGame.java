import java.util.Random;
import java.util.Scanner;

public class OthelloGame{
	public static final int ME = 1;
	public static final int OPPONENT = -1;
	public static final int BORDER = -2;
	public static final int EMPTY = 0;

	private Board gameBoard;
	private int currentPlayer;
	private String myColor;
	private String opponentColor;
	private String[] colors = {"B","W"};
	private int score;
	private Move move;
	private Scanner input = new Scanner(System.in);
	private int index;
	private String currentColor;

	public OthelloGame(){
		System.out.println("C Initialize Board: ");
		myColor = getMyColor();
		System.out.println("R " + myColor);
		gameBoard = new Board(myColor);
		index = 0;
		if (myColor == "B"){
			currentPlayer = ME;
		}
		else{
			currentPlayer = OPPONENT;
		}

		gameBoard.printBoard();
		while (!gameBoard.gameOver()){
			if (currentPlayer == ME){
				move = gameBoard.getMyMove();
				gameBoard.printMove(colors, index, move);
			}
			else{
				move = gameBoard.getOpponent();
			}
			gameBoard.applyMove(currentPlayer, move);
			
			gameBoard.printBoard();
			//System.out.println("C Number of pieces by empty space: " + gameBoard.piecesByEmptySpace(currentPlayer));
			currentPlayer = -1*currentPlayer;
			index = (index+1)%colors.length;
		}
		//printScore();
		gameBoard.numOfPieces(ME);
	}

	public String getMyColor(){
		String color = input.nextLine();
		if (color.equals("I B"))
			myColor = "B";
		if (color.equals("I W"))
			myColor = "W";
		return myColor;
	}

	public int printScore(){
		return score;
	}

	public static void main (String [] args){
		OthelloGame myGame = new OthelloGame();

	}
}