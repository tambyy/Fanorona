//
// Created by Fidoral on 31/07/2020.
//

#ifndef FANORONA_FANORONAENGINEMANAGER_H
#define FANORONA_FANORONAENGINEMANAGER_H

#include <string>
#include "akalana/search.h"

class FanoronaEngineManager {
public:
    FanoronaEngineManager(int searchCount);

    static int vectorToInt(Vector vector);
    static Vector intToVector(int i);

    static char intToChar(int i);
    static int charToInt(char c);

    static void positionsToChars(Bitboard positions, char* returnChars);

    void init();

    void setMode(int mode);
    int getVela();
    int getVelaBlack();
    void setFirstPlayerBlack(int black);
    int getFirstPlayerBlack();
    void setCurrentPlayerBlack(int black);
    int getCurrentPlayerBlack();

    char* blackPosition();
    char* whitePosition();
    void setPositions(const char* blackPosition, const char* whitePosition);

    int selectPiece(int x, int y, const char* traveledPositions, int lastVector);
    char* selectedPiece();
    char* movePiece(int vector, int percute);
    char* removedPieces(int vector, int percute);
    int stopMove();
    char* movablePieces();
    char* movableVectors();
    int canCatch(int vector, int percute);
    int currentBlack();
    int moveSessionOpened();
    char* traveledPositions();
    int lastVector();

    char* doSearch(int i);
    void ponder(int i);
    void setSearchDepth(int i, int depth);
    void setSearchMaxTime(int i, int maxTime);
    int getLastSearchTime(int i);
    int getLastSearchDepth(int i);
    int getLastNodesCount(int i);
    void stopSearch(int i);
    void clearTT(int i);

    int gameOver();
    int winner();

    int getSearchCount() const;

    Search* getSearchs();

    virtual ~FanoronaEngineManager();

private:
    int searchCount;
    Fanorona fanorona;
    Search* searchs;

    char* returnChars;
};


#endif //FANORONA_FANORONAENGINEMANAGER_H
