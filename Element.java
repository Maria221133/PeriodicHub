package application;

public class Element {

    private int atomicNumber;
    private String name;
    private String symbol;
    private double atomicMass;
    private int group;
    private int period;
    private int elementTypeId; 

    private double density;
    private double meltingPoint;
    private double boilingPoint;
    private String stateOfMatter;
    private String electronConfiguration;

    public Element(int atomicNumber, int elementTypeId, String name, String symbol, double atomicMass, int group, int period) {
        this.atomicNumber = atomicNumber;
        this.elementTypeId = elementTypeId;
        this.name = name;
        this.symbol = symbol;
        this.atomicMass = atomicMass;
        this.group = group;
        this.period = period;
    }


    public int getAtomicNumber() {
        return atomicNumber;
    }

    public void setAtomicNumber(int atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getAtomicMass() {
        return atomicMass;
    }

    public void setAtomicMass(double atomicMass) {
        this.atomicMass = atomicMass;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getElementTypeId() {
        return elementTypeId;
    }

    public void setElementTypeId(int elementTypeId) {
        this.elementTypeId = elementTypeId;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public double getMeltingPoint() {
        return meltingPoint;
    }

    public void setMeltingPoint(double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

    public double getBoilingPoint() {
        return boilingPoint;
    }

    public void setBoilingPoint(double boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

    public String getStateOfMatter() {
        return stateOfMatter;
    }

    public void setStateOfMatter(String stateOfMatter) {
        this.stateOfMatter = stateOfMatter;
    }

    public String getElectronConfiguration() {
        return electronConfiguration;
    }

    public void setElectronConfiguration(String electronConfiguration) {
        this.electronConfiguration = electronConfiguration;
    }

}