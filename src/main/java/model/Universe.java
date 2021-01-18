package model;

import java.util.ArrayList;

public class Universe {

	private static ArrayList<Character> characters = new ArrayList<Character>();
	private static ArrayList<Character> monsters = new ArrayList<Character>();

	public static ArrayList<Character> getCharacters() {
		return characters;
	}
	
	public static ArrayList<Character> getMonsters() {
		return monsters;
	}

	public static void creation() {
		
		Character human = new Character();
		human.setName("Aragorn");
		human.setHpMax(160);
		human.setAttack(60);
		human.setDodgeProbability(0.15);
		characters.add(human);
		
		Character elf = new Character();
		elf.setName("Legolas");
		elf.setHpMax(120);
		elf.setAttack(50);
		elf.setDodgeProbability(0.35);
		characters.add(elf);
		
		Character nain = new Character();
		nain.setName("Gimli");
		nain.setHpMax(230);
		nain.setAttack(40);
		nain.setDodgeProbability(0.05);
		characters.add(nain);

		Character rat = new Character();
		rat.setName("Rat");
		rat.setHpMax(20);
		rat.setAttack(2);
		rat.setDodgeProbability(0.5);
		monsters.add(rat);
		
		Character rat2 = new Character();
		rat2.setName("Rat");
		rat2.setHpMax(20);
		rat2.setAttack(2);
		rat2.setDodgeProbability(0.5);
		monsters.add(rat2);
		
		Character rat3 = new Character();
		rat3.setName("Rat");
		rat3.setHpMax(20);
		rat3.setAttack(2);
		rat3.setDodgeProbability(0.5);
		monsters.add(rat3);

		Character gobelin = new Character();
		gobelin.setName("Gobelin");
		gobelin.setHpMax(40);
		gobelin.setAttack(5);
		gobelin.setDodgeProbability(0.1);
		monsters.add(gobelin);
		
		Character gobelin2 = new Character();
		gobelin2.setName("Gobelin");
		gobelin2.setHpMax(40);
		gobelin2.setAttack(5);
		gobelin2.setDodgeProbability(0.1);
		monsters.add(gobelin2);
		
		Character orc = new Character();
		orc.setName("Orc");
		orc.setHpMax(80);
		orc.setAttack(10);
		orc.setDodgeProbability(0.05);
		monsters.add(orc);		

		Character dragon = new Character();
		dragon.setName("Smaug");
		dragon.setHpMax(300);
		dragon.setAttack(20);
		dragon.setDodgeProbability(0.15);
		monsters.add(dragon);

	}

}
