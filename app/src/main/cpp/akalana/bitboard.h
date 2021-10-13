#ifndef BITBOARD_H
#define BITBOARD_H

#include "type.h"
#include <string>

/**
 * @brief COLUMN_COUNT
 *
 * Représentation du bitboard de Fanorona
 * (des 45 positions)
 * dans un nombre de 64 bits
 *
 * .  X  X  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  X  X  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr int
        COLUMN_COUNT = 11,
        COLUMN_COUNT_2 = COLUMN_COUNT * 2,
        COLUMN_COUNT_3 = COLUMN_COUNT * 3,
        COLUMN_COUNT_4 = COLUMN_COUNT * 4;


/**
 * Convertit en Bitboard une coordonnée(x, y) donnée
 * d'une table Fanorona
 *
 * @param x
 *        index en vertical
 *
 * @param y
 *        index en horizontal
 *
 * @return position(x, y) en Bitboard
 */
constexpr Bitboard p(int x, int y) {
    return 1ull << (COLUMN_COUNT * x + y + 1);
}

/**
 *
 * @param position
 *        position d'origine
 *
 * @param vector
 *        vecteur de translation
 *
 * @return coordonnée en Bitboard de la nouvelle position
 *    si on applique la translation "vector"
 *    à "position"
 */
Bitboard movePosition(const Bitboard position, const Vector vector);
/**
 *
 * @param position
 *        position d'origine
 *
 * @param vector
 *        vecteur de translation
 *
 * @return coordonnée en Bitboard de la nouvelle position
 *    si on applique la translation inverse "vector"
 *    à "position"
 */
Bitboard movePositionBack(const Bitboard position, const Vector vector);

/**
 *
 * @param position
 *
 * @return la position symétrique
 *         de position
 */
Bitboard reversePosition(const Bitboard position);

/**
 *
 * @param position
 *        position ne doit définir qu'une seule position
 *        mais non un ensemble de position
 *
 * @return Abscisse d'une position
 */
int getXPosition(const Bitboard position);

/**
 *
 * @param position
 *        position ne doit définir qu'une seule position
 *        mais non un ensemble de position
 *
 * @return Ordonnée d'une position
 */
int getYPosition(const Bitboard position);

/**
 *
 * @param position
 * @return
 */
std::string pretty(const Bitboard position);

/**
 *
 * @param position
 * @return
 */
std::string prettyBitboard(const Bitboard position);

/**
 *
 * @param position1
 * @param position2
 * @return
 */
std::string pretty(const Bitboard position1, const Bitboard position2);

std::string prettyPoint(const Bitboard position);

/**
 * @brief LAKA
 *
 * X  .  X  .  X  .  X  .  X
 * .  X  .  X  .  X  .  X  .
 * X  .  X  .  X  .  X  .  X
 * .  X  .  X  .  X  .  X  .
 * X  .  X  .  X  .  X  .  X
 *
 */
constexpr Bitboard LAKA   = 0x2AAAAA8AA8AA2AA;
/**
 * @brief FOINA
 *
 * .  X  .  X  .  X  .  X  .
 * X  .  X  .  X  .  X  .  X
 * .  X  .  X  .  X  .  X  .
 * X  .  X  .  X  .  X  .  X
 * .  X  .  X  .  X  .  X  .
 *
 */
