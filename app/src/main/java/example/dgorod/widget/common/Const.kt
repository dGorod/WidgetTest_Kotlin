package example.dgorod.widget.common

/**
 * @author Dmytro Gorodnytskyi
 *          on 14-Apr-17.
 */

object Const {

    object Network {
        val CONNECT_TIMEOUT = 30L // seconds
        val READ_TIMEOUT = 60L // seconds
        val WRITE_TIMEOUT = 120L // seconds
    }

    object Action {
        val LIST_ITEM_CLICK = "action_list_item_click"
    }

    object Extra {
        val ITEM_POSITION = "extra_item_position"
        val ITEM_URL = "extra_item_url"
    }

    object PrefKey {
        val WIDGET_LOGIN = "widget_login_"
    }
}
