package servlets;

import java.util.Arrays;

/**
 * @author John Lima
 */
public class Board {

    private int TURN = 0;
    public static int BQUEEN = 0, BKING = 1, BBISHOP = 2, BKNIGHT = 3, BROOK = 4, BPAWN = 5;
    public static int WQUEEN = 6, WKING = 7, WBISHOP = 8, WKNIGHT = 9, WROOK = 10, WPAWN = 11, EMPTY = 12;
    public static int BPOINTS = 0, WPOINTS = 0;
    public final int BLACKPAWNSTARTINGROW = 1, WHITEPAWNSTARTINGROW = 6;
    public int BLACK_TURN = 1, WHITE_TURN = 0;

    private int[][] BOARD = {
        {BROOK, BKNIGHT, BBISHOP, BQUEEN, BKING, EMPTY, BKNIGHT, BROOK},
        {BPAWN, BPAWN, BPAWN, BPAWN, BPAWN, WPAWN, WPAWN, WPAWN},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
        {WPAWN, WPAWN, WPAWN, WPAWN, WPAWN, BPAWN, BPAWN, BPAWN},
        {WROOK, WKNIGHT, WBISHOP, WQUEEN, WKING, EMPTY, WKNIGHT, EMPTY}};

    public void movePiece(int ox, int oy, int dx, int dy) {
        boolean ok = false;
        // Peças Brancas
        if (TURN == WHITE_TURN) {
            if (BOARD[ox][oy] == WPAWN) {
                if (isPawnMovePossible(ox, oy, dx, dy, WPAWN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WROOK) {
                if (isXYMovePossible(ox, oy, dx, dy, WROOK)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WKING) {
                if (isKingMovePossible(ox, oy, dx, dy, WKING)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WQUEEN) {
                if (isQueenMovePossible(ox, oy, dx, dy, WQUEEN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WBISHOP) {
                if (isDiagonalMovePossible(ox, oy, dx, dy, WBISHOP)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == WKNIGHT) {
                if (isKnightMovePossible(ox, oy, dx, dy, WKNIGHT)) {
                    ok = true;
                }
            }
        }
        // Peças Pretas
        if (TURN == BLACK_TURN) {
            // Peão
            if (BOARD[ox][oy] == BPAWN) {
                if (isPawnMovePossible(ox, oy, dx, dy, BPAWN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BROOK) { // Torre
                if (isXYMovePossible(ox, oy, dx, dy, BROOK)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BKING) {
                if (isKingMovePossible(ox, oy, dx, dy, BKING)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BQUEEN) {
                if (isQueenMovePossible(ox, oy, dx, dy, BQUEEN)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BBISHOP) {
                if (isDiagonalMovePossible(ox, oy, dx, dy, BBISHOP)) {
                    ok = true;
                }
            } else if (BOARD[ox][oy] == BKNIGHT) {
                if (isKnightMovePossible(ox, oy, dx, dy, BKNIGHT)) {
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
                        return false; // Só retorna quando há um obstáculo
                    }
                }
            } else if (dy < oy) {  // Negativo
                for (int i = oy - 1; i > dy; i--) {
                    // Só imprime quando dá erro
                    if (BOARD[ox][i] != EMPTY) {
                        return false; // Só retorna quando há um obstáculo
                    }
                }
            }
            return checkDestiny(ox, oy, dx, dy, piece);
        } else if (oy == dy) { // Vertical
            if (dx > ox) { // Positivo
                for (int i = ox + 1; i < dx; i++) {
                    // Só imprime quando dá erro
                    if (BOARD[i][oy] != EMPTY) {
                        return false; // Só retorna quando há um obstáculo
                    }
                }
            } else if (dx < ox) { // Negativo
                for (int i = ox - 1; i > dx; i--) {
                    if (BOARD[i][oy] != EMPTY) {
                        return false; // Só retorna quando há um obstáculo
                    }
                }
            }
            return checkDestiny(ox, oy, dx, dy, piece);
        }
        return false;
    }

    // Diagonal Move: Bispo, Rainha
    private boolean isDiagonalMovePossible(int ox, int oy, int dx, int dy, int piece) {
        int direction = -1;
        System.out.println("Diagonal");
        if (dx > ox) { // Positivo
            if (dy > oy) { // Direita
                System.out.println("Positivo - Direita");
                direction = 0;
                return isDiagonalPathValid(ox, oy, dx, dy, piece, direction);
            } else if (dy < oy) { // Esquerda
                System.out.println("Positivo - Esquerda");
                direction = 1;
                return isDiagonalPathValid(ox, oy, dx, dy, piece, direction);
            }
        } else if (dx < ox) { // Negativo
            System.out.println("Negativo");
            if (dy > oy) { // Direita
                System.out.println("Negativo - Direita");
                direction = 2;
                return isDiagonalPathValid(ox, oy, dx, dy, piece, direction);
            } else if (dy < oy) { // Esquerda
                System.out.println("Negativo - Esquerda");
                direction = 3;
                return isDiagonalPathValid(ox, oy, dx, dy, piece, direction);
            }
        }
        return false;
    }

    // Movimentos de uma casa
    private boolean isKingMovePossible(int ox, int oy, int dx, int dy, int piece) {
        if ((piece == WKING || piece == BKING) && (ox == dx - 1 && oy == dy || ox == dx + 1 && oy == dy || ox == dx && oy == dy - 1 || ox == dx && oy == dy + 1 || ox == dx - 1 && oy == dy - 1 || ox == dx - 1 && oy == dy + 1 || ox == dx + 1 && oy == dy - 1 || ox == dx + 1 && oy == dy + 1)) {
            return checkDestiny(ox, oy, dx, dy, piece);
        }
        return false;
    }

    private boolean isQueenMovePossible(int ox, int oy, int dx, int dy, int piece) {
        return isXYMovePossible(ox, oy, dx, dy, piece) || isDiagonalMovePossible(ox, oy, dx, dy, piece);
    }

    private boolean isPawnMovePossible(int ox, int oy, int dx, int dy, int piece) {
        boolean ok = false;
        // Movimento de um Peão
        if ((ox == WHITEPAWNSTARTINGROW && dy == oy && (dx == 5 && isEmpty(dx, dy) || dx == 4 && isEmpty(dx, dy) && isEmpty(dx + 1, dy))) || (dx == ox - 1 && dy == oy && isEmpty(dx, dy)) || ((ox == BLACKPAWNSTARTINGROW && dy == oy && (dx == 2 && isEmpty(dx, dy) || dx == 3 && isEmpty(dx, dy) && isEmpty(dx - 1, dy))) || (dx == ox + 1 && dy == oy && isEmpty(dx, dy)))) {
            System.out.println("Mov Peao");
            move(ox, oy, dx, dy);
            if (!isPieceBlack(piece) && dx == 0) {
                System.out.println("Rainha Branca");
                BOARD[dx][dy] = WQUEEN;
            } else if (dx == 7) {
                System.out.println("Rainha Preta");
                BOARD[dx][dy] = BQUEEN;
            }
            ok = true;
        } // Peão come peça
        else if ((dx == ox - 1 && (dy == oy + 1 || dy == oy - 1) && isEnemy(piece, dx, dy)) || (dx == ox + 1 && (dy == oy - 1 || dy == oy + 1) && isEnemy(piece, dx, dy))) {
            System.out.println("Rainha Branca");
            eat(ox, oy, dx, dy, piece);
            if (!isPieceBlack(piece) && dx == 0) {
                System.out.println("Rainha Branca");
                BOARD[dx][dy] = WQUEEN;
            } else if (dx == 7) {
                System.out.println("Rainha Preta");
                BOARD[dx][dy] = BQUEEN;
            }
            ok = true;
        }
        return ok;
    }

    private boolean isDiagonalPathValid(int ox, int oy, int dx, int dy, int piece, int direction) {
        System.out.println("Direction: " + direction);
        switch (direction) {
            case 0:
                // Positive-Right
                if (dx - ox == dy - oy) {
                    // Percorre o caminho e verifica se tem obstáculo, se tiver não move
                    for (int row = ox + 1; row <= dx - 1;) {
                        for (int col = oy + 1; col <= dy - 1; col++, row++) {
                            System.out.println("PATH: [" + row + ", " + col + "]");
                            if (!isEmpty(row, col)) {
                                return false; // Só retorna quando há obstáculo
                            }
                        }
                    }
                    return checkDestiny(ox, oy, dx, dy, piece);
                }
                break;
            case 1:
                // Positive-Left
                if (dx - ox == oy - dy) {
                    for (int row = ox + 1; row <= dx - 1;) {
                        for (int col = oy - 1; col >= dy + 1; col--, row++) {
                            System.out.println("PATH: [" + row + ", " + col + "]");
                            if (!isEmpty(row, col)) {
                                return false; // Só retorna quando há obstáculo
                            }
                        }
                    }
                    return checkDestiny(ox, oy, dx, dy, piece);
                }
                break;
            case 2:
                // Negative-Right
                if (ox - dx == dy - oy) {
                    for (int row = ox - 1; row >= dx + 1;) {
                        for (int col = oy + 1; col <= dy - 1; col++, row--) {
                            System.out.println("PATH: [" + row + ", " + col + "]");
                            if (!isEmpty(row, col)) {
                                return false; // Só retorna quando há obstáculo
                            }
                        }
                    }
                    return checkDestiny(ox, oy, dx, dy, piece);
                }
                break;
            case 3:
                // Negative-Left
                if (ox - dx == oy - dy) {
                    for (int row = ox - 1; row >= dx + 1;) {
                        for (int col = oy - 1; col >= dy + 1; col--, row--) {
                            System.out.println("PATH: [" + row + ", " + col + "]");
                            if (!isEmpty(row, col)) {
                                return false; // Só retorna quando há obstáculo
                            }
                        }
                    }
                    return checkDestiny(ox, oy, dx, dy, piece);
                }
                break;
            default:
                break;
        }
        return false;
    }

    private boolean isKnightMovePossible(int ox, int oy, int dx, int dy, int piece) {
        int direction = -1;
        if (dx > ox) {
            if (dy > oy) { // Direita
                System.out.println("Positivo - Direita");
                direction = 0;
                return isKnightPathValid(ox, oy, dx, dy, piece, direction);
            } else if (dy < oy) { // Esquerda
                System.out.println("Positivo - Esquerda");
                direction = 1;
                return isKnightPathValid(ox, oy, dx, dy, piece, direction);
            }
        } else if (dx < ox) {
            if (dy > oy) { // Direita
                System.out.println("Negativo - Direita");
                direction = 2;
                return isKnightPathValid(ox, oy, dx, dy, piece, direction);
            } else if (dy < oy) { // Esquerda
                System.out.println("Negativo - Esquerda");
                direction = 3;
                return isKnightPathValid(ox, oy, dx, dy, piece, direction);
            }
        }
        return false;
    }

    private boolean isKnightPathValid(int ox, int oy, int dx, int dy, int piece, int direction) {
        if ((dx - ox == 1 && dy - oy == 2) || (dx - ox == 2 && dy - oy == 1) || (dx - ox == 1 && oy - dy == 2) || (dx - ox == 2 && oy - dy == 1) || (ox - dx == 1 && oy - dy == 2) || (ox - dx == 2 && oy - dy == 1) || (ox - dx == 1 && dy - oy == 2) || (ox - dx == 2 && dy - oy == 1)) {
            return checkDestiny(ox, oy, dx, dy, piece);
        }
        return false;
    }

    private boolean checkDestiny(int ox, int oy, int dx, int dy, int piece) {
        if (isEmpty(dx, dy)) { // Destino Vazio
            move(ox, oy, dx, dy);
            return true;
        } else if (isEnemy(piece, dx, dy)) { // É inimigo?
            move(ox, oy, dx, dy);
            addPoint(piece);
            return true;
        }
        return false;
    }

    public void printTabuleiro() {
        for (int[] linha : BOARD) {
            System.out.println(Arrays.toString(linha));
        }
    }

    private boolean isEmpty(int dx, int dy) {
        return BOARD[dx][dy] == EMPTY;
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

    private boolean isPieceWhite(int piece) {
        return piece > 5 && piece < EMPTY;
    }

    private boolean isEnemy(int piece, int dx, int dy) {
        return (isPieceBlack(BOARD[dx][dy]) && isPieceWhite(piece)) ? true : ((BOARD[dx][dy] > 5 && BOARD[dx][dy] < EMPTY) && (piece > 5 && piece < EMPTY) ? false : ((BOARD[dx][dy] > 5 && BOARD[dx][dy] < EMPTY) && piece <= 5) ? true : (BOARD[dx][dy] <= 5 && piece <= 5) ? false : false);
    }

//    private boolean isEnemy(int origin, int destiny) {
//        return isPieceBlack(destiny) && isPieceWhite(origin) ? true : isPieceWhite(destiny) && isPieceWhite(destiny) ? false : isPieceWhite(destiny) && isPieceBlack(origin) ? true : isPieceBlack(destiny) && isPieceBlack(origin) ? false : false;
//    }

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
}
