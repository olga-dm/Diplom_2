package dto.ingredientsDto;

import java.util.ArrayList;

public class IngredientsRootDto {
    public ArrayList<IngredientsDto> data;
    private boolean success;

    public IngredientsRootDto(ArrayList<IngredientsDto> data) {
        this.data = data;
    }

    public ArrayList<IngredientsDto> getData() {
        return data;
    }
}
