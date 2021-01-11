package com.xyziel.controllers;

import com.xyziel.models.pieces.Color;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class Controller {

    @FXML
    public GridPane grid;

    private Board board;

    public void initialize() {
        board = new Board(Color.WHITE);
        board.renderBoard();

        grid.getChildren().add(board);
    }

    public void newGame() {
        grid.getChildren().remove(0);

        Color color = board.getColorToMove();

        board = new Board(color);
        board.renderBoard();

        grid.getChildren().add(board);
    }

    public void changeSide() {
        grid.getChildren().remove(0);

        Color color = board.getColorToMove() == Color.WHITE ? Color.BLACK : Color.WHITE;

        board = new Board(color);
        board.renderBoard();

        grid.getChildren().add(board);
    }
}
