package gui;
import java.awt.*;

public class ScalablePanel extends Container {
    final private int minWidth;
    final private int minHeight;
    final private int maxWidth;
    final private int maxHeight;
    final private double widthScalePercentage;
    final private double heightScalePercentage;

    // creates with builder pattern
    public ScalablePanel(
        int minWidth,
        int minHeight,
        int maxWidth,
        int maxHeight,
        double widthPercentage,
        double heightPercentage
    ) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.widthScalePercentage = widthPercentage;
        this.heightScalePercentage = heightPercentage;
    }

    @Override
    public Dimension getPreferredSize() {
        Container parent = this.getParent();

        int computedWidth = (int) (parent.getWidth() * this.widthScalePercentage);
        int computedHeight = (int) (parent.getHeight() * this.heightScalePercentage);

        int width = Math.clamp(computedWidth, this.minWidth, this.maxWidth);
        int height = Math.clamp(computedHeight, this.minHeight, this.maxHeight);

        return new Dimension(width, height);
    }
}
