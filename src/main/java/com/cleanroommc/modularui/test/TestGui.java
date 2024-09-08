package com.cleanroommc.modularui.test;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.CustomModularScreen;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.SortableListWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SideOnly(Side.CLIENT)
public class TestGui extends CustomModularScreen {

    private List<String> lines;
    private List<String> configuredOptions;

    private Map<String, AvailableElement> availableElements;

    @Override
    public void onClose() {
        ModularUI.LOGGER.info("New values: {}", this.configuredOptions);
    }

    @Override
    public @NotNull ModularPanel buildUI(ModularGuiContext context) {
        if (this.lines == null) {
            this.lines = IntStream.range(0, 20).mapToObj(i -> "Option " + (i + 1)).collect(Collectors.toList());
            this.configuredOptions = this.lines;
            this.availableElements = new Object2ObjectOpenHashMap<>();
        }
        final Map<String, SortableListWidget.Item<String>> items = new Object2ObjectOpenHashMap<>();
        for (String line : this.lines) {
            items.put(line, new SortableListWidget.Item<>(line).child(item -> new Row()
                    .child(new Widget<>()
                            .addTooltipLine(line)
                            .background(GuiTextures.BUTTON_CLEAN)
                            .overlay(IKey.str(line))
                            .expanded().heightRel(1f))
                    .child(new ButtonWidget<>()
                            .onMousePressed(button -> item.removeSelfFromList())
                            .overlay(GuiTextures.CROSS)
                            .width(10).heightRel(1f))));
        }
        SortableListWidget<String> sortableListWidget = new SortableListWidget<String>()
                .children(configuredOptions, items::get)
                .debugName("sortable list");
        List<List<AvailableElement>> availableMatrix = Grid.mapToMatrix(2, this.lines, (index, value) -> {
            AvailableElement availableElement = new AvailableElement().overlay(IKey.str(value))
                    .size(60, 14)
                    .addTooltipLine(value)
                    .onMousePressed(mouseButton1 -> {
                        if (this.availableElements.get(value).available) {
                            sortableListWidget.child(items.get(value));
                            this.availableElements.get(value).available = false;
                        }
                        return true;
                    });
            this.availableElements.put(value, availableElement);
            return availableElement;
        });
        for (String value : this.lines) {
            this.availableElements.get(value).available = !this.configuredOptions.contains(value);
        }

        ModularPanel panel = ModularPanel.defaultPanel("test");

        /*List<List<IWidget>> matrix = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            int r = i / 20;
            int c = i % 20;
            List<IWidget> row;
            if (matrix.size() <= r) {
                row = new ArrayList<>();
                matrix.add(row);
            } else {
                row = matrix.get(r);
            }
            row.add(IKey.str(String.valueOf(i + 1)).asWidget());
        }
        panel.child(new Grid()
                .matrix(matrix)
                .scrollable()
                .pos(10, 10).right(10).bottom(10))*/

        panel.child(sortableListWidget
                .onRemove(stringItem -> this.availableElements.get(stringItem.getWidgetValue()).available = true)
                /*.onChange(list -> {
                    this.configuredOptions = list;
                    for (String value : this.lines) {
                        this.availableElements.get(value).available = !list.contains(value);
                    }
                })*/
                .pos(10, 10)
                .bottom(23)
                .width(100));
        IPanelHandler otherPanel = IPanelHandler.simple(panel, (mainPanel, player) -> {
            ModularPanel panel1 = ModularPanel.defaultPanel("Option Selection", 150, 120);
            return panel1.child(ButtonWidget.panelCloseButton())
                    .child(new Grid()
                            .matrix(availableMatrix)
                            .scrollable()
                            .pos(7, 7).right(14).bottom(7).debugName("available list"));
        }, true);
        panel.child(new ButtonWidget<>()
                .bottom(7).size(12, 12).leftRel(0.5f)
                .overlay(GuiTextures.ADD)
                .onMouseTapped(mouseButton -> {
                    otherPanel.openPanel();
                    return true;
                }));
        /*IDrawable optionHoverEffect = new Rectangle().setColor(Color.withAlpha(Color.WHITE.dark(5), 50));
        panel.child(new PopupMenu<>(ListWidget.builder(lines, t -> new SimpleWidget()
                        .width(1f).height(12)
                        .background(IKey.str(t).color(Color.WHITE.normal))
                        .hoverBackground(optionHoverEffect, IKey.str(t).color(Color.WHITE.normal)))
                                            .width(0.8f).height(36).top(1f)
                                            .background(new Rectangle().setColor(Color.BLACK.bright(2))))
                            .left(10)
                            .right(10)
                            .height(20)
                            .top(10)
                            .background(GuiTextures.BUTTON, IKey.str("Button")))*/
                /*.child(SlotGroup.playerInventory()
                        .flex(flex -> flex
                                .left(0.5f)
                                .bottom(7)));*/

        return panel;
    }

    private static class AvailableElement extends ButtonWidget<AvailableElement> {

        private boolean available = true;
        private final IDrawable activeBackground = GuiTextures.BUTTON_CLEAN;
        private final IDrawable background = GuiTextures.SLOT_FLUID;

        @Override
        public AvailableElement background(IDrawable... background) {
            throw new UnsupportedOperationException("Use overlay()");
        }

        @Override
        public IDrawable getBackground() {
            return this.available ? this.activeBackground : this.background;
        }
    }
}
