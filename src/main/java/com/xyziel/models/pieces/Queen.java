package com.xyziel.models.pieces;

import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

public class Queen extends Piece {

    private ImageView img;

    public Queen(PieceColor color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_QUEEN.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
    }

    @Override
    public ImageView getImg() {
        return img;
    }
}
