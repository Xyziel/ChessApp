package com.xyziel.models;

import com.xyziel.controllers.Board;
import com.xyziel.models.pieces.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Tile extends Label {

    private Piece piece;
    private Board board;
    private int position;
    private Color tileColor;


    public Tile(String color, int position, Board board) {
        this.board = board;
        this.position = position;
        this.setHeight(80.0);
        this.setWidth(80.0);
        this.setStyle("-fx-background-color : " + color + ";");
        initialPiecePositions(position);

        if(color.equals("white")) {
            tileColor = Color.WHITE;
        } else {
            tileColor = Color.BLACK;
        }

        this.setOnMouseClicked(e -> {
            highlightMoves();
        });
    }

    public void initialPiecePositions(int position) {
        switch (position) {
            case 0: case 7:
                piece = new Rook(Color.BLACK, false);
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
                piece = new King(Color.BLACK, false);
                this.setGraphic(piece.getImg());
                break;
            case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15:
                piece = new Pawn(Color.BLACK, false);
                this.setGraphic(piece.getImg());
                break;
            case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55:
                piece = new Pawn(Color.WHITE, false);
                this.setGraphic(piece.getImg());
                break;
            case 56: case 63:
                piece = new Rook(Color.WHITE, false);
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
                piece = new King(Color.WHITE, false);
                this.setGraphic(piece.getImg());
                break;
            default:
                try {
                    this.setGraphic(new ImageView(getClass().getResource("/images/TRANSPARENT.png").toURI().toString()));
                } catch (URISyntaxException e) {
                    System.out.println("Can't load an img");
                }
                break;
        }
    }

    public void displayInfo() {
    }

    public void highlightMoves() {
        if (this.piece != null) {
            ArrayList<Integer> moves = this.piece.getPossibleMoves(position, board, false);
            board.highlightMoves(moves, this);
        }
    }

    public void highlightMove() {
        if (this.tileColor == Color.WHITE) {
            this.setStyle("-fx-background-color : #949494;");
        } else {
            this.setStyle("-fx-background-color : #103828;");
        }
    }

    public void unhighlightMove() {
        if (this.tileColor == Color.WHITE) {
            this.setStyle("-fx-background-color : white;");
        } else {
            this.setStyle("-fx-background-color : #473515;");
        }
    }

    public int getPosition() {
        return this.position;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        this.setGraphic(this.piece.getImg());
        if(piece instanceof Pawn) {
            ((Pawn) piece).setHasMoved(true);
        } else if (piece instanceof King) {
            ((King) piece).setHasMoved(true);
        } else if(piece instanceof Rook) {
            ((Rook) piece).setHasMoved(true);
        }
    }

    public void clearTile() {
        this.piece = null;
        try {
            this.setGraphic(new ImageView(getClass().getResource("/images/TRANSPARENT.png").toURI().toString()));
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
    }

    public void setPieceTemporary(Piece piece) {
        this.piece = piece;
    }

    public void setPieceForEngine(Piece piece) {
        if(piece instanceof Pawn) {
            ((Pawn) piece).setHasMoved(true);
        } else if (piece instanceof King) {
            ((King) piece).setHasMoved(true);
        } else if(piece instanceof Rook) {
            ((Rook) piece).setHasMoved(true);
        }
        this.piece = piece;
    }
}