constexpr Bitboard FOINA  = 0x15455455155154;
/**
 * @brief LAKABE
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKABE = p(2, 4);

/**
 * @brief LAKA_AM_PAMONOANA_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKA_AM_PAMONOANA_1 = p(2, 2);
/**
 * @brief LAKA_AM_PAMONOANA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKA_AM_PAMONOANA_2 = p(2, 6);
/**
 * @brief LAKA_AM_PAMONOANA
 * LAKA_AM_PAMONOANA_1 | LAKA_AM_PAMONOANA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKA_AM_PAMONOANA =
        LAKA_AM_PAMONOANA_1 |
        LAKA_AM_PAMONOANA_2;

/**
 * @brief LAKAMANDRY_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANDRY_1 = p(1, 3);
/**
 * @brief LAKAMANDRY_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANDRY_2 = p(1, 5);
/**
 * @brief LAKAMANDRY_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANDRY_3 = p(3, 3);
/**
 * @brief LAKAMANDRY_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANDRY_4 = p(3, 5);
/**
 * @brief LAKAMANDRY
 * LAKAMANDRY_1 | LAKAMANDRY_2 | LAKAMANDRY_3 | LAKAMANDRY_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANDRY =
        LAKAMANDRY_1 |
        LAKAMANDRY_2 |
        LAKAMANDRY_3 |
        LAKAMANDRY_4;

/**
 * @brief LAKAMANGA_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANGA_1 = p(1, 1);
/**
 * @brief LAKAMANGA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANGA_2 = p(1, 7);
/**
 * @brief LAKAMANGA_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANGA_3 = p(3, 1);
/**
 * @brief LAKAMANGA_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANGA_4 = p(3, 7);
/**
 * @brief LAKAMANGA
 * LAKAMANGA_1 | LAKAMANGA_2 | LAKAMANGA_3 | LAKAMANGA_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAMANGA =
        LAKAMANGA_1 |
        LAKAMANGA_2 |
        LAKAMANGA_3 |
        LAKAMANGA_4;

/**
 * @brief VOHITRA_1
 *
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard VOHITRA_1 = p(0, 4);
/**
 * @brief VOHITRA_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 *
 */
constexpr Bitboard VOHITRA_2 = p(4, 4);
/**
 * @brief VOHITRA
 * VOHITRA_1 | VOHITRA_2
 *
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 *
 */
constexpr Bitboard VOHITRA =
        VOHITRA_1 |
        VOHITRA_2;

/**
 * @brief VOVONANA_1
 *
 * .  .  X  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard VOVONANA_1 = p(0, 2);
/**
 * @brief VOVONANA_2
 *
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard VOVONANA_2 = p(0, 6);
/**
 * @brief VOVONANA_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 *
 */
constexpr Bitboard VOVONANA_3 = p(4, 2);
/**
 * @brief VOVONANA_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  X  .  .
 *
 */
constexpr Bitboard VOVONANA_4 = p(4, 6);
/**
* @brief VOVONANA
 * VOVONANA_1 | VOVONANA_2 | VOVONANA_3 | VOVONANA_4
 *
 * .  .  X  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  X  .  .
 *
 */
constexpr Bitboard VOVONANA =
        VOVONANA_1 |
        VOVONANA_2 |
        VOVONANA_3 |
        VOVONANA_4;

/**
 * @brief ZARAN_DAKA_1
 *
 * X  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ZARAN_DAKA_1 = p(0, 0);
/**
 * @brief ZARAN_DAKA_2
 *
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ZARAN_DAKA_2 = p(0, 8);
/**
 * @brief ZARAN_DAKA_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ZARAN_DAKA_3 = p(4, 0);
/**
 * @brief ZARAN_DAKA_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  X
 *
 */
constexpr Bitboard ZARAN_DAKA_4 = p(4, 8);
/**
 * @brief ZARAN_DAKA
 * ZARAN_DAKA_1 | ZARAN_DAKA_2 | ZARAN_DAKA_3 | ZARAN_DAKA_4
 *
 * X  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  X
 *
 */
constexpr Bitboard ZARAN_DAKA =
        ZARAN_DAKA_1 |
        ZARAN_DAKA_2 |
        ZARAN_DAKA_3 |
        ZARAN_DAKA_4;

