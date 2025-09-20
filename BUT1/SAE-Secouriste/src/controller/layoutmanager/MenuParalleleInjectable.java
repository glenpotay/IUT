package controller.layoutmanager;

/**
 * Interface for controllers that need to inject a MenuParalleleController instance.
 * This is used to allow different controllers to set their own MenuParalleleController.
 * @author L. Carré, G. Potay, C. Brocart, T.Brami-Coatual
 * @version 1.0
 */
public interface MenuParalleleInjectable {

    /**
     * Sets the MenuParalleleController for this controller.
     * This method is used to inject the MenuParalleleController instance into the implementing controller.
     *
     * @param menuParalleleController The MenuParalleleController to set.
     */
    void setMenuParalleleController(MenuParalleleController menuParalleleController);
}

