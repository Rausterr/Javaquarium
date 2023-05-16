package fr.rauster;

import org.jetbrains.annotations.Nullable;

public class Fish extends LivingBeing{
    
    private final String name;
    private Gender gender;
    private final FishType type;
    public Fish(Aquarium aquarium, String name, Gender gender, FishType type) {
        super(aquarium);
        this.name = name;
        this.gender = gender;
        this.type = type;
    }
    public Fish(Aquarium aquarium, String name, Gender gender, FishType type, int age, int hp) {
        super(aquarium, hp);
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.age = age;
    }
    
    @Override
    public void live() {
        super.live();
        damage(1);
        
        if (hp <= 5) eat();
        else breed();
        
        if (getType().getSexualityType() == SexualityType.HERMAPHRODITE_WITH_AGE && age == 10) {
            gender = gender.opposite();
        }
    }
    
    @Override
    protected void breed() {
        if (isTooPopulated(aquarium.getFishes().size(), Aquarium.MAX_FISH_POPULATION)) return;
        Fish fish = randomFish();
        if (fish == null) return;
        if (fish.getGender() == getGender()) {
            if (getType().getSexualityType() == SexualityType.OPPORTUNIST_HERMAPHRODITE) {
                gender = gender.opposite();
            }
            else return;
        }
        
        Gender gender = Gender.FEMALE;
        if (r.nextBoolean()) gender = Gender.MALE;
        aquarium.addFish(gender, getType());
    }
    public void eat() {
        if (getType().getDiet() == Diet.HERBIVORE) {
            eatRandomPlant();
        }
        if (getType().getDiet() == Diet.CARNIVORE) {
            eatRandomFish();
        }
    }
    private void eatRandomPlant() {
        Plant plant = null;
        if (aquarium.getPlants().size() > 0)
            plant = aquarium.getPlants().get(r.nextInt(aquarium.getPlants().size()));
        if (plant == null) return;
        plant.damage(2);
        hp += 3;
    }
    private void eatRandomFish() {
        Fish fish = randomFish();
        if (fish == null || fish.getType() == getType() || fish == this) return;
        fish.damage(6);
        hp += 5;
    }
    private @Nullable Fish randomFish(){
        Fish fish = null;
        if (aquarium.getFishes().size() > 0)
            fish = aquarium.getFishes().get(r.nextInt(aquarium.getFishes().size()));
        if (fish == this) return null;
        return fish;
    }
    
    public String getName() {
        return name;
    }
    public Gender getGender() {
        return gender;
    }
    public FishType getType() {
        return type;
    }
    @Override
    public String toString() {
        return "Fish{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", type=" + type +
                ", hp=" + hp +
                ", age=" + age +
                '}';
    }
}
