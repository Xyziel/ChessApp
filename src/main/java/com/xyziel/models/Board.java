package com.xyziel.models;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Board extends GridPane {

    private Tile[] tiles;

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

    public void highlightMoves(ArrayList<Integer> moves) {
        System.out.println(moves);
        for(int move: moves) {
            tiles[move].highlightMove();
        }
    }

    public Tile getTileFromPosition(int position) {
        return tiles[position];
    }

    public Tile[] getTiles() {
        return tiles;
    }
}
