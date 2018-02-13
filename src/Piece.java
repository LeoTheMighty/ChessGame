import java.util.Vector;

public class Piece {
	private WhichPlayer whichPlayer;
	private WhichPlayer otherPlayer;
	private PieceNames pieceName;
	private String name;
	//value will probably be used if I decide to implement Minimax into this program lol
	private int value;
	private Position position;
	// This'll be useful for castling and pawns
	private boolean ifMoved;
	private boolean ifDoubleJumpedLastTurn;
	public double[][] boardMultiplier;
	
	Piece(PieceNames pieceName, WhichPlayer whichPlayer, Position position, boolean ifMoved, boolean db) {
		this.ifMoved = ifMoved;
		this.position = position;
		this.whichPlayer = whichPlayer;
		ifDoubleJumpedLastTurn = db;
		
		if (whichPlayer == WhichPlayer.WHITE)
			otherPlayer = WhichPlayer.BLACK;
		else
			otherPlayer = WhichPlayer.WHITE;
		
		this.pieceName = pieceName;
		ifMoved = false;
		boardMultiplier = new double[8][8];
		// to see visualization of chess piece multipliers, see ChessPieceMulltiplierRefence.png
		switch(pieceName) {
		case PAWN:
			name = "pawn";
			value = 10;
			
			//THIS BOARD MULTIPLIER IS CORRECT LOL
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					
					//i is x, j is y
					
					if (j == 6)
						boardMultiplier[x][y] = 5.0;
					else if ((j == 2 && i == 2) || (j == 2 && i == 5))
						boardMultiplier[x][y] = -1.0;
					else if ((j == 2 && i == 1) || (j == 2 && i == 6))
						boardMultiplier[x][y] = -0.5;
					else if (j == 1 && i < 5 && i > 2)
						boardMultiplier[x][y] = -2.0;
					else if ((j == 1 && (i == 1 || i == 2 || i == 5 || i ==6)) ||
							(j == 4 && (i == 2 || i == 5)) ||
							(j == 5 && (i < 2 || i > 5)))
						boardMultiplier[x][y] = 1.0;
					else if ((j == 3 && (i == 3 || i == 4)) ||
							(j == 5 && (i == 2 || i == 5)))
						boardMultiplier[x][y] = 2.0;
					else if ((j == 4 && (i == 3 || i == 4)))
						boardMultiplier[x][y] = 2.5;
					else if ((j == 5 && (i == 3 || i == 4)))
						boardMultiplier[x][y] = 3.0;
					else if (j == 4 || j == 1 || (j == 2 && i != 3 && i != 4))
						boardMultiplier[x][y] = 0.5;
					//else it's automatically 0.0
				}
			}
			break;
		case KNIGHT:
			name = "knight";
			value = 30;
			
			//THIS BOARD MULTIPLIER IS CORRECT
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					//i is x, j is y
					