/**
 * @brief LOHALAKA_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHALAKA_1 = p(2, 0);
/**
 * @brief LOHALAKA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHALAKA_2 = p(2, 8);
/**
 * @brief LOHALAKA
 * LOHALAKA_1 | LOHALAKA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHALAKA =
        LOHALAKA_1 |
        LOHALAKA_2;

/**
 * @brief LOHAMANDRY_1
 *
 * .  .  .  X  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANDRY_1 = p(0, 3);
/**
 * @brief LOHAMANDRY_2
 *
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANDRY_2 = p(0, 5);
/**
 * @brief LOHAMANDRY_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANDRY_3 = p(4, 3);
/**
 * @brief LOHAMANDRY_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  X  .  .  .
 *
 */
constexpr Bitboard LOHAMANDRY_4 = p(4, 5);
/**
 * @brief LOHAMANDRY_1
 *
 * .  .  .  X  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  X  .  .  .
 *
 */
constexpr Bitboard LOHAMANDRY =
        LOHAMANDRY_1 |
        LOHAMANDRY_2 |
        LOHAMANDRY_3 |
        LOHAMANDRY_4;

/**
 * @brief LOHAMANGANY_1
 *
 * .  X  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANGANY_1 = p(0, 1);
/**
 * @brief LOHAMANGANY_2
 *
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANGANY_2 = p(0, 7);
/**
 * @brief LOHAMANGANY_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LOHAMANGANY_3 = p(4, 1);
/**
 * @brief LOHAMANGANY_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  X  .
 *
 */
constexpr Bitboard LOHAMANGANY_4 = p(4, 7);
/**
 * @brief LOHAMANGANY
 *
 * .  X  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  X  .
 *
 */
constexpr Bitboard LOHAMANGANY =
        LOHAMANGANY_1 |
        LOHAMANGANY_2 |
        LOHAMANGANY_3 |
        LOHAMANGANY_4;

/**
 * @brief ANKELIKA_1
 *
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ANKELIKA_1 = p(1, 0);
/**
 * @brief ANKELIKA_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ANKELIKA_2 = p(1, 8);
/**
 * @brief ANKELIKA_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ANKELIKA_3 = p(3, 0);
/**
 * @brief ANKELIKA_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ANKELIKA_4 = p(3, 8);
/**
 * @brief ANKELIKA
 *
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard ANKELIKA =
        ANKELIKA_1 |
        ANKELIKA_2 |
        ANKELIKA_3 |
        ANKELIKA_4;

/**
 * @brief LAKAKELY_1
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_1 = p(1, 2);
/**
 * @brief LAKAKELY_2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_2 = p(1, 4);
/**
 * @brief LAKAKELY_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_3 = p(1, 6);
/**
 * @brief LAKAKELY_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_4 = p(2, 1);
/**
 * @brief LAKAKELY_5
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_5 = p(2, 3);
/**
 * @brief LAKAKELY_6
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_6 = p(2, 5);
/**
 * @brief LAKAKELY_7
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_7 = p(2, 7);
/**
 * @brief LAKAKELY_8
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_8 = p(3, 2);
/**
 * @brief LAKAKELY_9
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_9 = p(3, 4);
/**
 * @brief LAKAKELY_10
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY_10 = p(3, 6);
/**
 * @brief LAKAKELY
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  X  .  X  .  X  .  .
 * .  X  .  X  .  X  .  X  .
 * .  .  X  .  X  .  X  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard LAKAKELY =
        LAKAKELY_1 |
        LAKAKELY_2 |
        LAKAKELY_3 |
        LAKAKELY_4 |
        LAKAKELY_5 |
        LAKAKELY_6 |
        LAKAKELY_7 |
        LAKAKELY_8 |
        LAKAKELY_9 |
        LAKAKELY_10;

/**
 * @brief RANK_1
 *
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANK_1 =
        p(0, 0) |
        p(0, 1) |
        p(0, 2) |
        p(0, 3) |
        p(0, 4) |
        p(0, 5) |
        p(0, 6) |
        p(0, 7) |
        p(0, 8);
/**
 * @brief RANK_2
 *
 * .  .  .  .  .  .  .  .  .
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANK_2 = RANK_1 << COLUMN_COUNT;
/**
 * @brief RANK_3
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANK_3 = RANK_2 << COLUMN_COUNT;
/**
 * @brief RANK_4
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANK_4 = RANK_3 << COLUMN_COUNT;
/**
 * @brief RANK_5
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  X  X  X  X  X  X  X  X
 *
 */
