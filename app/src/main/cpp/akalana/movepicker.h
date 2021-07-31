#ifndef MOVEPICKER_H
#define MOVEPICKER_H

#include "fanorona.h"
#include "move.h"

class MovePicker
{

private:
    bool
        canCacthPiece,
        pickPercuteMoves;

    Bitboard
        movablePieces[8],
        movablePercutingPieces[8],
        movableAspiringPieces[8];

    Bitboard* currentMovablePieces;

    int pickIndex;
    Bitboard currentVectorMoves;

public:
    MovePicker(const Fanorona& scene);

    bool nextMove(PieceMove &move);
};

#endif // MOVEPICKER_H
