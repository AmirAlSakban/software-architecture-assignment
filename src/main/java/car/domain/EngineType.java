package car.domain;

/**
 * Engine type options for car configuration.
 */
public enum EngineType {
    V6("V6 Engine", 300),
    V8("V8 Engine", 450);
    
    private final String displayName;
    private final int horsepower;
    
    EngineType(String displayName, int horsepower) {
        this.displayName = displayName;
        this.horsepower = horsepower;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getHorsepower() {
        return horsepower;
    }
    
    @Override
    public String toString() {
        return displayName + " (" + horsepower + " HP)";
    }
}
