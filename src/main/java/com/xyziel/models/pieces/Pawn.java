package com.xyziel.models.pieces;

import com.xyziel.models.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece {

    private ImageView img;
    private Color pieceColor;
    private boolean hasMoved;
    private final int[] movesWhite = {
            /*moves to the top*/
            -7, -8, -9, -16 };
    private final int[] movesBlack = {
            /*moves to the bottom*/
            7, 8, 9, 16 };

    public Pawn(Color color, boolean hasMoved) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_PAWN.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
        this.pieceColor = color;
        this.hasMoved = hasMoved;
    }

    @Override
    public ImageView getImg() {
        return img;
    }

    @Override
    public ArrayList<Integer> getPossibleMoves(int position, Board board) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        Tile[] tiles = board.getTiles();
        int[] moves = this.pieceColor == Color.WHITE ? movesWhite : movesBlack;
        if(tiles[moves[0] + position].getPiece() != null && tiles[moves[0] + position].getPiece().getPieceColor() != this.pieceColor) {
            possibleMoves.add(moves[0] + position);
        }
        if(tiles[moves[2] + position].getPiece() != null && tiles[moves[2] + position].getPiece().getPieceColor() != this.pieceColor) {
            possibleMoves.add(moves[2] + position);
        }
        if(tiles[moves[1] + position].getPiece() == null) {
            possibleMoves.add(moves[1] + position);
        }
        if(!hasMoved && tiles[moves[1] + position].getPiece() == null && tiles[moves[3] + position].getPiece() == null) {
            possibleMoves.add(moves[3] + position);
        }
        return possibleMoves;
    }

    @Override
    public Color getPieceColor() {
        return pieceColor;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}
