#include "movegen.h"
#include <algorithm>
#include <iostream>

using namespace std;

/**
 * Recherche dichotomique dans une liste mouvement
 * pour trouver où mettre un mouvement avec un score "value"
 * dans la liste,
 * dans le but d'avoir une liste ordonnée selon les scores des mouvements
 *
 * @param begin
 *        Pointeur begin de la liste
 *
 * @param end
 *        Pointeur end de la liste
 *
 * @param value
 *        Score du mouvement à insérer dans la liste
 *
 * @return
 *        Pointeur indiquant où mettre le mouvement dans la liste
 *        A partir de ce pointeur vers la droite,
 *        On fait un decalaga à droite afin d"insérer le nouveau mouvement dans la liste
 */
inline PieceMoveSession* dichotomousSearch(PieceMoveSession* begin, PieceMoveSession* end, int value) {

    // Bien vérifier qu'on a au moins un élément dans la liste
    if (begin > end) {
        return NULL;
        // Si le dernier élement de la liste a un score inférieur à celui du mouvement à insérer
        // nul besoin de vérifier les autres éléments de la liste,
        // Dans ce cas, mettre l'élément à la end de la liste
    } else if (end->removedPiecesCount <= value) {
        return NULL;
        // Si le premier élement de la liste a un score supérieur à celui du mouvement à insérer
        // mettre l'élément au début de la liste
    } else if (begin->removedPiecesCount >= value) {
        return begin;
    }

    // Recherche dichotomique
    for (PieceMoveSession* middle; begin < end;) {
        // pointeur au milieu des pointeurs begin et end
        middle = begin + ((end - begin) >> 1);

        // ex: valeur à rechercher = 5

        // ex: 7
        if (middle->removedPiecesCount > value) {
            // ex: 6 - 7
            if ((middle - 1)->removedPiecesCount > value) {
                end = middle - 1;
                // ex:  4 - 7 ou 5 - 7
            } else {
                return middle;
            }
        } else {
            ++middle;

            // ex: 4
            if (middle->removedPiecesCount < value) {
                begin = middle + 1;
            } else {
                return middle;
            }
        }
    }

    return begin;
}

/**
 * Trier les éléments entre ]current et last]
 *
 * @param current
 * @param last
 */
inline void sortMoveList(PieceMoveSession* current, PieceMoveSession* last) {
    last->removedPiecesCount = bitCount(last->catchedPieces);

    PieceMoveSession* it = dichotomousSearch(current + 1, last - 1, last->removedPiecesCount);

    if (it != NULL) {
        PieceMoveSession tmp = *last;
        memmove(it + 1, it, sizeof(PieceMoveSession) * (last - it));
        *it = tmp;
    }
}

/**
 *
 * @param fanorona
 * @param movesList
 * @return
 */
