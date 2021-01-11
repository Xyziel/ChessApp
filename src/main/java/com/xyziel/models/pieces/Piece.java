package com.xyziel.models.pieces;

import com.xyziel.controllers.Board;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Piece {

    public abstract ImageView getImg();
    public abstract ArrayList<Integer> getPossibleMoves(int position, Board board, boolean onlyAttackedTiles);
    public abstract Color getPieceColor();


}
