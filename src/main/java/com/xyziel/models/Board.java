package com.xyziel.models;

import com.xyziel.models.pieces.Color;
import com.xyziel.models.pieces.King;
import com.xyziel.models.pieces.Pawn;
import com.xyziel.models.pieces.Piece;
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
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                tiles[i + 8*j] = new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j, this);
                this.add(tiles[i + 8*j], i, j);
            }
        }
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
//        if(clickedPiece != null && !clickedPiece.equals(startPosition)) {
//            for(int move: clickedPiece.getPiece().getPossibleMoves(clickedPiece.getPosition(), this)) {
//                tiles[move].unhighlightMove();
//                System.out.println("Move: " + move);
//                tiles[move].setOnMouseClicked(e -> tiles[move].highlightMoves());
//            }
//            System.out.println("Clicked Piece is: " + clickedPiece.getPosition());
//            tiles[clickedPiece.getPosition()].unhighlightMove();
//            tiles[clickedPiece.getPosition()].setOnMouseClicked(e -> tiles[clickedPiece.getPosition()].highlightMoves());
//        }

            for (int move : moves) {
                //pretend move
                System.out.println(startPosition.getPiece());
                Color color = startPosition.getPiece().getPieceColor();
//            System.out.println("Move: " + move);
                Piece startingPositionPiece = startPosition.getPiece();
                Piece movePiece = tiles[move].getPiece();
                pretendMove(startingPositionPiece, null, move, startPosition.getPosition());
                boolean isCheck = isKingUnderCheck(color);
                pretendMove(movePiece, startingPositionPiece, move, startPosition.getPosition());
                if (!isCheck) {
                    tiles[move].highlightMove();
                    tiles[move].setOnMouseClicked(e -> {
                        prepareMove(move, startPosition, moves);
                        tiles[move].displayInfo();
                    });
                }
            }
            tiles[startPosition.getPosition()].highlightMove();
            tiles[startPosition.getPosition()].setOnMouseClicked(e -> prepareMove(startPosition.getPosition(), startPosition, moves));
            //clickedPiece = startPosition;
        }
    }

    public void prepareMove(int clickedMove, Tile startPosition, ArrayList<Integer> moves) {
        if(clickedMove != startPosition.getPosition()) {
            Piece piece = startPosition.getPiece();
            if(piece instanceof Pawn) {
                if (!(((Pawn) piece).isHasMoved())) {
                    this.enpassant = clickedMove;
                    System.out.println("enpassant" + enpassant);
                }
            } else {
                enpassant = -1;
            }
            tiles[clickedMove].setPiece(piece);
            startPosition.clearTile();
            this.colorToMove = this.colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
        for(int move: moves) {
            tiles[move].unhighlightMove();
            tiles[move].setOnMouseClicked(e -> {tiles[move].highlightMoves(); tiles[move].displayInfo();});
        }
        tiles[startPosition.getPosition()].unhighlightMove();
        tiles[startPosition.getPosition()].setOnMouseClicked(e -> {tiles[startPosition.getPosition()].highlightMoves(); tiles[startPosition.getPosition()].displayInfo();});
        //clickedPiece = null;
    }

    public ArrayList<Integer> getAllMoves(Color color) {
        ArrayList<Integer> moves = new ArrayList<>();
        for(int i = 0; i < 64; i++) {
            if(tiles[i].getPiece() != null && tiles[i].getPiece().getPieceColor() == color) {
               moves.addAll(tiles[i].getPiece().getPossibleMoves(tiles[i].getPosition(), this));
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
}
