import java.util.Objects;

public class Zone {

    private final String name;
    private final double weight;
    private double coefficient;

    Zone(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
