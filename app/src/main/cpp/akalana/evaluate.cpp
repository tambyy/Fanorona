#include "evaluate.h"
#include "move.h"

#include <android/log.h>
#define  LOG_TAG    "AKALANA"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

constexpr int POSITIONS_SCORES[54] = {

        0,        2,    1,    3,    2,    4,    2,    3,    1,    2,        0,

        0,        1,   13,    6,   15,    8,   15,    6,   13,    1,        0,

        0,        3,    5,   17,   13,   20,   13,   17,    5,    3,        0,

        0,        1,   13,    6,   15,    8,   15,    6,   13,    1,        0,

        0,        2,    1,    3,    2,    4,    2,    3,    1,    2

};

//                                     0     1    2    3   4   5  6  7  8
constexpr int APPROCHE_SCORES[9] = {6561, 2187, 729, 243, 81, 27, 9, 3, 1};

Evaluate::Evaluate()
{

    for (int i = 0; i < 54; ++i) {
        for (int j = 0; j < 54; ++j) {
            APPROCHE_DISTANCES_SCORES[i][j] = APPROCHE_SCORES[distance(i / 11, (i % 11) - 1, j / 11, (j % 11) - 1)];
        }
    }

}

int Evaluate::evaluateScene(const Fanorona& scene) {

    bitboardUs    = scene.getUsBitboard(),
    bitboardThem  = scene.getThemBitboard(),
    emptyBitboard = scene.getEmptyBitboard(),
    bitboardUsOnLaka    = bitboardUs & LAKA,
    bitboardThemOnLaka  = bitboardThem & LAKA;

    const int v = bitboardUsOnLaka | bitboardThemOnLaka ? 8 : 4;

    int vector;
    int vector2;

    bool currentPlayerBlack = scene.getCurrentPlayerBlack();
    bool vela = scene.getVela();
    bool velaBlack = scene.getVelaBlack();

    movablePositionsCountUs = 0;
    movablePositionsCountThem = 0;

    ///////////////////////////////////////////////


    // Vérifier s'il n'y a qu'une seule pièce menaçante
    // avec un seul mouvement possible

    // Le but c'est d'exclure de la table les pièces adverses
    // qui seront de toutes façons obligatoirement eliminées par cette pièce au premier coût
    // et n'auront plus aucun impact sur la suite de déroulement du jeu
    /*bool uniqueThreatingUs = true;

    // La seule pièce menaçante
    Bitboard uniqueThreatingBitboardUs = 0;

    // Le seul mouvement que la pièce menaçante peut faire
    Vector uniqueThreatingVector = NULL_VECTOR;
    bool uniqueThreatingPercute = true;


    for (int i = 0; i < 8; ++i) {

        const int vector = LAKA_VECTORS[i];
        const int vector2 = vector << 1;


        const Bitboard movablePosition =
                movePosition(i < 4 ? bitboardUs : bitboardUsOnLaka, vector) & emptyBitboard;

        Bitboard threatedBitboardThem =
                movePosition(movablePosition, vector) & bitboardThem;

        // si élmination de pièces adverses possibles
        if (threatedBitboardThem) {
            if (uniqueThreatingBitboardUs || (threatedBitboardThem & (threatedBitboardThem - 1))) {
                uniqueThreatingUs = false;
                break;
            } else {
                uniqueThreatingVector = vector;
                uniqueThreatingPercute = true;
                uniqueThreatingBitboardUs = movePositionBack(threatedBitboardThem, vector2);
            }
        }

        if (uniqueThreatingUs) {
            threatedBitboardThem =
                    movePositionBack(movablePosition, vector2) & bitboardThem;

            // si élmination de pièces adverses possibles
            if (threatedBitboardThem) {
                if (uniqueThreatingBitboardUs || (threatedBitboardThem & (threatedBitboardThem - 1))) {
                    uniqueThreatingUs = false;
                    break;
                } else {
                    uniqueThreatingVector = vector;
                    uniqueThreatingPercute = false;
                    uniqueThreatingBitboardUs = movePosition(threatedBitboardThem, vector);
                }
            }
        }
    }

    // si c'est le cas
    // exclusion des pièces adverses inutiles
    // et deplacement de l'unique pièce capable de se déplacer
    if (uniqueThreatingBitboardUs == 0) {
        uniqueThreatingUs = false;
    }

    if (uniqueThreatingUs) {
        // éliminées par percussion
        if (uniqueThreatingPercute) {
            for (Bitboard b = movePosition(uniqueThreatingBitboardUs, uniqueThreatingVector << 1); b & bitboardThem; b = movePosition(b, uniqueThreatingVector)) {
                bitboardThem &= ~b;
            }
            // éliminées par aspiration
        } else {
            for (Bitboard b = movePositionBack(uniqueThreatingBitboardUs, uniqueThreatingVector); b & bitboardThem; b = movePositionBack(b, uniqueThreatingVector)) {
                bitboardThem &= ~b;
            }
        }

        // deplacer la pièce menaçante vers sa nouvelle position
        bitboardUs &= ~uniqueThreatingBitboardUs;
        bitboardUs |= uniqueThreatingBitboardUs = movePosition(uniqueThreatingBitboardUs, uniqueThreatingVector);

        bitboardUsOnLaka = bitboardUs & LAKA;
        bitboardThemOnLaka = bitboardThem & LAKA;
        emptyBitboard = FRAME & ~(bitboardUs | bitboardThem);
    }*/


    ///////////////////////////////////////////////


    movablePositionsBitboardUs   = 0;
    movablePositionsBitboardThem = 0;

    threatingPiecesBitboardUs   = 0;
    threatingPiecesBitboardThem = 0;

    Bitboard
            movablePositionPerVectorUs,
            movablePositionPerVectorThem,

            occupationPositionPerVectorUs,
            occupationPositionPerVectorThem,

            threatingPiecesPerVectorUs,
            threatingPiecesPerVectorThem,

            allOccupationPositionUs   = 0,
            allOccupationPositionThem = 0;

    for (int i = 0; i < v; ++i) {

        vector = LAKA_VECTORS[i];
        vector2 = vector << 1;


        // Us

        threatingBitboardPerVectorUs[0][i] = 0;
        threatingBitboardPerVectorUs[1][i] = 0;

        // positions où les pièces peuvent se deplacer
        movablePositionPerVectorUs = movePosition(i < 4 ? bitboardUs : bitboardUsOnLaka, vector) & emptyBitboard;

        if (movablePositionPerVectorUs != 0) {
            movablePositionsBitboardUs |= movablePositionPerVectorUs;

            occupationPositionPerVectorUs = movePosition(movablePositionPerVectorUs, vector) & FRAME;

            if (occupationPositionPerVectorUs) {
                allOccupationPositionUs |= occupationPositionPerVectorUs;

                // pièces qui menacent des pièces adverses
                threatingPiecesPerVectorUs = movePositionBack(occupationPositionPerVectorUs & bitboardThem, vector2);
                if (vela && currentPlayerBlack == velaBlack) {
                    movablePositionsCountUs += bitCount(threatingPiecesPerVectorUs);
                }
                threatingBitboardPerVectorUs[0][i] = threatingPiecesPerVectorUs;

                threatingPiecesBitboardUs |= threatingPiecesPerVectorUs;
            }

            occupationPositionPerVectorUs = movePositionBack(movablePositionPerVectorUs, vector2) & FRAME;

            if (occupationPositionPerVectorUs) {
                allOccupationPositionUs |= occupationPositionPerVectorUs;

                // pièces qui menacent des pièces adverses
                threatingPiecesPerVectorUs = movePosition(occupationPositionPerVectorUs & bitboardThem, vector);
                if (vela && currentPlayerBlack == velaBlack) {
                    movablePositionsCountUs += bitCount(threatingPiecesPerVectorUs);
                }
                threatingBitboardPerVectorUs[1][i] = threatingPiecesPerVectorUs;

                threatingPiecesBitboardUs |= threatingPiecesPerVectorUs;
            }
        }


        // Them

        threatingBitboardPerVectorThem[0][i] = 0;
        threatingBitboardPerVectorThem[1][i] = 0;

        // positions où les pièces peuvent se deplacer
        movablePositionPerVectorThem = movePosition((i < 4 ? bitboardThem : bitboardThemOnLaka), vector) & emptyBitboard;

        if (movablePositionPerVectorThem != 0) {
            movablePositionsBitboardThem |= movablePositionPerVectorThem;

            occupationPositionPerVectorThem = movePosition(movablePositionPerVectorThem, vector) & FRAME;

            if (occupationPositionPerVectorThem) {
                allOccupationPositionThem |= occupationPositionPerVectorThem;

                // pièces qui menacent des pièces adverses
                threatingPiecesBitboardThem |= threatingPiecesPerVectorThem = movePositionBack(occupationPositionPerVectorThem & bitboardUs, vector2);
                if (vela && currentPlayerBlack != velaBlack) {
                    movablePositionsCountThem += bitCount(threatingPiecesBitboardThem);
                }
                threatingBitboardPerVectorThem[0][i] = threatingPiecesPerVectorThem;
            }

            occupationPositionPerVectorThem = movePositionBack(movablePositionPerVectorThem, vector2) & FRAME;

            if (occupationPositionPerVectorThem) {
                allOccupationPositionThem |= occupationPositionPerVectorThem;

                // pièces qui menacent des pièces adverses
                threatingPiecesBitboardThem |= threatingPiecesPerVectorThem = movePosition(occupationPositionPerVectorThem & bitboardUs, vector);
                if (vela && currentPlayerBlack != velaBlack) {
                    movablePositionsCountThem += bitCount(threatingPiecesPerVectorThem);
                }
                threatingBitboardPerVectorThem[1][i] = threatingPiecesPerVectorThem;
            }
        }
    }


    // positions menacées
    occupationPositionsBitboardUs   = allOccupationPositionUs & emptyBitboard;

    // pièces menacées
    allThreatenedThem = allOccupationPositionUs & bitboardThem;


    // s'il n'y a qu'une seule pièce adverse menacée
    // alors enlever toute menace venant de cette pièce
    /*if (allThreatenedThem && (allThreatenedThem & (allThreatenedThem - 1)) == 0) {

        allOccupationPositionThem = 0;
        movablePositionsBitboardThem = 0;
        threatingPiecesBitboardThem = 0;

        for (int i = 0; i < v; ++i) {

            vector = LAKA_VECTORS[i];
            vector2 = vector << 1;


            // Them

            threatingBitboardPerVectorThem[0][i] = 0;
            threatingBitboardPerVectorThem[1][i] = 0;

            // positions où les pièces peuvent se deplacer
            movablePositionPerVectorThem = movePosition((i < 4 ? bitboardThem : bitboardThemOnLaka) & ~allThreatenedThem, vector) & emptyBitboard;

            if (movablePositionPerVectorThem != 0) {
                movablePositionsBitboardThem |= movablePositionPerVectorThem;

                occupationPositionPerVectorThem = movePosition(movablePositionPerVectorThem, vector) & FRAME;

                if (occupationPositionPerVectorThem) {
                    allOccupationPositionThem |= occupationPositionPerVectorThem;

                    // pièces qui menacent des pièces adverses
                    threatingPiecesBitboardThem |= threatingPiecesPerVectorThem = movePositionBack(occupationPositionPerVectorThem & bitboardUs, vector2);
                    threatingBitboardPerVectorThem[0][i] = threatingPiecesPerVectorThem;
                }

                occupationPositionPerVectorThem = movePositionBack(movablePositionPerVectorThem, vector2) & FRAME;

                if (occupationPositionPerVectorThem) {
                    allOccupationPositionThem |= occupationPositionPerVectorThem;

                    // pièces qui menacent des pièces adverses
                    threatingPiecesBitboardThem |= threatingPiecesPerVectorThem = movePosition(occupationPositionPerVectorThem & bitboardUs, vector);
                    threatingBitboardPerVectorThem[1][i] = threatingPiecesPerVectorThem;
                }
            }
        }

        // positions menacées
        occupationPositionsBitboardThem = allOccupationPositionThem & emptyBitboard;

        // pièces menacées
        allThreatenedUs   = allOccupationPositionThem & bitboardUs;

        // ne pas considérer les positions menacées par les pièces adverses
        // comme positions où les pièces peuvent se deplacer
        movablePositionsBitboardUs   &= ~occupationPositionsBitboardThem;
        movablePositionsBitboardThem &= ~occupationPositionsBitboardUs;
    } else */{

        // positions menacées
        occupationPositionsBitboardThem = allOccupationPositionThem & emptyBitboard;

        // pièces menacées
        allThreatenedUs   = allOccupationPositionThem & bitboardUs;

        // ne pas considérer les positions menacées par les pièces adverses
        // comme positions où les pièces peuvent se deplacer
        movablePositionsBitboardUs   &= ~occupationPositionsBitboardThem;
        movablePositionsBitboardThem &= ~occupationPositionsBitboardUs;
    }


    ///////////////////////////////////////////////


    Bitboard

    // pièces non bloquées
    // les pièces menaçant des pièces adverses
    // ne seront pas considérées comme bloquées
    noneBlockedThreatBitboardUs   = threatingPiecesBitboardUs,
            noneBlockedThreatBitboardThem = threatingPiecesBitboardThem,

            noneBlockedBitboardUs   = threatingPiecesBitboardUs,
            noneBlockedBitboardThem = threatingPiecesBitboardThem,

    // pièces non bloquées sur les coins
    noneBlockedOnCornerBitboardUs   = threatingPiecesBitboardUs,
            noneBlockedOnCornerBitboardThem = threatingPiecesBitboardThem,

    // pièces non bloquées sur les bords
    noneBlockedOnBorderBitboardUs   = threatingPiecesBitboardUs,
            noneBlockedOnBorderBitboardThem = threatingPiecesBitboardThem,

    // positions non menacées par les pièces
    noneOccupationPositionsBitboardUs   = FRAME & ~occupationPositionsBitboardUs,
            noneOccupationPositionsBitboardThem = FRAME & ~occupationPositionsBitboardThem,

    // positions protégées où des menaces éventuelles des pièces adverses sur les bords
    // peuvent se présenter
    borderProtectionPositionsBitboardUs   = SEMI_CENTER & occupationPositionsBitboardUs,
            borderProtectionPositionsBitboardThem = SEMI_CENTER & occupationPositionsBitboardThem,

            protectedOnBorderPiecesBitboardUs   = 0,
            protectedOnBorderPiecesBitboardThem = 0;

    mobileOnBorderMoreThem    = 0;
    threatenedBlockedMoreThem = 0;


    const bool threatUsThem = (allThreatenedUs | allThreatenedThem) != 0;


    for (int i = 0; i < v; ++i) {

        vector = LAKA_VECTORS[i];


        // Us

        movablePositionPerVectorUs = movePosition(i < 4 ? bitboardUs : bitboardUsOnLaka, vector);

        // pièces sur les bords protégées
        protectedOnBorderPiecesBitboardUs |= movePositionBack(movablePositionPerVectorUs & borderProtectionPositionsBitboardUs, vector);

        movablePositionPerVectorUs &= noneOccupationPositionsBitboardThem;

        if (movablePositionPerVectorUs) {
            noneBlockedBitboardUs         |= movePositionBack(movablePositionPerVectorUs & emptyBitboard, vector);
            // positions où les pièces peuvent se deplacer sans être menacées
            noneBlockedThreatBitboardUs   |= movePositionBack(movablePositionPerVectorUs, vector);
            // positions où les pièces peuvent se deplacer en dehors des coins sans être menacées
            noneBlockedOnCornerBitboardUs |= movePositionBack(movablePositionPerVectorUs & FRAME_WITHOUT_CORNER, vector);
            // positions où les pièces peuvent se deplacer en dehors des bords sans être menacées
            noneBlockedOnBorderBitboardUs |= movePositionBack(movablePositionPerVectorUs & FRAME_WITHOUT_BORDER, vector);
        }


        // Them

        movablePositionPerVectorThem = movePosition(i < 4 ? bitboardThem : bitboardThemOnLaka, vector);

        // pièces sur les bords protégées
        protectedOnBorderPiecesBitboardThem |= movePositionBack(movablePositionPerVectorUs & borderProtectionPositionsBitboardThem, vector);

        movablePositionPerVectorThem &= noneOccupationPositionsBitboardUs;

        if (movablePositionPerVectorThem) {
            noneBlockedBitboardThem         |= movePositionBack(movablePositionPerVectorThem & emptyBitboard, vector);
            // positions où les pièces peuvent se deplacer sans être menacées
            noneBlockedThreatBitboardThem   |= movePositionBack(movablePositionPerVectorThem, vector);
            // positions où les pièces peuvent se deplacer en dehors des coins sans être menacées
            noneBlockedOnCornerBitboardThem |= movePositionBack(movablePositionPerVectorThem & FRAME_WITHOUT_CORNER, vector);
            // positions où les pièces peuvent se deplacer en dehors des bords sans être menacées
            noneBlockedOnBorderBitboardThem |= movePositionBack(movablePositionPerVectorThem & FRAME_WITHOUT_BORDER, vector);
        }

        if (!threatUsThem) {
            const Bitboard
                    bitboardThemOnBorder = bitboardThem & BORDER,
                    semiCenterMovablePositionsUs = SEMI_CENTER & movablePositionsBitboardUs;


            if (bitboardThemOnBorder) {
                mobileOnBorderMoreThem    |= movePositionBack(movePosition(bitboardThemOnBorder & LAKA,  vector) & semiCenterMovablePositionsUs, vector);
                threatenedBlockedMoreThem |= movePositionBack(movePosition(bitboardThemOnBorder & FOINA, vector) & semiCenterMovablePositionsUs, vector);
            }
        }

    }


    // toutes les pièces bloquées
    allBlockedPiecesBitboardUs = bitboardUs & ~noneBlockedBitboardUs;
    allBlockedPiecesBitboardThem = bitboardThem & ~noneBlockedBitboardThem;

    // toutes les pièces bloquées par des menaces des pièces adverses
    allBlockedThreatPiecesBitboardUs = bitboardUs & ~noneBlockedThreatBitboardUs;
    allBlockedThreatPiecesBitboardThem = bitboardThem & ~noneBlockedThreatBitboardThem;

    // toutes les pièces bloquées et menacés
    threatenedBlockedUs   = allThreatenedUs   & allBlockedPiecesBitboardUs & ~threatingPiecesBitboardUs;
    threatenedBlockedThem = allThreatenedThem & allBlockedPiecesBitboardThem & ~threatingPiecesBitboardThem;

    // toutes les pièces menacées mais qui menacent
    threatenedThreatUs   = allThreatenedUs   & threatingPiecesBitboardUs;
    threatenedThreatThem = allThreatenedThem & threatingPiecesBitboardThem;

    if (threatingPiecesBitboardUs && (threatingPiecesBitboardUs & (threatingPiecesBitboardUs - 1)) == 0) {
        threatenedThreatUs = 0;
    }

    // toutes les pièces bloquées sur les bordures mais protégées
    blockedOnBorderProtectedUs   = allBlockedThreatPiecesBitboardUs   & BORDER & protectedOnBorderPiecesBitboardUs;
    blockedOnBorderProtectedThem = allBlockedThreatPiecesBitboardThem & BORDER & protectedOnBorderPiecesBitboardThem;

    // toutes les pièces bloquées sur les bordures
    blockedOnBorderUs   = allBlockedThreatPiecesBitboardUs   & BORDER & ~blockedOnBorderProtectedUs;
    blockedOnBorderThem = allBlockedThreatPiecesBitboardThem & BORDER & ~blockedOnBorderProtectedThem;

    // toutes les pièces qui ne peuvent se déplacer que sur les bords
    mobileOnBorderUs   = bitboardUs   & BORDER & ~(noneBlockedOnBorderBitboardUs   | protectedOnBorderPiecesBitboardUs);
    mobileOnBorderThem = bitboardThem & BORDER & ~(noneBlockedOnBorderBitboardThem | protectedOnBorderPiecesBitboardThem);

    // toutes les pièces qui ne peuvent se déplacer que vers les bords
    mobileToBorderUs   = bitboardUs   & SEMI_CENTER & ~noneBlockedOnBorderBitboardUs;
    mobileToBorderThem = bitboardThem & SEMI_CENTER & ~noneBlockedOnBorderBitboardThem;

    // toutes les pièces bloquées sur les semi-centres
    blockedOnSemiCenterUs   = allBlockedThreatPiecesBitboardUs   & SEMI_CENTER;
    blockedOnSemiCenterThem = allBlockedThreatPiecesBitboardThem & SEMI_CENTER;

    // toutes les pièces qui ne peuvent se déplacer que sur les coins
    mobileOnCornerUs   = bitboardUs   & CORNER & ~(noneBlockedOnCornerBitboardUs   | protectedOnBorderPiecesBitboardUs);
    mobileOnCornerThem = bitboardThem & CORNER & ~(noneBlockedOnCornerBitboardThem | protectedOnBorderPiecesBitboardThem);

    // toutes les pièces bloquées sur les centres
    blockedOnCenterUs   = allBlockedThreatPiecesBitboardUs   & CENTER;
    blockedOnCenterThem = allBlockedThreatPiecesBitboardThem & CENTER;

    ///////////////////////////////////////////////


    maxThreatValueUs = 0;
    maxThreatValueThem = 0;

    if (!vela && (threatingPiecesBitboardUs | threatingPiecesBitboardThem)/* && uniqueThreatingBitboardUs == 0*/)
    {
        for (int i = 0, threat; i < v; ++i) {

            vector = LAKA_VECTORS[i];
            vector2 = vector << 1;

            // Us

            const Bitboard noneThreatenedTreatingPercuteBitboardUs = threatingBitboardPerVectorUs[0][i];

            if (noneThreatenedTreatingPercuteBitboardUs) {
                // PERCUTER
                threat = 0;
                for (
                        Bitboard
                                treatingBitboardUs = noneThreatenedTreatingPercuteBitboardUs & ~allThreatenedUs,
                                bb = movePosition(noneThreatenedTreatingPercuteBitboardUs, vector2) & bitboardThem,
                                bbt = movePosition(treatingBitboardUs, vector2) & bitboardThem;

                        bb != 0;

                        bb = movePosition(bb, vector) & bitboardThem,
                                bbt = movePosition(bbt, vector) & allBlockedPiecesBitboardThem
                        ) {
                    threatenedBlockedThem |= bb;

                    ++threat;
                }

                if (threat > maxThreatValueThem) {
                    maxThreatValueThem = threat;
                }
            }

            const Bitboard noneThreatenedTreatingAspireBitboardUs = threatingBitboardPerVectorUs[1][i];

            if (noneThreatenedTreatingAspireBitboardUs) {
                // ASPIRER
                threat = 0;
                for (
                        Bitboard
                                treatingBitboardUs = noneThreatenedTreatingAspireBitboardUs & ~allThreatenedUs,
                                bb = movePositionBack(noneThreatenedTreatingAspireBitboardUs, vector) & bitboardThem,
                                bbt = movePositionBack(treatingBitboardUs, vector) & bitboardThem;

                        bb != 0;

                        bb = movePositionBack(bb, vector) & bitboardThem,
                                bbt = movePositionBack(bbt, vector) & allBlockedPiecesBitboardThem
                        ) {
                    threatenedBlockedThem |= bb;
                    ++threat;
                }

                if (threat > maxThreatValueThem) {
                    maxThreatValueThem = threat;
                }
            }


            // Them

            // PERCUTER
            // Ne pas traiter

            const Bitboard noneThreatenedTreatingAspireBitboardThem = threatingBitboardPerVectorThem[1][i];

            if (noneThreatenedTreatingAspireBitboardThem) {
                // ASPIRER
                threat = 0;

                for (
                        Bitboard
                                treatingBitboardThem = noneThreatenedTreatingAspireBitboardThem & ~allThreatenedThem,
                                bb = movePositionBack(treatingBitboardThem, vector) & allBlockedPiecesBitboardUs;

                        bb != 0;

                        bb = movePositionBack(bb, vector) & allBlockedPiecesBitboardUs
                        ) {
                    threatenedBlockedUs |= bb;
                    ++threat;
                }

                if (threat > maxThreatValueUs) {
                    maxThreatValueUs = threat;
                }
            }
        }
    }

    // toutes les pièces menacées qui ne ni ménacent ni sont bloquées
    allThreatenedUs |= threatenedBlockedUs;
    allThreatenedThem |= threatenedBlockedThem;
    threatenedUs   = allThreatenedUs   & ~(threatenedBlockedUs | threatenedThreatUs);
    threatenedThem = allThreatenedThem & ~(threatenedBlockedThem | threatenedThreatThem);

    /*if (uniqueThreatenedBlockedThem) {
        maxThreatValueThem = 0;
    }*/


    ///////////////////////////////////////////////



    piecesValueUs = 0;
    piecesValueThem = 0;

    positionsValueUs = 0;
    positionsValueThem = 0;

    occupationValueUs = 0;
    occupationValueThem = 0;

    movableValueUs = 0;
    movableValueThem = 0;

    threatenedBlockedValueUs = 0;
    threatenedBlockedValueThem = 0;

    threatenedValueUs = 0;
    threatenedValueThem = 0;

    threatenedThreatValueUs = 0;
    threatenedThreatValueThem = 0;

    blockedOnBorderValueUs = 0;
    blockedOnBorderValueThem = 0;

    blockedOnBorderProtectedValueUs = 0;
    blockedOnBorderProtectedValueThem = 0;

    mobileOnCornerValueUs = 0;
    mobileOnCornerValueThem = 0;

    mobileOnBorderValueUs = 0;
    mobileOnBorderValueThem = 0;

    mobileToBorderValueUs = 0;
    mobileToBorderValueThem = 0;

    blockedOnSemiCenterValueUs = 0;
    blockedOnSemiCenterValueThem = 0;

    blockedOnCenterValueUs = 0;
    blockedOnCenterValueThem = 0;

    blockedValueUs = 0;
    blockedValueThem = 0;

    mobileOnBorderMoreValueThem = 0;
    threatenedBlockedMoreValueThem = 0;

/*
    Bitboard position = ZARAN_DAKA_1;

    for (int i = 0, positionScore; i < 45; ++i) {
        positionScore = POSITIONS_SCORES[i];

        if (position & bitboardUs) {
            positionsValueUs += positionScore;

            if (position & threatenedBlockedUs) {
                ++threatenedBlockedValueUs;
            } else if (position & threatenedThreatUs) {
                ++threatenedThreatValueUs;
            } else if (position & threatenedUs) {
                ++threatenedValueUs;
            } else if (position & blockedOnBorderProtectedUs) {
                ++blockedOnBorderProtectedValueUs;
            } else if (position & blockedOnBorderUs) {
                ++blockedOnBorderValueUs;
            } else if (position & mobileOnCornerUs) {
                ++mobileOnCornerValueUs;
            } else if (position & mobileOnBorderUs) {
                ++mobileOnBorderValueUs;
            } else if (position & mobileToBorderUs) {
                ++mobileToBorderValueUs;
            } else if (position & blockedOnSemiCenterUs) {
                ++blockedOnSemiCenterValueUs;
            } else if (position & blockedOnCenterUs) {
                ++blockedOnCenterValueUs;
            } else if (position & allBlockedPiecesBitboardUs) {
                ++blockedValueUs;
            }

            if (gamePhase == END_GAME) {
                approchePositionsUs[piecesValueUs] = i;
            }

            ++piecesValueUs;
        } else if (position & bitboardThem) {
            positionsValueThem += positionScore;

            if (position & threatenedBlockedThem) {
                ++threatenedBlockedValueThem;
            } else if (position & threatenedBlockedMoreThem) {
                ++threatenedBlockedMoreValueThem;
            } else if (position & threatenedThreatThem) {
                ++threatenedThreatValueThem;
            } else if (position & threatenedThem) {
                ++threatenedValueThem;
            } else if (position & blockedOnBorderProtectedThem) {
                ++blockedOnBorderProtectedValueThem;
            } else if (position & blockedOnBorderThem) {
                ++blockedOnBorderValueThem;
            } else if (position & mobileOnCornerThem) {
                ++mobileOnCornerValueThem;
            } else if (position & mobileOnBorderThem) {
                ++mobileOnBorderValueThem;
            } else if (position & mobileOnBorderMoreThem) {
                ++mobileOnBorderMoreValueThem;
            } else if (position & mobileToBorderThem) {
                ++mobileToBorderValueThem;
            } else if (position & blockedOnSemiCenterThem) {
                ++blockedOnSemiCenterValueThem;
            } else if (position & blockedOnCenterThem) {
                ++blockedOnCenterValueThem;
            } else if (position & allBlockedPiecesBitboardThem) {
                ++blockedValueThem;
            }

            if (gamePhase == END_GAME) {
                approchePositionsThem[piecesValueThem] = i;
            }

            ++piecesValueThem;
        } else {
            if (position & occupationPositionsBitboardThem) {
                occupationValueThem += positionScore;
            } else if (position & movablePositionsBitboardUs) {
                movableValueUs += positionScore;
            }

            if (position & occupationPositionsBitboardUs) {
                occupationValueUs += positionScore;
            } else if (position & movablePositionsBitboardThem) {
                movableValueThem += positionScore;
            }
        }

        position <<= (position & FILE_9) ? 3 : 1;
    }
    */

    int pieceIndex = 0;

    piecesValueUs = bitCount(bitboardUs);

    const Bitboard blockedBitboardUs1 =
            blockedOnBorderProtectedUs |
            blockedOnBorderUs |
            mobileOnCornerUs |
            mobileOnBorderUs;

    const Bitboard blockedBitboardUs2 =
            mobileToBorderUs |
            blockedOnSemiCenterUs |
            blockedOnCenterUs;

    for (Bitboard temp = bitboardUs, position; temp; temp &= ~position) {
        position = temp & -temp;

        const int i = lsb(position);

        positionsValueUs += POSITIONS_SCORES[i];

        if (position & allThreatenedUs) {
            if (position & threatenedBlockedUs) {
                ++threatenedBlockedValueUs;
            } else if (position & threatenedThreatUs) {
                ++threatenedThreatValueUs;
            } else {
                ++threatenedValueUs;
            }
        } else if (position & blockedBitboardUs1) {
            if (position & blockedOnBorderProtectedUs) {
                ++blockedOnBorderProtectedValueUs;
            } else if (position & blockedOnBorderUs) {
                ++blockedOnBorderValueUs;
            } else if (position & mobileOnCornerUs) {
                ++mobileOnCornerValueUs;
            } else if (position & mobileOnBorderUs) {
                ++mobileOnBorderValueUs;
            }
        } else if (position & blockedBitboardUs2) {
            if (position & mobileToBorderUs) {
                ++mobileToBorderValueUs;
            } else if (position & blockedOnSemiCenterUs) {
                ++blockedOnSemiCenterValueUs;
            } else if (position & blockedOnCenterUs) {
                ++blockedOnCenterValueUs;
            }
        } else if (position & allBlockedPiecesBitboardUs) {
            ++blockedValueUs;
        }

        if (gamePhase == END_GAME) {
            approchePositionsUs[pieceIndex] = i;
            ++pieceIndex;
        }
    }

    pieceIndex = 0;

    piecesValueThem = bitCount(bitboardThem);

    const Bitboard threatenedBitboardThem = allThreatenedThem | threatenedBlockedMoreThem;

    const Bitboard blockedBitboardThem1 =
            blockedOnBorderProtectedThem |
            blockedOnBorderThem |
            mobileOnCornerThem |
            mobileOnBorderThem;

    const Bitboard blockedBitboardThem2 =
            mobileOnBorderMoreThem |
            mobileToBorderThem |
            blockedOnSemiCenterThem |
            blockedOnCenterThem;

    for (Bitboard temp = bitboardThem, position; temp; temp &= ~position) {
        position = temp & -temp;

        const int i = lsb(position);

        positionsValueThem += POSITIONS_SCORES[i];

        if (position & threatenedBitboardThem) {
            if (position & threatenedBlockedThem) {
                ++threatenedBlockedValueThem;
            } else if (position & threatenedBlockedMoreThem) {
                ++threatenedBlockedMoreValueThem;
            } else if (position & threatenedThreatThem) {
                ++threatenedThreatValueThem;
            } else {
                ++threatenedValueThem;
            }
        } else if (position & blockedBitboardThem1) {
            if (position & blockedOnBorderProtectedThem) {
                ++blockedOnBorderProtectedValueThem;
            } else if (position & blockedOnBorderThem) {
                ++blockedOnBorderValueThem;
            } else if (position & mobileOnCornerThem) {
                ++mobileOnCornerValueThem;
            } else if (position & mobileOnBorderThem) {
                ++mobileOnBorderValueThem;
            }
        } else if (position & blockedBitboardThem2) {
            if (position & mobileOnBorderMoreThem) {
                ++mobileOnBorderMoreValueThem;
            } else if (position & mobileToBorderThem) {
                ++mobileToBorderValueThem;
            } else if (position & blockedOnSemiCenterThem) {
                ++blockedOnSemiCenterValueThem;
            } else if (position & blockedOnCenterThem) {
                ++blockedOnCenterValueThem;
            }
        } else if (position & allBlockedPiecesBitboardThem) {
            ++blockedValueThem;
        }

        if (gamePhase == END_GAME) {
            approchePositionsThem[pieceIndex] = i;
            ++pieceIndex;
        }
    }

    if (occupationPositionsBitboardThem) {
        for (Bitboard temp = occupationPositionsBitboardThem, position; temp; temp &= ~position) {
            position = temp & -temp;

            if (position & occupationPositionsBitboardThem) {
                occupationValueThem += POSITIONS_SCORES[lsb(position)];
            }
        }
    }

    if (movablePositionsBitboardUs) {
        for (Bitboard temp = movablePositionsBitboardUs, position; temp; temp &= ~position) {
            position = temp & -temp;

            if (position & movablePositionsBitboardUs) {
                movableValueUs += POSITIONS_SCORES[lsb(position)];
            }
        }
    }

    if (occupationPositionsBitboardUs) {
        for (Bitboard temp = occupationPositionsBitboardUs, position; temp; temp &= ~position) {
            position = temp & -temp;

            if (position & occupationPositionsBitboardUs) {
                occupationValueUs += POSITIONS_SCORES[lsb(position)];
            }
        }
    }

    if (movablePositionsBitboardThem) {
        for (Bitboard temp = movablePositionsBitboardThem, position; temp; temp &= ~position) {
            position = temp & -temp;

            if (position & movablePositionsBitboardThem) {
                movableValueThem += POSITIONS_SCORES[lsb(position)];
            }
        }
    }

    if (gamePhase == END_GAME) {
        approcheValue = 0;
        for (int i = 0, j; i < piecesValueUs; ++i) {
            for (j = 0; j < piecesValueThem; ++j) {
                approcheValue += APPROCHE_DISTANCES_SCORES[approchePositionsUs[i]][approchePositionsThem[j]];
            }
        }
    }


    int piecesValueUsB = piecesValueUs - maxThreatValueUs;
    int piecesValueThemB = piecesValueThem - maxThreatValueThem;

    scoreUs =
            31000 * (piecesValueUsB > piecesValueThemB ? piecesValueUsB - piecesValueThemB : 0) +
            10000 * movablePositionsCountUs +
            -6000 * threatenedBlockedValueUs +
            -4500 * blockedOnBorderValueUs +
            -4000 * threatenedValueUs +
            -3000 * mobileOnCornerValueUs +
            -2500 * mobileOnBorderValueUs +
            -1500 * blockedOnBorderProtectedValueUs +
            -1000 * mobileToBorderValueUs +
             -750 * blockedOnSemiCenterValueUs +
             -500 * blockedOnCenterValueUs +
             -400 * threatenedThreatValueUs +
             -300 * blockedValueUs +
              100 * positionsValueUs +
                5 * occupationValueUs +
                4 * movableValueUs;

    scoreThem =
            31000 * (piecesValueThemB > piecesValueUsB ? piecesValueThemB - piecesValueUsB : 0) +
            10000 * movablePositionsCountThem +
            -6000 * threatenedBlockedValueThem +
            -5500 * threatenedBlockedMoreValueThem +
            -4500 * blockedOnBorderValueThem +
            -4000 * threatenedValueThem +
            -3000 * mobileOnCornerValueThem +
            -2500 * mobileOnBorderValueThem +
            -2000 * mobileOnBorderMoreValueThem +
            -1500 * blockedOnBorderProtectedValueThem +
            -1000 * mobileToBorderValueThem +
             -750 * blockedOnSemiCenterValueThem +
             -500 * blockedOnCenterValueThem +
             -400 * threatenedThreatValueThem +
             -300 * blockedValueThem +
              100 * positionsValueThem +
                5 * occupationValueThem +
                4 * movableValueThem;

    /*if (gamePhase == MIDDLE_GAME) {

        scoreUs -=
                2000 * threatenedBlockedValueUs   +
                1825 * threatenedValueUs   +
                1400 * blockedOnBorderValueUs +
                975 * mobileOnCornerValueUs +
                975 * mobileOnBorderValueUs;

        scoreThem -=
                2000 * threatenedBlockedValueThem +
                1825 * threatenedValueThem +
                1400 * blockedOnBorderValueThem +
                975 * mobileOnCornerValueThem +
                975 * mobileOnBorderValueThem;

    } else */
    if (gamePhase == END_GAME) {

        // Approche
        if (scoreUs < scoreThem) {
            scoreUs -=
                    10 * approcheValue / piecesValueThem;
        } else {
            scoreThem -=
                    10 * approcheValue / piecesValueUs;
        }

    }


    // bonus / malus
    bonusUs = 0;
    bonusThem = 0;

    if (gamePhase == END_GAME) {
        // Us

        // 1. s'il ne reste plus qu'une pièce
        //    et il reste plus d'une pièce adverse non menacee
        //    donner un bonus aux pièces adverses
        int piecesNotThreatenedValueThem = piecesValueThem - maxThreatValueThem;
        if (piecesNotThreatenedValueThem == 1 && piecesValueUs - (threatenedBlockedValueUs) > 1) {

            if ((allThreatenedThem | allBlockedThreatPiecesBitboardThem) == bitboardThem) {
                bonusUs += 35000;
            } else {
                bonusUs += 30000;
            }

        } else if (piecesNotThreatenedValueThem <= 2 &&
                   piecesValueUs - (threatenedBlockedValueUs) > 3) {

            if ((allThreatenedThem | allBlockedThreatPiecesBitboardThem) == bitboardThem) {
                bonusUs += 15000;
            } else {
                bonusUs += 13000;
            }

        }

            //
        else if (piecesNotThreatenedValueThem == 1 &&
                 (threatenedBlockedValueThem + threatenedBlockedMoreValueThem +
                  threatenedValueThem + threatenedThreatValueThem + blockedOnBorderValueThem +
                  mobileOnCornerValueThem + mobileOnBorderValueThem +
                  mobileOnBorderMoreValueThem) == piecesValueThem) {
            bonusUs += 30000;
        }

            // 2. si toutes les pieces sont bloquées
            //    donner un bonus aux pièces adverses
        else if (allBlockedPiecesBitboardThem == bitboardThem) {
            bonusUs += 30000;

            // 2.1. s'il ne reste plus que 2 pièces
            //    et que toutes les pièces sont bloquées
            if (piecesValueThem <= 2) {
                bonusUs += 8000;
            }
        }

            // 3.
        else if (piecesValueThem == 2 &&
                 (threatenedBlockedValueThem + threatenedBlockedMoreValueThem +
                  threatenedValueThem + threatenedThreatValueThem + blockedOnBorderValueThem +
                  mobileOnCornerValueThem + mobileOnBorderValueThem +
                  mobileOnBorderMoreValueThem) == piecesValueThem) {
            bonusUs += 28000;
        }

            // 3. s'il ne reste plus que 2 pièces
            //    et que toutes les pièces sont
            //    soient bloquées
            //    soient ne peut se deplacer que sur les coins / sur les bords / vers les bords
        else if (
                (piecesValueThem <= 3 &&
                 (piecesValueThem - threatenedBlockedValueThem - threatenedBlockedMoreValueThem -
                  threatenedValueThem - threatenedThreatValueThem - blockedOnBorderValueThem - blockedOnBorderProtectedValueThem - mobileOnCornerValueThem - mobileOnBorderValueThem) <= 2 &&
                 piecesValueUs - (threatenedBlockedValueUs + threatenedValueUs) >= 2) ||
                (piecesValueThem == 3 && piecesValueUs >= 2 * piecesValueThem)
                ) {

            int weakPiecesValueThem =
                    threatenedBlockedValueThem +
                    threatenedBlockedMoreValueThem +
                    threatenedValueThem +
                    threatenedThreatValueThem +
                    blockedOnBorderValueThem +
                    blockedOnBorderProtectedValueThem,

                    weakPiecesValueThem2 = weakPiecesValueThem,

                    piecesValueThem_1 = piecesValueThem - 1;

            if (piecesValueThem - weakPiecesValueThem <= piecesValueThem_1) {
                bonusUs += 15000;
            } else if (piecesValueThem - (weakPiecesValueThem += mobileOnCornerValueThem) <=
                       piecesValueThem_1) {
                bonusUs += 14000;
            } else if (piecesValueThem - (weakPiecesValueThem += mobileOnBorderValueThem +
                                                                 mobileOnBorderMoreValueThem) <=
                       piecesValueThem_1) {
                bonusUs += 13000;
            } else if (piecesValueThem - (weakPiecesValueThem += mobileToBorderValueThem) <=
                       piecesValueThem_1) {
                bonusUs += 8000;
            } else if (piecesValueThem - (weakPiecesValueThem += blockedOnSemiCenterValueThem) <=
                       piecesValueThem_1) {
                bonusUs += 7000;
            } else if (piecesValueThem - (weakPiecesValueThem += blockedOnCenterValueThem) <=
                       piecesValueThem_1) {
                bonusUs += 6000;
            }

            if (piecesValueThem - weakPiecesValueThem2 <= 0) {
                bonusUs += 15000;
            } else if (piecesValueThem - (weakPiecesValueThem2 += mobileOnCornerValueThem) <= 0) {
                bonusUs += 14000;
            } else if (piecesValueThem - (weakPiecesValueThem2 += mobileOnBorderValueThem +
                                                                  mobileOnBorderMoreValueThem) <=
                       0) {
                bonusUs += 13000;
            } else if (piecesValueThem - (weakPiecesValueThem2 += mobileToBorderValueThem) <= 0) {
                bonusUs += 8000;
            } else if (piecesValueThem - (weakPiecesValueThem2 += blockedOnSemiCenterValueThem) <=
                       0) {
                bonusUs += 7000;
            } else if (piecesValueThem - (weakPiecesValueThem2 += blockedOnCenterValueThem) <= 0) {
                bonusUs += 6000;
            }
        }


        // Them

        // 1. s'il ne reste plus qu'une pièce
        //    et il reste plus d'une pièce adverse non menacee
        //    donner un bonus aux pièces adverses
        if (piecesValueUs == 1 && piecesValueThem -
                                  (threatenedBlockedValueThem + threatenedBlockedMoreValueThem +
                                   threatenedValueThem + threatenedThreatValueThem) > 1) {

            if ((threatenedBlockedUs | allBlockedThreatPiecesBitboardUs) == bitboardUs) {
                bonusThem += 35000;
            } else {
                bonusThem += 30000;
            }

        } else if (piecesValueUs <= 2 && piecesValueThem - (threatenedBlockedValueThem +
                                                            threatenedBlockedMoreValueThem +
                                                            threatenedValueThem +
                                                            threatenedThreatValueThem) > 3) {

            if ((threatenedBlockedUs | allBlockedThreatPiecesBitboardUs) == bitboardUs) {
                bonusThem += 13000;
            } else {
                bonusThem += 10000;
            }

        }

            //
        else if (piecesValueUs == 1 &&
                 (threatenedBlockedValueUs + blockedOnBorderValueUs + mobileOnCornerValueUs) ==
                 piecesValueUs) {
            bonusThem += 30000;
        }

            // 2. si toutes les pieces sont bloquées
            //    donner un bonus aux pièces adverses
        else if (allBlockedPiecesBitboardUs == bitboardUs) {
            bonusThem += 30000;

            // 2.1. s'il ne reste plus que 2 pièces
            //    et que toutes les pièces sont bloquées
            if (piecesValueUs <= 2) {
                bonusThem += 8000;
            }
        }

            // 3.
        else if (piecesValueUs == 2 &&
                 (threatenedBlockedValueUs + threatenedValueUs + blockedOnBorderValueUs +
                  mobileOnCornerValueUs + mobileOnBorderValueUs) == piecesValueUs) {
            bonusThem += 13000;
        }

            // 3. s'il ne reste plus que 2 pièces
            //    et que toutes les pièces sont
            //    soient bloquées
            //    soient ne peut se deplacer que sur les coins / sur les bords / vers les bords
        else if (
                (piecesValueUs <= 3 &&
                 (piecesValueUs - threatenedBlockedValueUs - threatenedValueUs - blockedOnBorderValueUs - blockedOnBorderProtectedValueUs - mobileOnCornerValueUs - mobileOnBorderValueUs) <= 2 &&
                 piecesValueThem - (threatenedBlockedValueThem + threatenedBlockedMoreValueThem +
                                    threatenedValueThem + threatenedThreatValueThem) >= 2) ||
                (piecesValueUs == 3 && piecesValueThem >= 2 * piecesValueUs)
                ) {

            int weakPiecesValueUs =
                    threatenedBlockedValueUs +
                    threatenedValueUs +
                    blockedOnBorderValueUs +
                    blockedOnBorderProtectedValueUs,

                    weakPiecesValueUs2 = weakPiecesValueUs,

                    piecesValueUs_1 = piecesValueUs - 1;

            if (piecesValueUs - weakPiecesValueUs <= piecesValueUs_1) {
                bonusThem += 15000;
            } else if (piecesValueUs - (weakPiecesValueUs += mobileOnCornerValueUs) <=
                       piecesValueUs_1) {
                bonusThem += 14000;
            } else if (piecesValueUs - (weakPiecesValueUs += mobileOnBorderValueUs) <=
                       piecesValueUs_1) {
                bonusThem += 13000;
            } else if (piecesValueUs - (weakPiecesValueUs += mobileToBorderValueUs) <=
                       piecesValueUs_1) {
                bonusThem += 8000;
            } else if (piecesValueUs - (weakPiecesValueUs += blockedOnSemiCenterValueUs) <=
                       piecesValueUs_1) {
                bonusThem += 7000;
            } else if (piecesValueUs - (weakPiecesValueUs += blockedOnCenterValueUs) <=
                       piecesValueUs_1) {
                bonusThem += 6000;
            }

            if (piecesValueUs - weakPiecesValueUs2 <= 0) {
                bonusThem += 15000;
            } else if (piecesValueUs - (weakPiecesValueUs2 += mobileOnCornerValueUs) <= 0) {
                bonusThem += 14000;
            } else if (piecesValueUs - (weakPiecesValueUs2 += mobileOnBorderValueUs) <= 0) {
                bonusThem += 13000;
            } else if (piecesValueUs - (weakPiecesValueUs2 += mobileToBorderValueUs) <= 0) {
                bonusThem += 8000;
            } else if (piecesValueUs - (weakPiecesValueUs2 += blockedOnSemiCenterValueUs) <= 0) {
                bonusThem += 7000;
            } else if (piecesValueUs - (weakPiecesValueUs2 += blockedOnCenterValueUs) <= 0) {
                bonusThem += 6000;
            }
        }
    }

    score =
            (scoreUs + bonusUs)   / piecesValueUs -
            (scoreThem + bonusThem) / piecesValueThem;

    return score;
}

