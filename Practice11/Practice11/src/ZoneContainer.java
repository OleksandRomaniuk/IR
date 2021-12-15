

import java.util.*;

public class ZoneContainer{

    private final int id;
    private final Set<Zone> zones;

    public ZoneContainer(int id) {
        this.id = id;
        this.zones = new TreeSet<>(Comparator.comparing(Zone::getName));
    }

    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public Zone getZoneByName(String name) {
        for (Zone z : zones) {
            if (z.getName().equalsIgnoreCase(name)) {
                return z;
            }
        }
        return null;
    }

    public double getFileWeight() {
        double weight = 0;
        for (Zone z : zones) {
            weight += z.getWeight()*z.getCoefficient();
        }
        return weight;
    }

    public int getId() {
        return id;
    }




}