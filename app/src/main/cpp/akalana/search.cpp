#include "search.h"
#include <iostream>

#include <android/log.h>
#define  LOG_TAG    "AKALANA"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

Search::Search()
{
    for (int i = 0; i < 32; ++i) {
        refutationTable[i] = new PieceMoveSession[MAX_MOVES_COUNT];
    }
}

Search::~Search()
{
    for (int i = 0; i < 32; ++i) {
        delete[] refutationTable[i];
    }
}

int Search::search(const int depth, int alpha, int beta, const int IDDepth) {

    if (fanorona.getWhiteBitboard() == (p(1,1)|p(1,3)|p(1,5)|p(2,2)|p(2,3)|p(2,4)|p(1,8)|p(3,1)|p(3,3))) {
        ALOG("%d", fanorona.getCurrentPlayerBlack());
        ALOG("%s", fanorona.toString().c_str());
    }


    if (!forPonder && maxNodesCountToExplore > 0 && maxNodesCountToExplore <= nodesCount) {
        stopSearching();
    }

    // 0x3fff = (1 << 14) - 1
    // nodesCount & 0x3fff = nodesCount % (1 << 14)
    if ((nodesCount & 0x3fff) == 0) {
        adjustMaxNodesToExplore();
    }


    //////////////////////////////////////////////////////////////////


    // si le temps maximum d'évaluation est dépassé
    if (searchStoped) {
        // on arrête toutes recherches
        return TIME_ELAPSED_SCORE;
    }


    //////////////////////////////////////////////////////////////////


    // on incrémente le
    // nombre de noeuds parcourus
    // pendant cette recherche
    ++nodesCount;


    //////////////////////////////////////////////////////////////////


    // JOUEUR GAGNANT

    if (fanorona.gameOver()) {
        return fanorona.winner() == fanorona.getCurrentPlayerBlack() ? MAX_SCORE : MIN_SCORE;
    }


        //////////////////////////////////////////////////////////////////


        // EVALUATION DE LA SCENE

        // quand on rencontre une feuille,
        // c'est à dire qu'on arrive au bout de la profondeur
    else if (depth <= 0) {
        // on incrémente le
        // nombre de feuilles parcourues
        // pendant cette recherche
        // ++leavesCount;

        // Quiscient search
        /*if (fanorona.canCatchPiece()) {
            return -quiesce();
        }*/

        // et on renvoie comme score
        // l'évaluation de la scene

        if (fanorona.getVela()) {
            return evaluator.evaluateScene(fanorona);
        } else {
            // return evaluator.evaluateScene(fanorona);
            return checkUniqueMovableCatchingPiece(0);
        }
    }


    //////////////////////////////////////////////////////////////////


    // TRANSPOSTION TABLE

    int bestScore = MIN_SCORE;
    const int oldAlpha = alpha;
    unsigned int childNodesCount = nodesCount;
    TTEntry* entry = NULL;

    if (playerMemory && IDDepth == 0) {

        // Vérifier l'occurence de la configuration actuelle de la scene
        // dans la table de transposition
        entry = playerMemory->get(fanorona, depth);

        if (entry) {
            foundTTEntry++;

            // EXACT
            if (entry->type & EXACT) {
                return entry->score;
                // LOWER BOUND
            } else if (entry->type & LOWERBOUND) {
                if (entry->score > alpha) {
                    alpha = entry->score;
                }
                // UPPER BOUND
            } else if (entry->type & UPPERBOUND && entry->score < beta) {
                beta = entry->score;
            }

            // élagage alpha beta
            if (alpha > beta) {
                return entry->score;
            }
        }
    }


    //////////////////////////////////////////////////////////////////


    // FUTILITY MARGIN
/*
    if (depth <= 2) {
        int eval = evaluator.evaluateScene(fanorona);

        if (eval + (fanorona.canCatchPiece() ? 189000 : 63000) / bitCount(fanorona.getThemBitboard()) < alpha) {
            return eval;
        }
    }
*/

    //////////////////////////////////////////////////////////////////


    // RECHERCHE AVEC DES PROFONDEURS ITERATIVES

    // score attribué au noeud fils courant

    const int moveGenDepth = IDDepth > 0 ? IDDepth : depth;
    PieceMoveSession* pms = refutationTable[moveGenDepth];


    //////////////////////////////////////////////////////////////////


    // INTERNAL ITERATIVE DEEPENING

    // pour chaque noeud dont
    // la profondeur est supérieure ou égale à 3
    if (depth >= 5 && depth != currentIterativeDeepeningLevel) {
        // Effectuer une petite recherche à profondeur faible (1 ou 2).
        // 1 pour 4
        // 2 pour toutes les profondeurs > 4

        search(depth <= 8 ? 2 : 3, MIN_SCORE, MAX_SCORE, moveGenDepth);

        std::sort(pms, lastMovePerDepth[moveGenDepth], &PieceMovementComparator);

        if (searchStoped) {
            return TIME_ELAPSED_SCORE;
        }
    }

    else if (depth == 1 || depth != currentIterativeDeepeningLevel) {
        lastMovePerDepth[moveGenDepth] = generate(fanorona, pms, generateMovesSession);
    }


    //////////////////////////////////////////////////////////////////


    PieceMoveSession* last = lastMovePerDepth[moveGenDepth];
    int movesCount = last - pms;

    if (movesCount == 0) {
        return MIN_SCORE;
    }

    assert(movesCount > 0);


    //////////////////////////////////////////////////////////////////


    // PARCOURS EN PRONFONDEUR

    const int
    // prochaine profondeur
    nextDepth = depth - 1,
    // alpha de la prochaine profondeur
    _beta = -beta;

    int score;

    // MovePicker movePicker(fanorona);

    //////////////////////////////////////////////////////////////////

    const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();

    PieceMoveSession* last_1 = last - 1;

    const bool futilityTest =
            true &&
            depth == 1 &&
            IDDepth == 0 &&
            depth != currentIterativeDeepeningLevel &&
            last_1->removedPiecesCount > 1;

    // pour chaque mouvement possible

    for (PieceMoveSession* it = last_1; it >= pms; --it) {
        if (futilityTest && it->removedPiecesCount < last_1->removedPiecesCount) {
            break;
        }

        // Supprimer de la table
        // la pièce actuelle de sa originPosition d'origine et
        // les pièces adverses éliminées
        fanorona.removePieces(it->originPosition | it->catchedPieces);
        // Deplacer la pièce actuelle vers sa position conste
        fanorona.addPieces(it->lastPosition, currentPlayerBlack);

        fanorona.setCurrentPlayerBlack(!currentPlayerBlack);

        //////////////////////////////////////////////////////////////////

        // PARCOURS EN PROFONDEUR

        if ((it == last_1 || depth <= 2)) {
            // parcours en profondeur
            // profondeur suivante pour l'autre joueur
            score = -search(nextDepth, _beta, -alpha);
        } else {
            if (false && fanorona.getMode() == GameMode::MODE_VELA && it < last - 2 && nextDepth >= 4) {
                // LMR
                const int reducedDepth = nextDepth - 1;

                score = -search(reducedDepth, -(alpha+1), -alpha);
                if (alpha < score) {
                    score = -search(nextDepth, -(alpha+1), -alpha);
                }
            } else {
                score = -search(nextDepth, -(alpha+1), -alpha);
            }

            if (alpha < score && score < beta) {
                score = -search(nextDepth, _beta, -score, 0);
            }
        }

        //////////////////////////////////////////////////////////////////

        // RECULE

        // Se remettre à la configuration
        // avant deplacement de la pièce
        //fanorona.undoSession(it->lastPosition);
        fanorona.setCurrentPlayerBlack(!fanorona.getCurrentPlayerBlack());
        fanorona.undoMove(it->lastPosition, it->originPosition, it->catchedPieces);

        //////////////////////////////////////////////////////////////////

        // si le temps maximum d'évaluation est dépassé
        if (searchStoped) {
            // Arrêter toutes recherches
            it->score = bestScore = TIME_ELAPSED_SCORE;

            while (--it >= pms) {
                it->score = TIME_ELAPSED_SCORE;
            }

            break;
        } else {
            it->score = score;
        }

        //////////////////////////////////////////////////////////////////

        // SCORE const

        if (bestScore < score) {
            bestScore = score;

            // élagage
            if (alpha < bestScore) {
                alpha = bestScore;
                if (beta < alpha) {
                    break;
                }
            }
        }
    }


    //////////////////////////////////////////////////////////////////


    // TRANSPOSITION TABLE

    childNodesCount = nodesCount - childNodesCount;

    // Filtrer les mouvements/scores à mettre dans la table de transposition
    if (playerMemory && childNodesCount >= (forPonder ? 64 : 20) && bestScore != TIME_ELAPSED_SCORE && std::abs(bestScore) != MAX_SCORE) {

        // const bool saveScore = childNodesCount >= maxNodesCountToSaveForTT;

        // Type de l'entrée à mettre dans la table de transposition
        const TTEntryType type =
                // UPPERBOUND
                bestScore <= oldAlpha ? UPPERBOUND :
                // LOWERBOUND
                bestScore >= beta ? LOWERBOUND :
                // EXACT: Aucun élagage alpha-beta
                EXACT;

        // Pour une entrée déjà enregistrée dans la table de transposition,
        // Modifier le type et le score de l'entrée
        if (entry) {
            entry->type = type;
            entry->score = bestScore;

            // entry->save = entry->save || saveScore;
        } else {
            // Sinon,
            // ajouter une nouvelle entrée dans la TT
            playerMemory->add(fanorona, depth, TTEntry(bestScore, type));
        }

    }


    //////////////////////////////////////////////////////////////////


    return bestScore;
}

