#ifndef TT_H
#define TT_H

#include "fanorona.h"
#include <map>
#include <iostream>

#include <inttypes.h>

struct TTEntry {
public:
    TTEntry() :
            TTEntry(0, EXACT) {}

    TTEntry(const int score, const TTEntryType type) :
            score(score),
            type(type)
    {}

    int score;
    TTEntryType type;
};

class Tt
{

public:
    static constexpr int MAX_BLACK_TEAM_COUNT_SHIFT = 12;
    static constexpr Bitboard MAX_BLACK_TEAM_COUNT = (1ull << MAX_BLACK_TEAM_COUNT_SHIFT);
    static constexpr Bitboard MAX_BLACK_TEAM_MASK = MAX_BLACK_TEAM_COUNT - 1;

    static constexpr int MAX_DEEP_SHIFT = 4;
    static constexpr int MAX_DEEP = 1 << MAX_DEEP_SHIFT;

    static constexpr int VELA_COUNT = 3;

    /**
     * Integer - Vela : 0 : white(loser), 1 : black(loser), 2 : null
     * bool - tour (black = true, white = false)
     * Bitboard    - configuration des pièces noires
     * Bitboard    - configuration des pièces blanches
     */
    std::map<Bitboard, std::map<Bitboard, TTEntry>>* FOUND_RESULTS[VELA_COUNT * 2 * MAX_DEEP];

private:

    int entryCount = 0;

    /**
     * Ajout d'une nouvelle entrée
     *
     * @param deep
     *        profondeur de recherche
     *
     * @param blackPlayer
     *        Tour de jouer, 1 si le tour est au joueur noir, 0 sinon
     *
     * @param black
     *        configuration des pièces noires
     *
     * @param white
     *        configuration des pièces blanches
     *
     * @param movements
     *        Résultat
     *
     * @param newEntry
     *        indique s'il s'agit d'une nouvelle entrée
     */
    void add(const int vela, const int deep, const int blackPlayer, const Bitboard black, const Bitboard white, const TTEntry& entry);

    /**
     * Récuperer un résultat à partir des entrées:
     *
     * @param vela
     * @param deep
     *        profondeur de recherche
     *
     * @param blackPlayer
     *        Tour de jouer, 1 si le tour est au joueur noir, 0 sinon
     *
     * @param scene
     *        Configuration fanorona
     *
     * @return le résultat
     */
    TTEntry* get(const int vela, const int deep, const int blackPlayer, const Fanorona& scene) const;

    /**
     * si vela {blanc perdant => 0, noir perdant => 1}
     * sinon 2
     *
     * @param scene
     * @return
     */
    int getVela(const Fanorona& scene) const;

    /**
     * si vela {blanc perdant => 0, noir perdant => 1}
     * sinon 2
     *
     * @param scene
     * @return
     */
    bool getCurrentPlayer(const Fanorona& scene) const;

    /**
     *
     * @param scene
     * @param black
     * @return
     */
    static Bitboard serialize(const Fanorona& scene, const bool black);

    int getBlackTeam(const Bitboard black) const;

    int getTTIndex(const int vela, const int black, const int deep) const;

    void transform(Bitboard& black, Bitboard& white) const;

public:
    Tt();
    ~Tt();

    /**
     * Ajout d'une entrée, toutes les entrées ici sont considérées comme des entrées déjà éxistantes
     *
     * @param vela
     * @param deep
     *
     * @param blackPlayer
     *        Tour de jouer, 1 si le tour est au joueur noir, 0 sinon
     *
     * @param black
     *        configuration des pièces noires
     *
     * @param white
     *        configuration des pièces blanches
     * @param entry
     *
     *
     */
    void add(const int vela, const int deep, const bool blackPlayer, const Bitboard black, const Bitboard white, const TTEntry& entry);

    /**
     * Ajout d'une nouvelle entrée, toutes les entrées ici sont considérées comme des entrées nouvelles
     *
     * @param scene
     *        scene avec l'état actuel
     * =
     * @param deep
     * @param entry
     */
    void add(const Fanorona& scene, const int deep, const TTEntry& entry);

    /**
     * Récuperer un résultat à partir des entrées:
     *
     * @param scene
     *        scene avec l'état actuel
     * @param deep
     *
     * @return
     */
    TTEntry* get(const Fanorona& scene, const int deep) const;

    int getEntryCount() const;

    void clear();

};

inline void Tt::add(const int vela, const int deep, const bool blackPlayer, const Bitboard black, const Bitboard white, const TTEntry& entry) {
    add(vela, deep, blackPlayer ? 1 : 0, black, white, entry);
}

inline void Tt::add(const Fanorona& scene, const int deep, const TTEntry& entry) {
    const bool blackPlayerFirst = scene.getFirstPlayerBlack();

    Bitboard black = serialize(scene, blackPlayerFirst);
    Bitboard white = serialize(scene, !blackPlayerFirst);

    transform(black, white);

    add(
            getVela(scene),
            deep,
            getCurrentPlayer(scene) ? 1 : 0,
            black,
            white,
            entry
    );
}