constexpr Bitboard RANK_5 = RANK_4 << COLUMN_COUNT;

/**
 * @brief FILE_1
 *
 * X  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard FILE_1 =
        p(0, 0) |
        p(1, 0) |
        p(2, 0) |
        p(3, 0) |
        p(4, 0);
/**
 * @brief FILE_2
 *
 * .  X  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 * .  X  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard FILE_2 = FILE_1 << 1;
/**
 * @brief FILE_3
 *
 * .  .  X  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 * .  .  X  .  .  .  .  .  .
 *
 */
constexpr Bitboard FILE_3 = FILE_2 << 1;
/**
 * @brief FILE_4
 *
 * .  .  .  X  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 * .  .  .  X  .  .  .  .  .
 *
 */
constexpr Bitboard FILE_4 = FILE_3 << 1;
/**
 * @brief FILE_5
 *
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 * .  .  .  .  X  .  .  .  .
 *
 */
constexpr Bitboard FILE_5 = FILE_4 << 1;
/**
 * @brief FILE_6
 *
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  X  .  .  .
 * .  .  .  .  .  X  .  .  .
 *
 */
constexpr Bitboard FILE_6 = FILE_5 << 1;
/**
 * @brief FILE_7
 *
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  X  .  .
 * .  .  .  .  .  .  X  .  .
 *
 */
constexpr Bitboard FILE_7 = FILE_6 << 1;
/**
 * @brief FILE_8
 *
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  X  .
 * .  .  .  .  .  .  .  X  .
 *
 */
constexpr Bitboard FILE_8 = FILE_7 << 1;
/**
 * @brief FILE_9
 *
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  X
 *
 */
constexpr Bitboard FILE_9 = FILE_8 << 1;

/**
 * @brief FILES1
 *
 * X  X  X  .  .  .  .  .  .
 * X  X  X  .  .  .  .  .  .
 * X  X  X  .  .  .  .  .  .
 * X  X  X  .  .  .  .  .  .
 * X  X  X  .  .  .  .  .  .
 *
 */
constexpr Bitboard FILES1 =
        FILE_1 |
        FILE_2 |
        FILE_3;
/**
 * @brief FILES2
 *
 * .  .  .  X  X  X  .  .  .
 * .  .  .  X  X  X  .  .  .
 * .  .  .  X  X  X  .  .  .
 * .  .  .  X  X  X  .  .  .
 * .  .  .  X  X  X  .  .  .
 *
 */
constexpr Bitboard FILES2 =
        FILE_4 |
        FILE_5 |
        FILE_6;
/**
 * @brief FILES3
 *
 * .  .  .  .  .  .  X  X  X
 * .  .  .  .  .  .  X  X  X
 * .  .  .  .  .  .  X  X  X
 * .  .  .  .  .  .  X  X  X
 * .  .  .  .  .  .  X  X  X
 *
 */
constexpr Bitboard FILES3 =
        FILE_7 |
        FILE_8 |
        FILE_9;

/**
 * @brief RANKS1
 *
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANKS1 =
        RANK_1 |
        RANK_2;

/**
 * @brief RANKS2
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard RANKS2 =
        RANK_3 |
        RANK_4;


/**
 * @brief BORDER
 *
 * X  X  X  X  X  X  X  X  X
 * X  .  .  .  .  .  .  .  X
 * X  .  .  .  .  .  .  .  X
 * X  .  .  .  .  .  .  .  X
 * X  X  X  X  X  X  X  X  X
 *
 */
constexpr Bitboard BORDER =
        FILE_1 |
        RANK_1 |
        FILE_9 |
        RANK_5;
