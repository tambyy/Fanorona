#ifndef SEARCH_H
#define SEARCH_H

#include <algorithm>
#include <chrono>
#include <thread>
#include "type.h"
#include "fanorona.h"
#include "move.h"
#include "bitboard.h"
#include "movepicker.h"
#include "evaluate.h"
#include "tt.h"
#include "movegen.h"

bool PieceMovementComparator(const PieceMoveSession& ms1, const PieceMoveSession& ms2);

class Search
{
private:
    Fanorona fanorona;

    int level = 8;
    unsigned int currentIterativeDeepeningLevel = 0;

    static constexpr unsigned int maxNodesCountToExploreForTT = 16;
    bool forPonder;

    unsigned int nodesCount = 0;
    unsigned int maxNodesCountToExplore = 0;

    /**
     * @brief maxEvaluationTime
     *        Temps d'évaluation maximum en microsecond
     */
    std::chrono::time_point<std::chrono::steady_clock, std::chrono::nanoseconds> startTime;
    unsigned int maxEvaluationTime = 0;
    unsigned int evaluationTime = 0;
    bool searchStoped = true;

    bool generateMovesSession = true;

    Evaluate evaluator;

    // PieceMove pieceMove = PieceMove(0ull, NULL_VECTOR, true);

    // PieceMoveSession* refutationTable0;
    PieceMoveSession* refutationTable[32];
    PieceMoveSession* lastMovePerDepth[32];

    PieceMoveSession uniqueMove;

    // bool addMovementSession = true;
    // Bitboard selectedPieceCatchedPieces = 0ull;

    Tt* playerMemory = NULL;
    unsigned int foundTTEntry = 0;

    void startSearching();

    bool pondering = false;
    std::thread ponderThread;

    /**
     *
     * Fonction de recherche principale
     *
     * @param deep
     *        profondeur de recherche
     *
     * @param alpha
     *        alpha
     *
     * @param beta
     *        beta
     *
     * @param movementSession
     *        liste des mouvements possibles avec leurs scores
     *
     * @return score de l'évaluation du noeud actuel
     */
    int search(const int depth, int alpha, int beta, int IDDepth = 0);
    int checkUniqueMovableCatchingPiece(int n);

    int quiesce();

    /**
     * On récupère les meilleurs mouvements
     * ayant le même score dans movementSessions
     *
     * @param movementSessions
     * @return
     */
    PieceMoveSession *getResults(PieceMoveSession *movementSessions, PieceMoveSession *last);

    static int getRandomInt(const int min, const int max);

public:
    Search();
    ~Search();

    void startPondering(const Fanorona& f);
    void stopPondering();

    /**
     * ITERATIVE DEEPENING
     *
     *
     * Itérer les profondeurs de recherche
     * debutant avec une faible profondeur incrémentée
     * jusqu'à ce qu'on atteigne la profondeur de recherche
     * maximale
     *
     *
     * Cela permet de:
     *
     * 1 - LIMITER LE TEMPS DE RECHERCHE
     *     cad que, qu'à chaque itération, on vérifie si le temps de recherche est depassé
     *     si oui, on se base sur le résultat de la recherche de l'itération précédente
     *
     * 2 - Trouver la solution la plus courte
     *     Ici, à l'itération minimum qui mène à une originPosition gagnante pour le joueur courant
     *     on arrête la recherche (on ne continue pas jusqu'au bout de l'itération)
     *
     * 3 - Et le plus important,
     *     Maximiser le nombre de coupures alpha-beta pour reduire le temps de recherche.
     *     Le principe c'est d'utiliser le résutat de la précédente itération pour l'itération actuelle.
     *     Trier ce résultat (meilleurs mouvements en premier de la liste).
     *     Pour ainsi, dans l'itération actuelle, évaluer les potentiels meilleurs mouvements en premier
     *     Afin de converger plus rapidement les valeurs de alpha et beta vers leurs valeurs optimales
     *     pour permettre plusieurs coupures dès le début de la recherche.
     *
     * Combiné avec la table de transposition, l'ITERATIVE DEEPENING devient très rapide
     *
     * @param forPonder
     * @return le meilleur mouvement trouvé
     */
    PieceMoveSession *evaluate(const Fanorona& f, bool forPonder);
    void ponderEvaluate(const Fanorona& f);

    int getLevel() const;

    int getNodesCount() const;

    void setTt(Tt* tt);

    Tt* getTt();

    PieceMoveSession* evaluate(const Fanorona &fanorona) {
        return evaluate(fanorona, false);
    }

    void adjustMaxNodesToExplore();

    int getEvaluationReachedDepth() const;
    int perft(int deep);
    void stopSearching();
    int getFoundTTEntry() const;
    int getEvaluationTime() const;
    void setMaxEvaluationTime(int value);
    void setLevel(int value);
};

inline int max(const int a, const int b) {
    return a > b ? a : b;
}

inline int min(const int a, const int b) {
    return a < b ? a : b;
}

inline bool PieceMovementComparator(const PieceMoveSession& ms1, const PieceMoveSession& ms2);

inline void Search::startSearching() {
    searchStoped = false;
}

inline void Search::stopSearching() {
    searchStoped = true;
}

inline void Search::setLevel(int value)
{
    level = value > 32 ? 32 : value;
}

inline int Search::getLevel() const
{
    return level;
}

inline int Search::getEvaluationReachedDepth() const
{
    return currentIterativeDeepeningLevel;
}

inline int Search::getNodesCount() const
{
    return nodesCount;
}

inline void Search::setMaxEvaluationTime(int value)
{
    maxEvaluationTime = value;
}

inline int Search::getEvaluationTime() const
{
    return evaluationTime;
}

inline void Search::setTt(Tt* tt) {
    playerMemory = tt;
}

inline Tt* Search::getTt() {
    return playerMemory;
}

inline void Search::adjustMaxNodesToExplore() {
    if (maxEvaluationTime > 0) {
        std::chrono::time_point<std::chrono::steady_clock, std::chrono::nanoseconds> endTime = std::chrono::high_resolution_clock::now();
        auto duration = (std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime)).count();
        if (duration >= 10000) {
            maxNodesCountToExplore = (((double) maxEvaluationTime * 1000) / (double) duration) * nodesCount;
        }
    }
}

inline int Search::getFoundTTEntry() const
{
    return foundTTEntry;
}

#endif // SEARCH_H
