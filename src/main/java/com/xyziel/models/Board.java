package com.xyziel.models;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board extends GridPane {

    private Tile[] tiles;
    private Tile clickedPiece;

    public Board() {
        this.setStyle("-fx-border-color: black");
        tiles = new Tile[64];
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
        if(clickedPiece != null && !clickedPiece.equals(startPosition)) {
            for(int move: clickedPiece.getPiece().getPossibleMoves(clickedPiece.getPosition(), this)) {
                tiles[move].unhighlightMove();
                tiles[move].setOnMouseClicked(e -> tiles[move].highlightMoves());
            }

        }
        for(int move: moves) {
            tiles[move].highlightMove();
            tiles[move].setOnMouseClicked(e -> prepareMove(move, startPosition, moves));
        }
        clickedPiece = startPosition;
    }

    public void prepareMove(int clickedMove, Tile startPosition, ArrayList<Integer> moves) {
        if(clickedMove == startPosition.getPosition()) {
            for(int move: moves) {
                tiles[move].unhighlightMove();
                tiles[move].setOnMouseClicked(e -> tiles[move].highlightMoves());
            }
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setClickedPiece(Tile piece) {
        this.clickedPiece = piece;
    }
}
