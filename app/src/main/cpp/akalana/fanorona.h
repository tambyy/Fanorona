#ifndef FANORONA_H
#define FANORONA_H

#include <string>
#include <iostream>
#include "bitboard.h"

class Fanorona
{
public:
    Fanorona();
    Fanorona(const Fanorona& fanorona);

    bool piecesMenaced();

    /**
     *
     * @param config
     *        configuration
     *
     * @param position
     *        position à vérifier dans la configuration
     *
     * @return true s'il y a une pièce placée à "position"
     */
    static bool pieceExistsAt(const Bitboard config, const Bitboard position);

    /**
     * Si on essaie de se deplacer
     * à partir de la position origPosition
     * suivant le vecteur(vectorX, vectorY)
     * serait-on toujours positionné sur la table?
     *
     * @param position
     *
     * @return
     */
    static bool frames(const Bitboard position);

    //////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param position
     * @return
     */
    bool pieceExistsAt(const Bitboard position);

    /**
     * @brief clear
     */
    void clear();

    /**
     * Ajouter une/des pièce à la position "position"
     *
     * @param position
     *
     * @param black
     *        true => pièces noires
     *        false => pièces blanches
     */
    void addPieces(const Bitboard position, const bool black);

    /**
     *
     * @param position
     */
    void addTraveledPosition(const Bitboard position);

    /**
     *
     * Supprimer la pièce à la position indiquée
     *
     * @param position
     *
     */
    void removePieces(const Bitboard position);

    //////////////////////////////////////////////////////////////////////////////////

    Vector getLastVector() const;

    bool getCurrentPlayerBlack() const;

    void setCurrentPlayerBlack(const bool currentPlayerBlack);

    bool getFirstPlayerBlack() const;

    void setFirstPlayerBlack(const bool firstPlayerBlack);

    Bitboard getBlackBitboard() const;

    Bitboard getWhiteBitboard() const;

    Bitboard getEmptyBitboard() const;

    Bitboard getUsBitboard() const;

    Bitboard getThemBitboard() const;

    /**
     *
     * @return
     */
    bool getVela() const;

    /**
     *
     * @param vela
     */
    GameMode getMode();

    /**
     *
     * @param vela
     */
    void setMode(GameMode mode);

    /**
     *
     * @return
     */
    bool getVelaBlack() const;

    /**
     *
     * @param vela
     */
    void setVelaBlack(bool velaBlack);

    /**
     *
     * @return
     */
    bool winner() const;

    bool gameOver() const;

    //////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return liste des points traversés pendant l'enchainement de mouvements courant
     */
    Bitboard getTraveledPositions() const;

    /**
     *
     * @return la session de mouvements est encore ouverte
     */
    bool isMovementSessionOpened() const;

    /**
     * Modifier la pièce courante
     *
     * @param position
     * @return
     */
    bool setSelectedPiecePosition(const Bitboard position);

    /**
     * Récupérer la position de la pièce courante
     *
     * @return
     */
    Bitboard getSelectedPieceCurrentPosition() const;

    //////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param vector
     * @param percute
     * @return
     */
    int canCatchPiece() const;

    /**
     *
     * @param vector
     * @param percute
     * @return
     */
    bool canCatchPiece(const Vector vector, const bool percute) const;

    /**
     * Forcer de stopper
     * l'enchainnement de mouvements en cours
     *
     * @return la session de mouvements
     *         a été ouverte avant la fermeture
     */
    bool closeMovementsSession();

    /**
     *
     * @param vector
     * @param percute
     * @return
     */
    Bitboard removedPieces(const Bitboard position, const Vector vector, const bool percute) const;

    /**
     *
     * @param vector
     * @param percute
     * @return
     */
    template<GameMode mode>
    Bitboard removedPieces(const Bitboard position, const Vector vector, const bool percute) const;

    /**
     *
     * @param vector
     * @param percute
     * @return
     */
    Bitboard removedPieces(const Vector vector, const bool percute) const;

