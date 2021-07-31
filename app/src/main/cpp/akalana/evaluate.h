#ifndef EVALUATE_H
#define EVALUATE_H

#include "fanorona.h"


class Evaluate
{
public:
    Evaluate();
    int evaluateScene(const Fanorona& scene);
    void print();

    void setGamePhase(const GamePhase gamePhase) {
        this->gamePhase = gamePhase;
    }

public:
    static int distance(int i, int j, int k, int l);

    GamePhase gamePhase = END_GAME;

    // PIECES


    Bitboard
    //uniqueThreatenedBlockedThem,

            bitboardUs,
            bitboardThem,
            bitboardUsOnLaka,
            bitboardThemOnLaka,
            emptyBitboard,

            threatenedBlockedUs,
            threatenedBlockedThem,

            threatenedUs,
            threatenedThem,

            threatenedThreatUs,
            threatenedThreatThem,

            blockedOnBorderUs,
            blockedOnBorderThem,

            blockedOnBorderProtectedUs,
            blockedOnBorderProtectedThem,

            mobileOnCornerUs,
            mobileOnCornerThem,

            mobileOnBorderUs,
            mobileOnBorderThem,

            mobileToBorderUs,
            mobileToBorderThem,

            blockedOnSemiCenterUs,
            blockedOnSemiCenterThem,

            blockedOnCenterUs,
            blockedOnCenterThem,

            allThreatenedUs,
            allThreatenedThem,

            allBlockedPiecesBitboardUs,
            allBlockedPiecesBitboardThem,

            allBlockedThreatPiecesBitboardUs,
            allBlockedThreatPiecesBitboardThem,

            occupationPositionsBitboardUs,
            occupationPositionsBitboardThem,

            movablePositionsBitboardUs,
            movablePositionsBitboardThem,

            threatingPiecesBitboardUs,
            threatingPiecesBitboardThem,

            mobileOnBorderMoreThem = 0,
            threatenedBlockedMoreThem = 0;


    int
            piecesValueUs,
            piecesValueThem,

            positionsValueUs,
            positionsValueThem,

            occupationValueUs,
            occupationValueThem,

            movableValueUs,
            movableValueThem,

            threatenedBlockedValueUs,
            threatenedBlockedValueThem,

            threatenedValueUs,
            threatenedValueThem,

            threatenedThreatValueUs,
            threatenedThreatValueThem,

            blockedOnBorderValueUs,
            blockedOnBorderValueThem,

            blockedOnBorderProtectedValueUs,
            blockedOnBorderProtectedValueThem,

            mobileOnCornerValueUs,
            mobileOnCornerValueThem,

            mobileOnBorderValueUs,
            mobileOnBorderValueThem,

            mobileToBorderValueUs,
            mobileToBorderValueThem,

            blockedOnSemiCenterValueUs,
            blockedOnSemiCenterValueThem,

            blockedOnCenterValueUs,
            blockedOnCenterValueThem,

            blockedValueUs,
            blockedValueThem,

            approcheValue,

            maxThreatValueUs,
            maxThreatValueThem,

            mobileOnBorderMoreValueThem,
            threatenedBlockedMoreValueThem,

            movablePositionsCountUs,
            movablePositionsCountThem;

    int
            scoreUs,
            scoreThem,

            bonusUs,
            bonusThem,

            score;


    Bitboard threatingBitboardPerVectorUs[2][8];
    Bitboard threatingBitboardPerVectorThem[2][8];

    int APPROCHE_DISTANCES_SCORES[54][54];

    int
            approchePositionsUs[22],
            approchePositionsThem[22];
};

inline int Evaluate::distance(int i, int j, int k, int l) {
    if (i == k && j == l)
        return 0;

    int deltaX = i > k ? i - k : k - i,
            deltaY = j > l ? j - l : l - j,
            result = deltaX > deltaY ? deltaX : deltaY;

    return deltaX == deltaY && (i ^ j) ? result + 1 : result;
}

#endif // EVALUATE_H