PieceMoveSession *generate(Fanorona &fanorona, PieceMoveSession *movesList, bool addMove)
{
    PieceMoveSession* current;
    PieceMoveSession* last = movesList;

    bool first = true;
    const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();
    const bool vela = fanorona.getVela();
    Bitboard catchablePieces, movablePieces;

    if (fanorona.isMovementSessionOpened()) {
        last->originPosition    = fanorona.getSelectedPieceCurrentPosition();
        last->lastVector        = fanorona.getLastVector();
        last->lastPosition      = last->originPosition;
        last->catchedPieces     = 0;
        last->traveledPositions = fanorona.getTraveledPositions();
        last->init();
        ++last;

        current = movesList;
        first = false;
    } else {
        current = movesList - 1;
    }

    for (; current != last; ++current) {

        assert(current - movesList < MAX_MOVES_COUNT);

        if (first) {
            const Bitboard bbUs       = fanorona.getUsBitboard();
            const Bitboard bbThem     = fanorona.getThemBitboard();
            const Bitboard bbEmpty    = fanorona.getEmptyBitboard();
            const Bitboard bbOnLakaUs = bbUs & LAKA;

            bool catchPieces = false;

            int i = 0;

            const int v = bbOnLakaUs ? 8 : 4;

            if (vela) {
                catchPieces = currentPlayerBlack == fanorona.getVelaBlack();
            } else {
                for (; i < v; ++i) {
                    const Vector vector = LAKA_VECTORS[i];
                    const Bitboard movablePositions = movePosition(i < 4 ? bbUs : bbOnLakaUs, vector) & bbEmpty;

                    if (movablePositions && ((movePosition(movablePositions, vector) & bbThem) || (movePositionBack(movablePositions, vector << 1) & bbThem))) {
                        catchPieces = true;
                        break;
                    }
                }

                if (!catchPieces) {
                    i = 0;
                }
            }

            for (; i < v; ++i) {
                const Vector vector = LAKA_VECTORS[i];
                const Bitboard movablePositions = movePosition(i < 4 ? bbUs : bbOnLakaUs, vector) & bbEmpty;

                if (movablePositions) {
                    if (catchPieces) {

                        catchablePieces = movePosition(movablePositions, vector) & bbThem;
                        if (catchablePieces) {
                            movablePieces = movePositionBack(catchablePieces, vector << 1);

                            do {
                                const Bitboard position = movablePieces & -movablePieces;

                                last->originPosition    = position;
                                last->lastVector        = vector;
                                last->lastPosition      = movePosition(position, vector);
                                last->catchedPieces     = fanorona.removedPieces(position, vector, PERCUTE);
                                last->traveledPositions = position;

                                if (addMove) {
                                    last->addFirstMove(vector, PERCUTE);
                                }

                                sortMoveList(movesList - 1, last);

                                ++last;

                                movablePieces &= ~position;
                            } while (movablePieces);
                        }

                        catchablePieces = movePositionBack(movablePositions, vector << 1) & bbThem;
                        if (catchablePieces) {
                            movablePieces = movePosition(catchablePieces, vector);

                            do {
                                const Bitboard position = movablePieces & -movablePieces;

                                last->originPosition    = position;
                                last->lastVector        = vector;
                                last->lastPosition      = movePosition(position, vector);
                                last->catchedPieces     = fanorona.removedPieces(position, vector, ASPIRE);
                                last->traveledPositions = position;

                                if (addMove) {
                                    last->addFirstMove(vector, ASPIRE);
                                }

                                sortMoveList(movesList - 1, last);

                                ++last;

                                movablePieces &= ~position;
                            } while (movablePieces);
                        }
                    } else {
                        movablePieces = movePositionBack(movablePositions, vector);

                        do {
                            const Bitboard position = movablePieces & -movablePieces;

                            last->originPosition = position;
                            last->lastPosition   = movePosition(position, vector);
                            last->catchedPieces  = 0;
                            last->removedPiecesCount = 0;

                            if (addMove) {
                                last->addFirstMove(vector, PERCUTE);
                            }

                            ++last;

                            movablePieces &= ~position;
                        } while (movablePieces);
                    }
                }
            }

            if (!catchPieces || vela) {
                break;
            }

            first = false;
        } else {
            // do move
            fanorona.removePieces(current->originPosition | current->catchedPieces);
            fanorona.addPieces(current->lastPosition, currentPlayerBlack);

            const Bitboard bbUs       = current->lastPosition;
            const Bitboard bbThem     = fanorona.getThemBitboard();
            const Bitboard allowedPositions =
                    fanorona.getEmptyBitboard() & ~(current->traveledPositions | movePosition(bbUs, current->lastVector));

            const int v = bbUs & LAKA ? 8 : 4;

            for (int i = 0; i < v; ++i) {
                const Vector vector = LAKA_VECTORS[i];

                const Bitboard movablePosition = movePosition(bbUs, vector) & allowedPositions;

                if (movablePosition) {
                    if (movePosition(movablePosition, vector) & bbThem) {
                        if (addMove) {
                            // *last = *current;
                            last->moveCount = current->moveCount;
                            memmove(last->moves, current->moves, sizeof(Move) * current->moveCount);
                            last->addMove(vector, PERCUTE);
                        }

                        last->originPosition    = current->originPosition;
                        last->catchedPieces     = current->catchedPieces | fanorona.removedPieces<MODE_RIATRA>(bbUs, vector, PERCUTE);
                        last->traveledPositions = current->traveledPositions | bbUs;
                        last->lastPosition      = movablePosition;
                        last->lastVector        = vector;

                        sortMoveList(current, last);

                        ++last;
                    }

                    if (movePositionBack(movablePosition, vector << 1) & bbThem) {
                        if (addMove) {
                            // *last = *current;
                            last->moveCount = current->moveCount;
                            memmove(last->moves, current->moves, sizeof(Move) * current->moveCount);
                            last->addMove(vector, ASPIRE);
                        }

                        last->originPosition    = current->originPosition;
                        last->catchedPieces     = current->catchedPieces | fanorona.removedPieces<MODE_RIATRA>(bbUs, vector, ASPIRE);
                        last->traveledPositions = current->traveledPositions | bbUs;
                        last->lastPosition      = movablePosition;
                        last->lastVector        = vector;

                        sortMoveList(current, last);

                        ++last;
                    }
                }
            }

            // undo move
            fanorona.undoMove(current->lastPosition, current->originPosition, current->catchedPieces);
        }
    }

    return last;
}

