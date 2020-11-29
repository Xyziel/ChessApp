package com.xyziel.models;

import com.xyziel.models.pieces.*;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class PromotionWindow extends Dialog<Piece> {

    private Piece selectedFigure;

    public PromotionWindow(Color color) {
        setTitle("Pawn promotion");
        setResultConverter(f -> selectedFigure);
        HBox hbox = new HBox();
        hbox.getChildren().add(new ImageLabel(new Queen(color)));
        hbox.getChildren().add(new ImageLabel(new Rook(color, true)));
        hbox.getChildren().add(new ImageLabel(new Bishop(color)));
        hbox.getChildren().add(new ImageLabel(new Knight(color)));
        getDialogPane().setContent(hbox);
    }

    private class ImageLabel extends Label {

        Piece figure;

        ImageLabel(Piece figure) {
            setGraphic(figure.getImg());
            this.figure = figure;
            setOnMouseReleased(this::onMouseReleased);
        }

        private void onMouseReleased(MouseEvent e) {
            selectedFigure = figure;
            getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            close();
            e.consume();
        }
    }

    public Piece getSelectedFigure() {
        return selectedFigure;
    }
}
