package com.xyziel;

import com.xyziel.controllers.Board;
import com.xyziel.models.Tile;
import com.xyziel.models.pieces.*;
import java.util.*;

public class Engine {

    private Color color;
    private int depth;

    private int[] whitePawnPositionPoints = {0,  0,  0,  0,  0,  0,  0,  0,
                                            50, 50, 50, 50, 50, 50, 50, 50,
                                            10, 10, 20, 30, 30, 20, 10, 10,
                                            5,  5, 10, 25, 25, 10,  5,  5,
                                            0,  0,  0, 20, 20,  0,  0,  0,
                                            5, -5,-10,  0,  0,-10, -5,  5,
                                            5, 10, 10,-20,-20, 10, 10,  5,
                                            0,  0,  0,  0,  0,  0,  0,  0};

    private int[] blackPawnPositionPoints = {0,  0,  0,  0,  0,  0,  0,  0,
                                             5, 10, 10,-20,-20, 10, 10, 5,
                                             5, -5,-10,  0,  0,-10, -5,  5,
                                             0,  0,  0, 20, 20,  0,  0,  0,
                                             5,  5, 10, 25, 25, 10,  5,  5,
                                             10, 10, 20, 30, 30, 20, 10, 10,
                                             50, 50, 50, 50, 50, 50, 50, 50,
                                             0,  0,  0,  0,  0,  0,  0,  0};

    private int[] whiteKnightPositionPoints = {-50,-40,-30,-30,-30,-30,-40,-50,
                                                -40,-20,  0,  0,  0,  0,-20,-40,
                                                -30,  0, 10, 15, 15, 10,  0,-30,
                                                -30,  5, 15, 20, 20, 15,  5,-30,
                                                -30,  0, 15, 20, 20, 15,  0,-30,
                                                -30,  5, 10, 15, 15, 10,  5,-30,
                                                -40,-20,  0,  5,  5,  0,-20,-40,
                                                -50,-40,-30,-30,-30,-30,-40,-50};

    private int[] blackKnightPositionPoints = {-50,-40,-30,-30,-30,-30,-40,-50,
                                                -40,-20,  0,  5,  5,  0,-20,-40,
                                                -30,  5, 10, 15, 15, 10,  5,-30,
                                                -30,  0, 15, 20, 20, 15,  0,-30,
                                                -30,  5, 15, 20, 20, 15,  5,-30,
                                                -30,  0, 10, 15, 15, 10,  0,-30,
                                                -40,-20,  0,  0,  0,  0,-20,-40,
                                                -50,-40,-30,-30,-30,-30,-40,-50};

    private int[] whiteBishopPositionPoints = {-20,-10,-10,-10,-10,-10,-10,-20,
                                                -10,  0,  0,  0,  0,  0,  0,-10,
                                                -10,  0,  5, 10, 10,  5,  0,-10,
                                                -10,  5,  5, 10, 10,  5,  5,-10,
                                                -10,  0, 10, 10, 10, 10,  0,-10,
                                                -10, 10, 10, 10, 10, 10, 10,-10,
                                                -10,  5,  0,  0,  0,  0,  5,-10,
                                                -20,-10,-10,-10,-10,-10,-10,-20};

    private int[] blackBishopPositionPoints = {-20,-10,-10,-10,-10,-10,-10,-20,
                                                -10,  5,  0,  0,  0,  0,  5,-10,
                                                -10, 10, 10, 10, 10, 10, 10,-10,
                                                -10,  0, 10, 10, 10, 10,  0,-10,
                                                -10,  5,  5, 10, 10,  5,  5,-10,
                                                -10,  0,  5, 10, 10,  5,  0,-10,
                                                -10,  0,  0,  0,  0,  0,  0,-10,
                                                -20,-10,-10,-10,-10,-10,-10,-20};

    private int[] whiteRookPositionPoints = {0,  0,  0,  0,  0,  0,  0,  0,
                                             5, 10, 10, 10, 10, 10, 10,  5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                             0,  0,  0,  5,  5,  0,  0,  0};

    private int[] blackRookPositionPoints = {0,  0,  0,  5,  5,  0,  0,  0,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            -5,  0,  0,  0,  0,  0,  0, -5,
                                            5, 10, 10, 10, 10, 10, 10,  5,
                                            0,  0,  0,  0,  0,  0,  0,  0};

