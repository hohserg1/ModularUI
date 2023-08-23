package com.cleanroommc.modularui.screen;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.integration.nei.NEIState;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NEISettings {

    private NEIState neiState = NEIState.DEFAULT;
    private final List<IWidget> neiExclusionWidgets = new ArrayList<>();
    private final List<Rectangle> neiExclusionAreas = new ArrayList<>();

    public void enableNEI() {
        this.neiState = NEIState.ENABLED;
    }

    public void disableNEI() {
        this.neiState = NEIState.DISABLED;
    }

    public void defaultNEI() {
        this.neiState = NEIState.DEFAULT;
    }

    public boolean isNEIEnabled(ModularScreen screen) {
        return this.neiState.test(screen);
    }

    public void addNEIExclusionArea(Rectangle area) {
        if (!this.neiExclusionAreas.contains(area)) {
            this.neiExclusionAreas.add(area);
        }
    }

    public void removeNEIExclusionArea(Rectangle area) {
        this.neiExclusionAreas.remove(area);
    }

    public void addNEIExclusionArea(IWidget area) {
        if (!this.neiExclusionWidgets.contains(area)) {
            this.neiExclusionWidgets.add(area);
        }
    }

    public void removeNEIExclusionArea(IWidget area) {
        this.neiExclusionWidgets.remove(area);
    }

    public java.util.List<Rectangle> getNEIExclusionAreas() {
        return this.neiExclusionAreas;
    }

    public java.util.List<IWidget> getNEIExclusionWidgets() {
        return this.neiExclusionWidgets;
    }

    public java.util.List<Rectangle> getAllNEIExclusionAreas() {
        this.neiExclusionWidgets.removeIf(widget -> !widget.isValid());
        java.util.List<Rectangle> areas = new ArrayList<>(this.neiExclusionAreas);
        areas.addAll(this.neiExclusionWidgets.stream()
                .filter(IWidget::isEnabled)
                .map(IWidget::getArea)
                .collect(Collectors.toList()));
        return areas;
    }
}
