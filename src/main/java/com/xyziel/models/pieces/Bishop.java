package com.xyziel.models.pieces;

import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

public class Bishop extends Piece {

    private ImageView img;

    public Bishop(PieceColor color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_BISHOP.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
    }

    @Override
    public ImageView getImg() {
        return img;
    }
}
