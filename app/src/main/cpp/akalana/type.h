#ifndef TYPE_H
#define TYPE_H

#include <cstdint>
#include <string>

/**
 * @brief Bitboard
 */

typedef uint64_t Bitboard;

/**
 * @brief Vector
 */

typedef int Vector;

constexpr Vector
    RIGHT        = 1, //  0,  1
    BOTTOM       = 11, //  1,  0
    LEFT         = -RIGHT, //  0, -1
    TOP          = -BOTTOM, // -1,  0
    LEFT_TOP     =  LEFT  + TOP, // -1, -1
    LEFT_BOTTOM  =  LEFT  + BOTTOM, //  1, -1
    RIGHT_BOTTOM =  RIGHT + BOTTOM, //  1,  1
    RIGHT_TOP    =  RIGHT + TOP, // -1,  1
    NULL_VECTOR  = 0;

constexpr Vector FOINA_VECTORS[] = {
    LEFT, RIGHT, TOP, BOTTOM
};

constexpr Vector LAKA_VECTORS[] = {
    LEFT, RIGHT, TOP, BOTTOM, LEFT_BOTTOM, RIGHT_BOTTOM, LEFT_TOP, RIGHT_TOP
};

enum GameMode {
    MODE_RIATRA,
    MODE_VELA
};

enum PERCUSSION : bool {
    PERCUTE = true,
    ASPIRE = false
};

enum GamePhase {
    BEGIN_GAME,
    MIDDLE_GAME,
    END_GAME
};

enum TTEntryType : uint8_t {
    EXACT = 0x1,
    LOWERBOUND = 0x2,
    UPPERBOUND = 0x4
};

enum NodeType { NonPV, PV };

inline std::string vectorName(Vector vector) {
    switch (vector) {
    case RIGHT:
        return "RIGHT";
    case BOTTOM:
        return "BOTTOM";
    case LEFT:
        return "LEFT";
    case TOP:
        return "TOP";
    case LEFT_TOP:
        return "LEFT_TOP";
    case LEFT_BOTTOM:
        return "LEFT_BOTTOM";
    case RIGHT_BOTTOM:
        return "RIGHT_BOTTOM";
    case RIGHT_TOP:
        return "RIGHT_TOP";
    }

    return "NULL";
}

constexpr int MAX_MOVES_COUNT = 4096;
constexpr int MAX_SESSION_MOVES_COUNT = 24;

/**
 * @brief MAX_SCORE
 *        Score max pouvant être atteint
 */
constexpr int MAX_SCORE = 100000000;

/**
 * @brief MIN_SCORE
 *        Score max pouvant être atteint
 */
constexpr int MIN_SCORE = -MAX_SCORE;

/**
 * @brief TIME_ELAPSED_SCORE
 *        Score particulier pour les mouvements
 *        qui n'ont pas pu être évalué jusqu'au bout
 */
constexpr int TIME_ELAPSED_SCORE = MIN_SCORE - 1;

#endif // TYPE_H
