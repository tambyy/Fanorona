
#include "FanoronaEngineManager.h"

FanoronaEngineManager::FanoronaEngineManager(int searchCount) {
    initMobableBitboard();
    generateAllOccupations();
    initAttack();

    this->searchCount = searchCount;

    searchs = new Search[searchCount];

    for (int i = 0; i < searchCount; ++i) {
        searchs[i].setTt(new Tt());
    }

    returnChars = new char[128];
}

int FanoronaEngineManager::vectorToInt(Vector vector) {
    switch (vector) {
        case TOP:
            return 8;
        case RIGHT:
            return 6;
        case BOTTOM:
            return 2;
        case LEFT:
            return 4;
        case LEFT_TOP:
            return 7;
        case RIGHT_TOP:
            return 9;
        case RIGHT_BOTTOM:
            return 3;
        case LEFT_BOTTOM:
            return 1;
        default:
            return 0;
    }
}

Vector FanoronaEngineManager::intToVector(int i) {
    switch (i) {
        case 8:
            return TOP;
        case 6:
            return RIGHT;
        case 2:
            return BOTTOM;
        case 4:
            return LEFT;
        case 7:
            return LEFT_TOP;
        case 9:
            return RIGHT_TOP;
        case 3:
            return RIGHT_BOTTOM;
        case 1:
            return LEFT_BOTTOM;
        default:
            return NULL_VECTOR;
    }
}

char FanoronaEngineManager::intToChar(int i) {
    return (char) (i + 48);
}

int FanoronaEngineManager::charToInt(char c) {
    return (int) (c - 48);
}

void FanoronaEngineManager::positionsToChars(Bitboard positions, char* returnChars) {
    unsigned int i = 0;

    for (Bitboard b = ZARAN_DAKA_1; b <= ZARAN_DAKA_4; b <<= 1) {
        if (b & positions) {
            returnChars[i] = intToChar(getXPosition(b));
            returnChars[i + 1] = intToChar(getYPosition(b));

            i += 2;
        }
    }

    returnChars[i] = '\0';
}

void FanoronaEngineManager::init() {
    fanorona.clear();
    fanorona.addPieces(INITIAL_BLACK_POSITION, true);
    fanorona.addPieces(INITIAL_WHITE_POSITION, false);
}

void FanoronaEngineManager::setMode(int mode) {
    if (mode == 0) {
        fanorona.setMode(MODE_RIATRA);
    } else {
        fanorona.setMode(MODE_VELA);
        fanorona.setVelaBlack(mode == 1);
    }
}

int FanoronaEngineManager::getVela() {
    return fanorona.getVela() ? 1 : 0;
}

int FanoronaEngineManager::getVelaBlack() {
    return fanorona.getVelaBlack() ? 1 : 0;
}

void FanoronaEngineManager::setFirstPlayerBlack(int black) {
    fanorona.setFirstPlayerBlack(black != 0);
}

int FanoronaEngineManager::getFirstPlayerBlack() {
    return fanorona.getFirstPlayerBlack() ? 1 : 0;
}

void FanoronaEngineManager::setCurrentPlayerBlack(int black) {
    return fanorona.setCurrentPlayerBlack(black != 0);
}

int FanoronaEngineManager::getCurrentPlayerBlack() {
    return fanorona.getCurrentPlayerBlack() ? 1 : 0;
}


char* FanoronaEngineManager::blackPosition() {
    positionsToChars(fanorona.getBlackBitboard(), returnChars);

    return returnChars;
}

char* FanoronaEngineManager::whitePosition() {
    positionsToChars(fanorona.getWhiteBitboard(), returnChars);

    return returnChars;
}

void FanoronaEngineManager::setPositions(const char* blackPosition, const char* whitePosition) {
    fanorona.clear();

    for (int i = 0; blackPosition[i] != '\0'; i += 2) {
        fanorona.addPieces(p(charToInt(blackPosition[i]), charToInt(blackPosition[i + 1])), true);
    }

    for (int i = 0; whitePosition[i] != '\0'; i += 2) {
        fanorona.addPieces(p(charToInt(whitePosition[i]), charToInt(whitePosition[i + 1])), false);
    }
}

int FanoronaEngineManager::selectPiece(int x, int y, const char* traveledPositions, int lastVector) {
    int selectedPiece = fanorona.setSelectedPiecePosition(p(x, y)) ? 1 : 0;

    for (int i = 0; traveledPositions[i] != '\0'; i += 2) {
        fanorona.addTraveledPosition(p(charToInt(traveledPositions[i]), charToInt(traveledPositions[i + 1])));
    }

    fanorona.setLastVector(intToVector(lastVector));

    return selectedPiece;
}

