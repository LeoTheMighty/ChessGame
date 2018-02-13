import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintedGame {
	
	final int SCREEN_WIDTH;
	final int SCREEN_HEIGHT;
	final int TILE_WIDTH;
	final int TILE_HEIGHT;
	
	BufferedImage chessPieceArray;
	
	JFrame f;
	JPanel p;
	Piece[][] board;
	WhichPlayer wp;
	
	private Move chosenMove;
	
	@SuppressWarnings("serial")
	
	class Board extends JPanel implements MouseMotionListener, MouseListener {
		
		// These will be -1 each if out of bounds
		int mouseX;
		int mouseY;
		// if mouse isn't pressed, then selectedPiece is null
		// if it is pressed down, then there is a piece selected
		// if the mouse is pressed on something that isn't a piece,
		// then it's basically not pressed down yanno?
		Piece selectedPiece;
		
		Board() {
			addMouseListener(this);
			addMouseMotionListener(this);
			mouseX = mouseY = -1;
			selectedPiece = null;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					
					// The checkered board colors
					if ((i + j) % 2 == 0)  g.setColor(Color.WHITE);
					else  g.setColor(Color.darkGray);
					
					int x = i * TILE_WIDTH;
					int y = j * TILE_HEIGHT;
					
					if (wp == WhichPlayer.BLACK) {
						x = 7 * TILE_WIDTH - x;
						y = 7 * TILE_HEIGHT - y;
					}
					
					g.fillRect(x, y, TILE_WIDTH, TILE_HEIGHT);
					
					// We'll print the selectedPiece last, so that no weird overlap happens
					if (!board[i][j].equals(selectedPiece)) {
					
						// Printing the actual chess pieces
						int h = chessPieceArray.getHeight();
						int w = chessPieceArray.getWidth();
						int sy1 = 0, sy2 = 0;
						int sx1 = (int)(w / 6), sx2 = (int)(w / 6);
					
						// Actually look at ChessPiecesArray.png if you want to understand this
						if (board[i][j].getWhichPlayer() == WhichPlayer.BLACK) {
							sy1 = 0;
							sy2 = (int)(h / 2);
						}
						else {
							sy1 = (int)(h / 2);
							sy2 = h;
						}
					
						BufferedImage img = chessPieceArray;
					
						switch(board[i][j].getPieceName()) {
						case QUEEN:
							sx1 *= 0;
							sx2 *= 1;
							break;
						case KING:
							sx1 *= 1;
							sx2 *= 2;
							break;
						case ROOK:
							sx1 *= 2;
							sx2 *= 3;
							break;
						case KNIGHT:
							sx1 *= 3;
							sx2 *= 4;
							break;
						case BISHOP:
							sx1 *= 4;
							sx2 *= 5;
							break;
						case PAWN:
							sx1 *= 5;
							sx2 *= 6;
							break;
						default:
							img = null;
							break;
						}
						
						Piece p = new Piece(PieceNames.PAWN, WhichPlayer.WHITE, new Position(0 , 0), false, false);
					
						//img will be null if there's an empty spot, so it won't waste resources lol
						g.drawImage(img, x, y, x + TILE_WIDTH, y + TILE_HEIGHT, sx1, sy1, sx2, sy2, null);
//						g.setColor(Color.RED);
//						g.setFont(new Font("times new roman", Font.BOLD, 20));
//				
//						g.drawString(Double.toString(p.boardMultiplier[i][j]), x + (TILE_WIDTH / 2), y + (TILE_HEIGHT / 2));
					}
				}
			}
			
			// PRINT THE SELECTED PIECE
			if (selectedPiece != null) {
				int x = mouseX - (TILE_WIDTH / 2);
				int y = mouseY - (TILE_HEIGHT / 2);
				
				// Printing the actual chess pieces
				int h = chessPieceArray.getHeight();
				int w = chessPieceArray.getWidth();
				int sy1 = 0, sy2 = 0;
				int sx1 = (int)(w / 6), sx2 = (int)(w / 6);
			
				// Actually look at ChessPiecesArray.png if you want to understand this
				if (selectedPiece.getWhichPlayer() == WhichPlayer.BLACK) {
					sy1 = 0;
					sy2 = (int)(h / 2);
				}
				else {
					sy1 = (int)(h / 2);
					sy2 = h;
				}
			
				BufferedImage img = chessPieceArray;
			
				switch(selectedPiece.getPieceName()) {
				case QUEEN:
					sx1 *= 0;
					sx2 *= 1;
					break;
				case KING:
					sx1 *= 1;
					sx2 *= 2;
					break;
				case ROOK:
					sx1 *= 2;
					sx2 *= 3;
					break;
				case KNIGHT:
					sx1 *= 3;
					sx2 *= 4;
					break;
				case BISHOP:
					sx1 *= 4;
					sx2 *= 5;
					break;
				case PAWN:
					sx1 *= 5;
					sx2 *= 6;
					break;
				default:
					img = null;
					break;
				}
			
				//img will be null if there's an empty spot, so it won't waste resources lol
				g.drawImage(img, x, y, x + TILE_WIDTH, y + TILE_HEIGHT, sx1, sy1, sx2, sy2, null);
			}
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
			drawGame(board, wp);
		}

		@Override
		public void mouseMoved(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
			drawGame(board, wp);
		}

		@Override
		public void mouseClicked(MouseEvent me) {
			//Super unecessary but needs to be here i guess
		}

		@Override
		public void mouseEntered(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
			selectedPiece = null;
			drawGame(board, wp);
		}
		
		@Override
		public void mouseExited(MouseEvent me) {
			mouseX = mouseY = -1;
			selectedPiece = null;
			drawGame(board, wp);
		}

		@Override
		public void mousePressed(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
			
			selectedPiece = pieceAt(mouseX, mouseY);
			//TODO maybe you want to highlight the spots that it can go to?
			
			drawGame(board, wp);
		}

		@Override
		public synchronized void mouseReleased(MouseEvent me) {
			mouseX = me.getX();
			mouseY = me.getY();
			
			//CREATE A MOVE BASED ON WHERE mouseX and mouseY are
			if (selectedPiece != null) {
				Piece chosenPiece = pieceAt(mouseX, mouseY);
				chosenMove = new Move(selectedPiece, chosenPiece.getPosition(), SpecialMoves.UNKNOWN);
			}
			
			selectedPiece = null;
			drawGame(board, wp);
		}
		
		private Piece pieceAt(int x, int y) {
			Piece selectPiece = null;
			
			int pieceX = (int)(x / TILE_WIDTH);
			int pieceY = (int)(y / TILE_HEIGHT);
			
			if (wp == WhichPlayer.BLACK) {
				pieceX = 7 - pieceX;
				pieceY = 7 - pieceY;
			}
			
			if (pieceX >= 0 && pieceX < 8 && pieceY >= 0 && pieceY < 8)
				selectPiece = board[pieceX][pieceY];
			
			return selectPiece;
		}
	}
	void updateBoard(Piece[][] board) {
		this.board = board;
	}
	
	public PaintedGame(Piece[][] board, int w, int h) {
		
		try {
			chessPieceArray = loadMedia();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		chosenMove = null;
		this.board = board;
		SCREEN_WIDTH = w;
		SCREEN_HEIGHT = h;
		//This doesn't look pretty at all but it works :/
		TILE_WIDTH = (int)(w / 8) - 2;
		TILE_HEIGHT = (int)(h / 8) - 5;
		
		f = new JFrame("Chess");
		p = new Board();
		f.setSize(w, h);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.add(p);
	}
	
	private BufferedImage loadMedia() throws IOException {
		BufferedImage bimg = ImageIO.read(new File("img/ChessPiecesArray.png"));
		
		return bimg;
	}
	
	public void drawGame(Piece[][] board, WhichPlayer wp) {
		updateBoard(board);
		this.wp = wp;
		p.repaint();
	}
	
	public synchronized Move getMove() {
		return chosenMove;
	}
	
	public synchronized void doneMove() {
		chosenMove = null;
	}
}
