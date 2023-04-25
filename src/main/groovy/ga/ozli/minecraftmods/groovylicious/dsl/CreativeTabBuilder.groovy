package ga.ozli.minecraftmods.groovylicious.dsl

import ga.ozli.minecraftmods.groovylicious.api.GeneralUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.dsl.traits.AlignmentTrait
import groovy.transform.CompileStatic
import groovy.transform.Pure
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import org.jetbrains.annotations.Nullable

import static groovy.lang.Closure.DELEGATE_FIRST

// todo:
// - slotColour
// - labelColour
@CompileStatic
class CreativeTabBuilder implements AlignmentTrait {
    @Delegate
    final CreativeModeTab.Builder builder

    Component title = Component.empty()
    @Nullable ItemStack icon = null

    boolean canScroll = true
    boolean showTitle = true
    private boolean alignedRight = false

    CreativeTabBuilder() {
        this.builder = makeBuilder()
    }

    CreativeTabBuilder(final CreativeModeTab.Builder builder) {
        this.builder = builder
    }

    CreativeTabBuilder(@DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = makeBuilder()
        this.tap(closure)
    }

    CreativeTabBuilder(final CreativeModeTab.Builder builder,
                       @DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = builder
        this.tap(closure)
    }

    CreativeTabBuilder(final Component title, @DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = makeBuilder()
        this.title = title
        this.tap(closure)
    }

    CreativeTabBuilder(final CreativeModeTab.Builder builder, final Component title,
                       @DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = builder
        this.title = title
        this.tap(closure)
    }

    CreativeTabBuilder(final String title, @DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = makeBuilder()
        this.title = GeneralUtils.stringToComponent(title)
        this.tap(closure)
    }

    CreativeTabBuilder(final CreativeModeTab.Builder builder, final String title,
                       @DelegatesTo(value = CreativeTabBuilder, strategy = DELEGATE_FIRST) final Closure closure) {
        this.builder = builder
        this.title = GeneralUtils.stringToComponent(title)
        this.tap(closure)
    }

    //region title
    void setTitle(final Component title) {
        this.title = title
    }

    void setTitle(final String title) {
        this.title = GeneralUtils.stringToComponent(title)
    }

    Component getTitle() {
        return this.title
    }
    //endregion

    //region icon
    void setIcon(final ItemStack icon) {
        this.icon = icon
    }

    void setIcon(final Item icon) {
        this.icon = icon.defaultInstance
    }

    ItemStack getIcon() {
        return this.icon
    }
    //endregion icon

    //region displayItem(s)
    void setDisplayItem(final ItemLike displayItem) {
        this.displayItems((params, output) -> output.accept(displayItem))
    }

    void setDisplayItem(final ItemStack displayItem) {
        this.displayItems((params, output) -> output.accept(displayItem))
    }

    /**
     * @param displayItems A list of ItemStacks or ItemLike objects
     */
    CreativeModeTab.Builder setDisplayItems(final List displayItems) {
        return this.displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
            displayItems.each(output.&accept)
        })
    }
    //endregion

    //region alignedRight
    void setAlignedRight(final boolean alignedRight) {
        this.alignedRight = alignedRight
        this.alignment = alignedRight ? Alignment.RIGHT : Alignment.LEFT
    }

    boolean getAlignedRight() {
        return this.alignedRight
    }
    //endregion

    //region copiedFromExtension
    // some of these are copied from CreativeModeTabBuilderExtension as @Delegate doesn't seem to include extension methods (or at least IntelliJ doesn't recognise them) - even when moved to an earlier compiled sourceSet
    CreativeModeTab.Builder title(final String title) {
        return this.title(GeneralUtils.stringToComponent(title))
    }

    CreativeModeTab.Builder icon(final ItemStack icon) {
        return this.icon(() -> icon)
    }

    CreativeModeTab.Builder icon(final Item icon) {
        return this.icon(() -> icon.defaultInstance)
    }

    /**
     * @param displayItems A list of ItemStacks or ItemLike objects
     */
    CreativeModeTab.Builder displayItems(final List displayItems) {
        return this.displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
            displayItems.each(output.&accept)
        })
    }

    CreativeModeTab.Builder displayItems(final ItemLike... displayItems) {
        return this.displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
            displayItems.each(output.&accept)
        })
    }

    CreativeModeTab.Builder displayItems(final ItemStack... displayItems) {
        return this.displayItems((CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) -> {
            displayItems.each(output.&accept)
        })
    }
    //endRegion

    CreativeModeTab build() {
        builder.title(title)
        if (icon != null) builder.icon(() -> icon)
        if (!canScroll) builder.noScrollBar()
        if (!showTitle) builder.hideTitle()
        switch (alignment) {
            case Alignment.CENTRE -> throw new IllegalArgumentException("Creative tabs cannot be aligned to the centre")
            case Alignment.RIGHT -> builder.alignedRight()
        }

        return builder.build()
    }

    @Pure
    private static CreativeModeTab.Builder makeBuilder() {
        return new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
    }
}