char* FanoronaEngineManager::selectedPiece() {
    returnChars[0] = intToChar(getXPosition(fanorona.getSelectedPieceCurrentPosition()));
    returnChars[1] = intToChar(getYPosition(fanorona.getSelectedPieceCurrentPosition()));
    returnChars[2] = '\0';

    return returnChars;
}

char* FanoronaEngineManager::movePiece(int vector, int percute) {
    if (fanorona.getSelectedPieceCurrentPosition() != 0) {
        Bitboard removedPieces = fanorona.moveSelectedPiece(intToVector(vector), percute == 1);

        positionsToChars(removedPieces, returnChars);
    } else {
        returnChars[0] = '\0';
    }

    return returnChars;
}

char* FanoronaEngineManager::removedPieces(int vector, int percute) {
    if (fanorona.getSelectedPieceCurrentPosition() != 0) {
        Bitboard removedPieces = fanorona.removedPieces(intToVector(vector), percute == 1);

        positionsToChars(removedPieces, returnChars);
    } else {
        returnChars[0] = '\0';
    }

    return returnChars;
}

int FanoronaEngineManager::stopMove() {
    return fanorona.closeMovementsSession() ? 1 : 0;
}

char* FanoronaEngineManager::movablePieces() {
    Bitboard b = 0;

    MovePicker mp(fanorona);

    for (PieceMove pieceMove; mp.nextMove(pieceMove);) {
        b |= pieceMove.piecePosition;
    }

    positionsToChars(b, returnChars);

    return returnChars;
}

char* FanoronaEngineManager::movableVectors() {
    int i = 0;
    MovePicker mp(fanorona);

    for (PieceMove pieceMove; mp.nextMove(pieceMove);) {
        if (pieceMove.piecePosition == fanorona.getSelectedPieceCurrentPosition()) {
            returnChars[i++] = intToChar(vectorToInt(pieceMove.vector));
            returnChars[i++] = intToChar(pieceMove.percute ? 1 : 0);
        }
    }

    returnChars[i] = '\0';

    return returnChars;
}

int FanoronaEngineManager::canCatch(int vector, int percute) {
    return fanorona.canCatchPiece(intToVector(vector), percute == 1) ? 1 : 0;
}

int FanoronaEngineManager::currentBlack() {
    return fanorona.getCurrentPlayerBlack() ? 1 : 0;
}

int FanoronaEngineManager::moveSessionOpened() {
    return fanorona.isMovementSessionOpened() ? 1 : 0;
}

char* FanoronaEngineManager::traveledPositions() {
    positionsToChars(fanorona.getTraveledPositions(), returnChars);

    return returnChars;
}

int FanoronaEngineManager::lastVector() {
    return vectorToInt(fanorona.getLastVector());
}

char* FanoronaEngineManager::doSearch(int i) {
    PieceMoveSession* pms = searchs[i].evaluate(fanorona);

    returnChars[0] = intToChar(getXPosition(pms->originPosition));
    returnChars[1] = intToChar(getYPosition(pms->originPosition));

    int j = 0;

    for (; j < pms->moveCount; ++j) {
        Move move = pms->moves[j];

        returnChars[2 * j + 2] = intToChar(vectorToInt(move.vector));
        returnChars[2 * j + 3] = intToChar(move.percute ? 1 : 0);
    }

    returnChars[2 * j + 2] = '\0';

    return returnChars;
}

void FanoronaEngineManager::ponder(int i) {
    searchs[i].startPondering(fanorona);
}

void FanoronaEngineManager::setSearchDepth(int i, int depth) {
    searchs[i].setLevel(depth);
}

void FanoronaEngineManager::setSearchMaxTime(int i, int maxTime) {
    searchs[i].setMaxEvaluationTime(maxTime);
}

int FanoronaEngineManager::getLastSearchTime(int i) {
    return searchs[i].getEvaluationTime();
}

int FanoronaEngineManager::getLastSearchDepth(int i) {
    return searchs[i].getEvaluationReachedDepth();
}

int FanoronaEngineManager::getLastNodesCount(int i) {
    return searchs[i].getNodesCount();
}

void FanoronaEngineManager::stopSearch(int i) {
    return searchs[i].stopSearching();
}

void FanoronaEngineManager::clearTT(int i) {
    searchs[i].getTt()->clear();
}

int FanoronaEngineManager::gameOver() {
    return fanorona.gameOver();
}

int FanoronaEngineManager::winner() {
    return fanorona.winner();
}

int FanoronaEngineManager::getSearchCount() const {
    return searchCount;
}

Search* FanoronaEngineManager::getSearchs() {
    return searchs;
}

FanoronaEngineManager::~FanoronaEngineManager() {
    for (int i = 0; i < searchCount; ++i) {
        delete searchs[i].getTt();
    }

    delete[] searchs;
    delete[] returnChars;
}

