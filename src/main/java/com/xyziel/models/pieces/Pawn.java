package com.xyziel.models.pieces;

import com.xyziel.controllers.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Pawn extends Piece {

    boolean hasMoved;
    private ImageView img;
    private Color pieceColor;
    private final int[] movesWhite = {
            /*moves to the top*/
            -7, -8, -9, -16 };
    private final int[] movesBlack = {
            /*moves to the bottom*/
            7, 8, 9, 16 };

    public Pawn(Color color, boolean hasMoved) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_PAWN.png").toURI().toString());
        } catch (URISyntaxException e) {
            System.out.println("Can't load an img");
        }
        this.pieceColor = color;
        setHasMoved(hasMoved);
    }

    @Override
    public ImageView getImg() {
        return img;
    }

    @Override
    public ArrayList<Integer> getPossibleMoves(int position, Board board, boolean onlyAttackedTiles) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        Tile[] tiles = board.getTiles();
        int[] moves = this.pieceColor == Color.WHITE ? movesWhite : movesBlack;
        if(moves[0] + position >= 0 && moves[0] + position <= 63 && tiles[moves[0] + position].getPiece() != null && tiles[moves[0] + position].getPiece().getPieceColor() != this.pieceColor) {
            if(pieceColor == Color.WHITE) {
                if(position % 8 != 7) {
                    if(tiles[moves[0] + position].getPiece() instanceof King) {
                        if(onlyAttackedTiles) {
                            possibleMoves.add(moves[0] + position);
                        }
                    } else {
                        possibleMoves.add(moves[0] + position);
                    }
                }
            } else {
                if(position % 8 != 0) {
                    if(tiles[moves[0] + position].getPiece() instanceof King) {
                        if(onlyAttackedTiles) {
                            possibleMoves.add(moves[0] + position);
                        }
                    } else {
                        possibleMoves.add(moves[0] + position);
                    }
                }
            }
        }
        if(moves[2] + position >= 0 && moves[2] + position <= 63 && tiles[moves[2] + position].getPiece() != null && tiles[moves[2] + position].getPiece().getPieceColor() != this.pieceColor) {
            if(pieceColor == Color.WHITE) {
                if(position % 8 != 0) {
                    if(tiles[moves[2] + position].getPiece() instanceof King) {
                        if(onlyAttackedTiles) {
                            possibleMoves.add(moves[2] + position);
                        }
                    } else {
                        possibleMoves.add(moves[2] + position);
                    }
                }
            } else {
                if(position % 8 != 7) {
                    if(tiles[moves[2] + position].getPiece() instanceof King) {
                        if(onlyAttackedTiles) {
                            possibleMoves.add(moves[2] + position);
                        }
                    } else {
                        possibleMoves.add(moves[2] + position);
                    }
                }
            }
        }
        if(moves[1] + position >= 0 && moves[1] + position <= 63 && tiles[moves[1] + position].getPiece() == null) {
            possibleMoves.add(moves[1] + position);
        }
        if(moves[3] + position >= 0 && moves[3] + position <= 63 && !hasMoved && tiles[moves[1] + position].getPiece() == null && tiles[moves[3] + position].getPiece() == null) {
            possibleMoves.add(moves[3] + position);
        }
        //En passant
        if(board.getEnpassant() + 1 == position || board.getEnpassant() - 1 == position) {
            if(pieceColor == Color.WHITE) {
                possibleMoves.add(board.getEnpassant() - 8);
            } else {
                possibleMoves.add(board.getEnpassant() + 8);
            }

        }
        return possibleMoves;
    }

    @Override
    public Color getPieceColor() {
        return pieceColor;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

}