    /**
     *
     * @param vector
     *
     * @param percute
     *        true => percuter
     *        false => aspirer
     *
     * @return positions des pièces supprimées
     */
    Bitboard moveSelectedPiece(const Vector vector, const bool percute);

    /**
     *
     * @param vector
     *
     * @param percute
     *        true => percuter
     *        false => aspirer
     *
     * @return positions des pièces supprimées
     */
    void moveSelectedPieceWithoutCatchingPiece(const Bitboard position, const Vector vector);

    //////////////////////////////////////////////////////////////////////////////////

    void undoMove(const Bitboard currentPosition, const Bitboard oldPosition, const Bitboard removedPieces);

    void setLastVector(Vector lastVector);

    //////////////////////////////////////////////////////////////////////////////////

    std::string toString() const;

    bool operator!=(const Fanorona& f) {
        return
                blackBitboard != f.blackBitboard ||
                whiteBitboard != f.whiteBitboard ||
                emptyBitboard != f.emptyBitboard;
    }


private:

    /**
     * Dernier vecteur de deplacement
     * de la pièce en cours
     * S'il n'y a pas d'enchainnement de mouvements en cours
     * alors lastVector vaut null
     */
    Vector lastVector;

    //////////////////////////////////////////////////////////////////////////////////

    Bitboard

    /**
     * Configuration des pièces noires
     */
    blackBitboard,

    /**
     * Configuration des pièces blanches
     */
    whiteBitboard,

    /**
     * Configuration de toutes les pièces sur la table sous forme Bitboard
     * emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard)
     */
    emptyBitboard,

    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Liste des positions déjà traversées
     * par la pièce en cours de mouvement
     */
    traveledPositions,

    /**
     * Position actuelle
     * de la pièce en cours de mouvement
     */
    selectedPieceCurrentPosition;

    //////////////////////////////////////////////////////////////////////////////////

    bool

    /**
     * Si true,
     * C'est aux pièces noires de mener le jeu
     * Si false,
     * C'est aux pièces blanches de mener le jeu
     */
    firstPlayerBlack,

    /**
     * Si true,
     * Il est au tour des pièces noires de jouer
     * Si false,
     * C'est au tour des pièces blanches
     */
    currentPlayerBlack,

    /**
     * Mode Vela
     * true:  Mode Vela et joueur des pièces noires perdant de précédent jeu
     * false: Mode Vela et joueur des pièces blanches perdant de précédent jeu
     */
    velaBlack;

    GameMode mode = MODE_RIATRA;


    //////////////////////////////////////////////////////////////////////////////////

    /**
     * Ajouter une pièce à la position "position"
     *
     * @param config
     *        ancienne configuration
     *
     * @param position
     *        position cible
     *
     * @return nouvelle configuration
     */
    static Bitboard addPieceAt(const Bitboard config, const Bitboard position);

    /**
     * Enlever une pièce se trouvant à la position "position"
     *
     * @param config
     *        ancienne configuration
     *
     * @param position
     *        position cible
     *
     * @return nouvelle configuration
     */
    static Bitboard removePieceAt(const Bitboard config, const Bitboard position);

    /**
     *
     * @param lastVector
     * @return
     */
    bool checkCurrentPieceCanMove(const int lastVector);

};



inline bool Fanorona::pieceExistsAt(const Bitboard config, const Bitboard position) {
    return position & config;
}

inline bool Fanorona::frames(const Bitboard position) {
    return FRAME & position;
}

inline bool Fanorona::pieceExistsAt(const Bitboard position) {
    return (position & emptyBitboard) == 0ull;
}

inline Bitboard Fanorona::getBlackBitboard() const {
    return blackBitboard;
}

inline Bitboard Fanorona::getWhiteBitboard() const {
    return whiteBitboard;
}

inline Bitboard Fanorona::getEmptyBitboard() const {
    return emptyBitboard;
}

inline Bitboard Fanorona::getUsBitboard() const
{
    return currentPlayerBlack ? blackBitboard : whiteBitboard;
}

