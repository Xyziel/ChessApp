package com.xyziel.controllers;

import com.xyziel.models.Board;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    private GridPane grid;

    private Board board;

    public void initialize() {
        board = new Board();
        board.renderBoard();

        grid.getChildren().add(board);
    }

}
