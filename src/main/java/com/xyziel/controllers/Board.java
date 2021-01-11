package com.xyziel.controllers;

import com.xyziel.Engine;
import com.xyziel.PromotionWindow;
import com.xyziel.models.Tile;
import com.xyziel.models.pieces.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;


public class Board extends GridPane {

    private Tile[] tiles;
    private int enpassant;
    private Color colorToMove;
    private Color playerColor;
    private Engine engine;
    private boolean gameFinished;

    public Board(Color chosenColor) {
        this.setStyle("-fx-border-color: black");
        tiles = new Tile[64];
        colorToMove = Color.WHITE;
        playerColor = chosenColor;
        Color colorForEngine = playerColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        engine = new Engine(colorForEngine);
        gameFinished = false;
    }

    public void renderBoard() {

        if(playerColor == Color.WHITE) {
            //white bottom
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    tiles[i + 8*j] = new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j, this);
                    this.add(tiles[i + 8*j], i, j);
                }
            }
        } else {
            //black bottom
            for(int i = 7, k = 0; i >= 0; i--, k++) {
                for(int j = 7, l = 0; j >= 0; j--, l++) {
                    tiles[i + 8*j] = new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j, this);
                    this.add(tiles[i + 8*j], k, l);
                }
            }
            makeEngineMove();
        }
    }


    public void highlightMoves(ArrayList<Integer> moves, Tile startPosition) {
        if(startPosition.getPiece().getPieceColor() == this.colorToMove) {
            for (int i = 0; i < 64; i++) {
                tiles[i].unhighlightMove();
                int finalI = i;
                tiles[i].setOnMouseClicked(e -> {
                    tiles[finalI].highlightMoves();
                });
            }
            ArrayList<Integer> correctMoves = checkPieceMoves(moves, startPosition);
            for (int move : correctMoves) {
                tiles[move].highlightMove();
                tiles[move].setOnMouseClicked(e -> {
                    prepareMove(move, startPosition, moves);
                });
            }
            tiles[startPosition.getPosition()].highlightMove();
            tiles[startPosition.getPosition()].setOnMouseClicked(e -> {
                prepareMove(startPosition.getPosition(), startPosition, moves);
            });
        }
    }

    public ArrayList<Integer> checkPieceMoves(ArrayList<Integer> moves, Tile startPosition) {
        ArrayList<Integer> correctMoves = new ArrayList<>();
        for (int move : moves) {
            Color color = startPosition.getPiece().getPieceColor();
            Piece startingPositionPiece = startPosition.getPiece();
            if(startingPositionPiece instanceof King && (startPosition.getPosition() + 2 == move || startPosition.getPosition() - 2 == move)) {
                correctMoves.add(move);
            } else {
                Piece movePiece = tiles[move].getPiece();
                pretendMove(startingPositionPiece, null, move, startPosition.getPosition());
                boolean isCheck;
                isCheck = isKingUnderCheck(color);
                pretendMove(movePiece, startingPositionPiece, move, startPosition.getPosition());
                if (!isCheck) {
                    correctMoves.add(move);
                }
            }
        }
        return correctMoves;
    }

    public void prepareMove(int clickedMove, Tile startPosition, ArrayList<Integer> moves) {
        if(clickedMove != startPosition.getPosition()) {
            Piece piece = startPosition.getPiece();
            if(piece instanceof Pawn) {
                if((piece.getPieceColor() == Color.WHITE && clickedMove / 8 == 0) || (piece.getPieceColor() == Color.BLACK && clickedMove / 8 == 7)) {
                    PromotionWindow promotionWindow = new PromotionWindow(piece.getPieceColor());
                    promotionWindow.showAndWait();
                    piece = promotionWindow.getSelectedFigure();
                } else if(tiles[clickedMove].getPiece() == null && clickedMove % 8 != startPosition.getPosition() % 8) {
                    tiles[clickedMove].setPiece(piece);
                    tiles[clickedMove + 8].clearTile();
                } else if (!(((Pawn) piece).isHasMoved())) {
                    this.enpassant = clickedMove;
                }
            } else {
                enpassant = -1;
            }
            if(piece instanceof King) {
                if(clickedMove - 2 == startPosition.getPosition()) {
                    move(clickedMove - 1, tiles[clickedMove + 1].getPiece(), tiles[clickedMove + 1], true);
                } else if(clickedMove + 2 == startPosition.getPosition()) {
                    move(clickedMove + 1, tiles[clickedMove - 2].getPiece(), tiles[clickedMove - 2], true);
                }
            }
            move(clickedMove, piece, startPosition, false);
        }
        for(int move: moves) {
            tiles[move].unhighlightMove();
            tiles[move].setOnMouseClicked(e -> {
                tiles[move].highlightMoves();
            });
        }
        tiles[startPosition.getPosition()].unhighlightMove();
        tiles[startPosition.getPosition()].setOnMouseClicked(e -> {
            tiles[startPosition.getPosition()].highlightMoves();
        });
    }

    public void move(int position, Piece piece, Tile startPosition, boolean isCastle) {
        if(piece instanceof Pawn) {
            if(tiles[position].getPiece() == null && position % 8 != startPosition.getPosition() % 8) {
                if(piece.getPieceColor() == Color.WHITE) {
                    tiles[position + 8].clearTile();
                } else {
                    tiles[position - 8].clearTile();
                }
            }
        }
        tiles[position].setPiece(piece);
        startPosition.clearTile();
        if(!isCastle) {
            this.colorToMove = this.colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
        }

        if(isKingUnderCheck(colorToMove)) {
            checkIfThereIsCheckmate();
        } else {
            checkIfThereIsStalemate();
        }
        //makeEngineMove();
    }

    public void makeEngineMove() {
        if(colorToMove == engine.getColor() && !gameFinished) {
            engine.makeAMove(getAllMovesMap(colorToMove, tiles), this);
        }
    }

    public ArrayList<Integer> getAllAttackedTiles(Color color) {
        ArrayList<Integer> moves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {
            if(tiles[i].getPiece() != null && tiles[i].getPiece().getPieceColor() == color) {
               moves.addAll(tiles[i].getPiece().getPossibleMoves(tiles[i].getPosition(), this, true));
            }
        }
        return moves;
    }

    public ArrayList<Integer> getAllMoves(Color color) {
        ArrayList<Integer> moves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {
            if(tiles[i].getPiece() != null && tiles[i].getPiece().getPieceColor() == color) {
                moves.addAll(checkPieceMoves(tiles[i].getPiece().getPossibleMoves(tiles[i].getPosition(), this, false), tiles[i]));
            }
        }
        return moves;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Tile findKing(Color color) {
        for(Tile tile: tiles) {
            if(tile.getPiece() instanceof King && tile.getPiece().getPieceColor() == color) {
                return tile;
            }
        }
        return null;
    }

    public boolean isKingUnderCheck(Color color) {
        Tile king = findKing(color);
        return ((King) king.getPiece()).isCheck(this, king.getPosition());
    }

    public void pretendMove(Piece toPiece, Piece fromPiece, int to, int from) {
        tiles[to].setPieceTemporary(toPiece);
        tiles[from].setPieceTemporary(fromPiece);
    }

    public int getEnpassant() {
        return enpassant;
    }

    public void setEnpassant(int enpassant) {
        this.enpassant = enpassant;
    }

    public void checkIfThereIsCheckmate() {
        Tile king = findKing(colorToMove);
        if (((King) king.getPiece()).isCheckmate(this, colorToMove)) {
            Color winnerColor = this.colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
            Alert checkmateInfo = new Alert(Alert.AlertType.INFORMATION);
            checkmateInfo.setTitle("Game Over");
            checkmateInfo.setHeaderText(winnerColor + " wins!");
            checkmateInfo.showAndWait();
            this.setDisable(true);
            gameFinished = true;
        }
    }

    public void checkIfThereIsStalemate() {
        Tile king = findKing(colorToMove);
        if (((King) king.getPiece()).isStalemate(this, colorToMove)) {
            Alert checkmateInfo = new Alert(Alert.AlertType.INFORMATION);
            checkmateInfo.setTitle("Game Over");
            checkmateInfo.setHeaderText("Draw!");
            checkmateInfo.showAndWait();
            this.setDisable(true);
            gameFinished = true;
        }
    }

    public HashMap<Tile, ArrayList<Integer>> getAllMovesMap(Color color, Tile[] tiles) {
        HashMap<Tile, ArrayList<Integer>> movesMap = new HashMap<>();
        for(int i = 0; i < 64; i++) {
            if(tiles[i].getPiece() != null && tiles[i].getPiece().getPieceColor() == color) {
                ArrayList<Integer> moves = new ArrayList<>(checkPieceMoves(tiles[i].getPiece().getPossibleMoves(tiles[i].getPosition(), this, false), tiles[i]));
                if(!moves.isEmpty()) {
                    movesMap.put(tiles[i], moves);
                }
            }

        }
        return movesMap;
    }

    public int tryAMove(Tile tile, int move) {
        Piece piece = tile.getPiece();
        if(piece instanceof Pawn) {
            if((piece.getPieceColor() == Color.WHITE && move / 8 == 0) || (piece.getPieceColor() == Color.BLACK && move / 8 == 7)) {
//                PromotionWindow promotionWindow = new PromotionWindow(piece.getPieceColor());
//                promotionWindow.showAndWait();
//                piece = promotionWindow.getSelectedFigure();
            } else if(tiles[move].getPiece() == null && move % 8 != tile.getPosition() % 8) {
                tiles[move + 8].setPieceTemporary(null);
            } else if (!(((Pawn) piece).isHasMoved())) {
                if(tile.getPiece().getPieceColor() == Color.WHITE && tile.getPosition() == move - 16) {
                    this.enpassant = move;
                } else if(tile.getPiece().getPieceColor() == Color.BLACK && tile.getPosition() == move + 16) {
                    this.enpassant = move;
                }

            }
            ((Pawn) piece).setHasMoved(true);
        } else {
            enpassant = -1;
        }
        if(piece instanceof King) {
            ((King) piece).setHasMoved(true);
            if(move - 2 == tile.getPosition()) {
                //change rook position
                tiles[move - 1].setPieceForEngine(tiles[move + 1].getPiece());
                tiles[move + 1].setPieceTemporary(null);
            } else if(move + 2 == tile.getPosition()) {
                //change rook position
                tiles[move + 1].setPieceForEngine(tiles[move - 2].getPiece());
                tiles[move - 2].setPieceTemporary(null);
            }
        }
        //move - piece changes tile
        tiles[move].setPieceTemporary(piece);
        Color color = tile.getPiece().getPieceColor() == Color.WHITE ? Color.BLACK : Color.WHITE;
        tile.setPieceTemporary(null);
        Tile king = findKing(color);
        if(isKingUnderCheck(color)) {
            if(((King) king.getPiece()).isCheckmate(this, color)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if(((King) king.getPiece()).isStalemate(this, color)) {
                return 2;
            }
            return 0;
        }
    }

    public void undoMove(Tile tile, Piece pieceToMove, Piece pieceOnMovePosition, int move, boolean hasMoved) {

        tiles[move].setPieceTemporary(pieceOnMovePosition);
        tile.setPieceTemporary(pieceToMove);

        Color color = pieceToMove.getPieceColor() == Color.WHITE ? Color.WHITE : Color.BLACK;
        Color reverseColor = pieceToMove.getPieceColor() == Color.WHITE ? Color.BLACK : Color.WHITE;

        if(pieceToMove instanceof Pawn) {
            if(color == Color.WHITE && tile.getPosition() / 8 == 6) {
                ((Pawn) pieceToMove).setHasMoved(false);
            } else if(color == Color.BLACK &&  tile.getPosition() / 8 == 1){
                ((Pawn) pieceToMove).setHasMoved(false);
            } else {
                ((Pawn) pieceToMove).setHasMoved(true);
            }
            if((pieceToMove.getPieceColor() == Color.WHITE && move / 8 == 0) || (pieceToMove.getPieceColor() == Color.BLACK && move / 8 == 7)) {
//                PromotionWindow promotionWindow = new PromotionWindow(piece.getPieceColor());
//                promotionWindow.showAndWait();
//                piece = promotionWindow.getSelectedFigure();
            }
            else if(move == enpassant) {
                tiles[move + 8].setPieceTemporary(new Pawn(reverseColor, true));
            }
        }
        if(pieceToMove instanceof King) {
            ((King) pieceToMove).setHasMoved(hasMoved);
            if(move - 2 == tile.getPosition()) {
                ((King) pieceToMove).setHasMoved(false);
                tiles[move + 1].setPieceTemporary(new Rook(color, false));
                tiles[move - 1].setPieceTemporary(null);
            } else if(move + 2 == tile.getPosition()) {
                ((King) pieceToMove).setHasMoved(false);
                tiles[move - 2].setPieceTemporary(new Rook(color, false));
                tiles[move + 1].setPieceTemporary(null);
            }
        }
    }

//    public void savePosition() {
//        for(int i = 0; i < 64; i++) {
//            Piece piece = tiles[i].getPiece();
//            if(piece instanceof Pawn) {
//                boolean hasMoved = ((Pawn) piece).isHasMoved();
//                savedPosition[i] = new Pawn(piece.getPieceColor(), hasMoved);
//            } else if(piece instanceof Knight) {
//                savedPosition[i] = new Knight(piece.getPieceColor());
//            } else if(piece instanceof Bishop) {
//                savedPosition[i] = new Bishop(piece.getPieceColor());
//            } else if(piece instanceof Rook) {
//                boolean hasMoved = ((Rook) piece).isHasMoved();
//                savedPosition[i] = new Rook(piece.getPieceColor(), hasMoved);
//            } else if(piece instanceof Queen) {
//                savedPosition[i] = new Queen(piece.getPieceColor());
//            } else if(piece instanceof King) {
//                boolean hasMoved = ((King) piece).isHasMoved();
//                savedPosition[i] = new King(piece.getPieceColor(), hasMoved);
//            } else {
//                savedPosition[i] = null;
//            }
//        }
//    }

    public void displayBoard() {
        StringBuilder board = new StringBuilder();
        int i = 0;
        for(Tile tile: tiles) {
            Piece piece = tile.getPiece();
            if(piece instanceof Pawn) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("pW, ");
                } else {
                    board.append("pB, ");
                }
            } else if(piece instanceof Knight) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("kW, ");
                } else {
                    board.append("kB, ");
                }
            } else if(piece instanceof Bishop) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("bW, ");
                } else {
                    board.append("bB, ");
                }
            } else if(piece instanceof Rook) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("rW, ");
                } else {
                    board.append("rB, ");
                }
            } else if(piece instanceof Queen) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("qW, ");
                } else {
                    board.append("qB, ");
                }
            } else if(piece instanceof King) {
                if(piece.getPieceColor() == Color.WHITE) {
                    board.append("sW, ");
                } else {
                    board.append("sB, ");
                }
            } else {
                board.append("  , ");
            }
            i++;
            if(i == 8) {
                i = 0;
                System.out.println(board);
                board = new StringBuilder();
            }
        }
        System.out.println();
    }

    public Color getColorToMove() {
        return colorToMove;
    }
}