inline Bitboard Fanorona::getThemBitboard() const
{
    return currentPlayerBlack ? whiteBitboard : blackBitboard;
}

inline bool Fanorona::getVela() const {
    // si c'est une partie VELA
    // et si le nombre des pièces du joueur gagnant est > 5
    return mode == MODE_VELA && bitCount(velaBlack ? whiteBitboard : blackBitboard) > 5;
}

inline void Fanorona::clear() {
    blackBitboard = whiteBitboard = traveledPositions = selectedPieceCurrentPosition = 0;
    lastVector = NULL_VECTOR;
    emptyBitboard = FRAME;
}

inline void Fanorona::addPieces(const Bitboard position, const bool black) {
    if (black) {
        blackBitboard = addPieceAt(blackBitboard, position);
        whiteBitboard = removePieceAt(whiteBitboard, position);
    } else {
        whiteBitboard = addPieceAt(whiteBitboard, position);
        blackBitboard = removePieceAt(blackBitboard, position);
    }

    emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard);
}

inline void Fanorona::addTraveledPosition(const Bitboard position) {
    traveledPositions |= position;
}

inline void Fanorona::removePieces(const Bitboard position) {
    whiteBitboard = removePieceAt(whiteBitboard, position);
    blackBitboard = removePieceAt(blackBitboard, position);

    emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard);
}

inline Vector Fanorona::getLastVector() const {
    return lastVector;
}

inline bool Fanorona::getCurrentPlayerBlack() const {
    return currentPlayerBlack;
}

inline void Fanorona::setCurrentPlayerBlack(const bool currentPlayerBlack) {
    this->currentPlayerBlack = currentPlayerBlack;
}

inline bool Fanorona::getFirstPlayerBlack() const {
    return firstPlayerBlack;
}

inline void Fanorona::setFirstPlayerBlack(const bool firstPlayerBlack) {
    this->firstPlayerBlack = currentPlayerBlack = firstPlayerBlack;
}

inline void Fanorona::setMode(GameMode mode) {
    this->mode = mode;
}

inline GameMode Fanorona::getMode() {
    return mode;
}

inline bool Fanorona::getVelaBlack() const {
    return velaBlack;
}

inline void Fanorona::setVelaBlack(bool velaBlack) {
    this->velaBlack = velaBlack;
}

inline bool Fanorona::winner() const {
    // si ce n'est pas en mode Vela
    return getVela() == currentPlayerBlack;
}

inline Bitboard Fanorona::getTraveledPositions() const {
    return traveledPositions;
}

inline bool Fanorona::isMovementSessionOpened() const {
    return traveledPositions;
}

inline bool Fanorona::setSelectedPiecePosition(const Bitboard position) {
    if (traveledPositions) {
        return false;
    }

    selectedPieceCurrentPosition = position;

    return true;
}

inline Bitboard Fanorona::getSelectedPieceCurrentPosition() const {
    return selectedPieceCurrentPosition;
}

inline int Fanorona::canCatchPiece() const
{
    const Bitboard bitboardUs = currentPlayerBlack ? blackBitboard : whiteBitboard;
    const Bitboard bitboardThem = currentPlayerBlack ? whiteBitboard : blackBitboard;

    for (int i = 0; i < 8; ++i) {
        const int vector = LAKA_VECTORS[i];

        const Bitboard movablePositionUs =
                movePosition(i < 4 ? bitboardUs : bitboardUs & LAKA, vector) & emptyBitboard;

        if (movablePositionUs) {
            if (movePosition(movablePositionUs, vector) & bitboardThem) {
                return i;
            } else if (movePositionBack(movablePositionUs, vector << 1) & bitboardThem) {
                return i;
            }
        }
    }

    return -1;
}

inline bool Fanorona::closeMovementsSession() {
    if (traveledPositions) {
        // réinitiliser la session
        selectedPieceCurrentPosition = traveledPositions = 0ull;
        lastVector = NULL_VECTOR;

        // changement du joueur courant
        currentPlayerBlack = !currentPlayerBlack;

        return true;
    }

    return false;
}

