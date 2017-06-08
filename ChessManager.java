import java.util.Random;

public class ChessManager {
	private boolean whiteCastlingL = true;
	private boolean whiteCastlingR = true;
	private boolean blackCastlingL = true;
	private boolean blackCastlingR = true;
	private String[][] chessBoard = new String[8][8];
	private String firstPlayer = "";
	private String secondPlayer = "";
	private boolean tossACoin = false;
	private boolean isEnPassant = false;
	private int enPassantR = -1;
	private int enPassantC = -1;
	private int whiteKingR = 7;
	private int whiteKingC = 4;
	private int blackKingR = 0;
	private int blackKingC = 4;
	private int whiteCheck = -1;
	private int blackCheck = -1;
	private boolean pawnPromote = false;
	private int promoteRow;
	private int promoteColumn;
	private boolean whiteCh = false;
	private boolean blackCh = false;
	private String whiteTime = "(00:00:00)";
	private String blackTime = "(00:00:00)";
	private int threateningR = -1;
	private int threateningC = -1;

	
//class for the chess logic

	public ChessManager(){
		createChessBoard();
	}

	
	public boolean pawnPromotion(){
		if (pawnPromote){
			pawnPromote = false;
			return true;
		}else
			return false;
	}

	public int won(boolean isBlack){
		if (!isBlack)
			return whiteCheck;
		else
			return blackCheck;
	}
	
	public void addPlayer(String user){
		if (firstPlayer.equalsIgnoreCase("")){
			firstPlayer = user;
		}else{
			secondPlayer = user;
		}
	}
	
	public boolean firstPlayer(String user){
		return firstPlayer.equalsIgnoreCase(user);
	}
	
