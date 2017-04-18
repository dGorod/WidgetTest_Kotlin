package example.dgorod.widget.common;

/**
 * @author Dmytro Gorodnytskyi
 *         on 14-Apr-17.
 */

public interface Const {

    interface Network {
        int CONNECT_TIMEOUT = 30; // seconds
        int READ_TIMEOUT = 60; // seconds
        int WRITE_TIMEOUT = 120; // seconds
    }

    interface Action {
        String LIST_ITEM_CLICK = "action_list_item_click";
    }

    interface Extra {
        String ITEM_POSITION = "extra_item_position";
        String ITEM_URL = "extra_item_url";
    }

    interface PrefKey {
        String WIDGET_LOGIN = "widget_login_";
    }
}
