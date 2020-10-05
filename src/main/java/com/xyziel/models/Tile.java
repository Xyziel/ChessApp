package com.xyziel.models;

import com.xyziel.models.pieces.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

public class Tile extends Label {

    private Piece piece;
    private int position;
    private Color tileColor;


    public Tile(String color, int position) {
        this.position = position;
        this.setHeight(80.0);
        this.setWidth(80.0);
        this.setStyle("-fx-background-color : " + color + ";");
        setPiece(position);

        if(color.equals("white")) {
            tileColor = Color.WHITE;
        } else {
            tileColor = Color.BLACK;
        }

        this.setOnMouseClicked(e -> {
            displayInfo();
        });
    }

    public void setPiece(int position) {
        switch (position) {
            case 0: case 7:
                piece = new Rook(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 1: case 6:
                piece = new Knight(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 2: case 5:
                piece = new Bishop(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 3:
                piece = new Queen(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 4:
                piece = new King(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15:
                piece = new Pawn(Color.BLACK);
                this.setGraphic(piece.getImg());
                break;
            case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
                piece = new Pawn(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            case 56: case 63:
                piece = new Rook(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            case 57: case 62:
                piece = new Knight(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            case 58: case 61:
                piece = new Bishop(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            case 59:
                piece = new Queen(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            case 60:
                piece = new King(Color.WHITE);
                this.setGraphic(piece.getImg());
                break;
            default:
                try {
                    this.setGraphic(new ImageView(getClass().getResource("/images/transparent.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    System.out.println("Can't load an img");
                }
                break;
        }
    }

    public void displayInfo() {
        System.out.println(this.tileColor);
        System.out.println(piece);
    }
}