int Search::checkUniqueMovableCatchingPiece(int n) {
    if (fanorona.gameOver()) {
        return fanorona.winner() == fanorona.getCurrentPlayerBlack() ? MAX_SCORE - n : MIN_SCORE + n;
    }

    int score;
    uniqueMove.init();
    const bool moveSequenceOver = uniqueMovableCatchingPiece(fanorona, uniqueMove);

    if (uniqueMove.moveCount > 0) {
        if (moveSequenceOver) {
            const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();
            const Bitboard umOriginPosition = uniqueMove.originPosition;
            const Bitboard umLastPosition = uniqueMove.lastPosition;
            const Bitboard umCatchedPieces = uniqueMove.catchedPieces;

            // do move
            fanorona.removePieces(umOriginPosition | umCatchedPieces);
            fanorona.addPieces(umLastPosition, currentPlayerBlack);

            ++nodesCount;

            // if move sequence over
            // continue
            fanorona.setCurrentPlayerBlack(!currentPlayerBlack);
            score = -checkUniqueMovableCatchingPiece(n + 1);
            fanorona.setCurrentPlayerBlack(currentPlayerBlack);

            // undo move
            fanorona.undoMove(umLastPosition, umOriginPosition, umCatchedPieces);

        } else {
            // evaluate
            score = evaluator.evaluateScene(fanorona) - n;
        }
    } else {
        score = evaluator.evaluateScene(fanorona) - n;
    }

    return score;
}