void Evaluate::print() {
    ALOG("| pieces                   | %d %d", piecesValueUs                   , piecesValueThem                   );
    ALOG("| movablePositionsCount    | %d %d", movablePositionsCountUs         , movablePositionsCountThem         );
    ALOG("| threatenedBlocked        | %d %d", threatenedBlockedValueUs        , threatenedBlockedValueThem        );
    ALOG("| threatenedBlockedMore    | - %d" , threatenedBlockedMoreValueThem);
    ALOG("| threatened               | %d %d", threatenedValueUs               , threatenedValueThem               );
    ALOG("| threatenedThreat         | %d %d", threatenedThreatValueUs         , threatenedThreatValueThem         );
    ALOG("| blockedOnBorder          | %d %d", blockedOnBorderValueUs          , blockedOnBorderValueThem          );
    ALOG("| blockedOnBorderProtected | %d %d", blockedOnBorderProtectedValueUs , blockedOnBorderProtectedValueThem );
    ALOG("| mobileOnCorner           | %d %d", mobileOnCornerValueUs           , mobileOnCornerValueThem           );
    ALOG("| mobileOnBorder           | %d %d", mobileOnBorderValueUs           , mobileOnBorderValueThem           );
    ALOG("| mobileOnBorderMore       | - %d" , mobileOnBorderMoreValueThem);
    ALOG("| mobileToBorder           | %d %d", mobileToBorderValueUs           , mobileToBorderValueThem           );
    ALOG("| blockedOnSemiCenter      | %d %d", blockedOnSemiCenterValueUs      , blockedOnSemiCenterValueThem      );
    ALOG("| blockedOnCenter          | %d %d", blockedOnCenterValueUs          , blockedOnCenterValueThem          );
    ALOG("| blocked                  | %d %d", blockedValueUs                  , blockedValueThem                  );
    ALOG("| positions                | %d %d", positionsValueUs                , positionsValueThem                );
    ALOG("| occupation               | %d %d", occupationValueUs               , occupationValueThem               );
    ALOG("| movable                  | %d %d", movableValueUs                  , movableValueThem                  );
    ALOG("| approche                 | %d -" , approcheValue);
    ALOG("| maxThreat                | %d %d", maxThreatValueUs                , maxThreatValueThem                );
    ALOG("| threatenedBlockedMore    | - %d" , threatenedBlockedMoreValueThem);
    ALOG("| mobileOnBorderMore       | - %d" , mobileOnBorderMoreValueThem);
    ALOG("| Bonus                    | %d %d", bonusUs                         , bonusThem                         );
    ALOG("| Score                    | %d", score);
    ALOG("");
}