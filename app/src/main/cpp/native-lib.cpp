#include <jni.h>
#include <string>
#include "FanoronaEngineManager.h"
#include <sys/types.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <android/log.h>

extern "C" {

// Application

JNIEXPORT jlong JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_init
        (JNIEnv *, jobject, jint searchCount)
{
    return reinterpret_cast<jlong>(new FanoronaEngineManager(searchCount));
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_initBoard
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->init();
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_terminate
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    delete reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_loadTTFile
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jobject assetManager)
{

    AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
    assert(NULL != mgr);

    char vela[sizeof(int)];
    char depth[sizeof(int)];
    bool black[sizeof(bool)];
    char blackBitboard[sizeof(Bitboard)];
    char whiteBitboard[sizeof(Bitboard)];
    char score[sizeof(int)];
    char type[sizeof(TTEntryType)];

    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);
    int searchsCount = fem->getSearchCount();
    Search* searchs = fem->getSearchs();

    AAssetDir* assetDir = AAssetManager_openDir(mgr, "");
    const char* filename;
    while ((filename = AAssetDir_getNextFileName(assetDir)) != NULL) {
        AAsset* asset = AAssetManager_open(mgr, filename, AASSET_MODE_STREAMING);

        while (AAsset_read(asset, vela, sizeof(int)) > 0) {
            AAsset_read(asset, depth,         sizeof(int));
            AAsset_read(asset, black,         sizeof(bool));
            AAsset_read(asset, blackBitboard, sizeof(Bitboard));
            AAsset_read(asset, whiteBitboard, sizeof(Bitboard));
            AAsset_read(asset, score,         sizeof(int));
            AAsset_read(asset, type,          sizeof(TTEntryType));

            TTEntry entry(*((int*) score), (TTEntryType) type[0]);

            for (int i = 0; i < searchsCount; ++i) {
                Tt* tt = searchs[i].getTt();

                int d = *((int*) vela);
                __android_log_print(ANDROID_LOG_DEBUG, "Debug", ">>> %d", d);

                /*if (tt != NULL) {
                    tt->add(0, (int) d, (bool) black[0], *((Bitboard*) blackBitboard), *((Bitboard*) whiteBitboard), entry);
                }*/
            }

            //__android_log_print(ANDROID_LOG_INFO, "Test Asset Manager", "%d %d %d %llu %llu %d %d", (int) vela[0], (int) depth[0], (bool) black[0], *((Bitboard*) blackBitboard), *((Bitboard*) whiteBitboard), *((int*) score), (TTEntryType) type[0]);
        }
        AAsset_close(asset);
    }
    AAssetDir_close(assetDir);
}


// Mode

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setMode
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint mode)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->setMode(mode);
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getVela
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getVela();
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getVelaBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getVelaBlack();
}

JNIEXPORT int JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getFirstPlayerBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getFirstPlayerBlack();
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setFirstPlayerBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint black)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->setFirstPlayerBlack(black);
}

JNIEXPORT int JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getCurrentPlayerBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getCurrentPlayerBlack();
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setCurrentPlayerBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, int black)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->setCurrentPlayerBlack(black);
}


// Position

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_blackPosition
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->blackPosition());
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_whitePosition
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->whitePosition());
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setPositions
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jstring black, jstring white)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->setPositions(env->GetStringUTFChars(black, 0), env->GetStringUTFChars(white, 0));
}


// Movements

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_selectPiece
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jint x, jint y, jstring traveledPositions, jint lastVector)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->selectPiece(x, y, env->GetStringUTFChars(traveledPositions, 0), lastVector);
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_selectedPiece
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->selectedPiece());
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_movePiece
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jint vector, jint percute)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->movePiece(vector, percute));
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_stopMove
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->stopMove();
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_movablePieces
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->movablePieces());
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_movableVectors
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->movableVectors());
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_canCatch
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint vector, jint percute)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->canCatch(vector, percute);
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_currentBlack
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->currentBlack();
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_removedPieces
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jint vector, jint percute)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->removedPieces(vector, percute));
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_moveSessionOpened
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->moveSessionOpened();
}

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_traveledPositions
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->traveledPositions());
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_lastVector
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->lastVector();
}


// Search

JNIEXPORT jstring JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_search
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return env->NewStringUTF(fem->doSearch(index));
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_ponder
        (JNIEnv *env, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->ponder(index);
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setSearchDepth
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index, jint depth)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->setSearchDepth(index, depth);
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_setSearchMaxTime
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index, jint maxTime)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->setSearchMaxTime(index, maxTime);
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getLastSearchTime
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getLastSearchTime(index);
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getLastSearchDepth
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getLastSearchDepth(index);
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_getLastNodesCount
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->getLastNodesCount(index);
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_stopSearch
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->stopSearch(index);
}

JNIEXPORT void JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_clearTT
        (JNIEnv *, jobject, jlong fanoronaEnginePtr, jint index)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    fem->clearTT(index);
}


// End game

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_gameOver
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->gameOver();
}

JNIEXPORT jint JNICALL Java_com_tambyy_fanoronaakalana_engine_Engine_winner
        (JNIEnv *, jobject, jlong fanoronaEnginePtr)
{
    FanoronaEngineManager* fem = reinterpret_cast<FanoronaEngineManager*>(fanoronaEnginePtr);

    return fem->winner();
}

}
