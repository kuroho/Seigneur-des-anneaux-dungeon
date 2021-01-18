$(document).ready(function() {
	
	//apparition du bouton recommencer en fondu
	$(".choiceCharacter").append("<input type=\"button\" id=\"restart\" class=\"bigButton brownB disabled fade-in-3\" disabled value=\"Recommencer\">");
	
	setTimeout(function () {
		$("#restart").removeClass("disabled");
		$("#restart").prop("disabled",false);
	},3000);	
	
	//Réinitialisation des données pour une nouvelle partie
	$("#restart").click(function(){
		if(!$("#restart").hasClass("disabled")){
		document.location.href = "restart";
		}
	});
	
	//On récupère les infos de la partie
	$.ajax({
		type : "POST",
		url : "./endStat",
		success : function(data) {
			var infos = JSON.parse(data)[0];
			console.log(infos);
			$(".failed").append("<h2 class='big-font fade-in-3'>"+infos.ch_name+" est mort</h2>");
			$(".win").append("<h2 class='big-font fade-in-3'>"+infos.m_name+" a été terrassé</h2>");
			$(".nb_kills").append("<h2 class='big-font fade-in-5'>après avoir tué "+infos.foeNumber+" monstres</h2>");
			$(".nb_kills_win").append("<h2 class='big-font fade-in-5'>"+infos.ch_name+" a tué "+infos.foeNumber+" monstres</h2>");
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
	});
	
	//On récupère le journal de combat
	$.ajax({
		type : "POST",
		url : "./getCombatLog",
		success : function(data) {
			var combatLog = JSON.parse(data);
			console.log(combatLog);
			combatLog.forEach(function(log){
				$(".content").append("<p>"+log+"</p>");
			});
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
	});
	
});