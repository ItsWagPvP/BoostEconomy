package boostdevteam.placeholderapi;

import boostdevteam.boosteconomy.BoostEconomy;
import boostdevteam.boosteconomy.Data;
import boostdevteam.vaultapi.misc.Economy;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
public class Placeholders extends PlaceholderExpansion {

    public BoostEconomy plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public Placeholders(BoostEconomy plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "boosteconomy";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        // %boosteconomy_money%
        if(identifier.equals("money")){
            Economy eco = new Economy(player, 0);
            return toLong(eco.getBalance());
        }

        //%boosteconomy_money_formatted%
        if (identifier.equals("money_formatted")) {
            Economy eco = new Economy(player, 0);
            return fixMoney(eco.getBalance());
        }

        //%boosteconomy_servertotal%
        if(identifier.equals("servertotal")){
            Data data = new Data();
            double total[] = new double [data.getBalTop().size()];
            double sum = 0;
            for(int i=0; i< data.getBalTop().size(); i++){
                Data.BoostPlayerData pData = data.getBalTop().get(i);
                total[i] = pData.getMoney();
                sum = sum + total[i];
            }
            return "" + toLong(sum);
        }

        //%boosteconomy_servertotal_formatted%
        if(identifier.equals("servertotal_formatted")){
            Data data = new Data();
            double total[] = new double [data.getBalTop().size()];
            double sum = 0;
            for(int i=0; i< data.getBalTop().size(); i++){
                Data.BoostPlayerData pData = data.getBalTop().get(i);
                total[i] = pData.getMoney();
                sum = sum + total[i];
            }
            return "" + fixMoney(sum);
        }

        return "Invalid placeholder!";
    }

    private String toLong(double amt) {
        return String.valueOf((long) amt);
    }

    private String format(double d) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(d);
    }

    private String fixMoney(double d) {

        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000L) + BoostEconomy.getInstance().getConfig().getString("Config.Format.k");
        }
        if (d < 1000000000L) {
            return format(d / 1000000L) + BoostEconomy.getInstance().getConfig().getString("Config.Format.M");
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000L) + BoostEconomy.getInstance().getConfig().getString("Config.Format.B");
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000L) + BoostEconomy.getInstance().getConfig().getString("Config.Format.T");
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000L) + BoostEconomy.getInstance().getConfig().getString("Config.Format.Q");
        }

        return toLong(d);
    }

}
