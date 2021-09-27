#include "bitboard.h"
#include <iostream>

Bitboard MOVABLE_BITBOARD[MAX_MAGIC_POS_COUNT];
Bitboard OCCUPATIONS[MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
Bitboard ATTACKS[2][MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
Bitboard MAGIC_ATTACKS[2][MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
Bitboard MAGIC_ATTACKS_NUMBERS[2][MAX_MAGIC_POS_COUNT];
Bitboard ATTACKS_BLOCKERS[9][54];

std::string pretty(const Bitboard position) {
    std::string result = "";

    for (Bitboard i = ZARAN_DAKA_1; i <= ZARAN_DAKA_4; i <<= 1) {
        if (position & i) {
            result += " @ ";
        } else if (i & LAKA) {
            result += " * ";
        } else {
            result += " + ";
        }

        if (i & FILE_9) {
            result += "\n";
            i <<= 2;
        }
    }

    return result;
}

std::string pretty(const Bitboard position1, const Bitboard position2) {
    std::string result = "";

    for (Bitboard i = ZARAN_DAKA_1; i <= ZARAN_DAKA_4; i <<= 1) {
        if (position1 & i) {
            result += " B ";
        } else if (position2 & i) {
            result += " W ";
        } else if (i & LAKA) {
            result += " * ";
        } else {
            result += " + ";
        }

        if (i & FILE_9) {
            result += "\n";
            i <<= 2;
        }
    }

    return result;
}

std::string prettyPoint(const Bitboard position) {
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 9; j++) {
            if (p(i, j) & position) {
                return "(" + std::to_string(i) + ", " + std::to_string(j) + ")";
            }
        }
    }

    return "";
}

Bitboard generateMagic(int pos, bool percute) {
    int percuteIndex = percute ? 1 : 0;

    while (true) {
        for (int i = 0; i < MAX_MAGIC_COUNT; ++i) {
            MAGIC_ATTACKS[percuteIndex][pos][i] = FRAME;
        }

        Bitboard candidate_magic = ZeroBitBiasedRandom();
        bool collision = false;

        for (int k = 0; k < MAX_MAGIC_COUNT; ++k) {
            const Bitboard occupancy = OCCUPATIONS[pos][k];

            if (occupancy) {
                const Bitboard attack = ATTACKS[percuteIndex][pos][k];
                const int offset = (occupancy * candidate_magic) >> SHIFT_BITS[pos];

                if (MAGIC_ATTACKS[percuteIndex][pos][offset] == FRAME || MAGIC_ATTACKS[percuteIndex][pos][offset] == attack) {
                    MAGIC_ATTACKS[percuteIndex][pos][offset] = attack;
                } else {
                    collision = true;
                    break;
                }
            }
        }

        if (!collision) {
            return candidate_magic;
        }
    }
}

void generateOccupation(int pos, Bitboard movablePosition, Bitboard percuteAttackPosition, Bitboard aspireAttackPosition, int &index, int vectorIndex) {
    Bitboard position = 1ull << pos;

    for (int i = vectorIndex; i < (position & LAKA ? 8 : 4); ++i) {
        int vector = LAKA_VECTORS[i];

        const Bitboard newMovablePosition = movePosition(position, vector) & FRAME;

        if (newMovablePosition) {
            const Bitboard newPercuteAttackPosition = movePosition(newMovablePosition, vector) & FRAME;
            const Bitboard newAspireAttackPosition = movePositionBack(position, vector) & FRAME;

            const Bitboard o = OCCUPATIONS[pos][index] = movablePosition | newMovablePosition;
            const Bitboard pa = ATTACKS[1][pos][index] = percuteAttackPosition | newPercuteAttackPosition;
            const Bitboard aa = ATTACKS[0][pos][index] = aspireAttackPosition | newAspireAttackPosition;

            generateOccupation(pos, o, pa, aa, ++index, i + 1);
        }
    }
}

void generateAllOccupations() {
    for (int pos = 0; pos < MAX_MAGIC_POS_COUNT; ++pos) {
        if ((1ull << pos) & FRAME) {
            int index = 0;
            generateOccupation(pos, 0, 0, 0, index, 0);
            MAGIC_ATTACKS_NUMBERS[0][pos] = generateMagic(pos, false);
            MAGIC_ATTACKS_NUMBERS[1][pos] = generateMagic(pos, true);
        } else {
            MAGIC_ATTACKS_NUMBERS[0][pos] = 0ull;
            MAGIC_ATTACKS_NUMBERS[1][pos] = 0ull;
        }
    }
}

void initAttack()
{
    for (int vector : LAKA_VECTORS) {
        int vectorIndex = vector < -1 ? vector + 12 : (vector < 2 ? vector + 4 : vector - 4);

        for (unsigned int i = 0; i < 54; i++) {
            Bitboard pieceAttacks = 0;
            Bitboard position = 1ull << i;

            if ((position & FRAME) != 0 && ((position & LAKA) != 0 || vectorIndex % 2 == 1)) {
                for (position = movePosition(position, vector); (position & FRAME) != 0; position = movePosition(position, vector)) {
                    pieceAttacks |= position;
                }
            }

            ATTACKS_BLOCKERS[vectorIndex][i] = pieceAttacks;
        }
    }
}

void Bitboards::init() {
    initMobableBitboard();
    generateAllOccupations();
    initAttack();
}