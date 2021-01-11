package com.xyziel.models.pieces;

import com.xyziel.controllers.Board;
import com.xyziel.models.Tile;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class King extends Piece {

    boolean hasMoved;
    private ImageView img;
    private Color pieceColor;
    private final int[] moves = {
            /*moves to the left*/
            -1,
            /*moves to the top*/
            -7, -8, -9,
            /*moves to the right*/
            1,
            /*moves to the bottom*/
            7, 8, 9 };

    public King(Color color, boolean hasMoved) {
        try {
            img = new ImageView(getClass().getResource("/images/" + color + "_KING.png").toURI().toString());
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
        for(int move: moves) {
            if((move + position >= 0) && (move + position <= 63)) {
                if(!((position % 8 == 0 && (move + position) % 8 == 7) || (position % 8 == 7 && (move + position) % 8 == 0))) {
                    if(tiles[move + position].getPiece() == null) {
                        possibleMoves.add(move + position);
                    } else if(tiles[move + position].getPiece().getPieceColor() != this.pieceColor) {
                        if(tiles[move + position].getPiece() instanceof King) {
                            if(onlyAttackedTiles) {
                                possibleMoves.add(move + position);
                            }
                        } else {
                            possibleMoves.add(move + position);
                        }
                    }
                }
            }
        }
        //castle
        if(!onlyAttackedTiles && !hasMoved) {
            //right
            Piece rook = board.getTiles()[position + 3].getPiece();
            if(rook instanceof Rook && !((Rook) rook).isHasMoved() && board.getTiles()[position + 2].getPiece() == null
                    && board.getTiles()[position + 1].getPiece() == null && !isCheck(board, position)
                    && !isCheck(board, position + 1)
                    && !isCheck(board, position + 2)) {
                possibleMoves.add(position + 2);
            }
            //left
            if(position - 4 >= 0) {
                rook = board.getTiles()[position - 4].getPiece();
            }
            if(rook instanceof Rook && !((Rook) rook).isHasMoved() && board.getTiles()[position - 3].getPiece() == null
                    && board.getTiles()[position - 2].getPiece() == null && board.getTiles()[position - 1].getPiece() == null
                    && !isCheck(board, position)
                    && !isCheck(board, position - 1)
                    && !isCheck(board, position - 2)) {
                possibleMoves.add(position - 2);
            }
        }
        return possibleMoves;
    }

    public boolean isCheck(Board board, int position) {
        Color color = pieceColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        ArrayList<Integer> moves = board.getAllAttackedTiles(color);
        //if king position is one of the moves that opponent can make then returns true;
        return moves.contains(position);
    }

    public boolean isCheckmate(Board board, Color color) {
        ArrayList<Integer> moves = board.getAllMoves(color);
        return moves.isEmpty();
    }

    public boolean isStalemate(Board board, Color color) {
        ArrayList<Integer> moves = board.getAllMoves(color);
        return moves.isEmpty();
    }

    @Override
    public Color getPieceColor() {
        return this.pieceColor;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

}
