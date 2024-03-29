package com.xyziel.models.pieces;

import com.xyziel.controllers.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Bishop extends Piece {

    private ImageView img;
    private Color pieceColor;
    private final int[] moves = {
            /*moves to the top-left*/
            -9, -18, -27, -36, -45, -54, -63,
            /*moves to the top-right*/
            -7, -14, -21, -28, -35, -42, -49,
            /*moves to the bottom-left*/
            7, 14, 21, 28, 35, 42, 49,
            /*moves to the bottom-right*/
            9, 18, 27, 36, 45, 54, 63};

    public Bishop(Color color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_BISHOP.png").toURI().toString());
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
        for (int i = 0; i < moves.length; i++) {
            if ((moves[i] + position >= 0) && (moves[i] + position <= 63)) {
                if (((i / 7 == 0 || i / 7 == 2) && (moves[i] + position) % 8 != 7) || ((i / 7 == 1 || i / 7 == 3) && (moves[i] + position) % 8 != 0)) {
                    if ((tiles[moves[i] + position].getPiece() != null)) {
                        if (tiles[moves[i] + position].getPiece().getPieceColor() != this.pieceColor) {
                            if(tiles[moves[i] + position].getPiece() instanceof King) {
                                if(onlyAttackedTiles) {
                                    possibleMoves.add(moves[i] + position);
                                }
                            } else {
                                possibleMoves.add(moves[i] + position);
                            }
                        }
                        i = i / 7 * 7 + 6;
                    } else {
                        possibleMoves.add(moves[i] + position);
                    }
                } else {
                    i = i / 7 * 7 + 6;
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
