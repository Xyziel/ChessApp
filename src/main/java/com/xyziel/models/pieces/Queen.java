package com.xyziel.models.pieces;

import com.xyziel.models.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Queen extends Piece {

    private ImageView img;
    private Color pieceColor;
    private final int[] moves = {
            /*moves to the left*/
            -1, -2, -3, -4, -5, -6, -7,
            /*moves to the top*/
            -8, -16, -24, -32, -40, -48, -56,
            /*moves to the right*/
            1, 2, 3, 4, 5, 6, 7,
            /*moves to the bottom*/
            8, 16, 24, 32, 40, 48, 56,
            /*moves to the top-left*/
            -9, -18, -27, -36, -45, -54, -63,
            /*moves to the top-right*/
            -7, -14, -21, -28, -35, -42, -49,
            /*moves to the bottom-left*/
            7, 14, 21, 28, 35, 42, 49,
            /*moves to the bottom-right*/
            9, 18, 27, 36, 45, 54, 63};

    public Queen(Color color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_QUEEN.png").toURI().toString());
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
    public ArrayList<Integer> getPossibleMoves(int position, Board board) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        Tile[] tiles = board.getTiles();
        int row = position / 8;
        for (int i = 0; i < moves.length; i++) {
            if ((moves[i] + position >= 0) && (moves[i] + position <= 63)) {
                if (i < 28 && ((moves[i] + position) / 8 == row || moves[i] % 8 == 0)) {
                    if ((tiles[moves[i] + position].getPiece() != null)) {
                        if (tiles[moves[i] + position].getPiece().getPieceColor() != this.pieceColor) {
                            possibleMoves.add(moves[i] + position);
                        }
                        i = i / 7 * 7 + 6;
                    } else {
                        possibleMoves.add(moves[i] + position);
                    }
                }
                if (i > 27) {
                    if (((i / 7 == 4 || i / 7 == 6) && (moves[i] + position) % 8 != 7) || ((i / 7 == 5 || i / 7 == 7) && (moves[i] + position) % 8 != 0)) {
                        if ((tiles[moves[i] + position].getPiece() != null)) {
                            if (tiles[moves[i] + position].getPiece().getPieceColor() != this.pieceColor) {
                                possibleMoves.add(moves[i] + position);
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
        }
        return possibleMoves;
    }

    @Override
    public Color getPieceColor() {
        return pieceColor;
    }
}
