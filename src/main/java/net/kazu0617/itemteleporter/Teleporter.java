/*
 * author: kazu0617
 * Lisense: LGPL v3
 */
package net.kazu0617.itemteleporter;

import java.util.Random;
import org.bitbucket.ucchy.util.tellraw.ClickEventType;
import org.bitbucket.ucchy.util.tellraw.MessageComponent;
import org.bitbucket.ucchy.util.tellraw.MessageParts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Teleporter extends JavaPlugin implements Listener
{
    private ItemStack item;
    private ShapedRecipe recipe;
    private static final String DISPLAY_NAME = "Teleporter";
    String Pluginprefix = "[" + ChatColor.GREEN + getDescription().getName() + ChatColor.RESET +"] ";
    String Pluginname = "[" + getDescription().getName() +"] ";
    public ConsoleLog cLog = new ConsoleLog(this);
    boolean DebugMode = false;
    String[] ITPCommand = {"/itp player <player>","/itp location <x> <y> <z>","/itp location random","/itp get"};

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        this.item = new ItemStack(Material.FEATHER, 1);//プラグイン独自アイテム追加
        ItemMeta Teleporter = this.item.getItemMeta();
        Teleporter.setDisplayName(DISPLAY_NAME);
        item.setItemMeta(Teleporter);
        NewRecipe();
        cLog.info(Pluginname+"DebugMode is Now ["+DebugMode+"].");
    }

    @Override
    public void onDisable()
    {
    }

    public void NewRecipe()//新レシピ追加
    {
        this.recipe = new ShapedRecipe(TeleporterItem());
        this.recipe.shape(new String[]{"MCM", "PEP", "R R"});
        this.recipe.setIngredient('M', Material.EMPTY_MAP);
        this.recipe.setIngredient('C', Material.COMPASS);
        this.recipe.setIngredient('P', Material.ENDER_PEARL);
        this.recipe.setIngredient('E', Material.EYE_OF_ENDER);
        this.recipe.setIngredient('R', Material.RABBIT_FOOT);
        getServer().addRecipe(this.recipe);
    }

    private ItemStack TeleporterItem()//プラグイン独自アイテムを使用可能にする
    {
        return this.item.clone();
    }

    private void giveTeleportItem(Player player)//指定されたプレイヤーにテレポーターを与える
    {
        ItemStack Teleporter = TeleporterItem();
        ItemStack temp = player.getItemInHand();
        player.setItemInHand(Teleporter);
        if (temp != null)
        {
            player.getInventory().addItem(temp);
        }
    }
    
    @EventHandler
    private void Rightclickmenu(PlayerInteractEvent e)       
    {
        Player p = e.getPlayer();
        ItemStack Hand = p.getItemInHand();
        Action action = e.getAction();
        if(action != Action.LEFT_CLICK_AIR ||action != Action.LEFT_CLICK_BLOCK)
            return;
        if(DebugMode)  
            cLog.info(p+" Interact.");
        if((Hand == null) || (Hand.getType() == Material.AIR) || (!Hand.getItemMeta().hasDisplayName()) || (!Hand.getItemMeta().getDisplayName().equals(DISPLAY_NAME)))
            return;
        p.sendMessage(Pluginprefix + "下の枠をクリックするとコマンドが入力欄に自動的にコピーされます。");
        ButtonCreate(p,"プレイヤー名を指定してテレポートする","プレイヤー名を指定してテレポートすることが出来ます。",ITPCommand[0],ClickEventType.SUGGEST_COMMAND);
        ButtonCreate(p,"座標を指定してテレポートする","座標を指定してテレポートすることが出来ます。",ITPCommand[1],ClickEventType.SUGGEST_COMMAND);
        ButtonCreate(p,"ランダムな座標をにテレポートする","thpr☆",ITPCommand[2],ClickEventType.SUGGEST_COMMAND);
        return;
    }
    /**
     * ボタンを作成しやすくする。
     *
     * @param ms メッセージ送信先
     * @param Text 表示するテキスト
     * @param HoverText 表示するホバーテキスト
     * @param Value ボタンを押した時に出てくる文字列(Buttonによって変化)
     * @param Button クリックされた時に行われる動作
     */
    public void ButtonCreate(Player ms,String Text,String HoverText,String Value,ClickEventType Button)
    {
        MessageComponent msg = new MessageComponent();
        MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
        button1.addHoverText(HoverText);
        button1.setClickEvent(Button, Value);
        msg.addText(Pluginprefix);
        msg.addParts(button1);
        msg.send(ms);
    }
    /**
     * ボタンを作成しやすくする。
     *
     * @param ms メッセージ送信先
     * @param Text 表示するテキスト
     * @param Value ボタンを押した時に出てくる文字列(Buttonによって変化)
     * @param Button クリックされた時に行われる動作
     */
    public void ButtonCreate(Player ms,String Text,String Value,ClickEventType Button)
    {
        MessageComponent msg = new MessageComponent();
        MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
        button1.setClickEvent(Button, Value);
        msg.addText(Pluginprefix);
        msg.addParts(button1);
        msg.send(ms);
    }
    private void Message(CommandSender sender,String Text)
    {
        sender.sendMessage(Pluginprefix+Text);
        return;
    }
    private void Message(Player p,String Text)
    {
        p.sendMessage(Pluginprefix+Text);
        return;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            ItemStack Teleporter = player.getItemInHand();
            Location loc = player.getLocation();
            if ((args.length == 2) && ("player".equalsIgnoreCase(args[0])))// /itp player <player名>でテレポーターを消費してTP
            {
                if (sender.hasPermission("teleporter.toPlayers"))
                {
                    if (((Teleporter == null) || (Teleporter.getType() == Material.AIR) || (!Teleporter.getItemMeta().hasDisplayName()) || (!Teleporter.getItemMeta().getDisplayName().equals(DISPLAY_NAME))) && !sender.hasPermission("teleporter.admin")) {//テレポーターを持ってなければ処理終了
                        this.Message(sender, "[ " + ChatColor.AQUA + DISPLAY_NAME + " ]" + ChatColor.GREEN + "を持って使用してください！");
                        return false;
                    }
                    Player target = getPlayer(args[1]);
                    if(DebugMode)
                    {
                        cLog.info("target: " + target);
                        cLog.info("player: " + player);
                    }
                    if (target == null)
                    {//指定プレイヤーが居なければ処理終了
                        this.Message(sender, "[ " + ChatColor.GREEN + args[1] + ChatColor.RESET + " ]" + ChatColor.RED + "というプレイヤーが見つかりません！");
                        return false;
                    }
                    else if (target == player && !player.hasPermission("teleporter.admin"))
                    {
                        sender.sendMessage(Pluginprefix + "自分自身には飛べません！");
                        return false;
                    } 
                    else
                    {
                        Location loctarget = target.getLocation();
                        player.teleport(loctarget);//コマンドを打ったプレイヤーを指定プレイヤーの場所に飛ばす
                        player.getInventory().removeItem(Teleporter);//テレポーターを消去する
                        return true;
                    }
                }
            }
            if ((args.length >= 2)&&"location".equalsIgnoreCase(args[0]))
            {
                if (sender.hasPermission("teleporter.toLocation"))
                {//locationの後が<x>だから正直若干上手く動作するか際どい
                    if ( ((Teleporter == null) || (Teleporter.getType() == Material.AIR) || (!Teleporter.getItemMeta().hasDisplayName()) || (!Teleporter.getItemMeta().getDisplayName().equals(DISPLAY_NAME)) ) && !sender.hasPermission("teleporter.admin"))
                    {//テレポーターを持ってなければ処理終了
                        this.Message(sender, "[ " + ChatColor.AQUA + DISPLAY_NAME + " ]" + ChatColor.GREEN + "を持って使用してください！");
                        return true;
                    }
                    if (args.length == 4)
                    {
                        int x, y, z;
                        try {//args[1]～args[3]が数字じゃないとエラー吐くから事前に例外処理
                            x = Integer.parseInt(args[1]);
                            y = Integer.parseInt(args[2]);
                            z = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage("数字以外指定しないでください！");
                            return false;
                        }
                        loc.setX(x);//数字だったらプレイヤーの座標を設定しなおして飛ばす
                        loc.setY(y);
                        loc.setZ(z);
                        player.teleport(loc);
                        sender.sendMessage("テレポートしました");
                        if (!sender.hasPermission("teleorter.admin"))
                        {
                            player.getInventory().removeItem(Teleporter);//テレポーターを消去する
                        }
                        return true;
                    }
                    else if ((args.length == 2) && ("random".equalsIgnoreCase(args[1])))
                    {
                        if (((Teleporter == null) || (Teleporter.getType() == Material.AIR) || (!Teleporter.getItemMeta().hasDisplayName()) || (!Teleporter.getItemMeta().getDisplayName().equals(DISPLAY_NAME)))&& !sender.hasPermission("teleporter.admin"))
                        {//テレポーターを持ってなければ処理終了
                            this.Message(sender,"[ " + ChatColor.AQUA + DISPLAY_NAME + " ]" + ChatColor.GREEN + "を持って使用してください！");
                            return false;
                        }
                        World world = loc.getWorld();
                        int x = RandomLocation(0);//ランダムでx,y,zを選択
                        int y = RandomLocation(1);
                        int z = RandomLocation(2);
                        while (BlockCheck(x, y, z, world))//選択された座標のブロックを取得して、大丈夫かどうかを判別
                        {
                            x = RandomLocation(0);
                            y = RandomLocation(1);
                            z = RandomLocation(2);
                        }
                        loc.setX(x);//安全な地帯が決まったらプレイヤーの座標を設定しなおして飛ばす
                        loc.setY(y);
                        loc.setZ(z);
                        player.teleport(loc);
                        this.Message(sender, "[ " + ChatColor.AQUA + x + ChatColor.RESET + " ] [" + ChatColor.AQUA + y + ChatColor.RESET + "] [" + ChatColor.AQUA + z + ChatColor.RESET + " ]" + ChatColor.GREEN + "にテレポートしました");
                        player.getInventory().removeItem(Teleporter);//テレポーターを消去する
                        return true;
                    }
                    else
                    {
                        sender.sendMessage(Pluginprefix + ChatColor.DARK_RED + "指定の書式ではありません！");//指定の書式でなかった場合
                        return false;
                    }
                }
            }
            if ((args.length == 1) && ("get".equalsIgnoreCase(args[0])))
            {
                if (sender.hasPermission("teleporter.getItem") || sender.hasPermission("teleporter.admin"))//テレポーターをもらう
                {
                    giveTeleportItem(player);
                    return true;
                }
            }
            else
            {
                sender.sendMessage(Pluginprefix + "Commandlist");
                sender.sendMessage(Pluginprefix + "You can copy this command.");
                ButtonCreate((Player) sender, ITPCommand[0], "Teleport to Other Player.", ITPCommand[0], ClickEventType.SUGGEST_COMMAND);
                ButtonCreate((Player) sender, ITPCommand[1], "Teleport to Other location.", ITPCommand[1], ClickEventType.SUGGEST_COMMAND);
                ButtonCreate((Player) sender, ITPCommand[2], "Teleport to Random location.", ITPCommand[2], ClickEventType.SUGGEST_COMMAND);
               if (sender.hasPermission("teleporter.admin"))
                {
                    ButtonCreate((Player) sender, ITPCommand[3], "Get Teleport Item.", ITPCommand[3], ClickEventType.SUGGEST_COMMAND);
                }
                return true;
            }
        }
        else if(args.length==0)
        {
            cLog.info("Commandlist");
            cLog.info("You can copy this command.");
            cLog.info(ITPCommand[0]+" … Teleport to Other Player.");
            cLog.info(ITPCommand[1]+" … Teleport to Other location.");
            cLog.info(ITPCommand[2]+" … Teleport to random location.");
            cLog.info(ITPCommand[3]+" … Get Teleport Item.");
            return true;
        }
        else
        {
            cLog.info("Do not to inspect args.");
        }
        return false;
    }

    private int RandomLocation(int num)//ランダム、平面上は1000ブロックまで、上下は200ブロックまで
    {
        Random random = new Random();
        int[] loc = new int[3];
        loc[0] = random.nextInt(1000);
        loc[1] = random.nextInt(200);
        loc[2] = random.nextInt(1000);

        return loc[num];
    }

    private boolean BlockCheck(int x, int y, int z, World world)
    {
        if ((y <= 5) || (y >= 270))//いや・・・Y=5以下は下手すると積みそうだし、Y=270以上も積みそうじゃない？
        {
            return false;
        }
        Block[] CheckBlock = {world.getBlockAt(x, y, z),world.getBlockAt(x, y - 1, z),world.getBlockAt(x, y - 2, z)};
        if (CheckBlock[0].getType() != Material.AIR)//空気ブロック以外は再選択
        {
            return false;
        }
        if (CheckBlock[1].getType() == Material.AIR && CheckBlock[2].getType() == Material.AIR)//TPする座標の1つ下が空気だった場合は再選択
        {
            return false;
        }
        return true;
    }

    private Player getPlayer(String name)//指定されたプレイヤーが居るかどうかの判別
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.getName().equals(name))
            {
                return player;
            }
        }
        return null;
    }
}
