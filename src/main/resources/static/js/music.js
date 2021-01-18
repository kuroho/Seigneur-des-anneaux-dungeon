//Réglage du son à un volume de 0.1
var audio = document.getElementById("music");
audio.volume = 0.1;

//Sur la page chooseCharacter, son avancé à 31sec.
var choose = document.getElementsByClassName("choose");
if(choose.length>0){
	choose.music.currentTime = 31;
}