	public void makePromote(String piece, boolean isBlack){
		if (!isBlack ){
			if (piece.equalsIgnoreCase("queen")){
				chessBoard[promoteRow][promoteColumn] = "Q";
				queenCheck(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("rook")){
				chessBoard[promoteRow][promoteColumn] = "R";
				check(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("bishop")){
				chessBoard[promoteRow][promoteColumn] = "B";
				bishopCheck(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("knight")){
				chessBoard[promoteRow][promoteColumn] = "N";
				checkKnight(promoteRow,promoteColumn,isBlack);
			}
		}else{
			if (piece.equalsIgnoreCase("queen")){
				chessBoard[promoteRow][promoteColumn] = "q";
				queenCheck(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("rook")){
				chessBoard[promoteRow][promoteColumn] = "r";
				check(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("bishop")){
				chessBoard[promoteRow][promoteColumn] = "b";
				bishopCheck(promoteRow,promoteColumn,isBlack);
			}else if (piece.equalsIgnoreCase("knight")){
				chessBoard[promoteRow][promoteColumn] = "n";
				checkKnight(promoteRow,promoteColumn,isBlack);
			}
		}
	}
	
	
	public synchronized Boolean white(String user){
		if (!tossACoin){
			Random rnd = new Random();
			rnd.setSeed(System.currentTimeMillis()); 
			Float coin = rnd.nextFloat();
			if (coin > 0.5){
				String aux = firstPlayer;
				firstPlayer = secondPlayer;
				secondPlayer = aux;
			}
			tossACoin = true;
		}
		return user.equalsIgnoreCase(firstPlayer);
	}
	

	
	private void createChessBoard(){
		for (int j = 0 ; j < 8 ; j ++ ){
			chessBoard[1][j] = "p";
			chessBoard[6][j] = "P";
		}
		chessBoard[0][0] = chessBoard[0][7] = "r";
		chessBoard[7][0] = chessBoard[7][7] = "R";
		
		chessBoard[0][1] = chessBoard[0][6] = "n";
		chessBoard[7][1] = chessBoard[7][6] = "N";
		
		chessBoard[0][2] = chessBoard[0][5] = "b";
		chessBoard[7][2] = chessBoard[7][5] = "B";
		
		chessBoard[0][3] = "q";
		chessBoard[7][3] = "Q";
		chessBoard[0][4] = "k";
		chessBoard[7][4] = "K";
	}
	
	public void setFirstPlayer(String user){
		firstPlayer = user;
	}
	
	public void setSecondPlayer(String user){
		secondPlayer = user;
	}
	
	public String[][] getBoardWhite(){
		return chessBoard;
	}
	
	public String[][] getBoardBlack(){
		String[][] blocks = new String[8][8];
		for (int i = 0 ; i < 8; i ++){
			for(int j = 0; j < 8; j++){
				blocks[i][j] = chessBoard[8-i-1][8-j-1];
			}
		}
		
		return blocks;
	}
	
	
	private boolean validRook(int r1, int c1, int r2, int c2){
		if ( r1 != r2 && c1 != c2){
			return false;
			
		}else if (r1 == r2 ){
			int min = Math.min(c1,c2);
			int max = Math.max(c1, c2);
			
			for(int j = min + 1 ; j < max ; j++){
				if ( chessBoard[r1][j] != null )
					return false;
			}
			
		}else if (c1 == c2){
			int min = Math.min(r1,r2);
			int max = Math.max(r1, r2);
			for(int i = min + 1; i < max ; i++){
				if ( chessBoard[i][c1] != null)
					return false;
			}
		}
		return true;
	}
	
	private void check(int r2, int c2, boolean isBlack){
		boolean isCheck = false;
		int i = r2 - 1;
		
		/* up*/
		while ( !isCheck && i >= 0 ){
			if (!isBlack && chessBoard[i][c2]!=null && chessBoard[i][c2].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}if (isBlack  && chessBoard[i][c2]!=null && chessBoard[i][c2].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][c2]!=null && !chessBoard[i][c2].equalsIgnoreCase("k"))
				break;
			
			i--;
		}
		
		/* down */
		i = r2 + 1;
		while ( !isCheck && i < 8 ){
			if (!isBlack && chessBoard[i][c2]!=null && chessBoard[i][c2].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}if (isBlack  && chessBoard[i][c2]!=null && chessBoard[i][c2].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][c2]!=null && !chessBoard[i][c2].equalsIgnoreCase("k"))
				break;
			i++;
		}
		
		/* left */
		i = c2 - 1;
		while ( !isCheck && i >= 0 ){
			if (!isBlack && chessBoard[r2][i]!=null && chessBoard[r2][i].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}if (isBlack  && chessBoard[r2][i]!=null && chessBoard[r2][i].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[r2][i]!=null && !chessBoard[r2][i].equalsIgnoreCase("k"))
				break;
			i--;
		}
		
		/* right */
		i = c2 + 1;
		while ( !isCheck && i < 8 ){
			if (!isBlack && chessBoard[r2][i]!=null && chessBoard[r2][i].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}if (isBlack  && chessBoard[r2][i]!=null && chessBoard[r2][i].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[r2][i]!=null && !chessBoard[r2][i].equalsIgnoreCase("k"))
					break;
			i--;
		}
	}
	
	private boolean isValidBishop(int r1, int c1, int r2, int c2){
		int d1 = Math.abs(r2 - r1);
		int d2 = Math.abs(c2 - c1);
		if ( d1 != d2 )
			return false;
		
		int x = r1;
		int y = c1;

		for ( int i = 0; i < d1 - 1 ; i++ ){
			if (r1 < r2 )
				x++;
			else
				x--;
			
			if ( c1 < c2 )
				y++;
			else
				y--;
			
			if (chessBoard[x][y] != null)
				return false;
		}
		return true;
	}
	
	private void bishopCheck(int r2, int c2, boolean isBlack){
		int i = r2 - 1 ;
		int j = c2 - 1;
		boolean isCheck = false;
		
		while (!isCheck && i >= 0 && j >= 0){
			if (!isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][j]!=null && chessBoard[i][j].equalsIgnoreCase("k"))
				break;
			i--;
			j--;
		}
		
		i = r2 - 1;
		j = c2 + 1;
		while (!isCheck && i >= 0 && j < 8){
			if (!isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][j]!=null && chessBoard[i][j].equalsIgnoreCase("k"))
				break;
			i--;
			j++;
		}
		
		i = r2 + 1;
		j = c2 + 1;
		while (!isCheck && i < 8 && j < 8){
			if (!isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][j]!=null && chessBoard[i][j].equalsIgnoreCase("k"))
				break;
			i++;
			j++;
		}
		
		i = r2 + 1;
		j = c2 - 1;
		while (!isCheck && i < 8 && j >= 0){
			if (!isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
				isCheck = true;
				whiteCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (isBlack && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
				isCheck = true;
				blackCh = true;
				threateningR = r2;
				threateningC = c2;
			}else if (chessBoard[i][j]!=null && chessBoard[i][j].equalsIgnoreCase("k"))
				break;
			i++;
			j--;
		}
		
	}
	
	private boolean isValidQueen(int r1, int c1, int r2, int c2){
		if (r1 == r2 ){
			int min = Math.min(c1,c2);
			int max = Math.max(c1, c2);
			
			for(int j = min + 1 ; j < max ; j++){
				if ( chessBoard[r1][j] != null )
					return false;
			}
		}else if (c1 == c2){
			int min = Math.min(r1,r2);
			int max = Math.max(r1, r2);
			for(int i = min + 1; i < max ; i++){
				if ( chessBoard[i][c1] != null)
					return false;
			}
		}
		else{	
			int d1 = Math.abs(r2 - r1);
			int d2 = Math.abs(c2 - c1);
			if ( d1 != d2 )
				return false;	
			int x = r1;
			int y = c1;
			for ( int i = 0; i < d1 - 1 ; i++ ){
				if (r1 < r2 )
					x++;
				else
					x--;
				
				if ( c1 < c2 )
					y++;
				else
					y--;
				
				if (chessBoard[x][y] != null)
					return false;
			}
		}
		
		return true;
	}
	
	private void queenCheck(int r2, int c2, boolean isBlack){
		check(r2, c2, isBlack);
		if ( !whiteCh && !blackCh)
			bishopCheck(r2, c2, isBlack);
	}
	private boolean underAttack(int r2, int c2, boolean isBlack){
		int i;
		for ( i = r2 - 1 ; i >= 0 ; i --){
			if ((!isBlack && chessBoard[i][c2]!= null && ( chessBoard[i][c2].equals("q") || chessBoard[i][c2].equals("r")) ) ||
			     (isBlack && chessBoard[i][c2]!= null &&  ( chessBoard[i][c2].equals("Q") || chessBoard[i][c2].equals("R")) ))
				return true;
			else if ((!isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("q") && !chessBoard[i][c2].equals("r")) ||
					  (isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("Q") && !chessBoard[i][c2].equals("R"))   ){
				break;
			}
		}
	
		Boolean stop = false;
		i = r2 -1;
		int j = c2 + 1;
		while (i >= 0 && !stop && j < 8){
			if ((!isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ) ||
				 (isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ))
					return true;
			else if ((!isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) ||
					 (isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) )
				stop = true;
			j++;
			i--;
		}
		for ( j = c2 + 1; j < 8 ; j++){
			if ((!isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("q") || chessBoard[r2][j].equals("r")) ) ||
			     (isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("Q") || chessBoard[r2][j].equals("R")) ))
				return true;
			else if ((!isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("q") && !chessBoard[r2][j].equals("r")) ||
					 ( isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("Q") && !chessBoard[r2][j].equals("R")))
				break;
		}
		i = r2 +1;
		j = c2 + 1;
		stop = false;
		while (i<8 && !stop && j<8){
			
			if ((!isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ) ||
				 (isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ))
					return true;
			else if ((!isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) || 
					 ( isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) )
				stop = true;
			j++;
			i++;
		}
		for ( i = r2 + 1 ; i < 8 ; i ++){
			if ((!isBlack && chessBoard[i][c2]!=null && (chessBoard[i][c2].equals("q") || chessBoard[i][c2].equals("r")) ) ||
			   (  isBlack && chessBoard[i][c2]!=null && (chessBoard[i][c2].equals("Q") || chessBoard[i][c2].equals("R")) ))
				return true;
			else if ( (!isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("q") && !chessBoard[i][c2].equals("r")) || 
					  ( isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("Q") && !chessBoard[i][c2].equals("R")) )
				break;
		}
		i = r2 + 1 ;
		j = c2 - 1;
		stop = false;
		while ( i < 8 && !stop && j >= 0){
		
			if ((!isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ) ||
			     (isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ))
				return true;
			else if ((!isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b") ) ||
					  (isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) ){
				stop = true;
			}
			j--;
			i++;
		}
		for ( j = c2 - 1; j >= 0 ; j--){
			if ((!isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("q") || chessBoard[r2][j].equals("r")) ) ||
			     (isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("Q") || chessBoard[r2][j].equals("R")) ))
				return true;
			else if ( (!isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("q") && !chessBoard[r2][j].equals("r")) || 
					  ( isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("Q") && !chessBoard[r2][j].equals("R"))){
				break;
			}
		}
		i = r2 -1 ;
		j = c2 - 1;
		stop = false;
		while ( i >= 0 && !stop && j >= 0){
			
			if ((!isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ) ||
			     (isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ))
				return true;
			else if ( (!isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) ||
					  ( isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) ){
				stop = true;
			}
			j--;
			i--;
		}
	
		if ( (r2-1) >= 0 && (c2-2) >= 0){
			String aux1 = chessBoard[r2-1][c2-2];
			if (( !isBlack && (aux1!=null && aux1.equals("n"))) || ( isBlack && (aux1!=null && aux1.equals("N"))))
				return true;
		}
		if ((r2-2) >= 0 && (c2-1) >= 0){
			String aux2 = chessBoard[r2-2][c2-1];
			if (( !isBlack && (aux2!=null && aux2.equals("n"))) || ( isBlack && (aux2!=null && aux2.equals("N"))))
				return true;
		}
		if ((r2-2) >= 0 && (c2+1) < 8){
			String aux3 = chessBoard[r2-2][c2+1];
			if (( !isBlack && (aux3!=null && aux3.equals("n"))) || ( isBlack && (aux3!=null && aux3.equals("N"))))
				return true;
		}
		if ((r2-1) >= 0 && (c2+2) < 8){
			String aux4 = chessBoard[r2-1][c2+2];
			if (( !isBlack && (aux4!=null && aux4.equals("n"))) || ( isBlack && (aux4!=null && aux4.equals("N"))))
				return true;
		}
		if ((r2+1) < 8 && (c2+2) < 8){
			String aux5 = chessBoard[r2+1][c2+2];
			if (( !isBlack && (aux5!=null && aux5.equals("n"))) || ( isBlack && (aux5!=null && aux5.equals("N"))))
				return true;
		}
		if ((r2+2) < 8 && (c2+1) < 8 ) {
			String aux6 = chessBoard[r2+2][c2+1];
			if (( !isBlack && (aux6!=null && aux6.equals("n"))) || ( isBlack && (aux6!=null && aux6.equals("N"))))
				return true;
		}
		if ((r2+2) < 8 && (c2-1) >= 0 ){
			String aux7 = chessBoard[r2+2][c2-1];
			if (( !isBlack && (aux7!=null && aux7.equals("n"))) || ( isBlack && (aux7!=null && aux7.equals("N"))))
				return true;
		}
		if ((r2+1) < 8 && (c2-2) >= 0 ){
			String aux8 = chessBoard[r2+1][c2-2];
			if (( !isBlack && (aux8!=null && aux8.equals("n"))) || ( isBlack && (aux8!=null && aux8.equals("N"))))
				return true;
		}
		
		
		if ((r2-1) >= 0 && (c2-1) >= 0 && (c2+1) < 8){
			String aux9 = chessBoard[r2-1][c2-1];
			String aux10 = chessBoard[r2-1][c2+1];
			if ( !isBlack  && ( (aux9!=null && aux9.equals("p")) || (aux10!=null && aux10.equals("p"))) )
				return true;
		}
		
		if ((r2+1) < 8 && (c2-1) >= 0 && (c2+1) < 8){
			String aux11 = chessBoard[r2+1][c2-1];
			String aux12 = chessBoard[r2+1][c2+1];
			if ( isBlack  && ((aux11!=null && aux11.equals("P")) || (aux12!=null && aux12.equals("P"))) )
				return true;
		}
		
		return false;
	}
	
	private boolean validKing(int r1, int c1, int r2, int c2, boolean isBlack){
		String piece = chessBoard[r1][c1];
		chessBoard[r1][c1]=null;
		if (Math.abs(r2 - r1)>1 || Math.abs(c2 - c1)>1 || underAttack(r2, c2, isBlack)){
			
			chessBoard[r1][c1]=piece;
			return false;
		}
		chessBoard[r1][c1]=piece;
		
		return true;
	}
	
	private boolean validknight(int r1, int c1, int r2, int c2){
		int absR = Math.abs(r2 - r1);
		int absC = Math.abs(c2 - c1);
		
		if ((absR == 2 && absC == 1) || (absR == 1 && absC == 2))
			return true;
		
		return false;
	}
	
	private boolean isCheckKnight(int i, int j, boolean isBlack){
		if ( i >= 0 && i < 8 && j >= 0 && j < 8 && chessBoard[i][j] != null && chessBoard[i][j].equals("k") && !isBlack){
			whiteCh = true;
			return true;
		}else if ( i >= 0 && i < 8 && j >= 0 && j < 8 && chessBoard[i][j] != null && chessBoard[i][j].equals("K") && isBlack){
			blackCh = true;
			return true;
		}
		return false;
	}
	private void checkKnight(int r2, int c2, boolean isBlack){
		int i = r2 - 2;
		int j = c2 - 1;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 1;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		i = r2 - 1;
		j = c2 - 2;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 2;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		i = r2 + 2;
		j = c2 - 1;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 1;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		i = r2 + 1;
		j = c2 - 2;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 2;
		if (isCheckKnight(i,j,isBlack)){
			threateningR = r2;
			threateningC = c2;
			return;
		}
	}
	
	private boolean isValidPeon(int r1, int c1, int r2, int c2, Boolean isBlack){
		String end = chessBoard[r2][c2];
		if (!isBlack){
			if ( r1 == 6 && r2 == 4 && c1 == c2 && chessBoard[5][c1] == null && chessBoard[4][c1] == null)
				return true;
			else if ((r1-r2 == 1 )  && c1 == c2 && end == null){
				if ( r2==0 ){
					pawnPromote = true;
					promoteRow = r2;
					promoteColumn = c2;
				}
				return true;
			}else if ((r1-r2 == 1 ) && Math.abs(c2 - c1)==1 && end != null && !Character.isUpperCase(end.charAt(0))){
				if ( r2==0 ){
					pawnPromote = true;
					promoteRow = r2;
					promoteColumn = c2;
				}
				return true;
			}
		}else{
			if ( r1 == 1 && r2 == 3 && c1 == c2 && chessBoard[2][c1] == null && chessBoard[3][c1] == null )
				return true;
			else if ((r2 - r1 == 1) && c1 == c2 && end == null){
				if ( r2==7 ){
					pawnPromote = true;
					promoteRow = r2;
					promoteColumn = c2;
				}
				return true;
			}else if ( (r2-r1 == 1) && Math.abs(c2 - c1)==1 && end != null && Character.isUpperCase(end.charAt(0))){
				if ( r2==7 ){
					pawnPromote = true;
					promoteRow = r2;
					promoteColumn = c2;
				}
				return true;
			}
		}
		return false;
	}
	
	private void isCheckPeon(int r2, int c2, boolean isBlack){
		int i = r2 - 1;
		int j = c2 - 1;
		if (!isBlack && i >= 0 && j >= 0 && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
			whiteCh = true;
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 1;
		if (!isBlack && i >= 0 && j < 8 && chessBoard[i][j]!=null && chessBoard[i][j].equals("k")){
			whiteCh = true;
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		i = r2 + 1;
		j = c2 - 1;
		if (isBlack && i < 8 && j >= 0 && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
			blackCh = true;
			threateningR = r2;
			threateningC = c2;
			return;
		}
		
		j = c2 + 1;
		if (isBlack && i < 8 && j < 8 && chessBoard[i][j]!=null && chessBoard[i][j].equals("K")){
			blackCh = true;
			threateningR = r2;
			threateningC = c2;
			return;
		}
	}
	
	private boolean castling(String type, boolean isBlack){
		int r1 = 0; int c1 = 0;
		int r2 = 0; int c2 = 0;
		int r3 = 0; int c3 = 0;
		boolean empty = false;
		boolean inPosition = false;
		
		if (!isBlack && type.equalsIgnoreCase("short")){
			r1 = 7; c1 = 4;
			r2 = 7; c2 = 5;
			r3 = 7; c3 = 6;
			if (chessBoard[7][5] == null && chessBoard[7][6] == null)
				empty = true;
			
			if (chessBoard[7][4] != null && chessBoard[7][4].equals("K") && chessBoard[7][7]!=null && chessBoard[7][7].equals("R"))
				inPosition = true;
			
			if (underAttack(r1, c1, isBlack) || underAttack(r2, c2, isBlack) || underAttack(r3, c3, isBlack) || !empty || !inPosition)
				return false;
			
			chessBoard[7][4] = null; chessBoard[7][7] = null;
			chessBoard[7][6] = "K";  chessBoard[7][5] = "R";
			whiteKingR = 7;
			whiteKingC = 6;
			check(7, 5, isBlack);
			
		}else if (!isBlack && type.equalsIgnoreCase("long")){
			r1 = 7; c1 = 4;
			r2 = 7; c2 = 3;
			r3 = 7; c3 = 2;
			if (chessBoard[7][1] == null && chessBoard[7][2] == null && chessBoard[7][3] == null)
				empty = true;
			
			if (chessBoard[7][4]!=null && chessBoard[7][4].equals("K") && chessBoard[7][0]!=null && chessBoard[7][0].equals("R"))
				inPosition = true;
			
			if (underAttack(r1, c1, isBlack) || underAttack(r2, c2, isBlack) || underAttack(r3, c3, isBlack) || !empty || !inPosition)
				return false;
			
			chessBoard[7][4] = null; chessBoard[7][0] = null;
			chessBoard[7][2] = "K";  chessBoard[7][3] = "R";
			whiteKingR = 7;
			whiteKingC = 2;
			check(7, 3, isBlack);
			
		}else if (isBlack && type.equalsIgnoreCase("short")){
			r1 = 0; c1 = 4;
			r2 = 0; c2 = 5;
			r3 = 0; c3 = 6;
			if (chessBoard[0][5] == null && chessBoard[0][6] == null)
				empty = true;
			
			if (chessBoard[0][4]!=null && chessBoard[0][4].equals("k") && chessBoard[0][7]!=null && chessBoard[0][7].equals("r"))
				inPosition = true;
			
			if (underAttack(r1, c1, isBlack) || underAttack(r2, c2, isBlack) || underAttack(r3, c3, isBlack) || !empty || !inPosition)
				return false;
			
			chessBoard[0][4] = null; chessBoard[0][7] = null;
			chessBoard[0][6] = "k";  chessBoard[0][5] = "r";
			blackKingR = 0;
			blackKingC = 6;
			check(0, 5, isBlack);
			
		}else if (isBlack && type.equalsIgnoreCase("long")){
			r1 = 0; c1 = 4;
			r2 = 0; c2 = 3;
			r3 = 0; c3 = 2;
			if (chessBoard[0][1] == null && chessBoard[0][2] == null && chessBoard[0][3] == null)
				empty = true;
			
			if (chessBoard[0][4]!=null && chessBoard[0][4].equals("k") && chessBoard[0][0]!=null && chessBoard[0][0].equals("r"))
				inPosition = true;
			
			if (underAttack(r1, c1, isBlack) || underAttack(r2, c2, isBlack) || underAttack(r3, c3, isBlack) || !empty || !inPosition)
				return false;
			
			chessBoard[0][4] = null; chessBoard[0][0] = null;
			chessBoard[0][2] = "k";  chessBoard[0][3] = "r";
			blackKingR = 0;
			blackKingC = 6;
			check(0, 3, isBlack);
		}
		
		isEnPassant = false;
		return true;
	}
	
	private boolean isValidEnPassant(int r1, int c1, boolean isBlack){
		if ( r1 < 0 || r1 > 7 || c1 < 0 || c1 > 7 )
			return false;
		
		if (isBlack){
			r1 = 8 - r1 - 1;
			c1 = 8 - c1 - 1;
		}
		

		if (!isBlack && r1 == enPassantR && enPassantR == 3 && 
				chessBoard[enPassantR][enPassantC]!=null && chessBoard[enPassantR][enPassantC].equals("p") &&
				chessBoard[r1][c1]!=null && chessBoard[r1][c1].equals("P") && 
				( c1 == enPassantC +1 || c1 == enPassantC - 1)){
			
				
				chessBoard[enPassantR][enPassantC] = null;
				chessBoard[r1][c1] = null;
				chessBoard[enPassantR-1][enPassantC] = "P";
				isCheckPeon(enPassantR-1,enPassantC,isBlack);
				
				if (underAttack(whiteKingR, whiteKingC, isBlack)){
					chessBoard[enPassantR][enPassantC] = "p";
					chessBoard[r1][c1] = "P";
					chessBoard[enPassantR-1][enPassantC] = null;
					whiteCh = false;
					return false;
					
				}	
	
		}else if (isBlack && r1 == enPassantR && enPassantR == 4 && 
				  chessBoard[enPassantR][enPassantC]!=null && chessBoard[enPassantR][enPassantC].equals("P") &&
				  chessBoard[r1][c1]!=null && chessBoard[r1][c1].equals("p") &&
				  ( c1 == enPassantC +1 || c1 == enPassantC - 1)){

			
				chessBoard[enPassantR][enPassantC] = null;
				chessBoard[r1][c1] = null;
				chessBoard[enPassantR+1][enPassantC] = "p";
				isCheckPeon(enPassantR+1,enPassantC,isBlack);
				
				if (underAttack(blackKingR, blackKingC, isBlack)){
					chessBoard[enPassantR][enPassantC] = "P";
					chessBoard[r1][c1] = "p";
					chessBoard[enPassantR-1][enPassantC] = null;
					return false;
				}

		}else
			return false;
		
		isEnPassant = false;
		return true;
		
		
	}
	
	private boolean validCanMoveOneStep(int r2, int c2, boolean isBlack){
		
		if (!isBlack){
			if ( r2 >= 0 && r2 < 8 && c2 >= 0 && c2 < 8 &&
				(chessBoard[r2][c2]==null ||  Character.isUpperCase(chessBoard[r2][c2].charAt(0))) &&
				validKing(blackKingR, blackKingC, r2, c2, !isBlack))
				return true;
		}else {
			if ( r2 >= 0 && r2 < 8 && c2 >= 0 && c2 < 8 &&
					(chessBoard[r2][c2]==null ||  !Character.isUpperCase(chessBoard[r2][c2].charAt(0))) &&
					validKing(whiteKingR, whiteKingC, r2, c2, !isBlack))
					return true;
		}
		return false;
	}
	private boolean validCanMoveKing(boolean isBlack){

		if (!isBlack){
			/* king movements */
			if (validCanMoveOneStep(blackKingR - 1, blackKingC, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR - 1, blackKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR, blackKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR + 1, blackKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR + 1, blackKingC , isBlack)) return true;
			if (validCanMoveOneStep(blackKingR + 1, blackKingC - 1, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR, blackKingC - 1, isBlack)) return true;
			if (validCanMoveOneStep(blackKingR - 1, blackKingC - 1, isBlack)) return true;
			
		}else{
			/* king movements */
			if (validCanMoveOneStep(whiteKingR - 1, whiteKingC, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR - 1, whiteKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR, whiteKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR + 1, whiteKingC + 1, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR + 1, whiteKingC , isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR + 1, whiteKingC - 1, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR, whiteKingC - 1, isBlack)) return true;
			if (validCanMoveOneStep(whiteKingR - 1, whiteKingC - 1, isBlack)) return true;
		}
		
		return false;
	}
	
	private boolean vaildCanCapture(boolean isBlack){
		return underAttack(threateningR, threateningC, isBlack);
	}
	
	private boolean validCanBlockRook(boolean isBlack){
		
		if (!isBlack){
			if (threateningR != blackKingR && threateningC == blackKingC){
				int min = Math.min(threateningR, blackKingR);
				int max = Math.max(threateningR, blackKingR);
				for (int i = min + 1 ; i < max ; i++){
					if (validCanBlockIn(i,blackKingC, !isBlack))
						return true;
				}
				
			}else if (threateningC != blackKingC && threateningR == blackKingR){
				int min = Math.min(threateningC,blackKingC);
				int max = Math.max(threateningC,blackKingC);
				for (int j = min + 1 ; j < max ; j++){
					if (validCanBlockIn(blackKingR,j, !isBlack))
						return true;
				}
			}
		}else{
			if (threateningR != whiteKingR && threateningC == whiteKingC){
				int min = Math.min(threateningR, whiteKingR);
				int max = Math.max(threateningR, whiteKingR);
				for (int i = min + 1 ; i < max ; i++){
					if (validCanBlockIn(i,whiteKingC, !isBlack))
						return true;
				}
				
			}else if (threateningC != whiteKingC && threateningR == whiteKingR){
				int min = Math.min(threateningC,whiteKingC);
				int max = Math.max(threateningC,whiteKingC);
				for (int j = min + 1 ; j < max ; j++){
					if (validCanBlockIn(whiteKingR,j, !isBlack))
						return true;
				}
			}
		}
		return false;
	}
	
	private boolean validCanBlockIn(int r2, int c2, boolean isBlack){
		if (!isBlack){
			if (( (r2 +1) < 8 && chessBoard[r2 +1][c2]!=null && chessBoard[r2 +1][c2].equals("P")) ||
				( (r2 +2) == 6 && chessBoard[r2+2][c2]!=null && chessBoard[r2+2][c2].equals("P"))	)
				return true;
		}else{
			if (( (r2 - 1) > 0 && chessBoard[r2 -1][c2]!=null && chessBoard[r2 -1][c2].equals("p")) ||
				( (r2 -2) == 1 && chessBoard[r2-2][c2]!=null && chessBoard[r2-2][c2].equals("p"))	)
					return true;
		}
		
		
		int i;
		for ( i = r2 - 1 ; i >= 0 ; i --){
			if ((!isBlack && chessBoard[i][c2]!= null && ( chessBoard[i][c2].equals("Q") || chessBoard[i][c2].equals("R")) ) ||
			     (isBlack && chessBoard[i][c2]!= null &&  ( chessBoard[i][c2].equals("q") || chessBoard[i][c2].equals("r")) ))
				return true;
			else if ((!isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("Q") && !chessBoard[i][c2].equals("R")) ||
					  (isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("q") && !chessBoard[i][c2].equals("r"))   ){
				break;
			}
		}
		Boolean stop = false;
		i = r2 -1;
		int j = c2 + 1;
		while (i >= 0 && !stop && j < 8){
			if ((!isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ) ||
				 (isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ))
					return true;
			else if ((!isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) ||
					 (isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) )
				stop = true;
			j++;
			i--;
		}
		for ( j = c2 + 1; j < 8 ; j++){
			if ((!isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("Q") || chessBoard[r2][j].equals("R")) ) ||
			     (isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("q") || chessBoard[r2][j].equals("r")) ))
				return true;
			else if ((!isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("Q") && !chessBoard[r2][j].equals("R")) ||
					 ( isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("q") && !chessBoard[r2][j].equals("r")))
				break;
		}
		i = r2 +1;
		j = c2 + 1;
		stop = false;
		while (i<8 && !stop && j<8){
			
			if ((!isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ) ||
				 (isBlack && chessBoard[i][j]!= null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ))
					return true;
			else if ((!isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) || 
					 ( isBlack && chessBoard[i][j]!= null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) )
				stop = true;
			j++;
			i++;
		}
		for ( i = r2 + 1 ; i < 8 ; i ++){
			if ((!isBlack && chessBoard[i][c2]!=null && (chessBoard[i][c2].equals("Q") || chessBoard[i][c2].equals("R")) ) ||
			   (  isBlack && chessBoard[i][c2]!=null && (chessBoard[i][c2].equals("q") || chessBoard[i][c2].equals("r")) ))
				return true;
			else if ( (!isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("Q") && !chessBoard[i][c2].equals("R")) || 
					  ( isBlack && chessBoard[i][c2]!=null && !chessBoard[i][c2].equals("q") && !chessBoard[i][c2].equals("r")) )
				break;
		}
		
		i = r2 + 1 ;
		j = c2 - 1;
		stop = false;
		while ( i < 8 && !stop && j >= 0){
		
			if ((!isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ) ||
			     (isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ))
				return true;
			else if ((!isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B") ) ||
					  (isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) ){
				stop = true;
			}
			j--;
			i++;
		}
		for ( j = c2 - 1; j >= 0 ; j--){
			if ((!isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("Q") || chessBoard[r2][j].equals("R")) ) ||
			     (isBlack && chessBoard[r2][j]!= null && ( chessBoard[r2][j].equals("q") || chessBoard[r2][j].equals("r")) ))
				return true;
			else if ( (!isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("Q") && !chessBoard[r2][j].equals("R")) || 
					  ( isBlack && chessBoard[r2][j]!= null && !chessBoard[r2][j].equals("q") && !chessBoard[r2][j].equals("r"))){
				break;
			}
		}

		i = r2 -1 ;
		j = c2 - 1;
		stop = false;
		while ( i >= 0 && !stop && j >= 0){
			
			if ((!isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("Q") || chessBoard[i][j].equals("B")) ) ||
			     (isBlack && chessBoard[i][j]!=null && ( chessBoard[i][j].equals("q") || chessBoard[i][j].equals("b")) ))
				return true;
			else if ( (!isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("Q") && !chessBoard[i][j].equals("B")) ||
					  ( isBlack && chessBoard[i][j]!=null && !chessBoard[i][j].equals("q") && !chessBoard[i][j].equals("b")) ){
				stop = true;
			}
			j--;
			i--;
		}

		if ( (r2-1) >= 0 && (c2-2) >= 0){
			String aux1 = chessBoard[r2-1][c2-2];
			if (( !isBlack && (aux1!=null && aux1.equals("N"))) || ( isBlack && (aux1!=null && aux1.equals("n"))))
				return true;
		}

		if ((r2-2) >= 0 && (c2-1) >= 0){
			String aux2 = chessBoard[r2-2][c2-1];
			if (( !isBlack && (aux2!=null && aux2.equals("N"))) || ( isBlack && (aux2!=null && aux2.equals("n"))))
				return true;
		}

		if ((r2-2) >= 0 && (c2+1) < 8){
			String aux3 = chessBoard[r2-2][c2+1];
			if (( !isBlack && (aux3!=null && aux3.equals("N"))) || ( isBlack && (aux3!=null && aux3.equals("n"))))
				return true;
		}

		if ((r2-1) >= 0 && (c2+2) < 8){
			String aux4 = chessBoard[r2-1][c2+2];
			if (( !isBlack && (aux4!=null && aux4.equals("N"))) || ( isBlack && (aux4!=null && aux4.equals("n"))))
				return true;
		}
		
		if ((r2+1) < 8 && (c2+2) < 8){
			String aux5 = chessBoard[r2+1][c2+2];
			if (( !isBlack && (aux5!=null && aux5.equals("N"))) || ( isBlack && (aux5!=null && aux5.equals("n"))))
				return true;
		}
		
		if ((r2+2) < 8 && (c2+1) < 8 ) {
			String aux6 = chessBoard[r2+2][c2+1];
			if (( !isBlack && (aux6!=null && aux6.equals("N"))) || ( isBlack && (aux6!=null && aux6.equals("n"))))
				return true;
		}
		
		if ((r2+2) < 8 && (c2-1) >= 0 ){
			String aux7 = chessBoard[r2+2][c2-1];
			if (( !isBlack && (aux7!=null && aux7.equals("N"))) || ( isBlack && (aux7!=null && aux7.equals("n"))))
				return true;
		}
		
		if ((r2+1) < 8 && (c2-2) >= 0 ){
			String aux8 = chessBoard[r2+1][c2-2];
			if (( !isBlack && (aux8!=null && aux8.equals("N"))) || ( isBlack && (aux8!=null && aux8.equals("n"))))
				return true;
		}
		
		return false;
		
	}
	private boolean validCanBlockBishop(boolean isBlack){
		if (!isBlack){
			if (threateningR < blackKingR && threateningC > blackKingC){
				int i = blackKingR - 1;
				int j = blackKingC + 1 ;
				while ( i > threateningR && j < threateningC ){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i--;
					j++;
				}
			}else if (threateningR > blackKingR && threateningC > blackKingC){
				int i = blackKingR + 1;
				int j = blackKingC + 1;
				while ( i < threateningR && j < threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i++;
					j++;
				}
			}else if (threateningR > blackKingR && threateningC < blackKingC){
				int i = blackKingR + 1;
				int j = blackKingC - 1;
				while ( i < threateningR && j > threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i++;
					j--;
				}
			}else if (threateningR < blackKingR && threateningC < blackKingC){
				int i = blackKingR - 1;
				int j = blackKingC - 1;
				while(i > threateningR && j > threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i--;
					j--;
				}
			}
		}else{
			if (threateningR < whiteKingR && threateningC > whiteKingC){
				int i = whiteKingR - 1;
				int j = whiteKingC + 1 ;
				while ( i > threateningR && j < threateningC ){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i--;
					j++;
				}
			}else if (threateningR > whiteKingR && threateningC > whiteKingC){
				int i = whiteKingR + 1;
				int j = whiteKingC + 1;
				while ( i < threateningR && j < threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i++;
					j++;
				}
			}else if (threateningR > whiteKingR && threateningC < whiteKingC){
				int i = whiteKingR + 1;
				int j = whiteKingC - 1;
				while ( i < threateningR && j > threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i++;
					j--;
				}
			}else if (threateningR < whiteKingR && threateningC < whiteKingC){
				int i = whiteKingR - 1;
				int j = whiteKingC - 1;
				while(i > threateningR && j > threateningC){
					if (validCanBlockIn(i,j,!isBlack)){
						return true;
					}
					i--;
					j--;
				}
			}
		}
		return false;
		
	}
	
	private boolean validCanBlockQueen(boolean isBlack){
		if (validCanBlockRook(isBlack))
			return true;
		else{
			return validCanBlockBishop(isBlack);
		}
		
		
	}
	
	private boolean validCanBlock(boolean isBlack){
		String threatening = chessBoard[threateningR][threateningC];
		if (threatening != null){
			if (threatening.equalsIgnoreCase("n") || threatening.equalsIgnoreCase("p")){
				return false;
				
			}
				
			
			if (threatening.equalsIgnoreCase("r")){
				return validCanBlockRook(isBlack);
			}
		
			if (threatening.equalsIgnoreCase("b")){
				validCanBlockBishop(isBlack);
			}
			
			if (threatening.equalsIgnoreCase("q")){
				return validCanBlockQueen(isBlack);
			}
			
		}
		return false;
	}
	private void checkMate(boolean isBlack){
		boolean a = validCanMoveKing(isBlack);
		boolean b = vaildCanCapture(isBlack);
		boolean c = validCanBlock(isBlack);
		
		if ( !a && !b && !c){
			if (!isBlack){
				whiteCheck = 0; /* winner */
				blackCheck = 1;
			}else{
				blackCheck = 0; /* winner */
				whiteCheck = 1;
			}
		}
			
		
					
	}

	public Boolean validMove(String at, String to, Boolean isBlack){
		if (!isBlack)
			blackCh = false;
		else
			whiteCh = false;
			
		String[] parts1 = at.split(" ");
		String[] parts2 = to.split(" ");
	
		if (parts1.length == 2 && parts1[0].equalsIgnoreCase("castling") && 
		   (parts1[1].equalsIgnoreCase("short") || parts1[1].equalsIgnoreCase("long"))){
			
			if ((!isBlack && parts1[1].equalsIgnoreCase("short") && !whiteCastlingR) || 
				(!isBlack && parts1[1].equalsIgnoreCase("long") && !whiteCastlingL) ||
				( isBlack && parts1[1].equalsIgnoreCase("short") && !whiteCastlingR) ||
				( isBlack && parts1[1].equalsIgnoreCase("long") && !whiteCastlingL)){
				return false;
				
			}else{
				return castling(parts1[1], isBlack);
			}
		}
		if(parts1.length == 3 && parts1[0].equalsIgnoreCase("enPassant")){
			if (!isEnPassant)
				return false;
			int r1 = Integer.parseInt(parts1[1])-1;
			int c1 = Integer.parseInt(parts1[2])-1;
			
			return isValidEnPassant(r1, c1, isBlack);
				
		}
		
		if (parts1.length != 2 || parts2.length != 2)
			return false;
					
		int r1 = Integer.parseInt(parts1[0]);
		int c1 = Integer.parseInt(parts1[1]);
		int r2 = Integer.parseInt(parts2[0]);
		int c2 = Integer.parseInt(parts2[1]);
		
		if ( r1 < 1 || r1 > 8 || c1 < 1 || c1 > 8 || r2 < 1 || r2 > 8 || c2 < 1 || c2 > 8)
			return false;
		
		r1 --; c1--; r2--; c2--;
		
		if (isBlack){
			r1 = 8 - r1 - 1;
			c1 = 8 - c1 - 1;
			r2 = 8 - r2 - 1;
			c2 = 8 - c2 - 1;
		}
		
		String piece = chessBoard[r1][c1];
		String end = chessBoard[r2][c2];
		
		if (( r1==r2 && c1==c2) || (piece == null))
			return false;
		
		if (!isBlack && !Character.isUpperCase(piece.charAt(0)))
			return false;
		
		if (isBlack && Character.isUpperCase(piece.charAt(0)))
			return false;
			
		if (!isBlack && end != null && Character.isUpperCase(end.charAt(0)))
			return false;
		
		if (isBlack && end != null && !Character.isUpperCase(end.charAt(0)))
			return false;
				
		if (piece.equalsIgnoreCase("r")){
			if (!validRook(r1, c1, r2, c2))
				return false;
			else
				check(r2, c2, isBlack);
		}

		if (piece.equalsIgnoreCase("b")){
			if (!isValidBishop(r1, c1, r2, c2))
				return false;
			else 
				bishopCheck(r2, c2, isBlack);
		}

		if (piece.equalsIgnoreCase("q")){
			if (!isValidQueen(r1, c1, r2, c2))
				return false;
			else
				queenCheck(r2, c2, isBlack);
		}
		
		if (piece.equalsIgnoreCase("k")){
			if (!validKing(r1, c1, r2, c2, isBlack))
				return false;
			else{
				if (!isBlack){
					whiteKingR = r2;
					whiteKingC = c2;
				
				}else{
					blackKingR = r2;
					blackKingC = c2;
				}
			}
		}

		if (piece.equalsIgnoreCase("n")){
			if (!validknight(r1, c1, r2, c2))
				return false;
			else 
				checkKnight(r2, c2, isBlack);
		}

		if (piece.equalsIgnoreCase("p")){
			if (!isValidPeon(r1, c1, r2, c2, isBlack))
				return false;
			else
				isCheckPeon(r2, c2, isBlack);
		}

		if (!isBlack){
			if (whiteCastlingL && r1==7 && c1==0 ){
				whiteCastlingL = false;
				
			}else if (whiteCastlingR && r1==7 && c1==7){
				whiteCastlingR = false;
				
			}else if((whiteCastlingL || whiteCastlingR) && r1==7 && c1==4){
				whiteCastlingL = false;
				whiteCastlingR = false;
			}
		}else{
			if (blackCastlingL && r1==0 && c1==0){
				blackCastlingL = false;
			
			}else if (blackCastlingR && r1==0 && c1==7){
				blackCastlingR = false;
				
			}else if ((blackCastlingL || blackCastlingR) && r1==0 && c1==4){
				blackCastlingL = false;
				blackCastlingR = false;
			}
		}
		
		
		if (piece.equalsIgnoreCase("p") &&
			((r1 == 6 && r2 == 4 && c1 == c2 && chessBoard[5][c1] == null && chessBoard[4][c1] == null) || 
		     (r1 == 1 && r2 == 3 && c1 == c2 && chessBoard[2][c1] == null && chessBoard[3][c1] == null))){
			isEnPassant = true;
			enPassantR = r2;
			enPassantC = c2;
		}else
			isEnPassant = false;
		
		chessBoard[r1][c1] = null;
		chessBoard[r2][c2] = piece;
		
		if (!isBlack){
			if (underAttack(whiteKingR, whiteKingC, isBlack)){
				System.out.println("is Under Attack white king");
				chessBoard[r1][c1] = piece;
				chessBoard[r2][c2] = end;
				whiteCh = false;
				return false;
				
			}	
		}else{
			if (underAttack(blackKingR, blackKingC, isBlack)){
				System.out.println("is Under Attack black king");
				chessBoard[r1][c1] = piece;
				chessBoard[r2][c2] = end;
				blackCh = false;
				return false;
			}
		}
		
		if (whiteCh){
			System.out.println("White Check");
			checkMate(isBlack);
				
		}else if (blackCh){
			System.out.println("Black Check");
			checkMate(isBlack);
		}
		return true;
		
	}
	
	public String getTime(boolean isBlack){
		if (!isBlack)
			return blackTime;
		else
			return whiteTime;
	}
	
	public void setTime(boolean isBlack, String time){
		if (!isBlack)
			whiteTime = time;
		else
			blackTime = time;
	}

}