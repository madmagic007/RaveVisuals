package me.madmagic.ravevisuals.instances;

public class VarInstance {

    private double currentValue;
    private String name;
    private Runnable onChanged;

    public VarInstance(Double currentValue) {
        this.currentValue = currentValue;
    }

    public VarInstance(int currentValue) {
        this.currentValue = currentValue;
    }

    public VarInstance(String name, double currentValue) {
        this(currentValue);
        this.name = name;
    }

    public void setValue(double newValue) {
        currentValue = newValue;

        if (onChanged != null)
            onChanged.run();
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    public int getInt() {
        return (int) Math.round(currentValue);
    }

    public double getDouble() {
        return currentValue;
    }

    public Object getForSaving() {
        if (name != null) {
            return name;
        }

        if(currentValue == getInt())
            return Integer.parseInt(String.format("%d", getInt()));
        else
            return currentValue;
    }

    @Override
    public String toString() {
        if(currentValue == getInt())
            return String.format("%d", getInt());
        else
            return String.format("%s", currentValue);
    }
}