    private int[] whiteQueenPositionPoints = {-20,-10,-10, -5, -5,-10,-10,-20,
                                              -10,  0,  0,  0,  0,  0,  0,-10,
                                              -10,  0,  5,  5,  5,  5,  0,-10,
                                               -5,  0,  5,  5,  5,  5,  0, -5,
                                                0,  0,  5,  5,  5,  5,  0, -5,
                                              -10,  5,  5,  5,  5,  5,  0,-10,
                                              -10,  0,  5,  0,  0,  0,  0,-10,
                                              -20,-10,-10, -5, -5,-10,-10,-20};

    private int[] blackQueenPositionPoints = {-20,-10,-10, -5, -5,-10,-10,-20,
                                                -10,  0,  5,  0,  0,  0,  0,-10,
                                                -10,  5,  5,  5,  5,  5,  0,-10,
                                                0,  0,  5,  5,  5,  5,  0, -5,
                                                -5,  0,  5,  5,  5,  5,  0, -5,
                                                -10,  0,  5,  5,  5,  5,  0,-10,
                                                -10,  0,  0,  0,  0,  0,  0,-10,
                                                -20,-10,-10, -5, -5,-10,-10,-20};

    private int[] whiteKingPositionPoints = {-30,-40,-40,-50,-50,-40,-40,-30,
                                             -30,-40,-40,-50,-50,-40,-40,-30,
                                             -30,-40,-40,-50,-50,-40,-40,-30,
                                             -30,-40,-40,-50,-50,-40,-40,-30,
                                             -20,-30,-30,-40,-40,-30,-30,-20,
                                             -10,-20,-20,-20,-20,-20,-20,-10,
                                              20, 20,  0,  0,  0,  0, 20, 20,
                                              20, 30, 10,  0,  0, 10, 30, 20};

    private int[] blackKingPositionPoints = {20, 30, 10,  0,  0, 10, 30, 20,
                                             20, 20,  0,  0,  0,  0, 20, 20,
                                            -10,-20,-20,-20,-20,-20,-20,-10,
                                            -20,-30,-30,-40,-40,-30,-30,-20,
                                            -30,-40,-40,-50,-50,-40,-40,-30,
                                            -30,-40,-40,-50,-50,-40,-40,-30,
                                            -30,-40,-40,-50,-50,-40,-40,-30,
                                            -30,-40,-40,-50,-50,-40,-40,-30};

    public Engine(Color color) {
        this.color = color;
        this.depth = 3;
    }

    public void makeAMove(HashMap<Tile, ArrayList<Integer>> moves, Board board) {
        int bestMoveEvaluation;
        if(color == Color.WHITE) {
            bestMoveEvaluation = -99999;
        } else {
            bestMoveEvaluation = 99999;
        }
        Tile bestPiece = moves.keySet().iterator().next();
        int bestMove = 0;
        boolean isWhite = color == Color.WHITE;
        for(Tile tile: moves.keySet()) {
            for(int move: moves.get(tile)) {
                Piece pieceOnMovePosition = board.getTiles()[move].getPiece();
                Piece pieceToMove = tile.getPiece();
                boolean hasMoved = true;
                if (pieceToMove instanceof King) {
                    hasMoved = ((King) pieceToMove).isHasMoved();
                } else if(pieceToMove instanceof Rook) {
                    hasMoved = ((Rook) pieceToMove).isHasMoved();
                }
                if(!(pieceOnMovePosition instanceof King)) {
                    int flag = board.tryAMove(tile, move);
                    if(flag == 1) {
                        board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                        if(color == Color.WHITE) {
                            bestMoveEvaluation = 9999999;
                        } else {
                            bestMoveEvaluation = -9999999;
                        }
                        bestPiece = tile;
                        bestMove = move;
                        break;
                    }
                    int evaluationValue = minimax(depth, board, isWhite, -100000, 100000);
                    if(color == Color.WHITE) {
                        if(evaluationValue > bestMoveEvaluation) {
                            bestMoveEvaluation = evaluationValue;
                            bestPiece = tile;
                            bestMove = move;
                        }
                    } else {
                        if(evaluationValue < bestMoveEvaluation) {
                            bestMoveEvaluation = evaluationValue;
                            bestPiece = tile;
                            bestMove = move;
                        }
                    }
                    board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                }
            }
        }
        System.out.println("Best piece: " + bestPiece.getPiece() + "    best move: " + bestMove);
        System.out.println("Evaluation: " + bestMoveEvaluation);
        board.move(bestMove, bestPiece.getPiece(), bestPiece, false);
    }

