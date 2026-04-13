package zorp_the_solar_alien.gameFactory;

import zorp_the_solar_alien.GameObject;

/**
 * This interface defines the crucial method createProduct which creates different type of object based on client needs
 * to support factory design pattern used in this project.
 */
public interface ZorpTheSolarAlienInterface {
    GameObject createProduct(String levels, double x, double y);
}
