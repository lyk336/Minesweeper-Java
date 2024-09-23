package gui;

public class ScalablePanelBuilder {
    private int minWidth = 0;
    private int minHeight = 0;
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double widthScalePercentage = 1;
    private double heightScalePercentage = 1;

    public ScalablePanelBuilder setMinSizes(int minWidth, int minHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;

        return this;
    }

    public ScalablePanelBuilder setMaxSizes(int maxWidth, int maxHeight) {
        if(maxWidth < this.minWidth || maxHeight < this.minHeight) {
            System.err.println("Max size cannot be lower than min size.");
            return this;
        }

        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;

        return this;
    }

    public ScalablePanelBuilder setScalePercentage(double widthPercentage, double heightPercentage) {
        this.widthScalePercentage = widthPercentage;
        this.heightScalePercentage = heightPercentage;

        return this;
    }

    public ScalablePanel build() {
        return new ScalablePanel(
            this.minWidth,
            this.minHeight,
            this.maxWidth,
            this.maxHeight,
            this.widthScalePercentage,
            this.heightScalePercentage
        );
    }
}
