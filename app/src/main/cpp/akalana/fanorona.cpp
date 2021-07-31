#include "fanorona.h"

Fanorona::Fanorona() :

    lastVector(NULL_VECTOR),

    blackBitboard(INITIAL_BLACK_POSITION),
    whiteBitboard(INITIAL_WHITE_POSITION),
    emptyBitboard(FRAME & ~(blackBitboard | whiteBitboard)),

    traveledPositions(0ull),
    selectedPieceCurrentPosition(0ull),

    firstPlayerBlack(true),
    currentPlayerBlack(firstPlayerBlack),
    velaBlack(true),

    mode(MODE_RIATRA)
{}

Fanorona::Fanorona(const Fanorona &fanorona) :

    lastVector(fanorona.lastVector),

    blackBitboard(fanorona.blackBitboard),
    whiteBitboard(fanorona.whiteBitboard),
    emptyBitboard(fanorona.emptyBitboard),

    traveledPositions           (fanorona.traveledPositions),
    selectedPieceCurrentPosition(fanorona.selectedPieceCurrentPosition),

    firstPlayerBlack  (fanorona.firstPlayerBlack),
    currentPlayerBlack(fanorona.currentPlayerBlack),
    velaBlack(fanorona.velaBlack),

    mode(fanorona.mode)

{}

bool Fanorona::gameOver() const {
    const bool isVela = getVela();
    // si ce n'est pas en mode Vela
    if (!isVela) {
        // on vérifie s'il ne reste plus aucune pièce pour le joueur courant
        return blackBitboard == 0 || whiteBitboard == 0;

        // sinon
    } else {

        const Bitboard
                bitboardUs = currentPlayerBlack ? blackBitboard : whiteBitboard,
                bitboardThem = currentPlayerBlack ? whiteBitboard : blackBitboard;

        for (int i = 0; i < 8; ++i) {
            const int vector = LAKA_VECTORS[i];

            const Bitboard vectorMovablePositions =
                    movePosition(i < 4 ? bitboardUs : bitboardUs & LAKA, vector) & emptyBitboard;

            if (vectorMovablePositions) {
                if (
                    velaBlack != currentPlayerBlack ||
                    ((movePosition(vectorMovablePositions, vector)) & bitboardThem) != 0 ||
                    ((movePositionBack(vectorMovablePositions, vector << 1)) & bitboardThem) != 0
                ) {
                    return false;
                }
            }
        }
    }

    // sinon, alors le joueur courant est gagnant
    return true;
}
/*
bool Fanorona::checkCurrentPieceCanMove(const int lastVector) {
    Bitboard newPosition;
    // catchPosition,
    const Bitboard bitboardThem = currentPlayerBlack ? whiteBitboard : blackBitboard;

    const int length = (selectedPieceCurrentPosition & LAKA ? 8 : 4);
    const Vector* vectors = (selectedPieceCurrentPosition & LAKA ? LAKA_VECTORS : FOINA_VECTORS);

    for (int i = 0; i < length; ++i) {
        const Vector vector = vectors[i];

        if (
            vector != lastVector
            // la prochaine nouvelle position de la pièce
            // si cette position n'a pas été encore parcourue
            // et s'il n'y a pas de pièce à cette position
            && ((newPosition = movePosition(selectedPieceCurrentPosition, vector)) & emptyBitboard & ~traveledPositions) != 0l
            && (
                ((movePosition(newPosition,               vector) & bitboardThem) != 0l) ||
                ((movePositionBack(selectedPieceCurrentPosition, vector) & bitboardThem) != 0l)
            )
        ) {
            return true;
        }
    }

    return false;
}
*/
bool Fanorona::piecesMenaced()
{
    const Bitboard
        bitboardUs        = currentPlayerBlack ? blackBitboard : whiteBitboard,
        bitboardThem      = currentPlayerBlack ? whiteBitboard : blackBitboard;

    for (int i = 0; i < 8; ++i) {
        const Vector vector = LAKA_VECTORS[i];

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

        // pièces percutées
        if (movePosition(vectorMovablePositionsUs, vector) & bitboardThem) {
            return true;
        }

        // pièces aspirées
        if (movePositionBack(vectorMovablePositionsUs, vector << 1) & bitboardThem) {
            return true;
        }

    }

    return false;
}

std::string Fanorona::toString() const {
    std::string result = "";

    for (Bitboard position = ZARAN_DAKA_1; position <= ZARAN_DAKA_4; position <<= 1) {
        if (blackBitboard & position) {
            result += " B(" + std::to_string(getXPosition(position)) + ", " + std::to_string(getYPosition(position)) + ") ";
        } else if (whiteBitboard & position) {
            result += " W(" + std::to_string(getXPosition(position)) + ", " + std::to_string(getYPosition(position)) + ") ";
        } else if (position & LAKA) {
            result += "    *    ";
        } else {
            result += "    +    ";
        }

        if (position & FILE_9) {
            result += "\n";
            position <<= 2;
        }
    }

    return result;
}