/**
 * @brief CORNER
 *
 * X  X  .  .  .  .  .  X  X
 * X  .  .  .  .  .  .  .  X
 * .  .  .  .  .  .  .  .  .
 * X  .  .  .  .  .  .  .  X
 * X  X  .  .  .  .  .  X  X
 *
 */
constexpr Bitboard CORNER =
        ZARAN_DAKA |
        LOHAMANGANY |
        ANKELIKA;
/**
 * @brief FRAME
 *
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 *
 */
constexpr Bitboard FRAME =
        RANK_1 |
        RANK_2 |
        RANK_3 |
        RANK_4 |
        RANK_5;
/**
 * @brief FRAME_WITHOUT_BORDER
 *
 * .  .  .  .  .  .  .  .  .
 * .  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  .
 * .  X  X  X  X  X  X  X  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard FRAME_WITHOUT_BORDER = FRAME & ~BORDER;
/**
 * @brief FRAME_WITHOUT_CORNER
 *
 * .  .  X  X  X  X  X  .  .
 * .  X  X  X  X  X  X  X  .
 * X  X  X  X  X  X  X  X  X
 * .  X  X  X  X  X  X  X  .
 * .  .  X  X  X  X  X  .  .
 *
 */
constexpr Bitboard FRAME_WITHOUT_CORNER = FRAME & ~CORNER;
/**
 * @brief BORDER
 *
 * .  .  .  .  .  .  .  .  .
 * .  X  X  X  X  X  X  X  .
 * .  X  .  .  .  .  .  X  .
 * .  X  X  X  X  X  X  X  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard SEMI_CENTER =
        (
                FILE_2 |
                RANK_2 |
                FILE_8 |
                RANK_4
        )
        & FRAME_WITHOUT_BORDER;

/**
 * @brief BORDER
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  X  X  X  X  X  .  .
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard CENTER =
        p(2, 2) | p(2, 3) | p(2, 4) | p(2, 5) | p(2, 6);

/**
 * @brief INITIAL_BLACK_POSITION
 *
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 * .  X  .  X  .  .  X  .  X
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 *
 */
constexpr Bitboard INITIAL_BLACK_POSITION =
        RANK_4 |
        RANK_5 |
        p(2, 0) |
        p(2, 2) |
        p(2, 5) |
        p(2, 7);

/**
 * @brief INITIAL_WHITE_POSITION
 *
 * .  .  .  .  .  .  .  .  .
 * .  .  .  .  .  .  .  .  .
 * X  .  X  .  .  X  .  X  .
 * X  X  X  X  X  X  X  X  X
 * X  X  X  X  X  X  X  X  X
 *
 */
constexpr Bitboard INITIAL_WHITE_POSITION =
        RANK_1 |
        RANK_2 |
        p(2, 1) |
        p(2, 3) |
        p(2, 6) |
        p(2, 8);

constexpr Bitboard H_SYM_ZONE_A =
        FILE_1 | FILE_2 | FILE_3 | FILE_4;

constexpr Bitboard H_SYM_ZONE_B =
        FILE_6 | FILE_7 | FILE_8 | FILE_9;

constexpr Bitboard H_SYM_ZONE_C =
        FILE_1 | FILE_2 | FILE_6 | FILE_7;

constexpr Bitboard H_SYM_ZONE_D =
        FILE_3 | FILE_4 | FILE_8 | FILE_9;

constexpr Bitboard H_SYM_ZONE_E =
        FILE_1 | FILE_3 | FILE_6 | FILE_8;

constexpr Bitboard H_SYM_ZONE_F =
        FILE_2 | FILE_4 | FILE_7 | FILE_9;

constexpr Bitboard P_SYM_ZONE_A = (1ull << 32) - 1;
constexpr Bitboard P_SYM_ZONE_B = P_SYM_ZONE_A << 32;

inline int bitCount(const Bitboard bitboard) {
    return __builtin_popcountll(bitboard);
}

