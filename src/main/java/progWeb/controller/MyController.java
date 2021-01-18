package progWeb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Character;
import model.Universe;
import model.Log;

@Controller
public class MyController {
	
	//Redirection vers la page de sélection de personnages
	@RequestMapping(value = "/chooseCharacter", method = RequestMethod.GET)
	public void chooseCharacter(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		response.sendRedirect("/chooseCharacter.html");
	}

	//Récupération de la liste des personnages
	@RequestMapping(value="/lstCharacters",method=RequestMethod.POST)
    public @ResponseBody String lstCharacters() {
		ArrayList<Character> Characters = Universe.getCharacters();
		JSONArray lstChar = new JSONArray();
		
		for(Character c : Characters) {
			JSONObject character = new JSONObject();
			character.put("name", c.getName());
			character.put("pv", c.getHpMax());
			character.put("attack", c.getAttack());
			character.put("esquive", c.getDodgeProbability());
			lstChar.put(character);
		}
		System.out.println("Liste des personnages :");
		System.out.println(lstChar);
        return lstChar.toString();
    }

	//Sélection d'un personnage
	@RequestMapping(value = "/characterSelect", method = RequestMethod.POST)
	public void choiceCharacter(HttpServletRequest request, HttpServletResponse response,  @RequestParam(name="name") String name) 
			throws UnsupportedEncodingException, IOException {
		ArrayList<Character> Characters = Universe.getCharacters();
		for(Character c : Characters) {
			if(c.getName().equals(name)){
				System.out.println("Sélection du personnage :");
				System.out.println(c.toString());
				Cookie ch_attack = new Cookie("ch_attack", Integer.toString(c.getAttack()));
				Cookie ch_name = new Cookie("ch_name", c.getName());
				Cookie ch_hp_max = new Cookie("ch_hp_max", Integer.toString(c.getHpMax()));
				Cookie ch_hp = new Cookie("ch_hp", Integer.toString(c.getHpMax()));
				Cookie ch_dodge = new Cookie("ch_dodge", Double.toString(c.getDodgeProbability()));
				response.addCookie(ch_name);
				response.addCookie(ch_hp_max);
				response.addCookie(ch_hp);
				response.addCookie(ch_attack);
				response.addCookie(ch_dodge);
			}
		}
		response.sendRedirect("/combat.html");
	}
	
	//Récupération des données d'un personnage depuis les cookies
	@RequestMapping(value="/getCharacter",method=RequestMethod.POST)
    public @ResponseBody String Character(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		
		String ch_name = "", ch_hp = "", ch_hp_max = "", ch_dodge = "", ch_attack = "";
		Cookie[] Cookies = request.getCookies();
		
		if(Cookies == null) {
			response.sendRedirect("/index.html");
		} else {
			for(Cookie c : Cookies) {
				switch(c.getName()) {
				  case "ch_name":
					ch_name = c.getValue();
				    break;
				  case "ch_hp":
					ch_hp = c.getValue();
				    break;
				  case "ch_hp_max":
					ch_hp_max = c.getValue();
					break;
				  case "ch_dodge":
					ch_dodge = c.getValue();
					break;
				  case "ch_attack":
					ch_attack = c.getValue();
					break;
				  default:
				}
			}
		}

		JSONArray CharStat = new JSONArray();
		JSONObject character = new JSONObject();
		
		character.put("ch_name", ch_name);
		character.put("ch_hp", ch_hp);
		character.put("ch_hp_max", ch_hp_max);
		character.put("ch_attack", ch_attack);
		character.put("ch_dodge", ch_dodge);
		CharStat.put(character);
		
		System.out.println("Création du cookie personnage :");
		System.out.println(CharStat);
		
		Log.addLog("Sélection du héros <label class='log-hero-name'>"+ch_name+".</label>");
        return CharStat.toString();
    }
	