int Search::quiesce()
{
    PieceMoveSession* pms = refutationTable[0];
    PieceMoveSession* best = generate(fanorona, pms, false) - 1;

    /*int bestScore = bitCount(best->catchedPieces);
    for (PieceMoveSession* it = best - 1; it >= pms; --it) {
        int score = bitCount(it->catchedPieces);

        if (score > bestScore) {
            best = it;
            bestScore = score;
        }
    }
*/
    const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();

    // Supprimer de la table
    // la pièce actuelle de sa originPosition d'origine et
    // les pièces adverses éliminées
    fanorona.removePieces(best->originPosition | best->catchedPieces);
    // Deplacer la pièce actuelle vers sa position conste
    fanorona.addPieces(best->lastPosition, currentPlayerBlack);

    fanorona.setCurrentPlayerBlack(!currentPlayerBlack);

    //////////////////////////////////////////////////////////////////

    // PARCOURS EN PROFONDEUR

    int score;

    if (fanorona.gameOver()) {
        score = fanorona.winner() == fanorona.getCurrentPlayerBlack() ? MAX_SCORE : MIN_SCORE;
    } else {
        score = evaluator.evaluateScene(fanorona);
    }

    //////////////////////////////////////////////////////////////////

    // RECULE

    // Se remettre à la configuration
    // avant deplacement de la pièce
    //fanorona.undoSession(it->lastPosition);
    fanorona.setCurrentPlayerBlack(!fanorona.getCurrentPlayerBlack());
    fanorona.undoMove(best->lastPosition, best->originPosition, best->catchedPieces);

    return score;
}

