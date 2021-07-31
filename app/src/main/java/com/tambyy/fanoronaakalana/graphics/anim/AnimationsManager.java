package com.tambyy.fanoronaakalana.graphics.anim;

import android.util.Log;

import java.util.List;

public class AnimationsManager {

    /**
     * 30 images par seconde (1000 ms)
     * alors le temps d'intervalle entre deux images sera 1000 ms / 30
     */
    public static int ANIMATION_TIME_INTERVAL = 1000 / 100;

    /**
     * Animation -> temps d'éxecution écoulé
     */
    private final java.util.List<Animation> animations = new java.util.ArrayList<>();

    /**
     * Ajouter une nouvelle animation
     * Son temps d'exécution sera initilisé à 0
     *
     * @param animation
     */
    public void addAnimation(Animation animation) {
        synchronized (animations) {
            animations.add(animation);
        }
    }

    public List<Animation> getAnimations() {
        return animations;
    }

    /**
     * Enlever une animation
     *
     * @param animation
     *
     * @return {@code animation} enlevée
     */
    public boolean removeAnimation(Animation animation) {
        synchronized (animations) {
            return animations.remove(animation);
        }
    }

    /**
     * Annuler toutes les animations
     */
    public void clear() {
        synchronized (animations) {
            animations.clear();
        }
    }

    /**
     * Quand le timeout est lancé,
     * le gestionnaire d'animations éxécute chaque animation.
     *
     * Quand le temps d'éxécution d'une animation a dépassé son delai,
     * elle ne sera plus éxécutée la prochaine fois, dans ce cas
     * tous ses écouteurs d'évènement de délai depassé seront éxécutés
     *
     * @return -> au moins une animation a été éxécutée
     */
    public boolean advance() {
        return advance(ANIMATION_TIME_INTERVAL);
    }

    public boolean advance(int timeIncrementation) {
        if (animations.isEmpty()) {

            return false;
        }

        java.util.List<Animation> exceededAnimations = new java.util.ArrayList<>();

        synchronized (animations) {
            for (java.util.Iterator<Animation> it = animations.iterator(); it.hasNext();) {
                Animation animation = it.next();

                if (!animation.advance(timeIncrementation)) {
                    exceededAnimations.add(animation);
                    it.remove();
                }
            }
        }

        launchExceededAnimationsListeners(exceededAnimations);

        return true;
    }

    /**
     *
     * @param exceededAnimations
     *        Liste des animations ayant le temps de delai dépassé
     */
    private static void launchExceededAnimationsListeners(java.util.List<Animation> exceededAnimations) {
        for (Animation managedAnimation : exceededAnimations) {
            for (AnimationDelayExceededListener listener : managedAnimation.getAnimationDelayExceededListeners()) {
                listener.onDelayExceeded(managedAnimation);
            }
        }
    }
}