	//Récupération du monstre en parcourant le tableau des monstres
	@RequestMapping(value="/getMonster",method=RequestMethod.POST)
    public @ResponseBody String Monster(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		int previousFoe = -1;
		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("foeNumber")) {
				previousFoe = Integer.parseInt(c.getValue());
			}
		}
		try {
			Character foe = Universe.getMonsters().get(previousFoe + 1);
			response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
			Cookie m_attack = new Cookie("m_attack", Integer.toString(foe.getAttack()));
			Cookie m_name = new Cookie("m_name", foe.getName());
			Cookie m_hp_max = new Cookie("m_hp_max", Integer.toString(foe.getHpMax()));
			Cookie m_hp = new Cookie("m_hp", Integer.toString(foe.getHpMax()));
			Cookie m_dodge = new Cookie("m_dodge", Double.toString(foe.getDodgeProbability()));
			
			response.addCookie(m_name);
			response.addCookie(m_hp_max);
			response.addCookie(m_hp);
			response.addCookie(m_attack);
			response.addCookie(m_dodge);
			
			JSONArray monsterStat = new JSONArray();
			JSONObject monster = new JSONObject();
			
			monster.put("m_name", m_name.getValue());
			monster.put("m_hp", m_hp.getValue());
			monster.put("m_hp_max", m_hp_max.getValue());
			monster.put("m_attack", m_attack.getValue());
			monster.put("m_dodge", m_dodge.getValue());
			monsterStat.put(monster);
			
			System.out.println("Création du cookie monstre :");
			System.out.println(monsterStat);
			
			Log.addLog("<label class='log-monster-name'>"+m_name.getValue()+"</label> est apparu.");
			
			return monsterStat.toString();

		} catch (IndexOutOfBoundsException e) {
			response.addCookie(new Cookie("foeNumber", "" + (previousFoe + 1)));
			System.out.println("Tous les montres ont été tués");
			response.sendRedirect("/end.html");
			return null;
		}
	}
	
	//Gestion des dégâts
	@RequestMapping(value = "/damage", method = RequestMethod.POST)
	public @ResponseBody String Damage(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException, IOException {
		Cookie[] Cookies = request.getCookies();
		if(Cookies == null) {
			response.sendRedirect("/index.html");
			return null;
		} else {
			String ch_name="", m_name = "";
			int ch_hp=0, ch_attack=0, m_hp=0, m_attack = 0;
			double ch_dodge=0, m_dodge = 0;
			
			for(Cookie c : Cookies) {
			    switch(c.getName()) {
			    	case "ch_name":
			    		ch_name = c.getValue();
			    		break;
			    	case "ch_hp":
			    		ch_hp = Integer.parseInt(c.getValue());
			    		break;
			    	case "ch_attack":
			    		ch_attack = Integer.parseInt(c.getValue());
			    		break;
			    	case "ch_dodge":
			    		ch_dodge = Double.parseDouble(c.getValue());
			    		break;
			    	case "m_name":
			    		m_name = c.getValue();
			    		break;
			    	case "m_hp":
			    		m_hp = Integer.parseInt(c.getValue());
			    		break;
			    	case "m_attack":
			    		m_attack = Integer.parseInt(c.getValue());
			    		break;
			    	case "m_dodge":
			    		m_dodge = Double.parseDouble(c.getValue());
			    		break;
			    }
		    }
			
			double dodge_number = 0;
			
			for(Cookie c : Cookies) {
				switch(c.getName()) {
					//Dégâts sur le héros
				  	case "ch_hp" :
						dodge_number = Math.random();
						//Gestion de l'esquive du héros
						if(dodge_number<=ch_dodge) {
							System.out.println("Le héros a esquivé");
							Log.addLog("<label class='log-hero-name'>"+ch_name+"</label> a esquivé l'attaque.");
							break;
						} else {
							//Gestion des dégâts d'attaque, avec marge de 20%
							int attack_alea = (int) Math.round(m_attack*(Math.random() * (1.2 - 0.8) + 0.8));
							ch_hp = ch_hp-attack_alea;
							if(ch_hp<0) {
								ch_hp = 0;
							}
							Log.addLog("<label class='log-hero-name'>"+ch_name+"</label> a subit <label class='log-damage'>"+attack_alea+"</label> de dégâts, il ne lui reste que <label class='log-hero-hp'>"+ch_hp+"</label> points de vie.");
							if(ch_hp==0) {
								Log.addLog("<label class='log-hero-name'>"+ch_name+"</label> est mort.");
							}
							System.out.println("Le héros a subit "+attack_alea+" de dégâts, il ne lui reste que "+ch_hp+" points de vie");
							c.setValue(Integer.toString(ch_hp));
							response.addCookie(c);
						}
						break;
					//Dégâts sur le monstre
				  	case "m_hp" :
			  			dodge_number = Math.random();
			  			//Gestion de l'esquive du monstre
						if(dodge_number<=m_dodge) {
							System.out.println("Le monstre a esquivé");
							Log.addLog("<label class='log-monster-name'>"+m_name+"</label> a esquivé l'attaque.");
							break;
						} else {
							//Gestion des dégâts d'attaque, avec marge de 20%
							int attack_alea = (int) Math.round(ch_attack*(Math.random() * (1.2 - 0.8) + 0.8));
							m_hp = m_hp-attack_alea;
							if(m_hp<0) {
								m_hp = 0;
							}
							Log.addLog("<label class='log-monster-name'>"+m_name+"</label> a subit <label class='log-damage'>"+attack_alea+"</label> de dégâts, il ne lui reste que <label class='log-monster-hp'>"+m_hp+"</label> points de vie.");
							if(m_hp==0) {
								Log.addLog("<label class='log-monster-name'>"+m_name+"</label> est mort.");
							}
							System.out.println("Le monstre a subit "+attack_alea+" de dégâts, il ne lui reste que "+m_hp+" points de vie");
							c.setValue(Integer.toString(m_hp));
							response.addCookie(c);
						}
						break;
				  default:
				    continue;
				}
			}
			
			//On envoie les points de vie du héros/monstre
			JSONArray combat = new JSONArray();
			JSONObject stats = new JSONObject();
			
			stats.put("ch_hp", Integer.toString(ch_hp));
			stats.put("m_hp", Integer.toString(m_hp));
			combat.put(stats);
			
			System.out.println("Résumé du combat :");
			System.out.println(combat);
			System.out.println("---------------");
	        return combat.toString();
		}
	}

	//récupération des données de combats (nom du héros, nom du monstre, nombre d'ennemis battus)
	@RequestMapping(value="/endStat",method=RequestMethod.POST)
    public @ResponseBody String endStat(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
		String ch_name = "", m_name = "", foeNumber = "";
		Cookie[] Cookies = request.getCookies();
		
		for(Cookie c : Cookies) {
			switch(c.getName()) {
			  case "ch_name":
				ch_name = c.getValue();
			    break;
			  case "foeNumber":
				foeNumber = c.getValue();
			    break;
			  case "m_name":
				m_name = c.getValue();
			  default:
			}
		}

		JSONArray endStat = new JSONArray();
		JSONObject info = new JSONObject();
		
		info.put("ch_name", ch_name);
		info.put("m_name", m_name);
		info.put("foeNumber", foeNumber);
		endStat.put(info);
		
		System.out.println("Envoi des infos de fin :");
		System.out.println(endStat);
        return endStat.toString();
    }
	
	//récupération des données de combats (journal de combat)
		@RequestMapping(value="/getCombatLog",method=RequestMethod.POST)
	    public @ResponseBody String getCombatLog(HttpServletRequest request, HttpServletResponse response) 
	    		throws UnsupportedEncodingException, IOException {

			JSONArray CombatLogs = new JSONArray();
			JSONObject CombatLog = new JSONObject();
			
			ArrayList<String> logs = Log.getLog();
			
			for(String log : logs) {
				CombatLog.put("log", log);
				CombatLogs.put(log);
			}

			System.out.println("Envoi des logs de combats :");
			System.out.println(CombatLogs);
	        return CombatLogs.toString();
	    }
	
	//Réinitialisation du cookie foeNumber et renvoie vers la page de sélection de personnages
	@RequestMapping(value="/restart",method=RequestMethod.GET)
    public void restart(HttpServletRequest request, HttpServletResponse response) 
    		throws UnsupportedEncodingException, IOException {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("foeNumber")) {
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
			Log.reset();
			response.sendRedirect("/chooseCharacter");
	}

}