PieceMoveSession* Search::getResults(PieceMoveSession* pms, PieceMoveSession* last) {
    PieceMoveSession* it = last - 1;

    int score = it->score;
    for (; it >= pms; --it) {
        if (score != it->score) {
            break;
        }
    }

    return it + 1;
}

int Search::getRandomInt(const int min, const int max) {
    srand(time(NULL));
    return rand() % (max - min + 1) + min;
}

PieceMoveSession* Search::evaluate(const Fanorona &f, bool forPonder) {

    if (!forPonder) {
        //stopPondering();
    }

    this->forPonder = forPonder;

    fanorona = f;

    startTime = std::chrono::high_resolution_clock::now();

    startSearching();

    // on réinitialise le nombre
    // de noeuds et de feuilles parcourus
    nodesCount = 0;
    foundTTEntry = 0;

    //////////////////////////////////////////////////////////////////

    int nbPieces1 = bitCount(fanorona.getCurrentPlayerBlack() ? fanorona.getBlackBitboard() : fanorona.getWhiteBitboard()),
            nbPieces2 = bitCount(fanorona.getCurrentPlayerBlack() ? fanorona.getWhiteBitboard() : fanorona.getBlackBitboard());

    int nombrePieces = nbPieces1 + nbPieces2;

    // 2 - l'évaluation de originPosition des pièces
    // quand le nombre total de pièces est <= 30
    // pour permettre aux pièces du joueur courant
    // d'occuper certaines positions plus bénéfiques
    // et de repousser les pièces adverses vers des positions moins bénéfiques
    if (nombrePieces <= 30) {
        evaluator.setGamePhase(MIDDLE_GAME);
    }

    // 3 - l'évaluation d'approche
    // qui calcule la moyenne de distance des pièces du joueur courant
    // par rapport aux pièces adverses
    // Le but c'est de permettre aux pièces du joueur courant
    // de chasser les pièces adverses quand elles sont éloignées et moins nombreuses
    // ou de les fuire quand les pièces adverses sont par contre beaucoup plus nombreuses
    if (nbPieces1 <= 4 || nbPieces2 <= 4) {
        evaluator.setGamePhase(END_GAME);
    }

    //////////////////////////////////////////////////////////////////

    // ITERATIVE DEEPENING

    // on incrémente de 1 niveau à chaque itération
    int searchValue = 0;

    generateMovesSession = true;

    // pour les profondeurs de recherche initiales paires,
    // on commence la recherche itérative à partir d'une profondeur de niveau 2
    // et celles impaires à partir d'une profondeur de niveau 1
    int n = (forPonder ? std::max(level + 3, 8) : level);
    for (
            currentIterativeDeepeningLevel = 1;
        // tant qu'on n'atteind pas le niveau du joeur intelligent dans l'itération
            currentIterativeDeepeningLevel <= n;
        // on incrémente chaque itération par 2 niveaux
            ++currentIterativeDeepeningLevel
    ) {
        //std::cout << currentIterativeDeepeningLevel << " ";
        //ALOG("%d: %d", (int) forPonder, currentIterativeDeepeningLevel);
        ALOG(" >> %d %d %d %d %d", currentIterativeDeepeningLevel, (int) forPonder, (int) pondering, n, nodesCount);

        //if (!pondering) {
        //std::cout << currentIterativeDeepeningLevel << " ";
        //}

        PieceMoveSession* pms = refutationTable[currentIterativeDeepeningLevel];

        int
                alpha = MIN_SCORE,
                beta  = MAX_SCORE,
                delta = 0;

        if (currentIterativeDeepeningLevel >= 3) {
            delta = 3300/*0 / bitCount(fanorona.getNextPlayerBitboard())*/;
            if (currentIterativeDeepeningLevel % 2 == 0) {
                alpha = std::max(searchValue + delta, MIN_SCORE);
                beta = std::min(searchValue + 5 * delta / 4, MAX_SCORE);
            } else {
                alpha = std::max(searchValue - delta, MIN_SCORE);
                beta = std::min(searchValue - 5 * delta / 4, MAX_SCORE);
            }
        }


        // ASPIRATION SEARCH

        while (true) {
            // on effectue une recherche avec currentIterativeDeepeningLevel
            // qui contient la profondeur d'itération actuelle
            searchValue = search(currentIterativeDeepeningLevel, alpha, beta, currentIterativeDeepeningLevel);

            if (searchValue <= alpha && alpha != MIN_SCORE) {
                beta = (alpha + beta) / 2;
                alpha = max(searchValue - delta, MIN_SCORE);
            } else if (searchValue >= beta && beta != MAX_SCORE) {
                //alpha = (alpha + beta) / 2;
                beta = min(searchValue + delta, MAX_SCORE);
            } else {
                break;
            }

            if (searchStoped) {
                break;
            }

            delta += delta / 4 + 5;
        }

        generateMovesSession = false;

        //std::cout << searchValue << std::endl;

        PieceMoveSession* last = lastMovePerDepth[currentIterativeDeepeningLevel];

        int size = last - pms;

        if (searchStoped) {
            bool useCurrentIDScore = false;

            int score = (last - 1)->score;
            if (std::abs(score) != MAX_SCORE && score != TIME_ELAPSED_SCORE) {
                for (PieceMoveSession* it = last - 1; it >= pms; --it) {
                    if (it->score > score) {
                        useCurrentIDScore = true;
                        break;
                    }
                }
            }

            if (!useCurrentIDScore) {
                for (PieceMoveSession* it = pms; it != last; ++it) {
                    it->score = it->previousScore;
                }
            }

            break;
        }

        // si le meilleur mouvement de la liste est un mouvement gagnant
        // OU il n'y a qu'un mouvement possible
        // OU c'est la dernière itération
        // OU le temps de recherche est depassé

        std::sort(pms, last, &PieceMovementComparator);

        // Vérifier s'il s'agit du premier mouvement
        // Si oui, on va considérer que tous les mouvements possibles ont la même score
        if (
                (fanorona.getBlackBitboard() == INITIAL_BLACK_POSITION && fanorona.getWhiteBitboard() == INITIAL_WHITE_POSITION) ||
                (fanorona.getBlackBitboard() == INITIAL_WHITE_POSITION && fanorona.getWhiteBitboard() == INITIAL_BLACK_POSITION) ||
                (fanorona.getBlackBitboard() == SYM_INITIAL_BLACK_POSITION && fanorona.getWhiteBitboard() == SYM_INITIAL_WHITE_POSITION) ||
                (fanorona.getBlackBitboard() == SYM_INITIAL_WHITE_POSITION && fanorona.getWhiteBitboard() == SYM_INITIAL_BLACK_POSITION)
        ) {
            for (PieceMoveSession* it = pms; it != last; ++it) {
                if (it->score != MIN_SCORE && it->score != MAX_SCORE) {
                    it->score = 0;
                }
            }

            if (!forPonder) {
                break;
            }
        }

        if (
                (!forPonder && size <= 1) ||
                std::abs((last - 1)->score) == MAX_SCORE/* ||
                currentIterativeDeepeningLevel == level*/
                ) {
            // on arrête la recherche avec profondeur itérative
            break;
        }

        // copie pour l'itération suivante
        int nextDepth = currentIterativeDeepeningLevel + 1;

        for (PieceMoveSession* it = pms, *it2 = refutationTable[nextDepth]; it != last; ++it, ++it2) {
            *it2 = *it;
            it2->previousScore = it->score;
        }

        lastMovePerDepth[nextDepth] = refutationTable[nextDepth] + size;

        adjustMaxNodesToExplore();

    }


    //////////////////////////////////////////////////////////////////


    // on récupère aléatoirement un mouvement
    // parmi les meilleurs mouvements
    // des mouvements possibles
    PieceMoveSession* last = lastMovePerDepth[currentIterativeDeepeningLevel];
    PieceMoveSession* ms = last - 1;

    // si tous les meilleurs mouvement mène vers une originPosition perdant
    // pour le joueur courant,
    // alors on renvoie juste le premier mouvement de la liste
    if (ms->score != MIN_SCORE) {
        PieceMoveSession* lastResult = getResults(refutationTable[currentIterativeDeepeningLevel], last);
        ms = last - 1 - getRandomInt(0, last - lastResult - 1);
    }

    stopSearching();

    /*if (!forPonder) {
        startPondering(f);
    }*/

    auto endTime = std::chrono::high_resolution_clock::now();
    evaluationTime = (std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime)).count();

    for (PieceMoveSession* it = last - 1; it >= refutationTable[currentIterativeDeepeningLevel]; --it) {
        //ALOG(" >> %s", it->toString().c_str());
    }
    return ms;
}