inline Bitboard reverseBits(Bitboard i) {
    //return __builtin_bswap64(bitboard);

    i = (i & 0x5555555555555555ll) << 1 | (i >> 1) & 0x5555555555555555ll;
    i = (i & 0x3333333333333333ll) << 2 | (i >> 2) & 0x3333333333333333ll;
    i = (i & 0x0f0f0f0f0f0f0f0fll) << 4 | (i >> 4) & 0x0f0f0f0f0f0f0f0fll;
    i = (i & 0x00ff00ff00ff00ffll) << 8 | (i >> 8) & 0x00ff00ff00ff00ffll;
    i = (i << 48) | ((i & 0xffff0000ll) << 16) | ((i >> 16) & 0xffff0000ll) | (i >> 48);
    return i;
}

inline int lsb(Bitboard b) {
    return __builtin_ctzll(b);
}

inline int msb(Bitboard b) {
    return 63 ^ __builtin_clzll(b);
}

inline Bitboard poplsb(Bitboard& bitboard) {
    const Bitboard position = bitboard & -bitboard;
    bitboard &= ~position;
    return position;
}

inline Bitboard movePosition(const Bitboard position, const Vector vector)
{
    return vector < 0 ? position >> -vector : position << vector;
}

inline Bitboard movePositionBack(const Bitboard position, const Vector vector)
{
    return vector < 0 ? position << -vector : position >> vector;
}

inline Bitboard reversePosition(const Bitboard position)
{
    return reverseBits(position) >> 9;
}

inline Bitboard verticalSymmetricPosition(const Bitboard position)
{
    return
            ((position & RANK_1) << COLUMN_COUNT_4) |
            ((position & RANK_2) << COLUMN_COUNT_2) |
            (position & RANK_3) |
            ((position & RANK_4) >> COLUMN_COUNT_2) |
            ((position & RANK_5) >> COLUMN_COUNT_4);
}

inline Bitboard horizontalSymmetricPosition(const Bitboard position)
{
    Bitboard pos = ((position & H_SYM_ZONE_A) << 5) | ((position & H_SYM_ZONE_B) >> 5);
    pos = ((pos & H_SYM_ZONE_C) << 2) | ((pos & H_SYM_ZONE_D) >> 2);
    return ((pos & H_SYM_ZONE_E) << 1) | ((pos & H_SYM_ZONE_F) >> 1) | (position & FILE_5);
}

inline int getXPosition(const Bitboard position) {
    if (position & RANKS1) {
        if (position & RANK_1) {
            return 0;
        } else {
            return 1;
        }
    } else if (position & RANKS2) {
        if (position & RANK_3) {
            return 2;
        } else {
            return 3;
        }
    } else if (position & RANK_5) {
        return 4;
    }

    return -1;
}

inline int getYPosition(const Bitboard position) {
    if (position & FILES1) {
        if (position & FILE_1) {
            return 0;
        } else if (position & FILE_2) {
            return 1;
        } else {
            return 2;
        }
    } else if (position & FILES2) {
        if (position & FILE_4) {
            return 3;
        } else if (position & FILE_5) {
            return 4;
        } else {
            return 5;
        }
    } else if (position & FILES3) {
        if (position & FILE_7) {
            return 6;
        } else if (position & FILE_8) {
            return 7;
        } else {
            return 8;
        }
    }

    return -1;
}

const Bitboard SYM_INITIAL_WHITE_POSITION = verticalSymmetricPosition(INITIAL_BLACK_POSITION);
const Bitboard SYM_INITIAL_BLACK_POSITION = verticalSymmetricPosition(INITIAL_WHITE_POSITION);

constexpr int LAKA_MB =
        (1 << 0)         +              (1 << 1)         +         (1 << 2) +
        (1 << (COLUMN_COUNT))  +                                 (1 << (COLUMN_COUNT + 2)) +
        (1 << (2 * COLUMN_COUNT)) + (1 << (2 * COLUMN_COUNT + 1)) + (1 << (2 * COLUMN_COUNT + 2));

