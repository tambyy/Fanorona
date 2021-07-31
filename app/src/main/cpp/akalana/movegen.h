#ifndef MOVEGEN_H
#define MOVEGEN_H

#include "movepicker.h"

PieceMoveSession* generate(Fanorona &fanorona, PieceMoveSession* movesList, bool addMove);
bool uniqueMovableCatchingPiece(Fanorona &fanorona, PieceMoveSession &move);

#endif // MOVEGEN_H
