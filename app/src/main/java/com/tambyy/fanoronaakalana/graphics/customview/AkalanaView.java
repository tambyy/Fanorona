package com.tambyy.fanoronaakalana.graphics.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;

import com.tambyy.fanoronaakalana.config.Theme;
import com.tambyy.fanoronaakalana.engine.EngineAction;
import com.tambyy.fanoronaakalana.engine.EngineActionInit;
import com.tambyy.fanoronaakalana.engine.EngineActionMoveSelectedPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionSelectPiece;
import com.tambyy.fanoronaakalana.engine.EngineActionStopMove;
import com.tambyy.fanoronaakalana.graphics.anim.Animation;
import com.tambyy.fanoronaakalana.graphics.anim.AnimationDelayExceededListener;
import com.tambyy.fanoronaakalana.graphics.anim.item.DrawableMoveAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.item.DrawableOpacityAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.item.DrawableScaleAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.item.TimeoutAnimation;
import com.tambyy.fanoronaakalana.graphics.anim.interpolator.SecondDegreeInterpolator;
import com.tambyy.fanoronaakalana.graphics.drawable.item.Akalana;
import com.tambyy.fanoronaakalana.graphics.drawable.DrawableTouchListener;
import com.tambyy.fanoronaakalana.graphics.drawable.item.EditionPosition;
import com.tambyy.fanoronaakalana.graphics.drawable.item.MovablePosition;
import com.tambyy.fanoronaakalana.graphics.drawable.item.RemovablePawn;
import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;
import com.tambyy.fanoronaakalana.graphics.drawable.item.TraveledPosition;
import com.tambyy.fanoronaakalana.graphics.drawable.item.Pawn;
import com.tambyy.fanoronaakalana.engine.Engine;
import com.tambyy.fanoronaakalana.engine.Engine.Point;
import com.tambyy.fanoronaakalana.engine.Engine.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AkalanaView extends SceneView {

    /**
     * the canvas is divided into 9 x 5 cells
     * unitSize defines the width / height of a cell
     */
    protected int unitSize = 50;

    /**
     *
     */
    protected Akalana akalana;

    /**
     * All 22 "black" pieces
     */
    protected final List<Pawn> blackActivePieces = new ArrayList<>();

    /**
     * All 22 "white" pieces
     */
    protected final List<Pawn> whiteActivePieces = new ArrayList<>();

    /**
     * All 22 "black" pieces
     */
    protected final Stack<Pawn> blackPieces = new Stack<>();

    /**
     * All 22 "white" pieces
     */
    protected final Stack<Pawn> whitePieces = new Stack<>();

    /**
     * Movable positions mark
     */
    protected final List<MovablePosition> movablePositions = new ArrayList<>();

    /**
     * Removable pieces mark
     */
    protected final List<RemovablePawn> removablePawns = new ArrayList<>();

    /**
     * Traveled positions mark
     */
    protected final List<TraveledPosition> traveledPositions = new ArrayList<>();

    /**
     * Edition positions
     */
    protected final List<EditionPosition> editionPositions = new ArrayList<>();

    boolean
        movablePawnsShown = true,
        movablePositionsShown = true,
        removablePawnsShown = true,
        traveledPositionsShown = true;

    /**
     * interface which defines
     * the action to be applied
     * at the position of an editing area on the akalana
     * after a click on this area
     */
    public interface EditionModeAction {
        public void applyAction(Point position);
    }

    /**
     * 1. Edition mode action
     * remove piece at the clicked position
     */
    public final EditionModeAction EDITION_REMOVE_PAWN = position -> {
        Pawn pawnToRemove = getPieceAt(position.x, position.y);
        if (pawnToRemove != null) {
            removePiece(pawnToRemove);
            removeDrawable(pawnToRemove);
        }
    };

    /**
     * 2. Edition mode action
     * add black piece at the clicked position
     */
    public final EditionModeAction EDITION_ADD_BLACK_PAWN = position -> {
        // remove current pawn at the position
        // if exists
        Pawn pawnToRemove = getPieceAt(position.x, position.y);
        if (pawnToRemove != null) {
            removePiece(pawnToRemove);
            removeDrawable(pawnToRemove);
        }

        // if pawn is white,
        // replace with black pawn,
        // else do nothing
        if (pawnToRemove == null || !pawnToRemove.isBlack()) {
            Pawn pawnToAdd = addBlackPiece(position);

            if (pawnToAdd != null) {
                addDrawable(pawnToAdd);
            }
        }
    };

    /**
     * 3. Edition mode action
     * add white piece at the clicked position
     */
    public final EditionModeAction EDITION_ADD_WHITE_PAWN = position -> {
        // remove current pawn at the position
        // if exists
        Pawn pawnToRemove = getPieceAt(position.x, position.y);
        if (pawnToRemove != null) {
            removePiece(pawnToRemove);
            removeDrawable(pawnToRemove);
        }

        // if pawn is black,
        // replace with white pawn,
        // else do nothing
        if (pawnToRemove == null || pawnToRemove.isBlack()) {
            Pawn pawnToAdd = addWhitePiece(position);

            if (pawnToAdd != null) {
                addDrawable(pawnToAdd);
            }
        }
    };

    /**
     * current edition mode action
     */
    private EditionModeAction editionModeAction = null;

    /**
     * Fanorona engine
     */
    protected Engine engine = null;

    /**
     *
     */
    protected Theme theme;

    // Move sequence animation over

    /**
     *
     * @return
     */
    public int getUnitSize() {
        return unitSize;
    }

    public void setMovablePawnsShown(boolean movablePawnsShown) {
        this.movablePawnsShown = movablePawnsShown;
    }

    public void setMovablePositionsShown(boolean movablePositionsShown) {
        this.movablePositionsShown = movablePositionsShown;
    }

    public void setRemovablePawnsShown(boolean removablePawnsShown) {
        this.removablePawnsShown = removablePawnsShown;
    }

    public void setTraveledPositionsShown(boolean traveledPositionsShown) {
        this.traveledPositionsShown = traveledPositionsShown;
    }

    /**
     *
     */
    public static interface MovesSequenceOverListener {
        public void onMovesSequenceOver();
    }

    MovesSequenceOverListener movesSequenceOverListener = null;

    public void setMovesSequenceOverListener(MovesSequenceOverListener movesSequenceOverListener) {
        this.movesSequenceOverListener = movesSequenceOverListener;
    }

    /**
     *
     */
    public static interface MovesSequenceAnimationOverListener {
        public void onMovesSequenceAnimationOver();
    }

    public AkalanaView(Context context) {
        super(context);
        init();
    }

    public AkalanaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AkalanaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Akalana getAkalana() {
        return akalana;
    }

    public List<Pawn> getBlackActivePieces() {
        return blackActivePieces;
    }

    public List<Pawn> getWhiteActivePieces() {
        return whiteActivePieces;
    }

    /**
     * init
     */
    private void init() {
        clear();

        initAkalana();
        initBlackPieces();
        initWhitePieces();
        initEditionPositions();

        setTheme(new Theme());
    }

    /**
     * Create 22 black pieces
     * and add them to removed pieces collection
     * To add parts to put on the akalana,
     * we collect pieces from this collection
     */
    private void initBlackPieces() {
        for (int i = 0; i < 22; ++i) {

            Pawn pawn = new Pawn(true);
            pawn.setPos(0, 0);
            pawn.setFPos(0, 0);
            pawn.setSize(unitSize);
            pawn.setZ(2);
            pawn.addTouchListener(new DrawableTouchListener() {
                @Override
                public void onTouchStart(Touchable touchable) {
                    selectPiece((Pawn) touchable);
                }
            });

            blackPieces.push(pawn);
        }
    }

    /**
     * update black pieces size
     * on view measure
     */
    private void updateBlackPiecesSize() {
        for (final Pawn pawn : blackActivePieces) {
            pawn.setSize(unitSize);
            pawn.setPos(graphicsPosition(pawn.getFy()), graphicsPosition(pawn.getFx()));
        }
        for (final Pawn pawn : blackPieces) {
            pawn.setSize(unitSize);
        }
    }

    /**
     * Create 22 white pieces
     * and add them to removed pieces collection
     */
    private void initWhitePieces() {
        for (int i = 0; i < 22; ++i) {

            Pawn pawn = new Pawn(false);
            pawn.setPos(0, 0);
            pawn.setFPos(0, 0);
            pawn.setSize(unitSize);
            pawn.setZ(2);
            pawn.addTouchListener(new DrawableTouchListener() {
                @Override
                public void onTouchStart(Touchable touchable) {
                    selectPiece((Pawn) touchable);
                }
            });

            whitePieces.push(pawn);
        }
    }

    /**
     * update black pieces size
     * on view measure
     */
    private void updateWhitePiecesSize() {
        for (final Pawn pawn : whiteActivePieces) {
            pawn.setSize(unitSize);
            pawn.setPos(graphicsPosition(pawn.getFy()), graphicsPosition(pawn.getFx()));
        }
        for (final Pawn pawn : whitePieces) {
            pawn.setSize(unitSize);
        }
    }

    /**
     * init drawable Akalana
     */
    private void initAkalana() {
        // init Akalana Drawable
        akalana = new Akalana();
        akalana.setZ(0);
        addDrawable(akalana);
    }

    /**
     * update akalana size
     * on view measure
     */
    private void updateAkalanaSize() {
        akalana.setPos(0, 0);
        akalana.setSize(unitSize * 9, unitSize * 5);
    }

    /**
     * init touchable edition positions
     */
    private void initEditionPositions() {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                EditionPosition editionPosition = new EditionPosition(new Point(i, j));
                editionPosition.setSize(unitSize);
                editionPosition.setPos(graphicsPosition(j), graphicsPosition(i));
                editionPosition.setZ(4);
                editionPosition.addTouchListener(new DrawableTouchListener() {
                    @Override
                    public void onTouchOver(Touchable touchable) {
                        if (editionModeAction != null) {
                            editionModeAction.applyAction(((EditionPosition) touchable).getPosition());
                        }
                    }
                });

                editionPositions.add(editionPosition);
            }
        }
    }

    /**
     * update akalana size
     * on view measure
     */
    private void updateEditionPositionsSize() {
        for (final EditionPosition editionPosition : editionPositions) {
            editionPosition.setSize(unitSize);
            Point position = editionPosition.getPosition();
            editionPosition.setPos(graphicsPosition(position.y), graphicsPosition(position.x));
        }
    }

    /**
     * set Fanorona engine
     * @param engine
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * Configure pieces position
     * from engine
     */
    public void updateConfigFromEngine() {
        if (engine != null) {

            clear();
            movablePositions.clear();
            traveledPositions.clear();
            removablePawns.clear();

            // remove all pieces from akalana
            removeAllPieces();

            addDrawable(akalana);

            // add black pieces into akalana
            for (final Point point: engine.blackPosition()) {
                addBlackPiece(point);
            }
            addDrawables(blackActivePieces);

            // add white pieces into akalana
            for (final Point point: engine.whitePosition()) {
                addWhitePiece(point);
            }
            addDrawables(whiteActivePieces);

            // movable positions
            showMovablePositions();

            // movable pieces
            showMovablePieces();

            // selected piece
            showSelectedPiece();

            // show traveled positions
            updateTraveledPositions();

            // redraw
            draw();

            movesSequenceOver();
        }
    }

    /**
     *
     */
    public void initConfigFromEngine() {
        updateConfigFromEngine();
        onEngineAction(new EngineActionInit(engine.blackPosition(), engine.whitePosition(), engine.getMode(), engine.getFirstPlayerBlack()));
    }

    /**
     * Update theme
     * @param theme theme to add
     */
    public void setTheme(Theme theme) {
        this.theme = theme;

        // update akalana bg
        setBackgroundColor(theme.getAkalanaBgColor());
        akalana.setBitmap(theme.getAkalanaBitmap());
        akalana.setColor(theme.getAkalanaLinesColor());

        // update black pieces bitmaps

        Bitmap blackDefaultBitmap = theme.getBlackDefaultBitmap() != null ? Bitmap.createScaledBitmap(theme.getBlackDefaultBitmap(), unitSize, unitSize, false): null;
        Bitmap blackMovableBitmap = theme.getBlackMovableBitmap() != null ? Bitmap.createScaledBitmap(theme.getBlackMovableBitmap(), unitSize, unitSize, false): null;
        Bitmap blackSelectedBitmap = theme.getBlackSelectedBitmap() != null ? Bitmap.createScaledBitmap(theme.getBlackSelectedBitmap(), unitSize, unitSize, false): null;

        for (final Pawn pawn : blackActivePieces) {
            pawn.setBitmaps(
                    blackDefaultBitmap,
                    blackMovableBitmap,
                    blackSelectedBitmap
            );
            pawn.setColor(theme.getBlackDefaultColor());
            pawn.setStrokeColor(theme.getBlackStrokeColor());
        }

        for (final Pawn pawn : blackPieces) {
            pawn.setBitmaps(
                    blackDefaultBitmap,
                    blackMovableBitmap,
                    blackSelectedBitmap
            );
            pawn.setColor(theme.getBlackDefaultColor());
            pawn.setStrokeColor(theme.getBlackStrokeColor());
        }

        Bitmap whiteDefaultBitmap = theme.getWhiteDefaultBitmap() != null ? Bitmap.createScaledBitmap(theme.getWhiteDefaultBitmap(), unitSize, unitSize, false) : null;
        Bitmap whiteMovableBitmap = theme.getWhiteMovableBitmap() != null ? Bitmap.createScaledBitmap(theme.getWhiteMovableBitmap(), unitSize, unitSize, false): null;
        Bitmap whiteSelectedBitmap = theme.getWhiteSelectedBitmap() != null ? Bitmap.createScaledBitmap(theme.getWhiteSelectedBitmap(), unitSize, unitSize, false): null;

        // update black pieces bitmaps
        for (final Pawn pawn : whiteActivePieces) {
            pawn.setBitmaps(
                    whiteDefaultBitmap,
                    whiteMovableBitmap,
                    whiteSelectedBitmap
            );
            pawn.setColor(theme.getWhiteDefaultColor());
            pawn.setStrokeColor(theme.getWhiteStrokeColor());
        }

        for (final Pawn pawn : whitePieces) {
            pawn.setBitmaps(
                    whiteDefaultBitmap,
                    whiteMovableBitmap,
                    whiteSelectedBitmap
            );
            pawn.setColor(theme.getWhiteDefaultColor());
            pawn.setStrokeColor(theme.getWhiteStrokeColor());
        }

        draw();
    }

    // Mode

    /**
     *
     */
    public void setModePlay() {

        // clear view
        removeAllPieces();
        clear();
        // and add drawable akalana
        addDrawable(akalana);

        initConfigFromEngine();
    }

    /**
     *
     */
    public void setModeEdition() {

        // clear view
        removeAllPieces();
        clear();
        // and add drawable akalana
        addDrawable(akalana);

        // add edition positions to view
        addDrawables(editionPositions);
    }

    /**
     *
     * @param editionModeAction
     */
    public void setModeEditionAction(EditionModeAction editionModeAction) {
        this.editionModeAction = editionModeAction;
    }


    /**
     * make the view size 9 * 5 unit
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        unitSize = Math.min(
                MeasureSpec.getSize(widthMeasureSpec) / 9,
                MeasureSpec.getSize(heightMeasureSpec) / 5
        );

        super.setMeasuredDimension(unitSize * 9, unitSize * 5);

        if (unitSize > 0) {
            updateAkalanaSize();
            updateBlackPiecesSize();
            updateWhitePiecesSize();
            updateEditionPositionsSize();

            removeMovablePositions();
            removeRemovablePieces();
            removeTraveledPositions();
            showMovablePositions();
            showMovablePositions();
            updateTraveledPositions();

            setTheme(theme);
        }

        draw();
    }

    /**
     * Convert to graphics position
     * @param p
     * @return
     */
    private int graphicsPosition(int p) {
        return unitSize * p;
    }

    /**
     * Convert to graphics position
     * @param p
     * @return
     */
    private Point graphicsPosition(Point p) {
        return new Point(graphicsPosition(p.x), graphicsPosition(p.y));
    }

    /**
     * Get piece at (x, y) coordinate
     * @param x
     * @param y
     * @return
     */
    private Pawn getPieceAt(int x, int y) {
        // black piece
        synchronized (blackActivePieces) {
            for (Pawn pawn : blackActivePieces) {
                if (pawn.getFx() == x && pawn.getFy() == y) {
                    return pawn;
                }
            }
        }
        // white piece
        synchronized (whiteActivePieces) {
            for (Pawn pawn : whiteActivePieces) {
                if (pawn.getFx() == x && pawn.getFy() == y) {
                    return pawn;
                }
            }
        }

        return null;
    }

    /**
     * show all movables pieces
     */
    private void showMovablePieces() {
        if (engine != null && movablePawnsShown) {
            // show movables pieces
            for (Point p : engine.movablePieces()) {
                Pawn pawn = getPieceAt(p.x, p.y);

                if (pawn != null) {
                    pawn.setState(Pawn.State.MOVABLE);
                }
            }
        }
    }

    /**
     *
     */
    private void hideMovablePieces() {
        // init all pieces state to none
        // before showing movable pieces
        for (Pawn pawn : blackActivePieces) {
            pawn.setState(Pawn.State.NONE);
        }
        for (Pawn pawn : whiteActivePieces) {
            pawn.setState(Pawn.State.NONE);
        }
    }

    /**
     *
     */
    private void showSelectedPiece() {
        if (engine != null) {
            Point selectedPiece = engine.selectedPiece();

            if (selectedPiece != null) {
                Pawn pawn = getPieceAt(selectedPiece.x, selectedPiece.y);

                if (pawn != null) {
                    pawn.setState(Pawn.State.SELECTED);
                }
            }
        }
    }

    /**
     *
     */
    private void hideSelectedPiece() {
        for (Pawn pawn : blackActivePieces) {
            if (pawn.getState() == Pawn.State.SELECTED) {
                pawn.setState(Pawn.State.NONE);
                return;
            }
        }
        for (Pawn pawn : whiteActivePieces) {
            if (pawn.getState() == Pawn.State.SELECTED) {
                pawn.setState(Pawn.State.NONE);
                return;
            }
        }
    }

    /**
     * show all selected piece movable positions
     * and animate them from
     * the selected piece position
     * to the destination position
     * where the selected piece can move
     */
    private void showMovablePositions() {
        if (engine != null && movablePositionsShown) {
            Point selectedPiecePosition = engine.selectedPiece();

            if (selectedPiecePosition != null) {
                Pawn selectedPiece = getSelectedPiece();

                if (selectedPiece != null) {

                    // move animation from position
                    final int animFromX = graphicsPosition(selectedPiecePosition.y);
                    final int animFromY = graphicsPosition(selectedPiecePosition.x);

                    for (final Engine.Move move : engine.movableVectors()) {
                        final MovablePosition movablePosition = new MovablePosition(selectedPiecePosition, move);
                        movablePosition.setSize(unitSize);
                        movablePosition.setScale(0);
                        movablePosition.setPos(animFromX, animFromY);
                        movablePosition.setZ(1);
                        movablePosition.setTouchable(false);
                        movablePosition.setColor(theme.getMovablePositionColor());
                        movablePositions.add(movablePosition);

                        // move animation to position
                        Point toPosition = selectedPiecePosition.add(move.vector);
                        final int animToX = graphicsPosition(toPosition.y);
                        final int animToY = graphicsPosition(toPosition.x);

                        // anim movable position move
                        // from selected piece position
                        // to the position where the selected piece is supposed to move
                        DrawableMoveAnimation movablePositionMoveAnim = new DrawableMoveAnimation(new SecondDegreeInterpolator());
                        movablePositionMoveAnim.setDrawable(movablePosition);
                        movablePositionMoveAnim.setDelay(100);
                        movablePositionMoveAnim.setFromTo(animFromX, animFromY, animToX, animToY);

                        // anim movable position scale
                        DrawableScaleAnimation movablePositionScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
                        movablePositionScaleAnim.setDrawable(movablePosition);
                        movablePositionScaleAnim.setDelay(100);
                        movablePositionScaleAnim.setFromTo(0f, 1f);

                        // anim movable position opacity
                        DrawableOpacityAnimation movablePositionOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
                        movablePositionOpacityAnim.setDrawable(movablePosition);
                        movablePositionOpacityAnim.setDelay(100);
                        movablePositionOpacityAnim.setFromTo(0, 80);

                        movablePositionMoveAnim.addDelayExceededListener(new AnimationDelayExceededListener() {
                            @Override
                            public void onDelayExceeded(Animation animation) {
                                movablePosition.setTouchable(true);
                            }
                        });

                        // add animations
                        addAnimation(movablePositionMoveAnim);
                        addAnimation(movablePositionScaleAnim);
                        addAnimation(movablePositionOpacityAnim);

                        // on touch
                        // move selected piece
                        movablePosition.addTouchListener(new DrawableTouchListener() {
                            @Override
                            public void onTouchOver(Touchable touchable) {
                                moveSelectedPiece(((MovablePosition) touchable).getMove(), true);
                            }
                        });
                    }

                    addDrawables(movablePositions);
                }
            }
        }
    }

    /**
     *
     */
    private void removeMovablePositions() {
        removeDrawables(movablePositions);
        movablePositions.clear();
    }

    /**
     * hide previous movable positions
     */
    private void hideMovablePositions() {

        for (final MovablePosition movablePosition : movablePositions) {
            movablePosition.setTouchable(false);

            // anim movable position scale
            DrawableScaleAnimation movablePositionScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
            movablePositionScaleAnim.setDrawable(movablePosition);
            movablePositionScaleAnim.setDelay(150);
            movablePositionScaleAnim.setFromTo(1f, 0f);

            // anim movable position opacity
            DrawableOpacityAnimation movablePositionOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
            movablePositionOpacityAnim.setDrawable(movablePosition);
            movablePositionOpacityAnim.setDelay(150);
            movablePositionOpacityAnim.setFromTo(80, 0);

            addAnimation(movablePositionScaleAnim);
            addAnimation(movablePositionOpacityAnim);

            movablePositionOpacityAnim.addDelayExceededListener(animation -> removeDrawable(movablePosition));
        }

        movablePositions.clear();
    }

    /**
     * selected piece
     * @return
     */
    public Pawn getSelectedPiece() {
        Point p = engine.selectedPiece();

        if (p != null) {
            return getPieceAt(p.x, p.y);
        }

        return null;
    }

    /**
     * Select piece at (x, y) coordinate
     * @param position
     * @return
     */
    private boolean selectPieceAt(Point position) {
        return selectPiece(getPieceAt(position.x, position.y));
    }

    /**
     * Select piece
     * @param pawn
     * @return
     */
    private boolean selectPiece(Pawn pawn) {
        if (engine != null) {

            if (
                    pawn != null &&
                    Arrays.asList(engine.movablePieces()).contains(new Point(pawn.getFx(), pawn.getFy()))
            ) {

                // deselect previous selected piece
                Pawn selectedPiece = getSelectedPiece();

                // set selected piece
                if (engine.selectPiece(pawn.getFx(), pawn.getFy())) {
                    if (selectedPiece != null) {
                        // set state to movable
                        selectedPiece.setState(Pawn.State.MOVABLE);
                        // hide previous selected piece movable positions
                        hideMovablePositions();
                        // hide previous selected piece removable pieces
                        hideRemovablePieces();
                    }

                    pawn.setState(Pawn.State.SELECTED);
                    // show current selected piece movable positions
                    showMovablePositions();

                    // history
                    onEngineAction(new EngineActionSelectPiece(engine.currentBlack(), new Point(pawn.getFx(), pawn.getFy())));

                    return true;
                }
            }

        }

        return false;
    }

    /**
     *
     * @param position
     * @param move
     * @return
     */
    private RemovablePawn createRemovablePiece(Point position, Move move) {
        RemovablePawn removablePawn = new RemovablePawn(position, move);
        removablePawn.setPos(graphicsPosition(position.y), graphicsPosition(position.x));
        removablePawn.setSize(unitSize);
        removablePawn.setZ(3);
        removablePawn.setColor(theme.getRemovablePieceColor());

        synchronized (removablePawns) {
            removablePawns.add(removablePawn);
        }

        // anim removable piece scale
        DrawableScaleAnimation removablePieceScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
        removablePieceScaleAnim.setDrawable(removablePawn);
        removablePieceScaleAnim.setDelay(150);
        removablePieceScaleAnim.setFromTo(0f, 1f);

        // anim removable piece opacity
        DrawableOpacityAnimation removablePieceOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
        removablePieceOpacityAnim.setDrawable(removablePawn);
        removablePieceOpacityAnim.setDelay(150);
        removablePieceOpacityAnim.setFromTo(0, 120);

        // mark.addClickListener(selectPiecesToRemoveListener);
        addAnimation(removablePieceScaleAnim);
        addAnimation(removablePieceOpacityAnim);

        // on touch
        // move selected piece
        removablePawn.addTouchListener(new DrawableTouchListener() {
            @Override
            public void onTouchStart(Touchable touchable) {
                moveSelectedPiece(((RemovablePawn) touchable).getMove(), false);
            }
        });

        return removablePawn;
    }

    /**
     *
     * @param move
     */
    private void showRemovablePieces(Move move) {
        if (removablePawnsShown) {
            Point[] removedPiecesPercute = engine.removedPieces(move.vector, true),
                    removedPiecesAspire = engine.removedPieces(move.vector, false);

            // Marque pour les pièces pouvant être percutées
            Move percuteMove = new Move(move.vector, true);
            for (final Point position : removedPiecesPercute) {
                createRemovablePiece(position, percuteMove);
            }

            // Marque pour les pièces pouvant être aspirées
            Move aspireMove = new Move(move.vector, false);
            for (final Point position : removedPiecesAspire) {
                createRemovablePiece(position, aspireMove);
            }

            addDrawables(removablePawns);
        }
    }

    /**
     *
     */
    private void removeRemovablePieces() {
        removeDrawables(removablePawns);
        removablePawns.clear();
    }

    /**
     *
     */
    private void hideRemovablePieces() {
        for (final RemovablePawn removablePawn : removablePawns) {

            // anim removable piece scale
            DrawableScaleAnimation removablePieceScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
            removablePieceScaleAnim.setDrawable(removablePawn);
            removablePieceScaleAnim.setDelay(150);
            removablePieceScaleAnim.setFromTo(1f, 0f);

            // anim removable piece opacity
            DrawableOpacityAnimation removablePieceOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
            removablePieceOpacityAnim.setDrawable(removablePawn);
            removablePieceOpacityAnim.setDelay(150);
            removablePieceOpacityAnim.setFromTo(120, 0);

            addAnimation(removablePieceScaleAnim);
            addAnimation(removablePieceOpacityAnim);

            removablePieceScaleAnim.addDelayExceededListener(animation -> removeDrawable(removablePawn));
        }

        removablePawns.clear();
    }

    /**
     *
     */
    private void removeTraveledPositions() {
        removeDrawables(traveledPositions);
        traveledPositions.clear();
    }

    /**
     *
     */
    private void updateTraveledPositions() {
        if (traveledPositionsShown && engine != null) {
            List<Point> previousTraveledPositions =
                    traveledPositions
                            .stream()
                            .map(TraveledPosition::getPosition)
                            .collect(Collectors.toList());

            List<Point> currentTraveledPositions = Arrays.asList(engine.traveledPositions());


            for (final Point position : currentTraveledPositions) {
                if (!previousTraveledPositions.contains(position)) {
                    TraveledPosition traveledPosition = new TraveledPosition(position);
                    traveledPosition.setSize(unitSize);
                    traveledPosition.setPos(graphicsPosition(position.y), graphicsPosition(position.x));
                    traveledPosition.setZ(1);
                    traveledPosition.setColor(theme.getTraveledPositionsColor());
                    traveledPositions.add(traveledPosition);

                    // anim traveled position scale
                    DrawableScaleAnimation traveledPositionScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
                    traveledPositionScaleAnim.setDrawable(traveledPosition);
                    traveledPositionScaleAnim.setDelay(150);
                    traveledPositionScaleAnim.setFromTo(0f, 1f);

                    // anim traveled position opacity
                    DrawableOpacityAnimation traveledPositionOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
                    traveledPositionOpacityAnim.setDrawable(traveledPosition);
                    traveledPositionOpacityAnim.setDelay(150);
                    traveledPositionOpacityAnim.setFromTo(0, 70);

                    addAnimation(traveledPositionScaleAnim);
                    addAnimation(traveledPositionOpacityAnim);
                    addDrawable(traveledPosition);
                }
            }

            for (final java.util.Iterator<TraveledPosition> it = traveledPositions.iterator(); it.hasNext(); ) {
                TraveledPosition traveledPosition = it.next();

                if (!currentTraveledPositions.contains(traveledPosition.getPosition())) {
                    it.remove();

                    // anim traveled position scale
                    DrawableScaleAnimation traveledPositionScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
                    traveledPositionScaleAnim.setDrawable(traveledPosition);
                    traveledPositionScaleAnim.setDelay(150);
                    traveledPositionScaleAnim.setFromTo(1f, 0f);

                    // anim traveled position opacity
                    DrawableOpacityAnimation traveledPositionOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
                    traveledPositionOpacityAnim.setDrawable(traveledPosition);
                    traveledPositionOpacityAnim.setDelay(150);
                    traveledPositionOpacityAnim.setFromTo(80, 0);

                    addAnimation(traveledPositionScaleAnim);
                    addAnimation(traveledPositionOpacityAnim);

                    traveledPositionOpacityAnim.addDelayExceededListener(animation -> removeDrawable(traveledPosition));
                }
            }
        }
    }

    /**
     *
     * @param pawn
     * @param position
     */
    private Pawn initPiecePosition(Pawn pawn, Point position) {
        pawn.setFPos(position.x, position.y);
        pawn.setScale(1f);
        pawn.setOpacity(255);
        pawn.setPos(graphicsPosition(position.y), graphicsPosition(position.x));

        return pawn;
    }

    /**
     *
     * @param position
     * @return
     */
    private Pawn addBlackPiece(Point position) {
        if (!blackPieces.empty()) {
            Pawn pawn = blackPieces.pop();
            blackActivePieces.add(initPiecePosition(pawn, position));
            return pawn;
        }

        return null;
    }

    /**
     *
     * @param position
     * @return
     */
    private Pawn addWhitePiece(Point position) {
        if (!whitePieces.empty()) {
            Pawn pawn = whitePieces.pop();
            whiteActivePieces.add(initPiecePosition(pawn, position));
            return pawn;
        }

        return null;
    }

    /**
     *
     * @param pawn
     */
    private void removePiece(Pawn pawn) {
        if (pawn.isBlack()) {
            blackActivePieces.remove(pawn);
            blackPieces.add(pawn);
        } else {
            whiteActivePieces.remove(pawn);
            whitePieces.add(pawn);
        }

        pawn.setState(Pawn.State.NONE);
    }

    /**
     *
     */
    public void removeAllPieces() {
        removeDrawables(blackActivePieces);
        removeDrawables(whiteActivePieces);

        blackPieces.addAll(blackActivePieces);
        whitePieces.addAll(whiteActivePieces);
        blackActivePieces.clear();
        whiteActivePieces.clear();

        // init pawn state to none
        for (Pawn pawn: blackPieces) {
            pawn.setState(Pawn.State.NONE);
        }
        for (Pawn pawn: whitePieces) {
            pawn.setState(Pawn.State.NONE);
        }
    }

    /**
     *
     * @param origin
     * @param movement
     * @return
     */
    private Point removedPiecesDestPosition(Point origin, Move movement) {
        Point point = new Point(movement.vector);

        if (!movement.percute) {
            point = new Point(-point.x, -point.y);
        }

        Point p;

        // move p from origin
        // to the first point outside the akalana
        for (p = origin; p.x >= 0 && p.x < 5 && p.y >= 0 && p.y < 9; p = p.add(point));

        return p;
    }

    /**
     *
     * @param removedPieces
     * @param origin
     * @param movement
     */
    private void removePieces(final Point[] removedPieces, Point origin, Move movement) {
        if (removedPieces.length > 0) {

            Point to = removedPiecesDestPosition(origin, movement);
            int animToX = graphicsPosition(to.y);
            int animToY = graphicsPosition(to.x);

            for (final Point position : removedPieces) {
                final Pawn pawn = getPieceAt(position.x, position.y);

                if (pawn != null) {
                    removePiece(pawn);

                    // anim movable position move
                    // from selected piece position
                    // to the position where the selected piece is supposed to move
                    DrawableMoveAnimation vatoMoveAnim = new DrawableMoveAnimation(new SecondDegreeInterpolator());
                    vatoMoveAnim.setDrawable(pawn);
                    vatoMoveAnim.setDelay(600);
                    vatoMoveAnim.setFromTo(pawn.getX(), pawn.getY(), animToX, animToY);

                    // anim movable position scale
                    DrawableScaleAnimation vatoScaleAnim = new DrawableScaleAnimation(new SecondDegreeInterpolator());
                    vatoScaleAnim.setDrawable(pawn);
                    vatoScaleAnim.setDelay(400);
                    vatoScaleAnim.setFromTo(1f, 0f);

                    // anim movable position opacity
                    DrawableOpacityAnimation vatoOpacityAnim = new DrawableOpacityAnimation(new SecondDegreeInterpolator());
                    vatoOpacityAnim.setDrawable(pawn);
                    vatoOpacityAnim.setDelay(80);
                    vatoOpacityAnim.setFromTo(255, 0);

                    // add animations
                    addAnimation(vatoMoveAnim);
                    addAnimation(vatoScaleAnim);
                    addAnimation(vatoOpacityAnim);

                    vatoOpacityAnim.addDelayExceededListener(animation -> removeDrawable(pawn));
                }
            }
        }
    }

    /**
     * Move selected piece with vector
     * @param move
     * @param confirmPiecesToRemove
     * if true and we can either hit or suck pieces,
     * we give the choice on which pieces to eliminate
     */
    private boolean moveSelectedPiece(Move move, boolean confirmPiecesToRemove) {
        if (engine != null) {

            Pawn selectedPiece = getSelectedPiece();
            if (selectedPiece != null) {

                // hide previous removable pieces mark
                hideRemovablePieces();

                // check if we can both percute or aspire piece
                boolean percute = engine.canCatch(move.vector, true),
                        aspire = engine.canCatch(move.vector, false);

                Boolean isVela = engine.getVela();

                if (confirmPiecesToRemove && (isVela == null || isVela == engine.currentBlack()) && percute && aspire) {
                    showRemovablePieces(move);
                } else {

                    // history
                    onEngineAction(new EngineActionMoveSelectedPiece(engine.currentBlack(), move));

                    // don't show anymore which pieces
                    // are movable
                    hideMovablePieces();
                    selectedPiece.setState(Pawn.State.SELECTED);

                    // move animation from position
                    int animFromX = selectedPiece.getX();
                    int animFromY = selectedPiece.getY();

                    // move animation to position
                    Point fromPosition = new Point(selectedPiece.getFx(), selectedPiece.getFy());
                    Point toPosition = fromPosition.add(move.vector);
                    int animToX = graphicsPosition(toPosition.y);
                    int animToY = graphicsPosition(toPosition.x);

                    // anim movable position move
                    // from selected piece position
                    // to the position where the selected piece is supposed to move
                    DrawableMoveAnimation movablePositionMoveAnim = new DrawableMoveAnimation(new SecondDegreeInterpolator());
                    movablePositionMoveAnim.setDrawable(selectedPiece);
                    movablePositionMoveAnim.setDelay(200);
                    movablePositionMoveAnim.setFromTo(animFromX, animFromY, animToX, animToY);
                    addAnimation(movablePositionMoveAnim);

                    // update selected piece position
                    Point[] removedPieces = engine.movePiece(move.vector, move.percute);
                    selectedPiece.setFPos(toPosition.x, toPosition.y);

                    // show traveled position
                    updateTraveledPositions();

                    // remove pieces
                    removePieces(removedPieces, fromPosition, move);

                    // hide last previous movable position
                    hideMovablePositions();

                    if (engine.moveSessionOpened()) {
                        addAnimation(new TimeoutAnimation(50, animation -> {
                            showMovablePositions();
                        }));
                    } else {
                        movesSequenceOver();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @return
     */
    public boolean stopMovesSequence() {
        if (engine != null) {
            if (engine.stopMove()) {
                hideMovablePositions();
                hideRemovablePieces();
                updateTraveledPositions();
                movesSequenceOver();

                // history
                onEngineAction(new EngineActionStopMove(!engine.currentBlack()));

                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    private void movesSequenceOver() {
        hideMovablePieces();
        showMovablePieces();

        if (movesSequenceOverListener != null) {
            movesSequenceOverListener.onMovesSequenceOver();
        }
    }

    /**
     *
     */
    public interface EngineActionListener {
        public void onEngineAction(EngineAction action);
    }

    private EngineActionListener engineActionListener = null;

    /**
     *
     * @param engineActionListener
     */
    public void setEngineActionListener(EngineActionListener engineActionListener) {
        this.engineActionListener = engineActionListener;
    }

    /**
     *
     * @param action
     */
    private void onEngineAction(EngineAction action) {
        if (engineActionListener != null) {
            engineActionListener.onEngineAction(action);
        }
    }

    /**
     *
     * @param action
     */
    public void applyEngineAction(EngineAction action) {
        if (engine != null) {
            if (action instanceof EngineActionInit) {
                action.applyToEngine(engine);
                initConfigFromEngine();
            } else if (action instanceof EngineActionSelectPiece) {
                EngineActionSelectPiece actionSelectPiece = (EngineActionSelectPiece) action;
                selectPieceAt(actionSelectPiece.getPosition());
            } else if (action instanceof EngineActionMoveSelectedPiece) {
                EngineActionMoveSelectedPiece actionMoveSelectedPiece = (EngineActionMoveSelectedPiece) action;
                moveSelectedPiece(actionMoveSelectedPiece.getMove(), false);
            } else if (action instanceof EngineActionStopMove) {
                stopMovesSequence();
            }
        }
    }

    /**
     *
     * @param actions
     */
    public void animEngineActions(List<EngineAction> actions, MovesSequenceAnimationOverListener movesSequenceAnimationOverListener) {
        animEngineActions(actions, movesSequenceAnimationOverListener, 250);
    }

    /**
     *
     * @param actions
     */
    public void animEngineActions(List<EngineAction> actions, MovesSequenceAnimationOverListener movesSequenceAnimationOverListener, int interval) {
        animEngineAction(actions.iterator(), movesSequenceAnimationOverListener, interval);
    }

    /**
     *
     * @param it
     */
    private void animEngineAction(java.util.Iterator<EngineAction> it, MovesSequenceAnimationOverListener movesSequenceAnimationOverListener, int interval) {
        applyEngineAction(it.next());

        if (it.hasNext()) {
            addAnimation(new TimeoutAnimation(interval, animation -> animEngineAction(it, movesSequenceAnimationOverListener, interval)));
        } else if (movesSequenceAnimationOverListener != null) {
            movesSequenceAnimationOverListener.onMovesSequenceAnimationOver();
        }
    }
}
