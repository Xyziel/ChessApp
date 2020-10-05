package com.xyziel.models.pieces;

import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

public class Knight extends Piece {

    private ImageView img;

    public Knight(PieceColor color) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_KNIGHT.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
    }

    @Override
    public ImageView getImg() {
        return img;
    }
}
