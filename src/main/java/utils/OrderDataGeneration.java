package utils;

import java.util.ArrayList;

public class OrderDataGeneration {
    public final ArrayList<String> ingredients;

    public OrderDataGeneration(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }

    public static OrderDataGeneration getIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        //Краторная булка
        ingredients.add("61c0c5a71d1f82001bdaaa6c");
        //Мясо бессмертных моллюсков Protostomia
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        //Соус Spicy-X
        ingredients.add("61c0c5a71d1f82001bdaaa72");
        return new OrderDataGeneration(ingredients);
    }

    public static OrderDataGeneration getEmptyIngredients(){
        ArrayList<String> ingredients = new ArrayList<>();
        return new OrderDataGeneration(ingredients);
    }

    public static OrderDataGeneration getInstanceHashIsNotCorrect(){
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0cwwyewr%63");
        ingredients.add("Ytcvsf652424");
        return new OrderDataGeneration(ingredients);
    }
}
