/* Since it is important for our implementation to get the images of chairs loaded on browser before any further
 * execution of action, use of '$(document).ready' is avoided. 
 */

window.onload = init;

function init() {
	var div = document.getElementById('c1');
	ev = document.getElementById('pool');
	for (var i = 0; i < 10; i++) {
		clone = div.cloneNode(true);
		clone.id = 'c' + i;
		clone.hidden = false;
		ev.appendChild(clone);
	}
}

function allowDrop(ev) {
	ev.preventDefault();
}

function drag(ev) {
	ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
	ev.preventDefault();
	var data = ev.dataTransfer.getData("text");
	ev.target.appendChild(document.getElementById(data));
}

function dropParent(ev) {
	ev.preventDefault();
	var data = ev.dataTransfer.getData("text");
	ev.target.parentNode.appendChild(document.getElementById(data));
}

function redr() {
	clearRoom('pool');
	clearRoom('room1');
	clearRoom('room2');
	clearRoom('room3');

	var n = parseInt(document.getElementById("chairs").value);
	var div = document.getElementById('c1');
	ev = document.getElementById('pool');
	for (var i = 0; i < n; i++) {
		clone = div.cloneNode(true);
		clone.id = 'c' + i;
		clone.hidden = false;
		ev.appendChild(clone);
	}
	return false;
}

function clearRoom(rId) {
	node = document.getElementById(rId);
	while (node.hasChildNodes()) {
		node.removeChild(node.lastChild);
	}
}

function roomData(rId) {
	var a = [];
	node = document.getElementById(rId);
	children = node.childNodes;

	for (child in children) {
		if (children[child].id != null)
			a.push({[children[child].id] : rId});
	}
	return a;
}

function callServ() {
	a = {};
	a['floor']=document.getElementById('floorname').value;
	b = [];
	b = b.concat(roomData('room1'));
	b = b.concat(roomData('room2'));
	b = b.concat(roomData('room3'));
	a['arr']=b;
	console.log(JSON.stringify(a));

	$.ajax({
		type : "POST",
		
		// the url where you want to sent the data to
		url : 'http://localhost:8080/ChairArrangement/RequestHandler',
		
		dataType : 'json',	
		async : false,
		
		// json object to sent to the url
		data : JSON.stringify(a)
	})
	
	window.location = "http://localhost:8080/ChairArrangement/Report";
}