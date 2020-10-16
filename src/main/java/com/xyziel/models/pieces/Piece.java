package com.xyziel.models.pieces;

import com.xyziel.models.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Piece {

    public abstract ImageView getImg();
    public abstract ArrayList<Integer> getPossibleMoves(int position, Board board);
    public abstract Color getPieceColor();
}