					if ((j == 7 || j == 0) && (i == 0 || i == 7))
						boardMultiplier[x][y] = -5.0;
					else if (((j == 7 || j == 0) && (i == 1 || i == 6)) ||
							((j == 1 || j == 6) && (i == 0 || i == 7)))
						boardMultiplier[x][y] = -4.0;
					// this works because of the ones before
					else if (j == 7 || j == 0 || i == 0 || i == 7)
						boardMultiplier[x][y] = -3.0;
					else if ((j == 1 || j == 6) && (i == 1 || i == 6))
						boardMultiplier[x][y] = -2.0;
					else if (((j == 2 || j == 4) && (i == 1 || i == 6)) ||
							(j == 1 && (i == 3 || i == 4)))
						boardMultiplier[x][y] = 0.5;
					else if ((j == 2 || j == 5) && (i == 2 || i == 5))
						boardMultiplier[x][y] = 1.0;
					else if (j == 2 || (j == 5 && i != 6 && i != 1) ||
							((j == 3 || j == 4) && (i == 2 || i == 5)))
						boardMultiplier[x][y] = 1.5;
					else if ((j == 3 || j == 4) && (i == 3 || i == 4))
						boardMultiplier[x][y] = 2.0;
					//else it's automatically 0.0
				}
			}
			break;
		case BISHOP:
			name = "bishop";
			value = 30;
			
			//THIS BOARD MULTIPLIER IS CORRECT
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					//i is x, j is y
					
					if ((j == 0 || j == 7) && (i == 0 || i == 7))
						boardMultiplier[x][y] = -2.0;
					else if (j == 0 || j == 7 || i == 0 || i == 7)
						boardMultiplier[x][y] = -1.0;
					else if (j == 2 || (j == 3 && i != 1 && i != 6) ||
							((j == 4 || j == 5) && (i == 3 || i == 4)))
						boardMultiplier[x][y] = 1.0;
					else if (j == 4 || (j == 5 && (i == 2 || i == 5)) ||
							(j == 1 && (i == 1 || i == 6)))
						boardMultiplier[x][y] = 0.5;
					//else it's automatically 0.0
				}
			}
			break;
		case ROOK:
			name = "rook";
			value = 50;
			
			//THIS BOARD MULTIPLIER IS CORRECT
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					//i is x, j is y
					
					if ((j > 0 && j < 6) && (i == 0 || i == 7))
						boardMultiplier[x][y] = -0.5;
					else if ((j == 6 && (i == 0 || i == 7)) ||
							(j == 0 && (i == 3 || i == 4)))
						boardMultiplier[x][y] = 0.5;
					else if (j == 6)
						boardMultiplier[x][y] = 1.0;
					//else it's automatically 0.0
				}
			}
			break;
		case QUEEN:
			name = "queen";
			value = 90;
			
			//THIS BOARD MULTIPLIER IS CORRECT
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					//i is x, j is y
					
					if ((j == 0 || j == 7) && (i == 0 || i == 7))
						boardMultiplier[x][y] = -2.0;
					else if (((j == 0 || j == 7) && (i < 3 || i > 4)) ||
							((j < 3 || j > 4) && (i == 0 || i == 7))) 
						boardMultiplier[x][y] = -1.0;
					else if (j == 0 || j == 7 || (j == 3 && i == 7) ||
							(j == 4 && (i == 0 || i == 7)))
						boardMultiplier[x][y] = -0.5;
					else if ((j == 1 && i == 2) || (j == 2 && (i < 6)) || 
							((j == 3 || j == 4 || j == 5) && (i > 1 && i < 6)))
						boardMultiplier[x][y] = 0.5;
					//else it's automatically 0.0
				}
			}
			break;
		case KING:
			name = "king";
			value = 900;
			
			//THIS MULTIPLIER BOARD IS CORRECT
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					//flip the board if black
					int x = i, y = j;
					if (whichPlayer == WhichPlayer.WHITE)
						y = 7 - j;
					//i is x, j is y
					
					if (j > 3 && (i == 3 || i == 4))
						boardMultiplier[x][y] = -5.0;
					else if ((j > 3 && i > 0 && i < 7) || (j == 3 && (i == 3 || i == 4)))
						boardMultiplier[x][y] = -4.0;
					else if ((j > 3 && (i == 0 || i == 7)) || (j == 3 && i > 0 && i < 7))
						boardMultiplier[x][y] = -3.0;
					else if ((j == 3 && (i == 0 || i == 7)) || (j == 2 && (i > 0 && i < 7)))
						boardMultiplier[x][y] = -2.0;
					else if (j == 2 && (i == 0 || i == 7))
						boardMultiplier[x][y] = -1.0;
					else if (j == 0 && (i == 2 || i == 5))
						boardMultiplier[x][y] = 1.0;
					else if (i == 0 || i == 7 || (j == 1 && (i == 1 || i == 6)))
						boardMultiplier[x][y] = 2.0;
					else if (j == 0 && (i == 1 || i == 6))
						boardMultiplier[x][y] = 3.0;
					//else it's automatically 0.0
				}
			}
			break;
		default:
			name = "";
			value = 0;
			break;
		}
	}
	
	public Move[] getValidMoves(Piece[][] board) {
		// Gets all moves possible by this piece
		// JUST REMEMBER TO CHECK IF THE KING'S IN DANGER LATER
		// You need the context of everyone else's moves to do this
		
		// Potential Moves
		Vector<Move> pm = new Vector<Move>();
		
		int x0 = position.getX(), y0 = position.getY();
		int x = x0, y = y0;
		
		//KING, PAWN, and KNIGHT are special-ish
		
		//BISHOP, ROOK, and QUEEN all go until they're gone
		
		switch(pieceName) {
		case PAWN:
			// shows which way the pawn can move
			int direction = 0;
			
			if (whichPlayer == WhichPlayer.WHITE)
				direction = -1;
			else
				direction = 1;
			
			y += direction;
			
			// eat to the right
			if (inBounds(board, x + 1, y)) {
				if (board[x + 1][y].whichPlayer == otherPlayer)
					pm.add(makeMove(x + 1, y, SpecialMoves.NORMAL));
				if (board[x + 1][y - direction].whichPlayer == otherPlayer
						&& board[x + 1][y - direction].getIfCanEnPassent())
					pm.add(makeMove(x + 1, y, SpecialMoves.ENPASSENT));
			}
			
			// eat to the left
			if (inBounds(board, x - 1, y)) {
				if (board[x - 1][y].whichPlayer == otherPlayer)
					pm.add(makeMove(x - 1, y, SpecialMoves.NORMAL));
				if (board[x - 1][y - direction].whichPlayer == otherPlayer
						&& board[x - 1][y - direction].getIfCanEnPassent())
					pm.add(makeMove(x - 1, y, SpecialMoves.ENPASSENT));
			}
			
			// move forward one
			if (inBounds(board, x, y)) {
				if (board[x][y].whichPlayer == WhichPlayer.NONE) {
					pm.add(makeMove(x, y, SpecialMoves.NORMAL));
					//move forward two
					if (!ifMoved) {
						if (board[x][y].whichPlayer == WhichPlayer.NONE && board[x][y + direction].whichPlayer == WhichPlayer.NONE)
							pm.add(makeMove(x, y + direction, SpecialMoves.DOUBLEJUMP));
					}
				}
			}
			
			break;
		case KNIGHT:
			for (int i = 0; i < 8; i++) {
				//resets for each iteration
				x = x0;
				y = y0;
				switch(i) {
				// iterates through each x y combination for knights
				// I'm gonna put it in unit circle direction for shits and giggles
				case 0: x += 2; y -= 1; break;
				case 1: x += 1; y -= 2; break;
				case 2: x -= 1; y -= 2; break;
				case 3: x -= 2; y -= 1; break;
				case 4: x -= 2; y += 1; break;
				case 5: x -= 1; y += 2; break;
				case 6: x += 1; y += 2; break;
				case 7: x += 2; y += 1; break;
				}
				
				if (inBounds(board, x, y)) {
					if (board[x][y].whichPlayer != whichPlayer)
						pm.add(makeMove(x, y, SpecialMoves.NORMAL));
				}
			}
			break;
		case KING:
			for (int i = 0; i < 8; i++) {
				//resets for each iteration
				x = x0;
				y = y0;
				switch(i) {
				// iterates through each x y combination for the king
				// I'm gonna put it in unit circle direction for shits and giggles
				case 0: x += 1; y -= 0; break;
				case 1: x += 1; y -= 1; break;
				case 2: x -= 0; y -= 1; break;
				case 3: x -= 1; y -= 1; break;
				case 4: x -= 1; y += 0; break;
				case 5: x -= 1; y += 1; break;
				case 6: x += 0; y += 1; break;
				case 7: x += 1; y += 1; break;
				}
				
				if (inBounds(board, x, y)) {
					if (board[x][y].whichPlayer != whichPlayer)
						pm.add(makeMove(x, y, SpecialMoves.NORMAL));
				}
			}
			
			x = x0; y = y0;
			
			// include castling
			if (!ifMoved) {
				//long castling, 3 pieces in between
				if (!board[0][y].ifMoved &&
						board[1][y].pieceName == PieceNames.NONE &&
						board[2][y].pieceName == PieceNames.NONE &&
						board[3][y].pieceName == PieceNames.NONE)
					pm.add(makeMove(x - 2, y, SpecialMoves.CASTLE));
				
				//short castling, 2 pieces in between
				if (!board[7][y].ifMoved &&
						board[6][y].pieceName == PieceNames.NONE &&
						board[5][y].pieceName == PieceNames.NONE)
					pm.add(makeMove(x + 2, y, SpecialMoves.CASTLE));
			}
			
			break;
		case BISHOP:
			for (int i = 0; i < 4; i++) {
				x = x0;
				y = y0;
				int dx = 0, dy = 0;
				switch (i) {
				// ways the bishop can start moving
				case 0: dx =  1; dy = -1; break;
				case 1: dx = -1; dy = -1; break;
				case 2: dx = -1; dy =  1; break;
				case 3: dx =  1; dy =  1; break;
				}
				x += dx; y += dy;
				
				// make the moves until you reach the end of the board
				while (inBounds(board, x, y)) {
					if (board[x][y].whichPlayer != whichPlayer) {
						pm.add(makeMove(x, y, SpecialMoves.NORMAL));
						
						if (board[x][y].whichPlayer != WhichPlayer.NONE) {
							//hacky way of saying don't fucking continue
							x = -1; y = -1;
						}
						else {
							x += dx; y += dy;
						}
					}
					else {
						x = -1; y = -1;
					}
				}
			}
			break;
		case ROOK:
			for (int i = 0; i < 4; i++) {
				x = x0;
				y = y0;
				int dx = 0, dy = 0;
				switch (i) {
				// ways the rook can start moving
				case 0: dx =  1; dy =  0; break;
				case 1: dx =  0; dy = -1; break;
				case 2: dx = -1; dy =  0; break;
				case 3: dx =  0; dy =  1; break;
				}
				x += dx; y += dy;
				
				// make the moves until you reach the end of the board
				while (inBounds(board, x, y)) {
					if (board[x][y].whichPlayer != whichPlayer) {
						pm.add(makeMove(x, y, SpecialMoves.NORMAL));
						
						if (board[x][y].whichPlayer != WhichPlayer.NONE) {
							//hacky way of saying don't fucking continue
							x = -1; y = -1;
						}
						else {
							x += dx; y += dy;
						}
					}
					else {
						x = -1; y = -1;
					}
				}
			}
			break;
		case QUEEN:
			for (int i = 0; i < 8; i++) {
				x = x0;
				y = y0;
				int dx = 0, dy = 0;
				switch (i) {
				// ways the queen can start moving
				// rook-wise
				case 0: dx =  1; dy =  0; break;
				case 2: dx =  0; dy = -1; break;
				case 4: dx = -1; dy =  0; break;
				case 6: dx =  0; dy =  1; break;
				// bishop-wise
				case 1: dx =  1; dy = -1; break;
				case 3: dx = -1; dy = -1; break;
				case 5: dx = -1; dy =  1; break;
				case 7: dx =  1; dy =  1; break;
				}
				x += dx; y += dy;
				
				// make the moves until you reach the end of the board
				// make the moves until you reach the end of the board
				while (inBounds(board, x, y)) {
					if (board[x][y].whichPlayer != whichPlayer) {
						pm.add(makeMove(x, y, SpecialMoves.NORMAL));
						
						if (board[x][y].whichPlayer != WhichPlayer.NONE) {
							//hacky way of saying don't fucking continue
							x = -1; y = -1;
						}
						else {
							x += dx; y += dy;
						}
					}
					else {
						x = -1; y = -1;
					}
				}
			}
			break;
		case NONE:
			break;
		}
		
		return pm.toArray(new Move[pm.size()]);
	}
	
	private Move makeMove(int x, int y, SpecialMoves sm) {
		return new Move(this, new Position(x, y), sm);
	}
	
	private boolean inBounds(Piece[][] board, int x, int y) {
		return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
	}
	
	public boolean getIfCanEnPassent() {
		return ifDoubleJumpedLastTurn;
	}
	
	public boolean getIfMoved() {
		return ifMoved;
	}
	
	public int getX() {
		return position.getX();
	}
	
	public int getY() {
		return position.getY();
	}
	
	public Position getPosition() {
		return position;
	}
	
	private double getMultiplier() {
		return boardMultiplier[position.getX()][position.getY()];
	}
	
	public double getValue() {
		return value * getMultiplier();
	}
	
	public WhichPlayer getWhichPlayer() {
		return whichPlayer;
	}
	
	public PieceNames getPieceName() {
		return pieceName;
	}
	
	public String getName() {
		return name;
	}
}
