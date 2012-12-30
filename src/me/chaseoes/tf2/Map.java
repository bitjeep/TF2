package me.chaseoes.tf2;

import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CaptureStatus;
import me.chaseoes.tf2.utilities.SerializableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Map {

    private TF2 plugin;
    private String name;
    private Location p1;
    private Location p2;
    private Location blueLobby;
    private Location redLobby;
    private Location blueSpawn;
    private Location redSpawn;
    private HashMap<Integer, CapturePoint> points = new HashMap<Integer, CapturePoint>();
    private int redTeamTeleportTime;
    private int timelimit;
    private int playerlimit;

    private File customConfigFile;
    private FileConfiguration customConfig;

    public Map(TF2 plugin, String map) {
        this.plugin = plugin;
        name = map;
        load();
    }

    public void load() {
        customConfigFile = new File(plugin.getDataFolder(), name + ".yml");
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        if (customConfig.isString("region.p1.w")) {
            p1 = new Location(Bukkit.getWorld(customConfig.getString("region.p1.w")), customConfig.getInt("region.p1.x"), customConfig.getInt("region.p1.y"), customConfig.getInt("region.p1.z"));
        }
        if (customConfig.isString("region.p2.w")) {
            p2 = new Location(Bukkit.getWorld(customConfig.getString("region.p2.w")), customConfig.getInt("region.p2.x"), customConfig.getInt("region.p2.y"), customConfig.getInt("region.p2.z"));
        }
        if (customConfig.isString("blue.lobby.w")) {
            blueLobby = new Location(Bukkit.getWorld(customConfig.getString("blue.lobby.w")), customConfig.getInt("blue.lobby.x"), customConfig.getInt("blue.lobby.y"), customConfig.getInt("blue.lobby.z"), (float) customConfig.getDouble("blue.lobby.yaw"), (float) customConfig.getDouble("blue.lobby.pitch"));
        }
        if (customConfig.isString("red.lobby.w")) {
            redLobby = new Location(Bukkit.getWorld(customConfig.getString("red.lobby.w")), customConfig.getInt("red.lobby.x"), customConfig.getInt("red.lobby.y"), customConfig.getInt("red.lobby.z"), (float) customConfig.getDouble("red.lobby.yaw"), (float) customConfig.getDouble("red.lobby.pitch"));
        }
        if (customConfig.isString("blue.spawn.w")) {
            blueSpawn = new Location(Bukkit.getWorld(customConfig.getString("blue.spawn.w")), customConfig.getInt("blue.spawn.x"), customConfig.getInt("blue.spawn.y"), customConfig.getInt("blue.spawn.z"), (float) customConfig.getDouble("blue.spawn.yaw"), (float) customConfig.getDouble("blue.spawn.pitch"));
        }
        if (customConfig.isString("red.spawn.w")) {
            redSpawn = new Location(Bukkit.getWorld(customConfig.getString("red.spawn.w")), customConfig.getInt("red.spawn.x"), customConfig.getInt("red.spawn.y"), customConfig.getInt("red.spawn.z"), (float) customConfig.getDouble("red.spawn.yaw"), (float) customConfig.getDouble("red.spawn.pitch"));
        }
        if (customConfig.isConfigurationSection("capture-points")) {
            for (String id : customConfig.getConfigurationSection("capture-points").getKeys(false)) {
                Integer iid = Integer.parseInt(id);
                Location loc = SerializableLocation.getUtilities().stringToLocation(customConfig.getString("capture-points." + iid));
                points.put(iid, new CapturePoint(name, iid, loc));
            }
        }
        if (customConfig.isInt("teleport-red-team")) {
            redTeamTeleportTime = customConfig.getInt("teleport-red-team");
        }
        if (customConfig.isInt("timelimit")) {
            timelimit = customConfig.getInt("timelimit");
        }
        if (customConfig.isInt("playerlimit")) {
            playerlimit = customConfig.getInt("playerlimit");
        }
    }

    public void saveCapturePoints() {
        customConfig.set("capture-points", null);
        ConfigurationSection section = customConfig.createSection("capture-points");
        for (java.util.Map.Entry<Integer, CapturePoint> keyVal : points.entrySet()) {
            section.set(String.valueOf(keyVal.getKey()), SerializableLocation.getUtilities().locationToString(keyVal.getValue().getLocation()));
        }
        saveConfig();
    }

    private void saveConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CapturePoint getCapturePoint(Integer id) {
        return points.get(id);
    }

    public void setCapturePoint(Integer id, CapturePoint point) {
        points.put(id, point);
        customConfig.set("capture-points." + id, SerializableLocation.getUtilities().locationToString(point.getLocation()));
        saveConfig();
    }

    public List<Location> getCapturePointsLocations() {
        List<Location> locs = new ArrayList<Location>();
        for (CapturePoint point : points.values()){
            locs.add(point.getLocation());
        }
        return locs;
    }

    public Set<CapturePoint> getCapturePoints(){
        return new HashSet<CapturePoint>(points.values());
    }

    public String getName() {
        return name;
    }

    public Location getP1() {
        return p1;
    }

    public void setP1(Location p1) {
        this.p1 = p1;
        customConfig.set("region.p1.w", p1.getWorld().getName());
        customConfig.set("region.p1.x", p1.getBlockX());
        customConfig.set("region.p1.y", p1.getBlockY());
        customConfig.set("region.p1.z", p1.getBlockZ());
        saveConfig();
    }

    public Location getP2() {
        return p2;
    }

    public void setP2(Location p2) {
        this.p2 = p2;
        customConfig.set("region.p2.w", p1.getWorld().getName());
        customConfig.set("region.p2.x", p1.getBlockX());
        customConfig.set("region.p2.y", p1.getBlockY());
        customConfig.set("region.p2.z", p1.getBlockZ());
        saveConfig();
    }

    public Location getRedLobby() {
        return redLobby;
    }

    public void setRedLobby(Location redLobby) {
        this.redLobby = redLobby;
        customConfig.set("red.lobby.w", redLobby.getWorld().getName());
        customConfig.set("red.lobby.x", redLobby.getBlockX());
        customConfig.set("red.lobby.y", redLobby.getBlockY());
        customConfig.set("red.lobby.z", redLobby.getBlockZ());
        customConfig.set("red.lobby.pitch", redLobby.getYaw());
        customConfig.set("red.lobby.yaw", redLobby.getPitch());
        saveConfig();
    }

    public Location getBlueLobby() {
        return blueLobby;
    }

    public void setBlueLobby(Location blueLobby) {
        this.blueLobby = blueLobby;
        customConfig.set("blue.lobby.w", blueLobby.getWorld().getName());
        customConfig.set("blue.lobby.x", blueLobby.getBlockX());
        customConfig.set("blue.lobby.y", blueLobby.getBlockY());
        customConfig.set("blue.lobby.z", blueLobby.getBlockZ());
        customConfig.set("blue.lobby.pitch", blueLobby.getYaw());
        customConfig.set("blue.lobby.yaw", blueLobby.getPitch());
        saveConfig();
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
        customConfig.set("blue.spawn.w", blueLobby.getWorld().getName());
        customConfig.set("blue.spawn.x", blueLobby.getBlockX());
        customConfig.set("blue.spawn.y", blueLobby.getBlockY());
        customConfig.set("blue.spawn.z", blueLobby.getBlockZ());
        customConfig.set("blue.spawn.pitch", blueLobby.getYaw());
        customConfig.set("blue.spawn.yaw", blueLobby.getPitch());
        saveConfig();
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
        customConfig.set("red.spawn.w", redLobby.getWorld().getName());
        customConfig.set("red.spawn.x", redLobby.getBlockX());
        customConfig.set("red.spawn.y", redLobby.getBlockY());
        customConfig.set("red.spawn.z", redLobby.getBlockZ());
        customConfig.set("red.spawn.pitch", redLobby.getYaw());
        customConfig.set("red.spawn.yaw", redLobby.getPitch());
        saveConfig();
    }

    public int getRedTeamTeleportTime() {
        return redTeamTeleportTime;
    }

    public void setRedTeamTeleportTime(int redTeamTeleportTime) {
        this.redTeamTeleportTime = redTeamTeleportTime;
        customConfig.set("teleport-red-team", redTeamTeleportTime);
        saveConfig();
    }

    public int getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(int timelimit) {
        this.timelimit = timelimit;
        customConfig.set("timelimit", timelimit);
        saveConfig();
    }

    public int getPlayerlimit() {
        return playerlimit;
    }

    public void setPlayerlimit(int playerlimit) {
        this.playerlimit = playerlimit;
        playerlimit = customConfig.getInt("playerlimit");
        saveConfig();
    }

    public Boolean allCaptured() {
        Integer possiblepoints = getCapturePoints().size();
        Integer captured = 0;
        for(CapturePoint point : points.values()){
            if(point.getStatus() == CaptureStatus.CAPTURED)
                captured++;
        }

        if (possiblepoints == captured) {
            return true;
        }

        return false;
    }

    public Boolean capturePointBeforeHasBeenCaptured(Integer i) {
        if (i == 1) {
            return true;
        }
        Integer before = i - 1;
        String capped = getCapturePoint(before).getStatus().string();
        if (capped.equalsIgnoreCase("captured")) {
            return true;
        }
        return false;
    }

    public void uncaptureAll() {
        for(CapturePoint point : points.values()){
            point.setStatus(CaptureStatus.UNCAPTURED);
        }
    }
}