template<>
inline Bitboard Fanorona::removedPieces<MODE_VELA>(const Bitboard position, const Vector vector, const bool percute) const
{
    if (percute) {
        return
                movePosition(movePosition(position, vector), vector) &
                (currentPlayerBlack ? whiteBitboard : blackBitboard);
    } else {
        return
                movePosition(position, -vector) &
                (currentPlayerBlack ? whiteBitboard : blackBitboard);
    }
}

template<>
inline Bitboard Fanorona::removedPieces<MODE_RIATRA>(const Bitboard position, const Vector vector, const bool percute) const
{
    /*const int percuteVectorValue = percute ? vector : -vector;

    Bitboard percutePosition = percute ? movePosition(position, vector) : position,
            // liste des positions des pièces attrapées
            removedPiecesPositions = 0l;

    const Bitboard bitboardThem = currentPlayerBlack ? whiteBitboard : blackBitboard;

    while (
       ((percutePosition = movePosition(percutePosition, percuteVectorValue)) & bitboardThem) != 0l
    ) {
        // on ajoute dans la liste des positions des pièces éliminées la position "percutePosition"
        removedPiecesPositions |= percutePosition;
    }

    return removedPiecesPositions;*/

    const Bitboard bitboardThem = currentPlayerBlack ? whiteBitboard : blackBitboard;

    if (percute) {
        const Bitboard* vectorAttacks = ATTACKS_BLOCKERS[vector < -1 ? vector + 12 : (vector < 2 ? vector + 4 : vector - 4)];

        const Bitboard removedPieces = vectorAttacks[lsb(movePosition(position, vector))];
        const Bitboard blockers = movePositionBack(removedPieces & ~bitboardThem, vector);

        return blockers ? removedPieces & ~vectorAttacks[vector < 0 ? msb(blockers) : lsb(blockers)] : removedPieces;
    } else {
        const Bitboard* vectorAttacks = ATTACKS_BLOCKERS[vector > 1 ? 12 - vector : (vector > -2 ? 4 - vector : -4 - vector)];

        const Bitboard removedPieces = vectorAttacks[lsb(position)];
        const Bitboard blockers = movePosition(removedPieces & ~bitboardThem, vector);

        return blockers ? removedPieces & ~vectorAttacks[vector > 0 ? msb(blockers) : lsb(blockers)] : removedPieces;
    }
}

inline Bitboard Fanorona::removedPieces(const Bitboard position, const Vector vector, const bool percute) const
{
    // liste des pièces adverses éliminées par la pièce
    return getVela() ?
           (velaBlack == currentPlayerBlack ?
            removedPieces<MODE_VELA>(position, vector, percute) : 0) :
           removedPieces<MODE_RIATRA>(position, vector, percute);
}

inline Bitboard Fanorona::addPieceAt(const Bitboard config, const Bitboard position) {
    return config | position;
}

inline Bitboard Fanorona::removePieceAt(const Bitboard config, const Bitboard position) {
    return config & ~position;
}

inline bool Fanorona::canCatchPiece(const Vector vector, const bool percute) const {
    const Bitboard bitboardUs = currentPlayerBlack ? blackBitboard : whiteBitboard;

    if (selectedPieceCurrentPosition & bitboardUs) {

        const Bitboard bitboardThem  = currentPlayerBlack ? whiteBitboard : blackBitboard,
                catchPosition = percute ?
                                movePosition(selectedPieceCurrentPosition, vector << 1) :
                                movePositionBack(selectedPieceCurrentPosition, vector);

        return
            // il y a une pièce adverse à la position "percutePosition"
                catchPosition & bitboardThem;
    }

    return false;
}

