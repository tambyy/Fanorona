
#include "FanoronaEngineManager.h"

#include <inttypes.h>
#include <android/log.h>
#define  LOG_TAG    "AKALANA"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

FanoronaEngineManager::FanoronaEngineManager(int searchCount) {
    Bitboards::init();

    this->searchCount = searchCount;

    searchs = new Search[searchCount];

    for (int i = 0; i < searchCount; ++i) {
        Tt* tt = new Tt();
        searchs[i].setTt(tt);
        initExistingTTEntry(tt);
    }

    returnChars = new char[128];
}

void FanoronaEngineManager::initExistingTTEntry(Tt* tt) {
    int ttentriesCount = 31;
    TTExistingEntry ttentries[] = {
            TTExistingEntry(17987857129930752ull, 2770203518ull, false),
            TTExistingEntry(17987719690977280ull, 2770269150ull, false),
            TTExistingEntry(17987444813070336ull, 2770301942ull, false),
            TTExistingEntry(17987994300448768ull, 2703225854ull, false),
            TTExistingEntry(17987994300448768ull, 2233463806ull, false),

            ////////////////////////////////////// 5

            TTExistingEntry(16298593610629120ull, 551988753406ull, true),
            TTExistingEntry(16861544503574528ull, 2770203646ull,   true),
            TTExistingEntry(8978595888234496ull,  2501768190ull,   true),
            TTExistingEntry(16861543564050432ull, 2770203646ull,   true),

            TTExistingEntry(17706381884784640ull, 2770301950ull,   true),
            TTExistingEntry(17987994166231040ull,  2434790398ull,   true),
            TTExistingEntry(17987993226706944ull, 2434790398ull,   true),

            TTExistingEntry(17846638437466112ull, 2770318326ull,   true),
            TTExistingEntry(17811419705638912ull,  2803839990ull,   true),
            TTExistingEntry(17952226047688704ull, 2770334678ull,   true),

            TTExistingEntry(16861407198838784ull,  2770334526ull,   true),
            TTExistingEntry(17706382153220096ull, 140142048126ull,   true),

            TTExistingEntry(17952500925595648ull, 2770236414ull,   true),

            ////////////////////////////////////// 18

            TTExistingEntry(16298594684370944ull, 2232775158ull, false),
            TTExistingEntry(16861269625733120ull, 2231432094ull, false),

            TTExistingEntry(16859345614536704ull, 2769679102ull, false),
            TTExistingEntry(16861543429963776ull, 2703094718ull, false),
            TTExistingEntry(16299143366443008ull, 2232807934ull, false),

            TTExistingEntry(16296395661115392ull, 549841138654ull, false),


            TTExistingEntry(17882166541156352ull, 2753451002ull, false),


            TTExistingEntry(16861956820434944ull, 2703094590ull, false),
            TTExistingEntry(16861407333056512ull, 2703225662ull, false),

            TTExistingEntry(17706381884915712ull, 140142048062ull, false),

            ////////////////////////////////////// 28

            TTExistingEntry(17882166406938624ull, 2753516506ull, true),
            TTExistingEntry(17882132173029376ull, 2770228210ull, true),
            TTExistingEntry(17882166406938624ull, 2753516506ull, true),

            ////////////////////////////////////// 31

    };

    Fanorona fanorona1;
    fanorona1.setCurrentPlayerBlack(true);
    for (int deep = 0; deep <= 12; ++deep) {
        for (int i = 0; i < ttentriesCount; ++i) {
            fanorona1.clear();
            fanorona1.addPieces(ttentries[i].black, true);
            fanorona1.addPieces(ttentries[i].white, false);
            fanorona1.setCurrentPlayerBlack(ttentries[i].blackFirst);
            tt->add(fanorona1, deep, TTEntry((MAX_SCORE - 100) * (deep % 2 == 0 ? 1 : -1), EXACT));
        }
    }
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
        ALOG(">> %" PRIu64 "ull %" PRIu64 "ull %d", fanorona.getBlackBitboard(), fanorona.getWhiteBitboard(), fanorona.getCurrentPlayerBlack());
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

