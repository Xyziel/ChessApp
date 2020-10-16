package com.xyziel.models.pieces;

import com.xyziel.models.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class King extends Piece {

    private ImageView img;
    private Color pieceColor;
    private final int[] moves = {
            /*moves to the left*/
            -1,
            /*moves to the top*/
            -7, -8, -9,
            /*moves to the right*/
            1,
            /*moves to the bottom*/
            7, 8, 9 };
    private ArrayList<Integer> possibleMoves;

    public King(Color color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_KING.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
        possibleMoves = new ArrayList<>();
        this.pieceColor = color;
    }

    @Override
    public ImageView getImg() {
        return img;
    }

    @Override
    public ArrayList<Integer> getPossibleMoves(int position, Board board) {
        Tile[] tiles = board.getTiles();
        for(int move: moves) {
            if((move + position >= 0) && (move + position <= 63)) {
                if(tiles[move + position].getPiece() == null /*tu jeszcze szach*/) {
                    possibleMoves.add(move + position);
                } else if(tiles[move + position].getPiece().getPieceColor() != this.pieceColor) {
                    possibleMoves.add(move + position);
                }
            }
        }
        possibleMoves.add(position);
        return possibleMoves;
    }

    @Override
    public Color getPieceColor() {
        return pieceColor;
    }
}
