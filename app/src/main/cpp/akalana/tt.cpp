#include "tt.h"

Tt::Tt()
{
    int size = 3 * 2 * MAX_DEEP;
    for (int i = 0; i < size; ++i) {
        FOUND_RESULTS[i] = NULL;
    }
}

Tt::~Tt()
{
    clear();
}

void Tt::clear()
{
    const int size = 3 * 2 * MAX_DEEP;
    for (int i = 0; i < size; ++i) {
        std::map<Bitboard, std::map<Bitboard, TTEntry>>* map3 = FOUND_RESULTS[i];

        if (map3 != NULL) {
            delete[] map3;
        }
    }
}