constexpr int LAKA_MB_RANK_1 =
        (1 << 0)        +                           +         (1 << 2) +
        (1 << (COLUMN_COUNT)) + (1 << (COLUMN_COUNT + 1)) + (1 << (COLUMN_COUNT + 2));

constexpr int FOINA_MB =
        (1 << 1) +
        (1 << (COLUMN_COUNT)) +
        (1 << (COLUMN_COUNT + 2)) +
        (1 << (2 * COLUMN_COUNT + 1));

constexpr int FOINA_MB_RANK_1 =
        (1 << 0) + (1 << 2) +
        (1 << (COLUMN_COUNT + 1));

inline Bitboard movablePositionsFrom(const Bitboard position) {
    return position & RANK_1 ?
           (position >>  1) * (position & LAKA ? LAKA_MB_RANK_1 : FOINA_MB_RANK_1) :
           (position >> 12) * (position & LAKA ? LAKA_MB        : FOINA_MB);
}

constexpr Bitboard LAKA_RMB =
        (1ull << 0)         +              (1ull << 2)         +         (1ull << 4) +
        (1ull << (2 * COLUMN_COUNT)) +                                 (1ull << (2 * COLUMN_COUNT + 4)) +
        (1ull << (4 * COLUMN_COUNT)) + (1ull << (4 * COLUMN_COUNT + 2)) + (1ull << (4 * COLUMN_COUNT + 4));


//////////////////////////////////////////////////////


// MAGIC BITBOARD

const int MAX_MAGIC_POS_COUNT = COLUMN_COUNT * 5 - 1;
const int MAX_MAGIC_COUNT = 256;

extern Bitboard MOVABLE_BITBOARD[MAX_MAGIC_POS_COUNT];
extern Bitboard OCCUPATIONS[MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
extern Bitboard ATTACKS[2][MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
extern Bitboard MAGIC_ATTACKS[2][MAX_MAGIC_POS_COUNT][MAX_MAGIC_COUNT];
extern Bitboard MAGIC_ATTACKS_NUMBERS[2][MAX_MAGIC_POS_COUNT];
extern Bitboard ATTACKS_BLOCKERS[9][54];

const Bitboard SHIFT_BITS[MAX_MAGIC_POS_COUNT] = {
        0, 64 - 3, 64 - 3, 64 - 5, 64 - 3, 64 - 5, 64 - 3, 64 - 5, 64 - 3, 64 - 3, 0,
        0, 64 - 3, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 3, 0,
        0, 64 - 5, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 0,
        0, 64 - 3, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 4, 64 - 8, 64 - 3, 0,
        0, 64 - 3, 64 - 3, 64 - 5, 64 - 3, 64 - 5, 64 - 3, 64 - 5, 64 - 3, 64 - 3
};

inline void initMobableBitboard() {
    for (int pos = 1; pos < MAX_MAGIC_POS_COUNT; ++pos) {
        Bitboard b = 1ull << pos;

        if (b & FRAME) {
            MOVABLE_BITBOARD[pos] = movablePositionsFrom(b);
        }
    }
}

// Returns a unsigned 64 bit random number.
inline Bitboard U64Rand() {
    return
            (Bitboard(0xFFFF & rand()) << 48) |
            (Bitboard(0xFFFF & rand()) << 32) |
            (Bitboard(0xFFFF & rand()) << 16) |
            Bitboard(0xFFFF & rand());
}

// Bias the random number to contain more 0 bits.
inline Bitboard ZeroBitBiasedRandom() {
    return U64Rand() & U64Rand() & U64Rand();
}

Bitboard generateMagic(int pos, bool percute);
void generateOccupation(int pos, Bitboard movablePosition, Bitboard percuteAttackPosition, Bitboard aspireAttackPosition, int &index, int vectorIndex);
void generateAllOccupations();
void initAttack();

namespace Bitboards {
    void init();
}

#endif // BITBOARD_H
