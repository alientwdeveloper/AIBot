/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Setting.GuildSetting;
import Private.Private;
import Resource.Prefix;
import Command.*;
import Command.InformationModule.*;
import Command.ModerationModule.*;
import Command.UtilityModule.*;
import Command.MusicModule.*;
import Command.FunModule.*;
import Command.RestrictedModule.*;
import Listener.*;
import Audio.*;
import static Audio.Music.playerManager;
import Resource.Info;
import Setting.SmartLogger;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;


import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.entities.Game;

import java.util.HashMap;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class Main {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, Command> commands = new HashMap<String, Command>();
    public static HashMap<String, GuildSetting> guilds = new HashMap<String, GuildSetting>();
    public static long timeStart = 0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Music.musicStartup();
            jda = new JDABuilder(AccountType.BOT)
                    .addListener(new CommandListener())
                    .setToken(Private.BOT_TOKEN)
                    .buildBlocking();
            jda.getPresence().setGame(Game.of(Info.B_GAME));
            jda.setAutoReconnect(true);
            
            startUp();
            
            timeStart = System.currentTimeMillis();
            
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
            SmartLogger.updateLog("Exception thrown while logging.");
        }
    }
    
    public static void startUp()
    {
        addCommands();
        ConsoleListener console = new ConsoleListener();
        
        SmartLogger.updateLog("Bot Start Up. Commands Added.");
    }
    
    public static void shutdown()
    {
        System.out.println("Bot Shut Down Successfully");
        SmartLogger.updateLog("Bot Shut Down Successfully");
        
        jda.shutdown();
        System.exit(0);
    }
    
    public static void setStatus(OnlineStatus stat)
    {
        jda.getPresence().setStatus(stat);
        SmartLogger.updateLog("Bot Status set to " + stat.toString());
    }
    
    public static void setGame(String game)
    {
        if(!"".equals(game))
            jda.getPresence().setGame(Game.of(game));
        else if("default".equals(game))
            game = Info.B_GAME;
        SmartLogger.updateLog("Bot Game set to " + game);
    }
        
    private static void addCommands() {
        // Information Commands
        commands.put("help", new HelpCommand());
        commands.put("h", new HelpCommand());
        commands.put("invite", new InviteCommand());
        
        commands.put("botinfo", new InfoBotCommand());
        commands.put("bi", new InfoBotCommand());
        commands.put("serverinfo", new InfoServerCommand());
        commands.put("si", new InfoServerCommand());
        commands.put("channelinfo", new InfoChannelCommand());
        commands.put("ci", new InfoChannelCommand());
        commands.put("userinfo", new InfoUserCommand());
        commands.put("ui", new InfoUserCommand());
        
        commands.put("prefix", new PrefixCommand());
        commands.put("ping", new PingCommand());
        
        commands.put("about", new AboutCommand());
        commands.put("status", new StatusCommand("status"));
        commands.put("uptime", new StatusCommand("uptime"));
        commands.put("support", new SupportCommand());
        
        // Moderation Commands
        commands.put("prune", new PruneCommand());
        commands.put("p", new PruneCommand());
        
        commands.put("kick", new KickCommand());
        commands.put("k", new KickCommand());
        
        commands.put("ban", new BanCommand());
        commands.put("b", new BanCommand());
        commands.put("unban", new UnbanCommand());
        commands.put("ub", new UnbanCommand());
        
        //Utility Commands
        commands.put("number", new NumberCommand());
        commands.put("num", new NumberCommand());
        commands.put("n", new NumberCommand());
        commands.put("math", new MathCommand());
        commands.put("calc", new MathCommand());
        commands.put("m", new MathCommand());
        
        commands.put("say", new SayCommand());
        commands.put("emoji", new EmojiCommand());
        commands.put("emote", new EmojiCommand());
        commands.put("e", new EmojiCommand());
        commands.put("weather", new WeatherCommand());
        commands.put("w", new WeatherCommand());
        
        commands.put("search", new SearchCommand("search"));
        commands.put("google", new SearchCommand("google"));
        commands.put("g", new SearchCommand("google"));
        commands.put("wiki", new SearchCommand("wiki"));
        commands.put("urban", new SearchCommand("ub"));
        commands.put("github", new SearchCommand("git"));
        commands.put("git", new SearchCommand("git"));
        commands.put("imdb", new IMDbCommand());
        
        commands.put("image", new ImageCommand("image"));
        commands.put("imgur", new ImageCommand("imgur"));
        commands.put("imgflip", new ImageCommand("imgflip"));
        commands.put("gif", new ImageCommand("gif"));
        commands.put("meme", new ImageCommand("meme"));
        
        //Fun Commands
        commands.put("8ball", new EightBallCommand());
        commands.put("face", new FaceCommand());
        commands.put("game", new GameCommand());
        commands.put("lenny", new FaceCommand());
        commands.put("f", new FaceCommand());
        commands.put("rockpaperscissors", new RPSCommand());
        commands.put("rps", new RPSCommand());
        commands.put("guessnum", new GuessNumberCommand());
        commands.put("gn", new GuessNumberCommand());
        commands.put("tictactoe", new TicTacToeCommand());
        commands.put("ttt", new TicTacToeCommand());
        commands.put("hangman", new HangManCommand());
        commands.put("hm", new HangManCommand());
        commands.put("hangmancheater", new HangManCheaterCommand());
        commands.put("hmc", new HangManCheaterCommand());
        
        // Music Commands
        commands.put("join", new JoinCommand());
        commands.put("summon", new JoinCommand());
        commands.put("j", new JoinCommand());
        commands.put("leave", new LeaveCommand());
        commands.put("l", new LeaveCommand());
        commands.put("play", new PlayCommand());
        commands.put("pause", new PauseCommand("pause"));
        commands.put("resume", new PauseCommand("resume"));
        commands.put("unpause", new PauseCommand("resume"));
        commands.put("skip", new SkipCommand());
        commands.put("nowplaying", new NowPlayingCommand());
        commands.put("current", new NowPlayingCommand());
        commands.put("np", new NowPlayingCommand());
        commands.put("queue", new QueueCommand());
        commands.put("q", new QueueCommand());
        commands.put("volume", new VolumeCommand());
        commands.put("stop", new StopCommand());
        commands.put("lyrics", new LyricsCommand());
        
        //Restricted Commands
        commands.put("shutdown", new ShutDownCommand());
        commands.put("source", new SourceCommand());
        commands.put("setStatus", new PresenceCommand("setStatus"));
        commands.put("setGame", new PresenceCommand("setGame"));
    }
}