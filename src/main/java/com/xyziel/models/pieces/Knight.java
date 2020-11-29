package com.xyziel.models.pieces;

import com.xyziel.models.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Knight extends Piece {

    private ImageView img;
    private Color pieceColor;
    private final int[] moves = {
            /*moves to the top-left*/
            -17, -10,
            /*moves to the top-right*/
            -15, -6,
            /*moves to the bottom-left*/
            6, 15,
            /*moves to the bottom-right*/
            10, 17 };

    public Knight(Color color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_KNIGHT.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
        this.pieceColor = color;
    }

    @Override
    public ImageView getImg() {
        return img;
    }

    @Override
    public ArrayList<Integer> getPossibleMoves(int position, Board board, boolean onlyAttackedTiles) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        Tile[] tiles = board.getTiles();
        for(int move: moves) {
            if((move + position >= 0) && (move + position <= 63) && ((move + position) % 8 >= (position % 8) - 2 && (move + position) % 8 <= (position % 8)+ 2)) {
                if(tiles[move + position].getPiece() == null) {
                    possibleMoves.add(move + position);
                } else if(tiles[move + position].getPiece().getPieceColor() != this.pieceColor) {
                    possibleMoves.add(move + position);
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public Color getPieceColor() {
        return pieceColor;
    }
}
