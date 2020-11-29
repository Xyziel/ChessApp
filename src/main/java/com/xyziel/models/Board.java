package com.xyziel.models;

import com.xyziel.models.pieces.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board extends GridPane {

    private Tile[] tiles;
    private int enpassant;
    private Color colorToMove;

    public Board() {
        this.setStyle("-fx-border-color: black");
        tiles = new Tile[64];
        colorToMove = Color.WHITE;
    }

    public void renderBoard() {
        //white bottom
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                tiles[i + 8*j] = new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j, this);
                this.add(tiles[i + 8*j], i, j);
            }
        }
        //black bottom
//        for(int i = 7, k = 0; i >= 0; i--, k++) {
//            for(int j = 7, l = 0; j >= 0; j--, l++) {
//                tiles[i + 8*j] = new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j, this);
//                this.add(tiles[i + 8*j], k, l);
//            }
//        }
    }

    public void highlightMoves(ArrayList<Integer> moves, Tile startPosition) {
        if(startPosition.getPiece().getPieceColor() == this.colorToMove) {
            for (int i = 0; i < 64; i++) {
                tiles[i].unhighlightMove();
                int finalI = i;
                tiles[i].setOnMouseClicked(e -> {
                    tiles[finalI].highlightMoves();
                    tiles[finalI].displayInfo();
                });
            }
            ArrayList<Integer> correctMoves = checkPieceMoves(moves, startPosition);
            //System.out.println("Correct moves:" + correctMoves);
            for (int move : correctMoves) {
                tiles[move].highlightMove();
                tiles[move].setOnMouseClicked(e -> {
                    prepareMove(move, startPosition, moves);
                    tiles[move].displayInfo();
                });
            }
            tiles[startPosition.getPosition()].highlightMove();
            tiles[startPosition.getPosition()].setOnMouseClicked(e -> {
                prepareMove(startPosition.getPosition(), startPosition, moves);
                tiles[startPosition.getPosition()].displayInfo();
            });
        }
    }

    public ArrayList<Integer> checkPieceMoves(ArrayList<Integer> moves, Tile startPosition) {
        ArrayList<Integer> correctMoves = new ArrayList<>();
        for (int move : moves) {
            //pretend move
            Color color = startPosition.getPiece().getPieceColor();
            Piece startingPositionPiece = startPosition.getPiece();
            //do not check for castle
            if(startingPositionPiece instanceof King && (startPosition.getPosition() + 2 == move || startPosition.getPosition() - 2 == move)) {
                correctMoves.add(move);
            } else {
                Piece movePiece = tiles[move].getPiece();
                pretendMove(startingPositionPiece, null, move, startPosition.getPosition());
                boolean isCheck = isKingUnderCheck(color);
                pretendMove(movePiece, startingPositionPiece, move, startPosition.getPosition());
                if (!isCheck) {
                    correctMoves.add(move);
                }
            }

        }
//        if (tiles[3].getPiece() instanceof King) {
//            System.out.println("tu juz zamina");
//        }
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
            tiles[move].setOnMouseClicked(e -> {tiles[move].highlightMoves(); tiles[move].displayInfo();});
        }
        tiles[startPosition.getPosition()].unhighlightMove();
        tiles[startPosition.getPosition()].setOnMouseClicked(e -> {tiles[startPosition.getPosition()].highlightMoves(); tiles[startPosition.getPosition()].displayInfo();});
    }

    public void move(int position, Piece piece, Tile startPosition, boolean isCastle) {
        //System.out.println("Robie ruch " + colorToMove);
        tiles[position].setPiece(piece);
        startPosition.clearTile();
        if(!isCastle) {
            this.colorToMove = this.colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
            //System.out.println("Zmienilem kolor na " + colorToMove);
        }
        if(isKingUnderCheck(colorToMove)) {
            checkIfThereIsCheckmate();
        } else {
            checkIfThereIsStalemate();
        }
    }

    public ArrayList<Integer> getAllAttackedTiles(Color color) {
        ArrayList<Integer> moves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {
            if(tiles[i].getPiece() != null && tiles[i].getPiece().getPieceColor() == color) {
               moves.addAll(tiles[i].getPiece().getPossibleMoves(tiles[i].getPosition(), this, true));
            }
        }
        //System.out.println(moves);
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
                //System.out.println("krol na pozycji: " + tile.getPosition());
                return tile;
            }
        }
        return null;
    }

    public boolean isKingUnderCheck(Color color) {
        //System.out.println("Sprawdzam check");
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

    public void checkIfThereIsCheckmate() {
        //System.out.println("sprawdzam checkmate");
        Tile king = findKing(colorToMove);
        if (((King) king.getPiece()).isCheckmate(this, colorToMove)) {
            Color winnerColor = this.colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
            Alert checkmateInfo = new Alert(Alert.AlertType.INFORMATION);
            checkmateInfo.setTitle("Game Over");
            checkmateInfo.setHeaderText(winnerColor + " wins!");
            checkmateInfo.showAndWait();
//                for(Tile tile: tiles) {
//                    tile.setOnMouseClicked(e -> System.out.println("koniec"));
//                }
            this.setDisable(true);
        }
    }

    public void checkIfThereIsStalemate() {
        //System.out.println("sprawdzam stalemate");
        Tile king = findKing(colorToMove);
        if (((King) king.getPiece()).isStalemate(this, colorToMove)) {
            Alert checkmateInfo = new Alert(Alert.AlertType.INFORMATION);
            checkmateInfo.setTitle("Game Over");
            checkmateInfo.setHeaderText("Draw!");
            checkmateInfo.showAndWait();
//                for(Tile tile: tiles) {
//                    tile.setOnMouseClicked(e -> System.out.println("koniec"));
//                }
            this.setDisable(true);
        }
    }
}