inline TTEntry* Tt::get(const Fanorona& scene, const int deep) const {
    return get(
            getVela(scene),
            deep,
            getCurrentPlayer(scene) ? 1 : 0,
            scene
    );
}

inline void Tt::add(const int vela, const int deep, const int blackPlayer, const Bitboard black, const Bitboard white, const TTEntry &entry) {
    if (deep <= 0) {
        return;
    }

    const int index = getTTIndex(vela, blackPlayer, deep - 1);

    std::map<Bitboard, std::map<Bitboard, TTEntry>>* map3 = FOUND_RESULTS[index];
    if (map3 == NULL) {
        map3 = FOUND_RESULTS[index] = new std::map<Bitboard, std::map<Bitboard, TTEntry>>[MAX_BLACK_TEAM_COUNT];
    }

    const int blackTeam = getBlackTeam(black);
    std::map<Bitboard, std::map<Bitboard, TTEntry>>& map4 = map3[blackTeam];
    const std::map<Bitboard, std::map<Bitboard, TTEntry>>::iterator& it = map4.find(black);

    if (it == map4.end()) {
        std::map<Bitboard, TTEntry> map5;

        map5[white] = entry;
        map4[black] = map5;
    } else {
        it->second[white] = entry;
    }

    ++entryCount;
}

inline TTEntry* Tt::get(const int vela, const int deep, const int blackPlayer, const Fanorona& scene) const {
    if (deep <= 0) {
        return NULL;
    }

    std::map<Bitboard, std::map<Bitboard, TTEntry>>* map3 = FOUND_RESULTS[getTTIndex(vela, blackPlayer, deep - 1)];
    if (map3 == NULL) {
		return NULL;
	}

	const bool blackPlayerFirst = scene.getFirstPlayerBlack();

    Bitboard black = serialize(scene, blackPlayerFirst);
    Bitboard white = serialize(scene, !blackPlayerFirst);

    transform(black, white);

    std::map<Bitboard, std::map<Bitboard, TTEntry>>& map4 = map3[getBlackTeam(black)];

	const std::map<Bitboard, std::map<Bitboard, TTEntry>>::iterator& it4 = map4.find(black);
	if (it4 == map4.end()) {
		return NULL;
	}

	std::map<Bitboard, TTEntry>& map5 = it4->second;

	const std::map<Bitboard, TTEntry>::iterator& it5 = map5.find(white);
	if (it5 == map5.end()) {
		return NULL;
	}

    return &(it5->second);
}

inline int Tt::getEntryCount() const {
    return entryCount;
}

inline int Tt::getVela(const Fanorona& scene) const {
    return !scene.getVela() ? 2 : (scene.getVelaBlack() ? 1 : 0);
}

inline bool Tt::getCurrentPlayer(const Fanorona& scene) const {
    return scene.getCurrentPlayerBlack() != scene.getFirstPlayerBlack();
}

inline Bitboard Tt::serialize(const Fanorona& scene, const bool black) {
    return black ?
           scene.getBlackBitboard() :
           //Position::reversePosition(
           scene.getWhiteBitboard()
        //)
            ;
}

inline int Tt::getBlackTeam(const Bitboard black) const {
    Bitboard team = black & MAX_BLACK_TEAM_MASK;

    for (int i = MAX_BLACK_TEAM_COUNT_SHIFT; i < 64; i += MAX_BLACK_TEAM_COUNT_SHIFT) {
        team ^= (black & (MAX_BLACK_TEAM_MASK << i)) >> i;
    }

    return (int) team;
/*
    return (int) (
            ( black & 0x000000000000FFFFull) ^
            ((black & 0x00000000FFFF0000ull) >> 16) ^
            ((black & 0x0000FFFF00000000ull) >> 32) ^
            ((black & 0xFFFF000000000000ull) >> 48)
    );
*/
}

inline int Tt::getTTIndex(const int vela, const int black, const int deep) const {
    // vela * 2 * 2^X + black * 2^X + deep
    // return (vela << 6) + (black << 5) + deep;
    return (vela << (MAX_DEEP_SHIFT + 1)) + (black << MAX_DEEP_SHIFT) + deep;
}

inline void Tt::transform(Bitboard &black, Bitboard &white) const {
    Bitboard tempB = horizontalSymmetricPosition(black);
    Bitboard tempC = verticalSymmetricPosition(black);
    Bitboard tempD = reversePosition(black);

    if (tempB > black && tempB > tempC && tempB > tempD) {
        black = tempB;
        white = horizontalSymmetricPosition(white);
    } else if (tempC > black && tempC > tempB && tempC > tempD) {
        black = tempC;
        white = verticalSymmetricPosition(white);
    } else if (tempD > black && tempD > tempB && tempD > tempC) {
        black = tempD;
        white = reversePosition(white);
    }
}

#endif // TT_H
