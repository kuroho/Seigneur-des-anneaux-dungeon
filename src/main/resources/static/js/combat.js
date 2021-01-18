$(document).ready(function() {
	
	var ch_hp=0;
	var m_hp=0;
	
	//On récupère le héros et on l'affiche
	$.ajax({
		type : "POST",
		url : "./getCharacter",
		success : function(data) {
			var character = JSON.parse(data)[0];
			console.log(character);
			ch_hp = parseInt(character.ch_hp_max);
			$(".characterCombat").append("<div class='character fade-in'>"+
			"<p class='name'>"+character.ch_name+"</p>"+
			"<div class='hp-hero'>"+character.ch_hp_max+"</div>"+
			"<div class='avatar_hero'>"+
			"<img src='/image/avatar/"+character.ch_name+".jpg' class='avatar' alt="+character.ch_name+">"+
			"</div>"+
			"<p><img src='/image/icons/HP.png' class='icons' alt='Point de vie'>"+character.ch_hp_max+
			"  <img src='/image/icons/Attack.png' class='icons' alt='Attaque'> "+character.ch_attack+
			"  <img src='/image/icons/Dodge.png' class='icons' alt='Taux d\'esquive'> "+character.ch_dodge*100+"%</p></div>");
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
	});
	
	//On récupère le monstre et on l'affiche
	$.ajax({
		type : "POST",
		url : "./getMonster",
		success : function(data) {
			var monster = JSON.parse(data)[0];
			console.log(monster);
			m_hp = parseInt(monster.m_hp_max);
			$(".monsterCombat").append("<div class='monster fade-in'>"+
			"<p class='name'>"+monster.m_name+"</p>"+
			"<div class='hp-monster'>"+monster.m_hp_max+"</div>"+
			"<div class='avatar_monster'>"+
			"<img src='/image/avatar/"+monster.m_name+".jpg' class='avatar' alt="+monster.m_name+">"+
			"</div>"+
			"<p><img src='/image/icons/HP.png' class='icons' alt='Point de vie'>"+monster.m_hp_max+
			"  <img src='/image/icons/Attack.png' class='icons' alt='Attaque'> "+monster.m_attack+
			"  <img src='/image/icons/Dodge.png' class='icons' alt='Taux d\'esquive'> "+monster.m_dodge*100+"%</p></div>");
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
	});
	
	$("#combat").click(function(){
		
		//Tremblement du héros/monstre lors de l'attaque
		$('.avatar').addClass('shake');
		$('.hp-monster').addClass('shake');
		$('.hp-hero').addClass('shake');

		setTimeout(function () { 
			$('.avatar').removeClass('shake');
			$('.hp-monster').removeClass('shake');
			$('.hp-hero').removeClass('shake');
		}, 2000);
		
		//Son aléatoire d'épée
		var rand = Math.round(Math.random() * (5 - 1) + 1);
		var hit = new Audio('../sound/soundeffect/sword-unsheathe'+rand+'.wav');
		hit.volume = 0.2;
		hit.play();
		
		//On récupère la vie du héros/monstre et on met à jour les données.
		$.ajax({
			type : "POST",
			url : "./damage",
			success : function(data) {
				var combat = JSON.parse(data);
				combat.forEach(function(fight){
					
					//On affiche la vie
					$(".hp-hero").text(fight.ch_hp);
					$(".hp-monster").text(fight.m_hp);
					
					//Si vie<35%, vie en rouge
					if(fight.ch_hp <= ch_hp*0.35) {
						$(".hp-hero").addClass("low-life");
						$(".hp-hero").removeClass("medium-life");
					}
					//Si vie<65%, vie en jaune
					else if(fight.ch_hp <= ch_hp*0.65) {
						$(".hp-hero").addClass("medium-life");
					}
					
					if(fight.m_hp <= m_hp*0.35) {
						$(".hp-monster").addClass("low-life");
						$(".hp-monster").removeClass("medium-life");
					}else if(fight.m_hp <= m_hp*0.65) {
						$(".hp-monster").addClass("medium-life");
					}
					
					//Si le monstre meurt, on récupère le monstre suivant
					if(fight.m_hp == 0){
						$.ajax({
							type : "POST",
							url : "./getMonster",
							success : function(data) {
								
								//Disparition du monstre et désactivation du bouton jusqu'à réapparition du prochain monstre
								$(".monsterCombat").addClass('fade-out-1');
								$('#combat').addClass('disabled');
								$("#combat").prop("disabled",true);
								
								setTimeout(function () { 
									$(".monsterCombat").removeClass('fade-out-1');
									$('#combat').removeClass('disabled');
									$("#combat").prop("disabled",false);
								
									//On affiche le nouveau monstre	
									try{
										var monster = JSON.parse(data)[0];
										m_hp = parseInt(monster.m_hp_max);
										
										$(".monsterCombat").empty();
										$(".monsterCombat").append("<div class='monster fade-in'>"+
										"<p class='name'>"+monster.m_name+"</p>"+
										"<div class='hp-monster'>"+monster.m_hp_max+"</div>"+
										"<div class='avatar_monster'>"+
										"<img src='/image/avatar/"+monster.m_name+".jpg' class='avatar' alt="+monster.m_name+">"+
										"</div>"+
										"<p><img src='/image/icons/HP.png' class='icons' alt='Point de vie'>"+monster.m_hp_max+
										"  <img src='/image/icons/Attack.png' class='icons' alt='Attaque'> "+monster.m_attack+
										"  <img src='/image/icons/Dodge.png' class='icons' alt='Taux d\'esquive'> "+monster.m_dodge*100+"%</p></div>");
									}catch(e){
										//Si une erreur survient (dépassement d'index du tableau des monstres), on redirige vers la page de fin 
										document.location.href="end.html"; 
									}
									
								}, 1000);
								
							},
						});
					}
					//Si le héros meurt, on redirige vers la page de fin
					if(fight.ch_hp == 0){
						document.location.href="failed.html"; 
					}
				});
			},
			error : function(e) {
				console.log("ERROR: ", e);
			},
		});
	});
	
});