bool uniqueMovableCatchingPiece(Fanorona &fanorona, PieceMoveSession &move) {

    const Bitboard bbUs       = move.moveCount ? move.lastPosition : fanorona.getUsBitboard();
    const Bitboard bbThem     = fanorona.getThemBitboard();
    const Bitboard bbOnLakaUs = bbUs & LAKA;
    // position vide
    // position non traversée
    // ne pas se déplacer avec le même dernier vecteur
    const Bitboard allowedPositions =
            fanorona.getEmptyBitboard() & ~(move.traveledPositions | (move.moveCount ? movePosition(bbUs, move.lastVector) : 0));

    const int v = bbOnLakaUs ? 8 : 4;

    int moveVector;
    Bitboard movablePiece = 0;
    Bitboard catchablePieces;
    bool percute;

    for (int i = 0; i < v; ++i) {
        const Vector vector = LAKA_VECTORS[i];

        const Bitboard movablePositions = movePosition(i < 4 ? bbUs : bbOnLakaUs, vector) & allowedPositions;

        if (movablePositions) {
            catchablePieces = movePosition(movablePositions, vector) & bbThem;

            if (catchablePieces) {
                // s'il a déjà pu éliminer des pièces avant avec un autre vecteur
                // on annule
                if (movablePiece) {
                    return false;
                    // s'il y a plusieurs pièces pui peuvent éliminer des pièces adverses
                    // on annule
                } else if (catchablePieces & (catchablePieces - 1)) {
                    return false;
                } else {
                    movablePiece = movePositionBack(catchablePieces, vector << 1);
                    percute = true;
                    moveVector = vector;
                }
            }

            catchablePieces = movePositionBack(movablePositions, vector << 1) & bbThem;

            if (catchablePieces) {
                // s'il a déjà pu éliminer des pièces avant avec un autre vecteur
                // on annule
                if (movablePiece) {
                    return false;
                    // s'il y a plusieurs pièces pui peuvent éliminer des pièces adverses
                    // on annule
                } else if (catchablePieces & (catchablePieces - 1)) {
                    return false;
                } else {
                    movablePiece = movePosition(catchablePieces, vector);
                    percute = false;
                    moveVector = vector;
                }
            }
        }
    }

    if (movablePiece) {
        if (move.moveCount == 0) {
            move.originPosition = movablePiece;
            move.catchedPieces = 0;
            move.traveledPositions = 0;
        }

        const Bitboard removedPieces = fanorona.removedPieces(movablePiece, moveVector, percute);
        const Bitboard lastPosition = movePosition(movablePiece, moveVector);

        move.lastVector         = moveVector;
        move.lastPosition       = lastPosition;
        move.catchedPieces     |= removedPieces;
        move.traveledPositions |= movablePiece;

        move.addMove(moveVector, percute);

        // do move
        fanorona.removePieces(movablePiece | move.catchedPieces);
        fanorona.addPieces(lastPosition, fanorona.getCurrentPlayerBlack());

        bool moveSequenceOver = uniqueMovableCatchingPiece(fanorona, move);

        // undo move
        fanorona.undoMove(lastPosition, movablePiece, removedPieces);

        return moveSequenceOver;
    }

    return true;
}

/**
 *
 * @param fanorona
 * @param movesList
 * @return
 */
PieceMoveSessionBack *generateBack(Fanorona &fanorona, PieceMoveSessionBack *movesList, int maxPiecesThem)
{

    PieceMoveSessionBack* current;
    PieceMoveSessionBack* last = movesList;

    bool first = true;
    const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();
    const bool vela = fanorona.getVela();
    Bitboard catchablePieces, movablePieces;

    for (current = movesList - 1; current != last; ++current) {

        assert(current - movesList < MAX_MOVES_COUNT);

        const Bitboard bbUs       = fanorona.getUsBitboard();
        const Bitboard bbThem     = fanorona.getThemBitboard();
        const Bitboard bbEmpty    = fanorona.getEmptyBitboard();
        const Bitboard bbOnLakaUs = bbUs & LAKA;

        const int v = bbOnLakaUs ? 8 : 4;

        for (int i = 0; i < v; ++i) {
            const Vector vector = LAKA_VECTORS[i];
            const Bitboard movablePositions =
                    movePosition(i < 4 ? bbUs : bbOnLakaUs, vector) & bbEmpty;

            movablePieces = movePositionBack(movablePositions, vector);

            do {
                const Bitboard position = movablePieces & -movablePieces;


                // A. move piece only

                last->originPosition    = position;
                last->lastVector        = vector;
                last->lastPosition      = movePosition(position, vector);
                last->addedPieces       = 0;
                last->traveledPositions = position;
                last->addFirstMove(vector, PERCUTE);


                // B. move piece and add removed piece (percute)

                ++last;

                last->originPosition    = position;
                last->lastVector        = vector;
                last->lastPosition      = movePosition(position, vector);
                last->addedPieces       = movePosition(last->lastPosition, vector) & bbEmpty;
                last->traveledPositions = position;
                last->addFirstMove(vector, PERCUTE);


                // C. move piece and add removed piece (aspire)

                ++last;

                last->originPosition    = position;
                last->lastVector        = vector;
                last->lastPosition      = movePosition(position, vector);
                last->addedPieces       = movePositionBack(position, vector) & bbEmpty;
                last->traveledPositions = position;
                last->addFirstMove(vector, PERCUTE);


                movablePieces &= ~position;
            } while (movablePieces);
        }
    }

    return last;
}