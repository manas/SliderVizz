/****Copyright (c) <2012> <Manas Agrawal>.
All rights reserved.

Redistribution and use in source and binary forms are permitted
provided that the above copyright notice and this paragraph are
duplicated in all such forms and that any documentation,
advertising materials, and other materials related to such
distribution and use acknowledge that the software was developed
by the <organization>.  The name of the
University may not be used to endorse or promote products derived
from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.*****/


var editor;
var groupByVal="";
function keyEventHandler(cm,ke){
	//if(ke.keyCode==59){
		
		//var q = cm.getValue();
		//var g = q.split('FROM')[0];
		//var table = ((q.split('FROM')[1]).split(';')[0]).split(',');

		//var prd = g.split('SELECT')[1];

		
//	}
	
}
////
//SELECT iddept,age,avg(salary),count(idemployee) FROM employee where age>35 group by iddept;
/////
function getPredicate(query){
	//"SELECT iddept,age,avg(salary),count(idemployee) FROM employee where age>35 group by age;";
	q = query;
	
	var qr = q.split(" ");
	//console.log(qr);
	pred = 0;
	var predicate="";
	attr = 0;
	var attribute = "";
	tabl = 0;
	var tables="";

	//if (q.){
		
	//}
	
	for(i in qr){
		//console.log(qr[i]);
		if (qr[i]=="SELECT"||qr[i]=="select"){
		//	console.log("select");
			attr = 1;
			
		}
		else if(qr[i].match(/FROM/)|| qr[i].match(/from/)){
		//	console.log("from");			
			attr = 0;
			tabl = 1;
		}
		else if(qr[i].match(/WHERE/)||qr[i].match(/where/)){
		//	console.log("where");
			tabl=0;
			pred = 1;
		}
		if (attr ==1){
			if(!(qr[i].match(/SELECT/) || qr[i].match(/select/))){
		//		console.log("select val");
				//all the table name
				attribute = attribute + qr[i];
			}
			
		}
		if (tabl==1){
			if(!(qr[i].match(/FROM/)|| qr[i].match(/from/))){
		//		console.log("table "+qr[i]);
				tables = tables + qr[i];
			}
		}
		if (pred==1){
			if(!(qr[i].match(/\w[<>]\d/)||
					qr[i].match(/AND/)||
					qr[i].match(/and/)||
					qr[i].match(/OR/)||
					qr[i].match(/or/)||
					qr[i].match(/WHERE/)||
					qr[i].match(/where/))){
				predicate = predicate +" "+ qr[i];
				//console.log("case1");
			}
			if((qr[i].match(/\w[<=>]\d/)) && !(qr[i].match(/\w[=]\d/))){
				console.log("case10");
				if(qr[i].match(/\w[<]\d/)){
					groupByVal = qr[i].split(/</)[0];
					//console.log("case11");
				}
				else if(qr[i].match(/\w[>]\d/)){
					groupByVal = qr[i].split(/>/)[0];console.log("case12");}
				else if(qr[i].match(/\w[=>]\d/)){console.log("case13");
					groupByVal = qr[i].split(/=>/)[0];}
				else if(qr[i].match(/\w[<=]\d/)){console.log("case14");
					groupByVal = qr[i].split(/<=/)[0];}				
				//groupByVal = qr[i].split(/>/)[0];
				//console.log("case2");
			}
		}
	}
	console.log("select "+groupByVal+","+attribute+" from "+tables+" where "+predicate+" group by "+groupByVal+";");
	return("select "+groupByVal+","+attribute+" from "+tables+" where "+predicate+" group by "+groupByVal+";");
	//normal query and normal results
	
}
function keyHandler(){
	
	var q = editor.getValue();
	console.log(q);
	var r_query = getPredicate(q);
	$.ajax({
		type: "POST",
		url: "Scroll",
		data: { query: r_query, attr: groupByVal}, 
		cache: false, //,
		success: function(resp) //,
		{	
			//console.log(resp);
		var obj = jQuery.parseJSON(resp);

		displaySlider();
		
		},
			
		error: function(xhr, errorstr, exception) {
			alert("Error");
		//	writeToConsole("error", "HTTP error " + xhr.status + ": " + xhr.statusText);	
		}
	});
	
	
}

function displaySuccess(msg) {
	//$("#result").append(msg);
	
}

function displaySlider(){
	$(function() {
		$( "#slider" ).slider({
			value:100,
			min: 0,
			max: 1000,
			step: 5,
			slide: function( event, ui ) {
				$( "#amount" ).val( ui.value );
			}
		});
		$( "#amount" ).val( $( "#slider" ).slider( "value" ) );
	});
}

function init(){
	editor = CodeMirror.fromTextArea(document.getElementById("code"), {
        lineNumbers: true,
        matchBrackets: true,
        indentUnit: 4,
        mode: "text/x-plsql",
        onKeyEvent: keyEventHandler
      });
}
