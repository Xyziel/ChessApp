package com.xyziel.models;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {

    public Board() {
        this.setStyle("-fx-border-color: black");
    }

    public void renderBoard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                this.add(new Tile((i+j) % 2 == 0 ? "white" : "#473515", i + 8*j), i, j);
            }
        }
    }
}
