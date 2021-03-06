/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Logic;

import Logic.Data.Player;
import Logic.Data.EntityDataUnit;
import Logic.Data.EntityDataMissile;
import Logic.Data.EntityData;
import Graphic.GraphicMain;
import Logic.Data.EntityDataParticle;
import Logic.IA.IA;
import Maths.Vector2f;
import GameState.GameStateLevel;
import Tests.Main;
import static Tests.Main.getGameState;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.glfw.GLFW;

public class Logic {
	public static double CURRENT_TIME=0;
	public static double DELTA_TIME=0;
	public static void updateTime() {
		DELTA_TIME=GLFW.glfwGetTime()-CURRENT_TIME;
		CURRENT_TIME+=DELTA_TIME;
	}
	private static EntityUnit PLAYER_ENTITY=null;
	private static Player PLAYER_DATA=null;
	public static float LEVEL_POWER=0.05f;
	public static Realm getActiveRealm() {
		return  ((GameStateLevel)getGameState(2)).getActiveRealm();
	}
	public static EntityUnit getPlayer() {
		if(PLAYER_DATA==null) {
			PLAYER_DATA=new Player();
			PLAYER_DATA.setName("Daratrix");
			PLAYER_DATA.setLevel(0);
		}
		if(PLAYER_ENTITY==null) {
			if(Realm.getRealmCount()>0) {
				Realm r=getActiveRealm();
				if(r!=null) {
					PLAYER_ENTITY=PLAYER_DATA.getPlayerEntity();
					Vector2f pos=r.getWayPoints().get(0).getPos();

					PLAYER_ENTITY.setPos(pos);
					r.addEntity(PLAYER_ENTITY);
				} else return null;
			} else return null;
		}
		return PLAYER_ENTITY;
	}
	public static void killPlayer() {
		try {
			for(int i=0;i<Realm.getRealmCount();i++) {
				Realm.getRealm(i).removeEntity(PLAYER_ENTITY);
			}
			PLAYER_ENTITY=null;
			Main.setGameState(Main.STATE_GAME_OVER);
		} catch (Exception ex) {
			Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public static void init() {
		try{
			loadEntityData("data/entity.data");
			XmlParser.loadFile("data/entityData.xml");
			IA.initIA_TEMPLATES();
			generateRun(0);
			//Realm.changeRealm(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void updateRealm(Realm r) {
		// IA
		ArrayList<EntityUnit> units=r.getUnits();
		for(int i=0;i<units.size();i++) {
			EntityUnit u=units.get(i);
			if(u.isActive()) {
				u.update();
			}
		}
		// Missile
		ArrayList<EntityMissile> missiles=r.getMissiles();
		for(int i=0;i<missiles.size();i++) {
			EntityMissile m=missiles.get(i);
			if(m.isActive())
				m.update();
		}
	}
	public static void loadEntityData(String path) throws IOException {
		System.out.println("Clear Current Entity Data");
		clearData();
		Charset encoding=StandardCharsets.UTF_8;
		System.out.println("Attempting to load "+path);
		List<String> lines = Files.readAllLines(Paths.get(path), encoding);
		System.out.println("Setting New Entity Data");
		createEntityData(lines);
	}
	private static void createEntityData(List<String> lines) {
		EntityData e=null;
		EntityDataUnit u=null;
		EntityDataMissile m=null;
		EntityDataParticle p=null;
		for(String l:lines) {
			if(l.charAt(0)=='+')
				continue;
			String[] data = l.split(" ");
			String action=data[0];
			if("new".equals(action)) { // create new thing
				String object=data[1];
					String name=data[2];
				if("EntityData".equals(object)) {
					u=null;
					m=null;
					p=null;
					e=EntityData.create(name);
					System.out.println("new EntityData "+name);
				} else if("EntityDataUnit".equals(object)) {
					u=EntityDataUnit.create(name);
					m=null;
					p=null;
					e=u;
					System.out.println("new EntityDataUnit "+name);
				} else if("EntityDataMissile".equals(object)) {
					u=null;
					m=EntityDataMissile.create(name);
					p=null;
					e=m;
					System.out.println("new EntityDataMissile "+name);
				} else if("EntityDataParticle".equals(object)) {
					u=null;
					m=null;
					p=EntityDataParticle.create(name);
					e=p;
					System.out.println("new EntityDataParticle "+name);
				}
			} else if("set".equals(action) && e!=null) { // set stuff
				String attribute=data[1];
				if("Size".equals(attribute)) {
					String sizeX=data[2];
					String sizeY=data[3];
					e.setBodySize(Float.valueOf(sizeX),Float.valueOf(sizeY));
				} else if("ModelSize".equals(attribute)) {
					String sizeX=data[2];
					String sizeY=data[3];
					e.setModelSize(Float.valueOf(sizeX),Float.valueOf(sizeY));
				} else if("Model".equals(attribute)) {
					String model=data[2];
					e.setModel(GraphicMain.getModel(model));
				} else if("Color".equals(attribute)) {
					String r=data[2];
					String g=data[3];
					String b=data[4];
					String a=data[5];
					e.setColor(Float.valueOf(r),Float.valueOf(g),Float.valueOf(b),Float.valueOf(a));
				} else if("Sound".equals(attribute)) {
					String key=data[2];
					String path=data[3];
					e.setSound(key,path);
				}
				if(u!=null) {
					if("Health".equals(attribute)) {
						String max=data[2];
						String regen=data[3];
						u.setMaxHealth(Float.valueOf(max));
						u.setRegenHealth(Float.valueOf(regen));
					} else if("Shield".equals(attribute)) {
						String max=data[2];
						String regen=data[3];
						((EntityDataUnit)e).setMaxShield(Float.valueOf(max));
						((EntityDataUnit)e).setRegenShield(Float.valueOf(regen));
					} else if("Energy".equals(attribute)) {
						String max=data[2];
						String regen=data[3];
						u.setMaxEnergy(Float.valueOf(max));
						u.setRegenEnergy(Float.valueOf(regen));
					} else if("Type".equals(attribute)) {
						String type=data[2];
					}
				} else if(m!=null) {
					if("Speed".equals(attribute)) {
						String speed=data[2];
						m.setSpeed(Float.valueOf(speed));
					} else if("Range".equals(attribute)) {
						String range=data[2];
						m.setRange(Float.valueOf(range));
					} else if("Damage".equals(attribute)) {
						String damage=data[2];
						m.setDamage(Integer.valueOf(damage));
					} else if("Radius".equals(attribute)) {
						String radius=data[2];
						m.setRadius(Float.valueOf(radius));
					}
				} else if(p!=null) {
					if("Speed".equals(attribute)) {
						String speed=data[2];
						p.setSpeed(Float.valueOf(speed));
					} else if("Rotation".equals(attribute)) {
						String rotation=data[2];
						p.setRotation(Float.valueOf(rotation));
					} else if("Duration".equals(attribute)) {
						String duration=data[2];
						p.setDuration(Float.valueOf(duration));
					} else if("Type".equals(attribute)) {
						String type=data[2];
						p.setType(Integer.valueOf(type));
					}
				}
			}
		}
	}
	public static void generateRun(int difficulty) {
		clearRun();
		XmlParser.loadFile("data/world/level0.xml");
		XmlParser.loadFile("data/world/level1.xml");
		XmlParser.loadFile("data/world/level2.xml");
		XmlParser.loadFile("data/world/level3.xml");
		XmlParser.loadFile("data/world/level4.xml");
		XmlParser.loadFile("data/world/level5.xml");
	}
	public static void loadLevelList(Collection<Integer> list) {
		for(int i:list) {
			try {
				loadLevel(i+".data");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void loadLevel(String path) throws IOException {
		Charset encoding=StandardCharsets.UTF_8;
		System.out.println("Attempting to load "+path);
		List<String> lines = Files.readAllLines(Paths.get("data/world/"+path), encoding);
		System.out.println("Build new level");
		createLevel(lines);
	}
	private static void createLevel(List<String> lines) {
		int realm=-1;
		Entity e=null;
		EntityUnit u=null;
		for(String l:lines) {
			if(l.length()==0 || l.charAt(0)=='+' || l.charAt(0)=='\n')
				continue;
			String[] data = l.split(" ");
			String action=data[0];
			if("set".equals(action)) { // set environnement value
				String value=data[1];
				if(value.equals("Background")) {
					String model=data[2];
					Realm.getRealm(realm).setBackground(GraphicMain.getModel(model));
					System.out.println("set Backround to "+model+" for "+realm);
				} else if(value.equals("Music")) {
					String music=data[2];
					Audio.Audio.loadSoundData(music);
					Realm r=Realm.getRealm(realm);
					if(r!=null)
						r.setMusic(music);
					System.out.println("set Music to "+music+" for "+realm);
				} else if(value.equals("CameraBound")) {
					float left=Float.valueOf(data[2]);
					float right=Float.valueOf(data[3]);
					float bottom=Float.valueOf(data[4]);
					float top=Float.valueOf(data[5]);
					
					Realm.getRealm(realm).setCameraBound(left,right,bottom,top);
					System.out.println("set CameraBound to "+left+" "+right+" "+bottom+" "+top+" for "+realm);
				} else if(value.equals("Boss")) {
					if(u!=null);
						Realm.getRealm(realm).setBoss(u);
					System.out.println("set Boss for "+realm);
				} else if(value.equals("CollisionOff")) {
					if(u!=null)
						u.setCollide(false);
					if(e!=null)
						e.setCollide(false);
					System.out.println("set Collision Off int "+realm);
				} else if(value.equals("IA")) {
					String ia=data[2];
					int APM=60;
					if(data.length>3) APM=Integer.valueOf(data[3]);
					if(u!=null) {
						u.setIA(IA.getIA_TEMPLATE(ia, APM));
					}
					System.out.println("set IA to "+ia+"("+APM+")");
				} else if(value.equals("CustomValue")) {
					int custom=0;
					if(data.length>2) custom=Integer.valueOf(data[2]);
					if(u!=null) {
						u.setCustomValue(custom);
					}
					System.out.println("set Custom Value to "+custom);
				}
			} else if("new".equals(action)) { // create new thing
				String object=data[1];
				if("Realm".equals(object)) {
					String name=data[2];
					realm=Realm.createRealm(name+"("+(Realm.getRealmCount()+1)+")");
					System.out.println("create Realm ("+realm+"): "+name);
				} else if(realm>-1) {
					String type=data[2];
					String posX=data[3];
					String posY=data[4];
					if("Entity".equals(object)) {
						System.out.println("create Entity: "+type+" "+posX+","+posY);
						u=null;
						e=new Entity();
						e.setData(EntityData.get(type));
						e.setPos(Float.valueOf(posX),Float.valueOf(posY));
						Realm.getRealm(realm).addEntity(e);
					} else if("WayPoint".equals(object)) {
						String direction=data[5];
						String target=data[6];
						String outX=data[7];
						String outY=data[8];
						EntityWayPoint temp;
						temp=EntityWayPoint.create();
						if(temp!=null) {
							temp.setData(EntityData.get(type));
							temp.setPos(Float.valueOf(posX),Float.valueOf(posY));
							temp.setDirection(Integer.valueOf(direction));
							temp.setTarget(Integer.valueOf(target));
							temp.setOut(Float.valueOf(outX),Float.valueOf(outY));
							temp.setRealm(realm);
							Realm.getRealm(realm).addEntity(temp);
							System.out.println("create WayPoint: "+type+" ("+posX+","+posY+")->("+outX+","+outY+") in realm "+temp.getRealm()+" with ID "+temp.getID());
						}
					} else if("Unit".equals(object)) {
						String team=data[5];
						System.out.println("create Unit: "+type+" "+posX+","+posY+" in team "+team);
						e=null;
						u=new EntityUnit();
						u.setData(EntityDataUnit.get(type));
						u.setPos(Float.valueOf(posX),Float.valueOf(posY));
						u.setTeam(Integer.valueOf(team));
						Realm.getRealm(realm).addEntity(u);
					}
					
				}
			
			}
		}
	}
	private static void clearRun() {
		Realm.clearAll();
	}
	private static void clearData() {
		EntityData.clear();
	}
	public static void clear() {
		clearData();
	}
	public static int MAX_LEVEL=100;
	public static int getNextLevelXP(int level) {
		return level*level*10;
	}
}
