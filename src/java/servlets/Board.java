package servlets;

import java.util.Arrays;

/**
 * @author John
 */
public class Board {

    private int TURN = 0;
    private int[][] BOARD = {
        {4, 3, 2, 0, 1, 2, 3, 4},
        {5, 5, 5, 5, 5, 5, 5, 5},
        {12, 12, 12, 12, 12, 12, 12, 12},
        {12, 12, 12, 12, 12, 12, 12, 12},
        {12, 12, 12, 12, 12, 12, 12, 12},
        {12, 12, 12, 12, 12, 12, 12, 12},
        {11, 11, 11, 11, 11, 11, 11, 11},
        {10, 9, 8, 6, 7, 8, 9, 10}};

    public static int BQUEEN = 0, BKING = 1, BKNIGTH = 2, BBISHOP = 3, BROOK = 4, BPAWN = 5;
    public static int WQUEEN = 6, WKING = 7, WKNIGTH = 8, WBISHOP = 9, WROOK = 10, WPAWN = 11, EMPTY = 12;
    public static int BPOINTS = 0, WPOINTS = 0;
    public final int BLACKPAWNSTARTINGROW = 1, WHITEPAWNSTARTINGROW = 6;
    public int BLACK = 1, WHITE = 0;

    public void movePiece(int ox, int oy, int dx, int dy) {
        boolean ok = false;
        // Peças Brancas
        if (TURN == WHITE) {
            if (BOARD[ox][oy] == WPAWN) {
                if (isPawnMovePossible(ox, oy, dx, dy, WPAWN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WROOK) {
                if (isXYMovePossible(ox, oy, dx, dy, WROOK)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WKING) {
                if (isKingStepPossible(ox, oy, dx, dy, WKING)) {
                    ok = true;
                }
            }
        }
        // Peças Pretas
        if (TURN == BLACK) {
            // Peão
            if (BOARD[ox][oy] == BPAWN) {
                if (isPawnMovePossible(ox, oy, dx, dy, BPAWN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BROOK) { // Torre
                if (isXYMovePossible(ox, oy, dx, dy, BOARD[ox][oy])) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BKING) {
                if (isKingStepPossible(ox, oy, dx, dy, BOARD[ox][oy])) {
                    ok = true;
                }
            }
        }

        if (ok) {
            TURN = (TURN + 1) % 2;
        }
    }

    // XY Move: Torre, Rainha
    private boolean isXYMovePossible(int ox, int oy, int dx, int dy, int piece) {
        if (ox == dx) { // Horizontal
            if (dy > oy) { // Positivo
                for (int i = oy + 1; i < dy; i++) {
                    // Só imprime quando dá erro
                    if (BOARD[ox][i] != EMPTY) {
                        System.out.println(BOARD[ox][i]);
                        return false;
                    }
                }
            } else if (dy < oy) {  // Negativo
                System.out.println("É Movimento Horizontal Negativo");
                for (int i = oy - 1; i > dy; i--) {
                    // Só imprime quando dá erro
                    if (BOARD[ox][i] != EMPTY) {
                        System.out.println(BOARD[ox][i]);
                        return false;
                    }
                }
            }

            if (BOARD[dx][dy] == EMPTY) { // Destino Vazio
                System.out.println("Destino Vazio");
                move(ox, oy, dx, dy);
                return true;
            } else if (isEnemy(piece, dx, dy)) { // É inimigo?
                move(ox, oy, dx, dy);
                addPoint(piece);
                return true;
            }
        } else if (oy == dy) { // Vertical
            if (dx > ox) { // Positivo
                System.out.println("É Movimento Vertical Positivo");
                for (int i = ox + 1; i < dx; i++) {
                    // Só imprime quando dá erro
                    if (BOARD[i][oy] != EMPTY) {
                        System.out.println(BOARD[i][oy]);
                        return false;
                    }
                }
            } else if (dx < ox) { // Negativo
                System.out.println("É Movimento Vertical Negativo");
                for (int i = ox - 1; i > dx; i--) {
                    // Só imprime quando dá erro
                    if (BOARD[i][oy] != EMPTY) {
                        System.out.println(BOARD[i][oy]);
                        return false;
                    }
                }
            }
            if (BOARD[dx][dy] == EMPTY) { // Destino Vazio
                System.out.println("Destino Vazio");
                move(ox, oy, dx, dy);
                return true;
            } else if (isEnemy(piece, dx, dy)) { // É inimigo?
                move(ox, oy, dx, dy);
                addPoint(piece);
                return true;
            }
            return false;
        }
        return false;
    }

    // Diagonal Move: Bispo, Rainha
    private boolean isDiagonalMovePossible(int ox, int oy, int dx, int dy, int piece) {
        return false;
    }

    // Movimentos de uma casa
    private boolean isKingStepPossible(int ox, int oy, int dx, int dy, int piece) {
        System.out.println("iskingStepPossible");
        if ((piece == WKING || piece == BKING) && (ox == dx - 1 && oy == dy || ox == dx + 1 && oy == dy || ox == dx && oy == dy - 1 || ox == dx && oy == dy + 1 || ox == dx - 1 && oy == dy - 1 || ox == dx - 1 && oy == dy + 1 || ox == dx + 1 && oy == dy - 1 || ox == dx + 1 && oy == dy + 1)) {
            System.out.println("Inside IF");
            if (BOARD[dx][dy] == EMPTY) {
                System.out.println("Inside IF IF");
                move(ox, oy, dx, dy);
                return true;
            } else if (isEnemy(piece, dx, dy)) {
                System.out.println("Inside IF ELSEIF");
                move(ox, oy, dx, dy);
                addPoint(piece);
                return true;
            }
        }
        return false;
    }

    private boolean isEnemy(int piece, int dx, int dy) {
        return (BOARD[dx][dy] <= 5 && piece > 5 && piece < EMPTY) ? true : ((BOARD[dx][dy] > 5 && BOARD[dx][dy] < EMPTY) && (piece > 5 && piece < EMPTY) ? false : ((BOARD[dx][dy] > 5 && BOARD[dx][dy] < EMPTY) && piece <= 5) ? true : (BOARD[dx][dy] <= 5 && piece <= 5) ? false : false);
    }

    private boolean isPawnMovePossible(int ox, int oy, int dx, int dy, int piece) {
        boolean ok = false;
        // Movimento de um Peão
        if ((ox == WHITEPAWNSTARTINGROW && dy == oy && (dx == 5 && BOARD[dx][dy] == EMPTY || dx == 4 && BOARD[dx][dy] == EMPTY && BOARD[dx + 1][dy] == EMPTY)) || (dx == ox - 1 && dy == oy && BOARD[dx][dy] == EMPTY) || ((ox == BLACKPAWNSTARTINGROW && dy == oy && (dx == 2 && BOARD[dx][dy] == EMPTY || dx == 3 && BOARD[dx][dy] == EMPTY && BOARD[dx - 1][dy] == EMPTY)) || (dx == ox + 1 && dy == oy && BOARD[dx][dy] == EMPTY))) {
            if (!isPieceBlack(piece) && dx == 0) {
                BOARD[dx][dy] = WQUEEN;
            } else if (dx == 7) {
                BOARD[dx][dy] = BQUEEN;
            }
            move(ox, oy, dx, dy);
            ok = true;
        } // Peão come peça
        else if ((dx == ox - 1 && (dy == oy + 1 || dy == oy - 1) && isEnemy(piece, dx, dy)) || (dx == ox + 1 && (dy == oy - 1 || dy == oy + 1) && isEnemy(piece, dx, dy))) {
            eat(ox, oy, dx, dy, piece);
            if (!isPieceBlack(piece) && dx == 0) {
                BOARD[dx][dy] = WQUEEN;
            } else if (dx == 7) {
                BOARD[dx][dy] = BQUEEN;
            }
            ok = true;
        }
        return ok;
    }

    private boolean isEmpty(int dx, int dy) {
        return BOARD[dx][dy] == EMPTY;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(BOARD);
    }

    public static void main(String[] args) {
        Board tab = new Board();
        tab.movePiece(1, 0, 3, 0);//xy|xy
        tab.movePiece(6, 3, 5, 3);
        System.out.println(tab.BOARD[1][0]);
        tab.printTabuleiro();
    }

    public void printTabuleiro() {
        for (int[] linha : BOARD) {
            System.out.println(Arrays.toString(linha));
        }
    }

    private void move(int ox, int oy, int dx, int dy) {
        //System.out.println("("+ox+", "+oy+")"+"("+dx+", "+dy+")");
        BOARD[dx][dy] = BOARD[ox][oy];
        BOARD[ox][oy] = EMPTY;
    }

    private void eat(int ox, int oy, int dx, int dy, int piece) {
        BOARD[dx][dy] = BOARD[ox][oy];
        BOARD[ox][oy] = EMPTY;
        addPoint(piece);
    }

    public void addPoint(int piece) {
        int p = piece <= 5 ? BPOINTS++ : WPOINTS++;
    }

    public int getTURN() {
        return TURN;
    }

    private boolean isPieceBlack(int piece) {
        return piece <= 5;
    }
}