//test

$(document).ready(function() {	
	
	//On récupère la liste des personnages et on l'affiche
	$.ajax({
		type : "POST",
		url : "./lstCharacters",
		success : function(data) {
			var lstCharacters = JSON.parse(data);
			lstCharacters.forEach(function(character){
				$(".lstCharacter").append("<form method='post' action='characterSelect'>"+
				"<div class='character fade-in'>"+
				"<input type='hidden' name='name' value='"+character.name+"'>"+
				"<p class='name'>"+character.name+"</p>"+
				"<img src='/image/avatar/"+character.name+".jpg' class='avatar' alt="+character.name+">"+
				"<p><img src='/image/icons/HP.png' class='icons' alt='Point de vie'>"+character.pv+
				"  <img src='/image/icons/Attack.png' class='icons' alt='Attaque'> "+character.attack+
				"  <img src='/image/icons/Dodge.png' class='icons' alt='Taux d\'esquive'> "+character.esquive*100+"%</p>"+
				"<input type='submit' class='brownB' class='icons' value='choisir "+character.name+"'><br></div>"+
				"</form>");
			});
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
	});
});