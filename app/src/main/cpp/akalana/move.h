#ifndef MOVE_H
#define MOVE_H

#include "type.h"
#include "bitboard.h"
#include "fanorona.h"
#include "assert.h"
#include <vector>
#include <iostream>

struct Move {
public:

    /**
     * Vecteur de deplacement
     */
    Vector vector;

    /**
     * Choix d'élimination de pièces : <br>
     * => true : persecuter<br>
     * => false : aspirer
     */
    bool percute;

    Move() : vector(NULL_VECTOR), percute(true) {}

    Move(const Vector vector, const bool percute) : vector(vector), percute(percute) {}

    std::string toString() const {
        return "(" + vectorName(vector) + " => " + std::to_string(percute) + ")";
    }

};

struct PieceMove {
public:

    /**
     * Position de la pièce
     */
    Bitboard piecePosition;

    /**
     * Vecteur de deplacement
     */
    Vector vector;

    /**
     * Choix d'élimination de pièces : <br>
     * => true : persecuter<br>
     * => false : aspirer
     */
    bool percute;

    PieceMove() :
            piecePosition(0),
            vector(NULL_VECTOR),
            percute(true)
    {}

    PieceMove(const Bitboard piecePosition, const Vector vector, const bool percute) :
            piecePosition(piecePosition),
            vector(vector),
            percute(percute)
    {}

    std::string toString() const {
        return "(" + std::to_string(getXPosition(piecePosition)) + ", " + std::to_string(getYPosition(piecePosition)) + ") : (" + vectorName(vector) + " => " + std::to_string(percute) + ")";
    }

};

struct PieceMoveSession {
public:

    /**
     * Position d'origine de la pièce
     */
    Bitboard originPosition;

    /**
     * Liste des pièces capturées
     * lors du mouvement
     */
    Bitboard catchedPieces;

    /**s
     * Liste des positions
     * parcourues par la pièce
     */
    Bitboard traveledPositions;

    /**
     * Position d'arrivée de la pièce
     */
    Bitboard lastPosition;

    Vector lastVector;

    /**
     * Score attribué au mouvement de la pièce
     */
    int score;
    int previousScore;
    /**
     * bitCount(catchedPieces)
     */
    int removedPiecesCount;
    int moveCount;

    Move moves[MAX_SESSION_MOVES_COUNT];

    inline void init() {
        moveCount = 0;
    }

    inline void addFirstMove(const Vector vector, const bool percute) {
        init();
        addMove(vector, percute);
    }

    inline void addMove(const Vector vector, const bool percute) {
        assert(moveCount < MAX_SESSION_MOVES_COUNT);

        Move& currentMove = moves[moveCount];
        currentMove.vector = vector;
        currentMove.percute = percute;

        ++moveCount;
    }

    inline bool operator<(const PieceMoveSession& pms) const {
        return score == pms.score ? previousScore < pms.previousScore : score < pms.score;
    }

    std::string toString() {
        std::string s = "(" + std::to_string(getXPosition(originPosition)) + ", " + std::to_string(getYPosition(originPosition)) + ") : ";

        bool first = true;

        for (int i = 0; i < moveCount; ++i) {

            if (first) {
                first = false;
            } else {
                s += ", ";
            }

            s += moves[i].toString();
        }

        s += " => " + std::to_string(score) + ", " + std::to_string(previousScore) + ", " + std::to_string(removedPiecesCount)/* + "\n" + pretty(catchedPieces)*/;

        return s;
    }
};

struct PieceMoveSessionBack {
public:

    /**
     * Position d'origine de la pièce
     */
    Bitboard originPosition;

    /**
     * Liste des pièces capturées
     * lors du mouvement
     */
    Bitboard addedPieces;

    /**s
     * Liste des positions
     * parcourues par la pièce
     */
    Bitboard traveledPositions;

    /**
     * Position d'arrivée de la pièce
     */
    Bitboard lastPosition;

    Vector lastVector;

    int moveCount;
    int score;

    Move moves[MAX_SESSION_MOVES_COUNT];

    inline void init() {
        moveCount = 0;
    }

    inline void addFirstMove(const Vector vector, const bool percute) {
        init();
        addMove(vector, percute);
    }

    inline void addMove(const Vector vector, const bool percute) {
        assert(moveCount < MAX_SESSION_MOVES_COUNT);

        Move& currentMove = moves[moveCount];
        currentMove.vector = vector;
        currentMove.percute = percute;

        ++moveCount;
    }

    std::string toString() {
        std::string s = "(" + std::to_string(getXPosition(originPosition)) + ", " + std::to_string(getYPosition(originPosition)) + ") : ";

        bool first = true;

        for (int i = 0; i < moveCount; ++i) {

            if (first) {
                first = false;
            } else {
                s += ", ";
            }

            s += moves[i].toString();
        }

        return s;
    }
};

#endif // MOVE_H
