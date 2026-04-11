package zorp_the_solar_alien.gameFactory;

import zorp_the_solar_alien.GameObject;

/**
 * This interface defines the crucial method createProduct which creates different type of object based on client needs
 * to support factory design pattern used in this project.
 */
public interface ZorpTheSolarAlienInterface {
    /**
     * Creates and returns a game object of the specified type at the given x and y position.
     */
    GameObject createProduct(String levels, double x, double y);
}
