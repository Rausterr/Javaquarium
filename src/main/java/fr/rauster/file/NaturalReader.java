package fr.rauster.file;

import fr.rauster.Aquarium;
import fr.rauster.LivingBeing;
import fr.rauster.Plant;
import fr.rauster.fish.Fish;
import fr.rauster.fish.FishType;
import fr.rauster.fish.Gender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaturalReader {
    
    private final Map<Integer, List<LivingBeing>> beingsToAdd = new HashMap<>();
    private final Aquarium aquarium;
    public NaturalReader(Aquarium aquarium, String path) {
        this.aquarium = aquarium;
        read(path);
    }
    
    private void read(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            
            int state = 1;
            String line = reader.readLine();
            List<LivingBeing> beings = new ArrayList<>();
            
            while (line != null) {
                if (line.matches("={5} state \\d+ ={5}")) { //changing state
                    line = line.replaceAll("=", "")
                            .replaceAll(" ", "")
                            .replaceAll("state", "");
                    beingsToAdd.put(state, new ArrayList<>(beings));
                    beings.clear();
                    state = Integer.parseInt(line);
                } else if (line.matches("\\d+ plants \\d+ years")) { //adding plants
                    line = line.replaceAll(" ", "")
                            .replaceAll("years", "");
                    String[] numbers = line.split("plants");
                    for (int i = 0 ; i < Integer.parseInt(numbers[0]) ; i++) {
                        beings.add(new Plant(aquarium, Integer.parseInt(numbers[1])));
                    }
                } else if (line.matches("\\w+, [a-zA-Z]+, \\d+ years")) { //adding fishes
                    line = line.replaceAll(" ", "")
                            .replaceAll("years", "");
                    String[] strings = line.split(",");
                    beings.add(new Fish(aquarium,
                            strings[0],
                            Gender.random(),
                            FishType.valueOf(strings[1].toUpperCase()),
                            Integer.parseInt(strings[2]),
                            10));
                }
                line = reader.readLine();
            }
            
            beingsToAdd.put(state, beings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<LivingBeing> getLivingBeing(int state) {
        return beingsToAdd.get(state);
    }
}
