#include "movepicker.h"

MovePicker::MovePicker(const Fanorona& scene) {

    canCacthPiece = false;
    pickPercuteMoves = false;
    pickIndex = -1;

    const bool currentPlayerBlack = scene.getCurrentPlayerBlack();
    const Vector lastVector = scene.getLastVector();

    const Bitboard
        traveledPositions = scene.getTraveledPositions(),
        emptyBitboard     = scene.getEmptyBitboard(),
        bitboardUs        = scene.isMovementSessionOpened() ? scene.getSelectedPieceCurrentPosition() : currentPlayerBlack ? scene.getBlackBitboard() : scene.getWhiteBitboard(),
        bitboardThem      = currentPlayerBlack ? scene.getWhiteBitboard() : scene.getBlackBitboard();

    // vérifier si on doit appliquer
    // la règle d'une partie VELA
    const bool mustCatchPece = !scene.getVela() || scene.getVelaBlack() == currentPlayerBlack;

    bool canAspirePiece = false;

    for (int i = 0; i < 8; ++i) {
        const Vector vector = LAKA_VECTORS[i];

        movablePieces[i] = 0;
        movablePercutingPieces[i] = 0;
        movableAspiringPieces[i] = 0;

        // 1. Ne pas se déplacer avec
        // le même vecteur précédent
        if (vector == lastVector) {
            continue;
        }

        // positions vides deplaçables
        // positions vides non traversées: ~(emptyBitboard | traveledPositions)
        const Bitboard vectorMovablePositionsUs =
                movePosition(i < 4 ? bitboardUs : bitboardUs & LAKA, vector) &
                emptyBitboard &
                ~traveledPositions;

        if (vectorMovablePositionsUs == 0) {
            continue;
        }

        if (mustCatchPece) {
            const Vector vector2 = vector << 1;

            // pièces percutées
            const Bitboard vectorPercutedPieces = movePosition(vectorMovablePositionsUs, vector) & bitboardThem;
            movablePercutingPieces[i] = movePositionBack(vectorPercutedPieces, vector2);
            if (!canCacthPiece && vectorPercutedPieces) {
                canCacthPiece = true;
                pickIndex = i;
            }

            // pièces aspirées
            const Bitboard vectorAspiredPieces = movePositionBack(vectorMovablePositionsUs, vector2) & bitboardThem;
            movableAspiringPieces[i] = movePosition(vectorAspiredPieces, vector);
            if (!canAspirePiece && vectorAspiredPieces) {
                canCacthPiece = canAspirePiece = true;
                pickIndex = i;
            }

        }

        if (!canCacthPiece) {
            movablePieces[i] = movePositionBack(vectorMovablePositionsUs, vector);
            if (pickIndex < 0) {
                pickIndex = i;
            }
        }
    }

    if (canCacthPiece && !canAspirePiece) {
        pickPercuteMoves = true;
    }

    if (pickIndex < 0) {
        pickIndex = 8;
        currentVectorMoves = 0;
    } else {
        currentMovablePieces = canCacthPiece ? (pickPercuteMoves ? movablePercutingPieces : movableAspiringPieces) : movablePieces;
        currentVectorMoves = currentMovablePieces[pickIndex];
    }
}

bool MovePicker::nextMove(PieceMove& move) {
    while (currentVectorMoves == 0) {
        if (++pickIndex >= 8) {
            if (canCacthPiece) {
                if (pickPercuteMoves) {
                    return false;
                } else {
                    pickIndex = 0;
                    pickPercuteMoves = true;
                    currentMovablePieces = movablePercutingPieces;
                }
            } else {
                return false;
            }
        }

        currentVectorMoves = currentMovablePieces[pickIndex];
    }

    const Bitboard position = currentVectorMoves & -currentVectorMoves;
    currentVectorMoves &= ~position;

    move.piecePosition = position;
    move.vector = LAKA_VECTORS[pickIndex];
    move.percute = pickPercuteMoves;

    return true;
}