inline Bitboard Fanorona::moveSelectedPiece(const Vector vector, const bool percute) {
    bool vela = getVela();

    // pièces éliminées
    const Bitboard removedPieces = this->removedPieces(vector, percute);

    const Bitboard position = selectedPieceCurrentPosition;

    // nouvelle position de la pièce
    selectedPieceCurrentPosition = movePosition(position, vector);

    if (currentPlayerBlack) {
        // on déplace la pièce
        blackBitboard = selectedPieceCurrentPosition | (blackBitboard & ~position);
        // Enlever les pièces éliminées
        whiteBitboard &= ~removedPieces;
    } else {
        // on déplace la pièce
        whiteBitboard = selectedPieceCurrentPosition | (whiteBitboard & ~position);
        // Enlever les pièces éliminées
        blackBitboard &= ~removedPieces;
    }

    emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard);

    // Indiquer le dernier vecteur mouvement
    // fait par la pièce courante
    lastVector = vector;

    // Indiquer que la pièce courante
    // a déjà traversé cette position
    addTraveledPosition(position);

    // si on est en mode Vela
    // ou si la pièce n'a éliminé aucune pièce
    // ou si elle n'a plus de pièce à éliminer
    if (
        removedPieces == 0 ||
        vela ||
        !checkCurrentPieceCanMove(vector)
    ) {
        closeMovementsSession();
    }

    // liste des pièces adverses éliminées par la pièce
    return removedPieces;
}

inline void Fanorona::moveSelectedPieceWithoutCatchingPiece(const Bitboard position, const Vector vector)
{
    if (currentPlayerBlack) {
        // on déplace la pièce
        blackBitboard = movePosition(position, vector) | (blackBitboard & ~position);
    } else {
        // on déplace la pièce
        whiteBitboard = movePosition(position, vector) | (whiteBitboard & ~position);
    }

    emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard);
}

inline bool Fanorona::checkCurrentPieceCanMove(const int lastVector) {

    // index de la position de la pièce
    const int pos = lsb(selectedPieceCurrentPosition);

    // positions où la pièce peut se deplacer
    const Bitboard movablePositions =
            MOVABLE_BITBOARD[pos] &
            emptyBitboard &
            // exclure les positions déjà parcourues
            // et éviter de se déplacer avec le même vecteur une 2è fois
            ~(traveledPositions | movePosition(selectedPieceCurrentPosition, lastVector));

    // si la pièce n'arrive plus à se deplacer
    if (movablePositions == 0) {
        return false;
    }

    // Recupérer l'index de l'attaque de la pièce
    // par rapport aux positions où la pièce peut se deplacer
    const int shiftBit = SHIFT_BITS[pos];
    const int percuteOffset = (movablePositions * MAGIC_ATTACKS_NUMBERS[1][pos]) >> shiftBit;

    // si la pièce peut encore attaquer une pièce adverse
    // alors, elle peut encore se déplacer

    // percute
    const Bitboard bitboardThem = getThemBitboard();
    if (bitboardThem & MAGIC_ATTACKS[1][pos][percuteOffset]) {
        return true;
    }

    // aspire
    const int aspireOffset = (movablePositions * MAGIC_ATTACKS_NUMBERS[0][pos]) >> shiftBit;
    return bitboardThem & MAGIC_ATTACKS[0][pos][aspireOffset];
}

inline Bitboard Fanorona::removedPieces(const Vector vector, const bool percute) const {
    return removedPieces(selectedPieceCurrentPosition, vector, percute);
}

inline void Fanorona::undoMove(const Bitboard currentPosition, const Bitboard oldPosition/*, const Vector lastVector*/, const Bitboard removedPieces) {
    if (currentPlayerBlack) {
        // on met la pièce courante à son ancienne position
        blackBitboard = oldPosition | (blackBitboard & ~currentPosition);
        // et on replace les pièces blanches éliminées
        whiteBitboard |= removedPieces;
    } else {
        // on met la pièce courante à son ancienne position
        whiteBitboard = oldPosition | (whiteBitboard & ~currentPosition);
        // et on replace les pièces noires éliminées
        blackBitboard |= removedPieces;
    }

    emptyBitboard = FRAME & ~(blackBitboard | whiteBitboard);
}

inline void Fanorona::setLastVector(Vector lastVector) {
    this->lastVector = lastVector;
}

#endif // FANORONA_H