    public int minimax(int depth, Board board, boolean isWhite, int alpha, int beta) {
        if(depth == 0) {
                return evaluateBoard(board);
        }
        Color color = isWhite ? Color.WHITE : Color.BLACK;
        HashMap<Tile, ArrayList<Integer>> moves = board.getAllMovesMap(color, board.getTiles());
        if(isWhite) {
            int bestMoveEvaluation = -99999;
            for(Tile tile: moves.keySet()) {
                for(int move: moves.get(tile)) {
                    Piece pieceOnMovePosition = board.getTiles()[move].getPiece();
                    Piece pieceToMove = tile.getPiece();
                    boolean hasMoved = true;
                    if (pieceToMove instanceof King) {
                        hasMoved = ((King) pieceToMove).isHasMoved();
                    } else if(pieceToMove instanceof Rook) {
                        hasMoved = ((Rook) pieceToMove).isHasMoved();
                    }
                    if(!(pieceOnMovePosition instanceof King)) {
                        int flag = board.tryAMove(tile, move);
                        if(flag == 1) {
                            board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                            return 999999;
                        } else if(flag == 2) {
                            board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                            return 0;
                        }
                        bestMoveEvaluation = Math.max(bestMoveEvaluation, minimax(depth - 1, board, !isWhite, alpha, beta));
                        board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                        alpha = Math.max(alpha, bestMoveEvaluation);
                        if(alpha >= beta) {
                            return bestMoveEvaluation;
                        }
                    }
                }
            }
            return bestMoveEvaluation;
        } else {
            int bestMoveEvaluation = 99999;
            for(Tile tile: moves.keySet()) {
                for(int move: moves.get(tile)) {
                    Piece pieceOnMovePosition = board.getTiles()[move].getPiece();
                    Piece pieceToMove = tile.getPiece();
                    boolean hasMoved = true;
                    if (pieceToMove instanceof King) {
                        hasMoved = ((King) pieceToMove).isHasMoved();
                    } else if(pieceToMove instanceof Rook) {
                        hasMoved = ((Rook) pieceToMove).isHasMoved();
                    }
                    if(!(pieceOnMovePosition instanceof King)) {
                        int flag = board.tryAMove(tile, move);
                        if(flag == 1) {
                            board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                            return -999999;
                        } else if(flag == 2) {
                            board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                            return 0;
                        }
                        bestMoveEvaluation = Math.min(bestMoveEvaluation, minimax(depth - 1, board, !isWhite, alpha, beta));
                        board.undoMove(tile, pieceToMove, pieceOnMovePosition, move ,hasMoved);
                        beta = Math.min(beta, bestMoveEvaluation);
                        if(beta <= alpha) {
                            return bestMoveEvaluation;
                        }
                    }
                }
            }
            return bestMoveEvaluation;
        }
    }

    public int evaluateBoard(Board board) {
        int sumWhite = 0;
        int sumBlack = 0;
        for(Tile tile: board.getTiles()) {
            Piece piece = tile.getPiece();
            if(piece != null) {
                if(piece.getPieceColor() == Color.WHITE) {
                    if(piece instanceof Pawn) {
                        sumWhite += 100 + whitePawnPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Knight) {
                        sumWhite += 320 + whiteKnightPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Bishop) {
                        sumWhite += 330 + whiteBishopPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Rook) {
                        sumWhite += 500 + whiteRookPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Queen) {
                        sumWhite += 900 + whiteQueenPositionPoints[tile.getPosition()];
                    } else {
                        sumWhite += whiteKingPositionPoints[tile.getPosition()];
                    }
                } else {
                    if(piece instanceof Pawn) {
                        sumBlack += 100 + blackPawnPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Knight) {
                        sumBlack += 320 + blackKnightPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Bishop) {
                        sumBlack += 330 + blackBishopPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Rook) {
                        sumBlack += 500 + blackRookPositionPoints[tile.getPosition()];
                    } else if(piece instanceof Queen) {
                        sumBlack += 900 + blackQueenPositionPoints[tile.getPosition()];
                    } else {
                        sumBlack += blackKingPositionPoints[tile.getPosition()];
                    }
                }
            }
         }
        return sumWhite - sumBlack;
    }

    public Color getColor() {
        return color;
    }
}