void Search::ponderEvaluate(const Fanorona &f) {
    pondering = true;
    evaluate(f, true);
    pondering = false;
}

void Search::startPondering(const Fanorona& f) {
    // ponderThread = std::thread(&Search::ponderEvaluate, this, f);
    evaluate(f, true);
}

void Search::stopPondering() {
    if (pondering) {
        stopSearching();
        if (ponderThread.joinable()) {
            ponderThread.join();
        }
    }
}

int Search::perft(int deep)
{
    int nodes = 0;

    PieceMoveSession* pms = refutationTable[deep];
    PieceMoveSession* last = generate(fanorona, pms, false);

    if (deep == 1) {
        return last - pms;
    } else {
        const bool currentPlayerBlack = fanorona.getCurrentPlayerBlack();

        for (PieceMoveSession* it = pms; it != last; ++it) {

            // Supprimer de la table
            // la pièce actuelle de sa originPosition d'origine et
            // les pièces adverses éliminées
            fanorona.removePieces(it->originPosition | it->catchedPieces);
            // Deplacer la pièce actuelle vers sa position conste
            fanorona.addPieces(it->lastPosition, currentPlayerBlack);

            fanorona.setCurrentPlayerBlack(!currentPlayerBlack);

            //////////////////////////////////////////////////////////////////

            // PARCOURS EN PROFONDEUR

            nodes += perft(deep - 1);

            //////////////////////////////////////////////////////////////////

            // RECULE

            // Se remettre à la configuration
            // avant deplacement de la pièce
            //fanorona.undoSession(it->lastPosition);
            fanorona.undoMove(it->lastPosition, it->originPosition, it->catchedPieces);

        }
    }

    return nodes;
}

bool PieceMovementComparator(const PieceMoveSession &ms1, const PieceMoveSession &ms2)
{
    return ms1.score == ms2.score ? ms1.previousScore < ms2.previousScore : ms1.score < ms2.score;
}
