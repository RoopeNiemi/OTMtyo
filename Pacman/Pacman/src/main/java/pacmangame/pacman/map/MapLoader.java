package pacmangame.pacman.map;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {

    /**
     * Loads the map from a file "map.txt", adds it line by line to an
     * ArrayList.
     *
     * @return An ArrayList representing the map.
     */
    public List<String> loadMap() {
        String path = "map.txt";
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        Scanner fileScanner = new Scanner(is);
        List<String> map = new ArrayList<>();
        while (fileScanner.hasNextLine()) {
            map.add(fileScanner.nextLine());
        }
        return map;
    }
}
